package it.diamonds.engine.audio;

import static org.lwjgl.openal.AL10.AL_NONE;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import it.diamonds.engine.ComponentInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import trb.sound.OggPlayer;

public final class OpenALAudio implements AudioInterface, ComponentInterface {

    private boolean audioStatus = false;

    private float[] listenerOrientation = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };

    private Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();

    private OggPlayer musicPlayer;

    private String musicDir = "data/audio/music/";

    private String musicExtension = ".ogg";

    private boolean isMusicLoaded = false;

    private OpenALAudio() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            throw new RuntimeException("Unable to initialize Audio System: " + e);
        }
        AL10.alDistanceModel(AL_NONE);
        initListener();
        musicPlayer = new OggPlayer();
        musicPlayer.setLoop(true);
        audioStatus = true;
    }

    public static AudioInterface create() {
        return new OpenALAudio();
    }

    public boolean isCreated() {
        return AL.isCreated();
    }

    public void shutDown() {
        musicPlayer.release();
        if (AL.isCreated()) {
            AL.destroy();
        }
        audioStatus = false;
    }

    public boolean isInitialised() {
        return audioStatus;
    }

    private void initListener() {
        AL10.alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f);
        AL10.alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        FloatBuffer floatBuff = BufferUtils.createFloatBuffer(7);
        floatBuff.put(listenerOrientation);
        AL10.alListener(AL_ORIENTATION, floatBuff);
    }

    public Sound createSound(String name) {
        Sound sound = sounds.get(name);
        if (sound == null) {
            sound = OpenALSound.create(name);
            sounds.put(name, sound);
        }
        return sound;
    }

    public void openMusic(String name) {
        try {
            File musicFile = new File(musicDir + name + musicExtension);
            musicPlayer.open(musicFile);
            isMusicLoaded = true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Audio can't find the background music file.");
        }
    }

    public void playMusic() {
        musicPlayer.playInNewThread(1);
    }

    public void stopMusic() {
        musicPlayer.stop();
    }

    public boolean isMusicLoaded() {
        return isMusicLoaded;
    }

    public boolean isMusicPlaying() {
        return musicPlayer.playing();
    }
}
