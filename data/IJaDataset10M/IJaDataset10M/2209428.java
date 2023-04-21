package org.dronus.al;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.dronus.gl.Buffers;
import org.hfbk.vis.Prefs;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

/**
 * helper functions for OpenAL
 * 
 * @author paul
 */
public class ALUtil {

    public static boolean closed = false;

    public static void enable(boolean enabled) {
        init();
        if (AL.isCreated()) AL10.alListenerf(AL10.AL_GAIN, enabled ? 1 : 0);
    }

    public static void init() {
        if (closed || AL.isCreated()) return;
        try {
            AL.create(null, 15, 22050, true);
            if (AL10.alGetError() != AL10.AL_NO_ERROR) throw new RuntimeException("ALUtil: Cannot open Al Context.");
            AL10.alListenerf(AL10.AL_GAIN, 0);
            AL10.alDopplerFactor(0);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    if (AL.isCreated()) {
                        closed = true;
                        AL.destroy();
                        if (Prefs.current.verbose) System.out.println("AL destroyed!");
                    }
                }
            });
        } catch (LWJGLException le) {
            closed = true;
            System.out.println("No audio support.");
        }
    }

    static String GetALErrorString(int err) {
        switch(err) {
            case AL10.AL_NO_ERROR:
                return ("AL_NO_ERROR");
            case AL10.AL_INVALID_NAME:
                return ("AL_INVALID_NAME");
            case AL10.AL_INVALID_VALUE:
                return ("AL_INVALID_VALUE");
            case AL10.AL_INVALID_OPERATION:
                return ("AL_INVALID_OPERATION");
            case AL10.AL_OUT_OF_MEMORY:
                return ("AL_OUT_OF_MEMORY");
        }
        ;
        return "ouch.";
    }

    static void check() {
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            throw new RuntimeException("OpenAL error " + error + " " + GetALErrorString(error));
        }
    }

    public static synchronized int createSource() {
        init();
        IntBuffer b = Buffers.intBuffer();
        AL10.alGenSources(b);
        int source = b.get(0);
        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 15f);
        AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, 3f);
        return source;
    }

    public static void setListenerOrientation(Vector3f dir, Vector3f up) {
        FloatBuffer b = BufferUtils.createFloatBuffer(6);
        dir.store(b);
        up.store(b);
        b.flip();
        AL10.alListener(AL10.AL_ORIENTATION, b);
    }

    /**
	 * creates a OpenAL audio buffer from WaveData
	 *
	 * @param wavFileName
	 * @return OpenAL buffer id
	 * @throws MalformedURLException 
	 */
    public static int createBuffer(WaveData wav) throws MalformedURLException {
        init();
        IntBuffer b = Buffers.intBuffer();
        AL10.alGenBuffers(b);
        int buffer = b.get(0);
        AL10.alBufferData(buffer, wav.format, wav.data, wav.samplerate);
        return buffer;
    }

    /**
	 * load a ms windows wav file
	 *
	 * @param wavFileName
	 * @return OpenAL buffer id
	 * @throws MalformedURLException 
	 */
    public static WaveData loadWav(String wavFileName) throws MalformedURLException {
        File f = new File(wavFileName);
        return WaveData.create(f.toURI().toURL());
    }

    public static short[] sample(WaveData wav, int step) {
        ByteBuffer b = wav.data;
        b.rewind();
        int len = b.remaining() / 2 / step;
        short[] sample = new short[len];
        for (int i = 0; i < len; i++) {
            sample[i] = b.getShort(i * 2 * step);
        }
        return sample;
    }
}
