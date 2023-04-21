package br.com.thelastsurvivor.util;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class MyPlayerMp3 implements OnCompletionListener {

    private static final int NOVO = 0;

    private static final int INICIADO = 1;

    private static final int PAUSADO = 2;

    private static final int PARADO = 3;

    private int status;

    private MediaPlayer player;

    private String mp3;

    public MyPlayerMp3() {
        status = NOVO;
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
    }

    public void start(String mp3) {
        Log.i("", "Inicio da m�sica: " + mp3);
        this.mp3 = mp3;
        try {
            switch(status) {
                case INICIADO:
                    player.stop();
                case PARADO:
                    player.reset();
                case NOVO:
                    player.setDataSource(mp3);
                    player.prepare();
                case PAUSADO:
                    player.start();
                    break;
            }
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        }
    }

    public void pause() {
        player.pause();
        status = PAUSADO;
    }

    public void stop() {
        player.stop();
        status = PARADO;
    }

    public void fechar() {
        stop();
        player.release();
        player = null;
    }

    public void onCompletion(MediaPlayer mp) {
        Log.i("", "Fim da m�sica: " + mp3);
    }
}
