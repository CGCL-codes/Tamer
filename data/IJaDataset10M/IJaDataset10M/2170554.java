package pgbennett.jampal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioFileFormat;

/** Common methods for audio examples.
 */
public class AudioCommon {

    private static boolean DEBUG = false;

    public static void setDebug(boolean bDebug) {
        DEBUG = bDebug;
    }

    /** TODO:
	 */
    public static void listSupportedTargetTypes() {
        String strMessage = "Supported target types:";
        AudioFileFormat.Type[] aTypes = AudioSystem.getAudioFileTypes();
        for (int i = 0; i < aTypes.length; i++) {
            strMessage += " " + aTypes[i].getExtension();
        }
        out(strMessage);
    }

    /**	Trying to get an audio file type for the passed extension.
		This works by examining all available file types. For each
		type, if the extension this type promisses to handle matches
		the extension we are trying to find a type for, this type is
		returned.
		If no appropriate type is found, null is returned.
	*/
    public static AudioFileFormat.Type findTargetType(String strExtension) {
        AudioFileFormat.Type[] aTypes = AudioSystem.getAudioFileTypes();
        for (int i = 0; i < aTypes.length; i++) {
            if (aTypes[i].getExtension().equals(strExtension)) {
                return aTypes[i];
            }
        }
        return null;
    }

    /** TODO:
	 */
    public static void listMixers() {
        out("Available Mixers:");
        Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
        for (int i = 0; i < aInfos.length; i++) {
            out(aInfos[i].getName());
        }
    }

    /**	TODO:
		This method tries to return a Mixer.Info whose name
		matches the passed name. If no matching Mixer.Info is
		found, null is returned.
	*/
    public static Mixer.Info getMixerInfo(String strMixerName) {
        Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
        for (int i = 0; i < aInfos.length; i++) {
            if (aInfos[i].getName().equals(strMixerName)) {
                return aInfos[i];
            }
        }
        return null;
    }

    /** TODO:
	 */
    public static TargetDataLine getTargetDataLine(String strMixerName, AudioFormat audioFormat, int nBufferSize) {
        TargetDataLine targetDataLine = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat, nBufferSize);
        try {
            if (strMixerName != null) {
                Mixer.Info mixerInfo = getMixerInfo(strMixerName);
                if (mixerInfo == null) {
                    out("AudioCommon.getTargetDataLine(): mixer not found: " + strMixerName + " Using default instead");
                } else {
                    Mixer mixer = AudioSystem.getMixer(mixerInfo);
                    targetDataLine = (TargetDataLine) mixer.getLine(info);
                }
            }
            if (targetDataLine == null) {
                if (DEBUG) {
                    out("AudioCommon.getTargetDataLine(): using default mixer");
                }
                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            }
            if (DEBUG) {
                out("AudioCommon.getTargetDataLine(): opening line...");
            }
            targetDataLine.open(audioFormat, nBufferSize);
            if (DEBUG) {
                out("AudioCommon.getTargetDataLine(): opened line");
            }
        } catch (LineUnavailableException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        if (DEBUG) {
            out("AudioCommon.getTargetDataLine(): returning line: " + targetDataLine);
        }
        return targetDataLine;
    }

    /** TODO:
	 */
    private static void out(String strMessage) {
        System.out.println(strMessage);
    }
}
