package com.avantilearning;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import java.io.InputStream;

/**
 *
 * @author allan
 */
public class MediaPlayerProxy implements PlayerListener {

    private boolean isPlaying = false;

    protected MediaPlayerProxy() {
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance()
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder {

        private static final MediaPlayerProxy INSTANCE = new MediaPlayerProxy();
    }

    public static MediaPlayerProxy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Plays an audio file
     * @param name Path and file name of file to play
     */
    public void playAudio(String name) {
        if (false == this.isPlaying) {
            try {
                String contentType[] = Manager.getSupportedContentTypes(null);
                boolean notFound = true;
                int n = 0;
                while ((true == notFound) && (n < contentType.length)) {
                    if (AvantiQuizlet.DEBUG_MODE) {
                        System.out.println("Content Type: " + contentType[n]);
                    }
                    if (contentType[n].equals("audio/mpeg")) {
                        notFound = false;
                        InputStream is = getClass().getResourceAsStream(name);
                        Player player = Manager.createPlayer(is, "audio/mpeg");
                        player.addPlayerListener(this);
                        player.realize();
                        VolumeControl vc = (VolumeControl) player.getControl("VolumeControl");
                        if (vc != null) {
                            vc.setLevel(80);
                        }
                        player.prefetch();
                        player.start();
                    }
                    n++;
                }
            } catch (Exception e) {
                System.err.println("Unable to locate or read audio file");
            }
        }
    }

    /** PlayerListener implementation. */
    public void playerUpdate(Player player, String event, Object eventData) {
        if (event.equals(PlayerListener.STARTED)) {
            this.isPlaying = true;
        } else if (event.equals(PlayerListener.END_OF_MEDIA) || event.equals(PlayerListener.STOPPED)) {
            try {
                player.removePlayerListener(this);
                player.close();
            } catch (IllegalStateException ex) {
                this.isPlaying = false;
            }
            this.isPlaying = false;
        } else if (event.equals(PlayerListener.ERROR)) {
            this.isPlaying = false;
        }
    }
}
