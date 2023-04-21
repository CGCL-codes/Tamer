package org.lwjgl.test.fmod3;

import java.io.File;
import java.io.IOException;
import org.lwjgl.fmod3.FMOD;
import org.lwjgl.fmod3.FMODException;
import org.lwjgl.fmod3.FSound;
import org.lwjgl.fmod3.FSoundStream;

/**
 * 
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 2383 $
 * $Id: StreamPlayer.java 2383 2006-06-23 08:14:49Z matzon $ <br>
 */
public class StreamPlayer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage:\n StreamPlayer <file>");
            args = new String[] { "res" + File.separator + "phero.mp3" };
            System.out.println("Using default: " + args[0]);
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("No such file: " + args[0]);
            System.exit(0);
        }
        try {
            FMOD.create();
        } catch (FMODException fmode) {
            fmode.printStackTrace();
            System.exit(0);
        }
        System.out.println("Initializing FMOD");
        if (!FSound.FSOUND_Init(44100, 32, 0)) {
            System.out.println("Failed to initialize FMOD");
            System.out.println("Error: " + FMOD.FMOD_ErrorString(FSound.FSOUND_GetError()));
            System.exit(0);
        }
        System.out.println("Loading " + args[0]);
        FSoundStream stream = FSound.FSOUND_Stream_Open(args[0], FSound.FSOUND_NORMAL, 0, 0);
        if (stream != null) {
            FSound.FSOUND_Stream_Play(0, stream);
            System.out.println("Press enter to stop playing");
            try {
                System.in.read();
            } catch (IOException ioe) {
            }
            System.out.println("Done playing. Cleaning up");
            FSound.FSOUND_Stream_Stop(stream);
            FSound.FSOUND_Stream_Close(stream);
        } else {
            System.out.println("Unable to play: " + args[0]);
            System.out.println("Error: " + FMOD.FMOD_ErrorString(FSound.FSOUND_GetError()));
        }
        FSound.FSOUND_Close();
        FMOD.destroy();
        System.exit(0);
    }
}
