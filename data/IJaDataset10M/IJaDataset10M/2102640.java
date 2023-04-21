package Game;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class ManagerSound {

    private ManagerSound() {
    }

    private static ManagerSound instance = null;

    public static ManagerSound getIntance() {
        if (instance == null) {
            instance = new ManagerSound();
            instance.Play(Type.COLLISION);
        }
        return instance;
    }

    private Clip clip;

    public void Setup() {
    }

    private URL getURL(String file) {
        URL url = null;
        try {
            url = this.getClass().getResource(file);
        } catch (Exception e) {
        }
        return url;
    }

    public enum Type {

        COLLISION, SHOT, JUMP
    }

    public void Play(Type type) {
        AudioInputStream sound = null;
        switch(type) {
            case COLLISION:
                try {
                    sound = AudioSystem.getAudioInputStream(getURL("/Sons/abre_porta_1.wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SHOT:
                try {
                    sound = AudioSystem.getAudioInputStream(getURL("/Sons/Hit.wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
        ;
        try {
            clip.open(sound);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clip.start();
    }
}
