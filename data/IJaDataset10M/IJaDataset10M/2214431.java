package org.lwjgl.test.fmod3;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.fmod3.FMOD;
import org.lwjgl.fmod3.FMODException;
import org.lwjgl.fmod3.FMusic;
import org.lwjgl.fmod3.FMusicModule;
import org.lwjgl.fmod3.FSound;

/**
 * <br>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 2286 $
 * $Id: MusicPlayer.java 2286 2006-03-23 19:32:21Z matzon $
 */
public class MusicPlayerMemory {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage:\n MusicPlayer <file>");
            args = new String[] { "res" + File.separator + "Missing_you.mod" };
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
            return;
        }
        System.out.println("Loading " + args[0]);
        ByteBuffer data = getData(args[0]);
        FMusicModule module = FMusic.FMUSIC_LoadSongEx(data, FSound.FSOUND_LOADMEMORY, null);
        if (module != null) {
            System.out.println("Loaded. Playing module of type: " + FMusic.FMUSIC_GetType(module));
            FMusic.FMUSIC_PlaySong(module);
            System.out.println("Press enter to stop playing");
            try {
                System.in.read();
            } catch (IOException ioe) {
            }
            FMusic.FMUSIC_StopSong(module);
            System.out.println("Done playing. Cleaning up");
            FMusic.FMUSIC_FreeSong(module);
        } else {
            System.out.println("Unable to play: " + args[0]);
            System.out.println("Error: " + FMOD.FMOD_ErrorString(FSound.FSOUND_GetError()));
        }
        FSound.FSOUND_Close();
        FMOD.destroy();
        System.exit(0);
    }

    /**
	 * Reads the file into a ByteBuffer
	 * 
	 * @param filename Name of file to load
	 * @return ByteBuffer containing file data
	 */
    protected static ByteBuffer getData(String filename) {
        ByteBuffer buffer = null;
        System.out.println("Attempting to load: " + filename);
        try {
            BufferedInputStream bis = new BufferedInputStream(StreamPlayerMemory.class.getClassLoader().getResourceAsStream(filename));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferLength = 4096;
            byte[] readBuffer = new byte[bufferLength];
            int read = -1;
            while ((read = bis.read(readBuffer, 0, bufferLength)) != -1) {
                baos.write(readBuffer, 0, read);
            }
            bis.close();
            buffer = ByteBuffer.allocateDirect(baos.size());
            buffer.order(ByteOrder.nativeOrder());
            buffer.put(baos.toByteArray());
            buffer.flip();
            System.out.println("loaded " + buffer.remaining() + " bytes");
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return buffer;
    }
}
