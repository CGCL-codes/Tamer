package gui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MusicThread {

    private String filename;

    private Player player;

    private volatile PlayThread t = null;

    private volatile LoopThread lt = null;

    class LoopThread extends Thread {

        private long interval = 0;

        /**
    	 * LoopThread constructor
    	 * @param i
    	 * interval
    	 */
        public LoopThread(long i) {
            interval = i * 1000;
        }

        public void run() {
            while (this != null) {
                try {
                    MusicThread.this.play();
                    Thread.sleep(interval);
                } catch (Exception e) {
                }
            }
        }
    }

    class PlayThread extends Thread {

        public void run() {
            try {
                player.play();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public MusicThread(String filename) {
        this.filename = filename;
        t = new PlayThread();
    }

    @SuppressWarnings("deprecation")
    public void close() {
        if (player != null) {
            player.close();
            player = null;
        }
        t.stop();
        lt.stop();
        t = null;
        lt = null;
    }

    /**
     * Play the audio file to the sound card
     */
    public void play() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }
        t = new PlayThread();
        t.start();
    }

    /**
     * Start the music loop
     * @param l
     */
    public void loopPlay(long l) {
        lt = new LoopThread(l);
        lt.start();
    }

    /**
     * Stop the music loop
     */
    public void loopStop() {
        if (lt != null && t.isAlive()) this.close();
    }

    /**
     * 
     * @return
     * true if the player is active, else false
     */
    public boolean isActive() {
        return player != null;
    }
}
