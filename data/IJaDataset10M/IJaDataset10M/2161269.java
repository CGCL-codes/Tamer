package net.sf.groofy.player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jgroove.JGroove;
import jgroove.JGroovex;
import jgroove.jsonx.JsonCountry;
import jgroove.jsonx.JsonGetSong;
import net.sf.groofy.logger.GroofyLogger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class GroovesharkPlayer {

    private class PlayerThread extends Thread {

        private Player player = null;

        private String songID = null;

        public PlayerThread(String songID) {
            this.songID = songID;
        }

        @Override
        public void run() {
            try {
                String s = JGroove.callMethodHttps(null, "getCountry");
                JsonCountry country = new Gson().fromJson(s, JsonCountry.class);
                JsonGetSong songURL = JGroovex.getSongURL(songID);
                InputStream stream = JGroovex.getSongStream(songURL.result.ip, songURL.result.streamKey);
                JGroovex.markSongComplete(songURL.result.streamServerID, songURL.result.streamKey, songID);
                player = new Player(new BufferedInputStream(stream));
            } catch (IOException e) {
                GroofyLogger.getInstance().logException(e);
            } catch (JavaLayerException e) {
                GroofyLogger.getInstance().logException(e);
            } catch (JsonParseException e) {
                GroofyLogger.getInstance().logException(e);
            }
        }

        public void stopPlaying() {
            if (player != null) {
                if (!player.isComplete()) player.close();
            }
        }
    }

    private static GroovesharkPlayer INSTANCE = null;

    public static GroovesharkPlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroovesharkPlayer();
        }
        return INSTANCE;
    }

    private PlayerThread playerThread;

    public GroovesharkPlayer() {
    }

    public void playSong(String songID) {
        if (playerThread != null) {
            playerThread.stopPlaying();
        }
        playerThread = new PlayerThread(songID);
        playerThread.start();
    }

    public void stop() {
        if (playerThread != null) {
            playerThread.stopPlaying();
            playerThread = null;
        }
    }
}
