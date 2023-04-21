package hablame.util;

import hablame.HablameApp;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author Juan Fernández
 */
public class SoundPlayer extends Thread {

    private String _audiofile;

    public void run() {
        try {
            InputStream inputStream = HablameApp.class.getResourceAsStream(_audiofile);
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            System.out.println("Problem playing file " + _audiofile);
            System.out.println(e);
        }
    }

    public void setAudioFile(String audioFile) {
        _audiofile = audioFile;
    }
}
