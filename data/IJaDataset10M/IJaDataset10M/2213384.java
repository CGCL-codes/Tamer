package it.diamonds.engine;

import java.util.ArrayList;
import it.diamonds.engine.audio.AudioInterface;
import it.diamonds.engine.audio.OpenALAudio;
import it.diamonds.engine.input.InputDeviceInterface;
import it.diamonds.engine.input.LWJGLKeyboard;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.LWJGLEngine;
import it.diamonds.tests.mocks.MockAudio;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockKeyboard;
import it.diamonds.tests.mocks.MockTimer;
import org.lwjgl.Sys;

public final class Environment {

    private static final String MSG_NO_AUDIO = "Couldn't initialize Audio System, starting game with no sounds.\nReadme.txt contain many solutions to this problem.";

    private Config config;

    private ArrayList<ComponentInterface> environmentComponents;

    private AbstractEngine engine;

    private AudioInterface audio;

    private InputDeviceInterface keyboard;

    private TimerInterface timer;

    private boolean fullscreen = false;

    private int width;

    private int height;

    private Environment() {
        config = Config.create();
        environmentComponents = new ArrayList<ComponentInterface>();
    }

    public static Environment create(String title) {
        Environment environment = new Environment();
        environment.detectInitialConfigurationValues();
        environment.createEngine(title);
        environment.createAudio();
        environment.createKeyboard();
        environment.createTimer();
        return environment;
    }

    public static Environment createForTesting(int width, int height, String title) {
        Environment environment = new Environment();
        environment.createEngineForTesting(width, height);
        environment.createAudioForTesting();
        environment.createKeyboardForTesting();
        environment.createTimerForTesting();
        return environment;
    }

    public Config getConfig() {
        return config;
    }

    private void detectInitialConfigurationValues() {
        width = config.getInteger("width");
        height = config.getInteger("height");
        if (config.getInteger("FullScreen") != 0) {
            fullscreen = true;
        }
    }

    private AbstractEngine createEngine(String title) {
        engine = LWJGLEngine.create(width, height, title, fullscreen);
        environmentComponents.add((ComponentInterface) engine);
        return engine;
    }

    private AbstractEngine createEngineForTesting(int width, int height) {
        engine = new MockEngine(width, height);
        return engine;
    }

    public AbstractEngine getEngine() {
        return engine;
    }

    private AudioInterface createAudio() {
        try {
            audio = OpenALAudio.create();
            environmentComponents.add((ComponentInterface) audio);
        } catch (RuntimeException exception) {
            Sys.alert("Warning", MSG_NO_AUDIO);
            createAudioForTesting();
        }
        audio.openMusic("theme_rock");
        return audio;
    }

    private AudioInterface createAudioForTesting() {
        audio = MockAudio.create();
        return audio;
    }

    public AudioInterface getAudio() {
        return audio;
    }

    private InputDeviceInterface createKeyboard() {
        keyboard = LWJGLKeyboard.create();
        environmentComponents.add((ComponentInterface) keyboard);
        return keyboard;
    }

    private InputDeviceInterface createKeyboardForTesting() {
        keyboard = MockKeyboard.create();
        return keyboard;
    }

    public InputDeviceInterface getKeyboard() {
        return keyboard;
    }

    private TimerInterface createTimer() {
        timer = Timer.create();
        return timer;
    }

    private TimerInterface createTimerForTesting() {
        timer = MockTimer.create();
        return timer;
    }

    public TimerInterface getTimer() {
        return timer;
    }

    public void shutDownAll() {
        for (ComponentInterface component : environmentComponents) {
            component.shutDown();
        }
    }
}
