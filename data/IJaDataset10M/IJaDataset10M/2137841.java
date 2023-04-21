package src.engine;

import com.xith3d.scenegraph.View;
import com.xith3d.sound.drivers.javasound.SoundDriverImpl;

public class AudioEngineJavaSound extends AudioEngine {

    public AudioEngineJavaSound(View view) {
        super(view);
    }

    /**
	 * Creates the soundDriver.
	 */
    protected void setupSoundDriver() {
        soundDriver = new SoundDriverImpl();
    }
}
