package net.sourceforge.ephemera.client;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;

public class Sounds extends Inputs {

    public IntBuffer alSource = BufferUtils.createIntBuffer(1);

    public IntBuffer alBuffer = BufferUtils.createIntBuffer(1);

    public boolean ALEXTvorbis = false;

    /**
	 * Initialises OpenAL
	 * 
	 */
    public void initAL() {
        banner("Starting OpenAL");
        try {
            AL.create();
            info("Engine\t: " + AL10.alGetString(AL10.AL_RENDERER));
            info("Version\t: " + AL10.alGetString(AL10.AL_VERSION));
            info("Vendor\t: " + AL10.alGetString(AL10.AL_VENDOR));
            info("Exts\t: " + AL10.alGetString(AL10.AL_EXTENSIONS));
            if (AL10.alIsExtensionPresent("AL_EXT_vorbis")) {
                ALEXTvorbis = true;
            } else {
                warning("Ogg Vorbis sound not available");
            }
        } catch (LWJGLException e) {
            error(e.getMessage());
        }
    }

    /**
	 * Loads all the various wave resources
	 * 
	 * @throws OpenALException
	 */
    public void loadSFX() throws OpenALException {
        banner("Loading Sounds");
        String[] wavs = { "menu_music.ogg", "wind.ogg" };
        alBuffer = BufferUtils.createIntBuffer(wavs.length);
        AL10.alGenBuffers(alBuffer);
        if (AL10.alGetError() != AL10.AL_NO_ERROR) throw new OpenALException("Unable to alGenBuffer");
        for (int i = 0; i < wavs.length; i++) {
            URL url = this.getClass().getClassLoader().getResource("sfx/" + wavs[i]);
            if (url != null) {
                info("Loading " + url.toString());
                if (ALEXTvorbis) {
                    ByteBuffer fbuf = getData("sfx/" + wavs[i]);
                    AL10.alBufferData(alBuffer.get(i), AL10.AL_FORMAT_VORBIS_EXT, fbuf, -1);
                    fbuf.clear();
                }
            } else {
                error("unable to load " + wavs[i]);
            }
        }
        info(Integer.toString(alBuffer.capacity()) + " sounds loaded");
    }
}
