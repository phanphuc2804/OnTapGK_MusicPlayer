package com.example.ontapgk_de1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.concurrent.TimeUnit;

public class MusicActivity extends AppCompatActivity {
    TextView player_position, player_duration;
    ImageView start_btn, pause_btn, ff_btn, rw_btn;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
     Handler  handler = new Handler();
     Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        player_position = findViewById(R.id.player_position);
        player_duration = findViewById(R.id.player_duration);
        start_btn = findViewById(R.id.start_button);
        pause_btn = findViewById(R.id.pause_button);
        ff_btn = findViewById(R.id.ff_btn);
        rw_btn = findViewById(R.id.rw_btn);
        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this,R.raw.ghostmp3);

        runnable = new Runnable() {
            @Override
            public void run() {
                //Set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //Convert millisecond to minute and second
        String sDuration = convertFormat(duration);
        //Set duration on text view
        player_duration.setText(sDuration);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hde play button
                start_btn.setVisibility(View.GONE);
                //Show pause button
                pause_btn.setVisibility(View.VISIBLE);
                //Start media player
                mediaPlayer.start();
                //Set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //Start handler
                handler.postDelayed(runnable, 0);
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide pause button
                pause_btn.setVisibility(View.GONE);
                //Show play button
                start_btn.setVisibility(View.VISIBLE);
                //Pause media player
                mediaPlayer.pause();
                //Stop handler
                handler.removeCallbacks(runnable);
            }
        });

        ff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //Get duration of media player
                int duration = mediaPlayer.getDuration();
                //Check condition
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    //When media is playing and duration is not equal to current postion
                    //Fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                    //Set current position on text view
                    player_position.setText(convertFormat(currentPosition));
                    //Set progress on seekbar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        rw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current positon of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //Check condition
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    //When media is playing and current postion in greater than 5 seconds
                    currentPosition = currentPosition - 5000;
                    //Get current position on text view
                    player_position.setText(convertFormat(currentPosition));
                    //Set progress on seekbar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Check condition
                if(fromUser) {
                    //When drag the seek bar
                    //Set progress on seek bar
                    mediaPlayer.seekTo(progress);
                }
                //Set current position on text view
                player_position.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Hide pause button
                pause_btn.setVisibility(View.GONE);
                //Show play button
                start_btn.setVisibility(View.VISIBLE);
                //Set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes(duration))));
    }
}