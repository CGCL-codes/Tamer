package Utils.Som;

import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.URL;

public class Music extends Thread {

    private Player musicPlayer;

    private String musicPath;

    private boolean repeat;

    public Music(String name) {
        musicPath = name;
        repeat = false;
    }

    public void play() {
        repeat = true;
        start();
    }

    public void playOnce() {
        repeat = false;
        start();
    }

    public void halt() {
        musicPlayer.close();
    }

    public void run() {
        BufferedInputStream bis = null;
        URL f = Music.class.getResource(musicPath);
        do {
            try {
                FileInputStream fis = new FileInputStream(f.getPath());
                bis = new BufferedInputStream(fis);
                musicPlayer = new Player(bis);
                musicPlayer.play();
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (JavaLayerException ex) {
                System.out.println(ex.getMessage());
            }
        } while (repeat);
    }
}
