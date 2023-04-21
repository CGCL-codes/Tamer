package jat.audio;

import jat.util.FileUtil;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <P>
 * The SoundPlayer Class provides a utility for playing sound files.
 *
 * @author 
 * @version 1.0
 */
public class SoundPlayer {

    /** Play the sound file.
	 * @param filename String containing the directory and filename
	 */
    public static void play(String filename) {
        Runplay play = new Runplay(filename);
        play.run();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

    public static void main(String[] args) {
        String fs = FileUtil.file_separator();
        String dir = FileUtil.getClassFilePath("jat.audio", "SoundPlayer") + "sounds";
        String file = dir + fs + "justwhat.wav";
        System.out.println("file = " + file);
        SoundPlayer.play(file);
        SoundPlayer.play(file);
    }

    private static class Runplay implements Runnable {

        String file;

        public Runplay(String f) {
            file = f;
        }

        public void run() {
            try {
                File file = new File(this.file);
                URI uri = file.toURI();
                System.out.println(uri);
                URL url = uri.toURL();
                System.out.println(url);
                String url_string = url.toString();
                url_string = url_string.replaceAll("file:/C", "file:///C");
                System.out.println("url_string = " + url_string);
                URL url_final = new URL(url_string);
                AudioClip clip = Applet.newAudioClip(url_final);
                clip.play();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
