package brickdoh.sound;

import java.net.URL;
import java.util.Hashtable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Will play sounds for CPU cycles.
 * @author Hj. Malthaner
 */
public class SoundPlayer {

    /**
     * We keep sounds in a cache to avoid loading them all over from disk.
     *
     * @author Hj. Malthaner
     */
    private static Hashtable soundCache = new Hashtable();

    private Clip loadClip(String file) {
        Clip clip = null;
        try {
            URL clipURL = getClass().getResource(file);
            AudioInputStream ais = AudioSystem.getAudioInputStream(clipURL);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception oops) {
            oops.printStackTrace();
        }
        return clip;
    }

    public void loadCache(String file) {
        Clip clip = loadClip(file);
        soundCache.put(file, clip);
    }

    /**
     * Read a sound file into the cache and play it
     *
     * @author Hj. Malthaner
     */
    public void playSoundFile(String file) {
        try {
            Clip clip;
            clip = (Clip) soundCache.get(file);
            if (clip == null) {
                loadCache(file);
            }
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }
}
