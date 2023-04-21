package diuf.diva.hephaistk.undercity.agents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import diuf.diva.hephaistk.config.LoggingManager;

public class MusicPlayerPlayer extends Thread {

    private final String testTrack = "/musique/Gorillaz/Gorillaz";

    private boolean isPlaying = false;

    private int nBytesRead = 0, nBytesWritten = 0;

    private ArrayList<String> tracksUris = null;

    private int currentTrack = 0;

    private boolean playPlease = false;

    private boolean dirtyTrack = false;

    private boolean isRunning = true;

    public void run() {
        testplay();
    }

    private void testplay() {
        while (isRunning) {
            if (playPlease) {
                LoggingManager.getLogger().debug("niarfou + " + tracksUris.get(currentTrack));
                playPlease = false;
                dirtyTrack = false;
                testPlay(tracksUris.get(currentTrack));
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LoggingManager.getLogger().error(e);
                }
            }
        }
    }

    public void testPlay(String filename) {
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            rawplay(decodedFormat, din);
            in.close();
        } catch (Exception e) {
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            line.start();
            while (nBytesRead != -1 && !dirtyTrack) {
                if (isPlaying) {
                    nBytesRead = din.read(data, 0, data.length);
                    if (nBytesRead != -1) {
                        nBytesWritten = line.write(data, 0, nBytesRead);
                    }
                }
            }
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public void setAlbum(String albumLoc) {
        albumLoc = testTrack;
        tracksUris = new ArrayList<String>();
        File trackFolder = new File(albumLoc);
        if (trackFolder.isDirectory()) {
            File[] albumtracks = trackFolder.listFiles();
            for (File element : albumtracks) {
                tracksUris.add(element.getAbsolutePath());
            }
            currentTrack = 0;
            playPlease = true;
        }
    }

    public void previousTrack() {
        if (currentTrack > 0) {
            currentTrack--;
        }
        dirtyTrack = true;
        playPlease = true;
    }

    public void play() {
        isPlaying = true;
    }

    public void pause() {
        isPlaying = false;
    }

    public void nextTrack() {
        if (currentTrack < tracksUris.size() - 1) {
            currentTrack++;
        }
        dirtyTrack = true;
        playPlease = true;
    }
}
