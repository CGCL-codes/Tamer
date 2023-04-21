package cdox.gui;

import cdox.*;
import cdox.util.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import javax.sound.sampled.*;

/**
 * This class plays a sound. It plays four different sounds (defined in /cdox/sounds/)
 * which (the file paths) are stored in a HashMap. To play a sound just call the static
 * play method with the a key of the sound you want to play. The four keys are:
 * <ul type="disk">
 * <li >question, a questioning muh</li>
 * <li>start, a real muh from hell</li>
 * <li>about, a dong-dong-dong-muh</li>
 * <li>alert, two eating cows</li>
 * </ul>
 * @author <a href="mailto:cdox@gmx.net">Rutger Bezema, Andreas Schmitz</a>
 * @version may 12th 2002
 */
public class PlaySounds implements Runnable {

    private static HashMap soundsMap = new HashMap();

    private static AudioInputStream ais;

    /**
     *A little static field, which insures the filled hashmap.
     */
    static {
        soundsMap.put("question", "/cdox/sounds/question.wav");
        soundsMap.put("start", "/cdox/sounds/kuh.wav");
        soundsMap.put("about", "/cdox/sounds/about.wav");
        soundsMap.put("alert", "/cdox/sounds/eat.wav");
    }

    /**
     *This method should be called to play a sound. The keys can be read in this class'
     *discription. It opens an AudioInputStream which is given to the clip to read it's
     *data from. 
     *@param key the key which sound to play.
     *@see #run
     */
    public static void play(String key) {
        if (soundsMap.containsKey(key)) {
            try {
                ais = AudioSystem.getAudioInputStream(PlaySounds.class.getResource((String) soundsMap.get(key)));
            } catch (Exception e) {
                if (e instanceof UnsupportedAudioFileException) {
                    Throwable noSound = new Throwable(CDox.getLocalizer().get("nosounderror"));
                    Preferences.userNodeForPackage(CDoxFrame.class).putBoolean("playsounds", false);
                    CDoxFrame.handleError(noSound, false);
                }
            }
            new Thread(new PlaySounds()).start();
        }
    }

    /**
     *This method is overwritten from the Runnable interface, here the real playing
     *starts. This method is called from play. It first creates a DataLine of type Clip()
     *if no DataLine is available from the AudioSystem, Exceptions are trown. 
     *@see #play
     */
    public void run() {
        if (ais != null) {
            try {
                DataLine.Info dl = new DataLine.Info(Clip.class, ais.getFormat());
                Clip cl = (Clip) AudioSystem.getLine(dl);
                cl.open(ais);
                cl.start();
                Thread.currentThread().sleep(cl.getMicrosecondLength() * 1000);
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    Throwable noSound = new Throwable(CDox.getLocalizer().get("nosounderror"));
                    Preferences.userNodeForPackage(CDoxFrame.class).putBoolean("playsounds", false);
                    CDoxFrame.handleError(noSound, false);
                }
            }
        }
    }
}
