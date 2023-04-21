package org.csm.csmstaffhelper;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author exception
 */
public class Alarm {

    public static void openUrl(final String url) throws IOException, URISyntaxException {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    Desktop desk = Desktop.getDesktop();
                    desk.browse(new URI(url));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public static void playSound(final String soundFileName) throws FileNotFoundException, IOException {
        if (soundFileName.length() == 0) {
            return;
        }
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    InputStream in = new FileInputStream(soundFileName);
                    AudioStream as = new AudioStream(in);
                    AudioPlayer.player.start(as);
                } catch (IOException ex) {
                    Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }
}
