package org.sonify.sound.speech;

import org.openide.util.Lookup;
import org.sodbeans.tts.api.TextToSpeech;
import org.sodbeans.tts.api.TextToSpeechPriority;

/**
 * This class is a singleton that manages all speach that is 
 *  outgoing on the system. To load up the synth, call
 *  the instance() method then make called to speak().
 *
 * @author Andreas Stefik
 */
public class SpeechSoundManager implements SpeechSoundManagerInterface {

    private static TextToSpeech ttsService = Lookup.getDefault().lookup(TextToSpeech.class);

    private static SpeechSoundManagerInterface instance = null;

    private long time = 0;

    private long stopTime = 0;

    private static final long TIME_BETWEEN_CALLS = 50;

    private static final long TIME_BETWEEN_STOPS = 500;

    private boolean needsStopping = false;

    private Speak speak = null;

    private Stopper stopper;

    private static Thread talk;

    /** Creates a new instance of SpeechSoundManager */
    private SpeechSoundManager() {
        init();
        speak = new Speak();
        talk = new Thread(speak);
        talk.start();
    }

    private static class Speak implements Runnable {

        private static String text = "";

        private static boolean needToSpeak = false;

        private static boolean needToStop = false;

        private static synchronized String getText() {
            return text;
        }

        private static synchronized void stop() {
            needToStop = true;
        }

        private static synchronized void setText(String t) {
            text = t;
            needToSpeak = true;
        }

        @SuppressWarnings("static-access")
        public void run() {
            try {
                while (true) {
                    if (needToSpeak) {
                        if (needToStop) {
                            ttsService.stop();
                            needToStop = false;
                        }
                        ttsService.speak(text, TextToSpeechPriority.HIGH);
                        needToSpeak = false;
                    }
                    Thread.sleep(TIME_BETWEEN_CALLS);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void threadMessage(String string) {
        }
    }

    private static class Stopper implements Runnable {

        private static boolean needToStop = false;

        public void run() {
            try {
                while (true) {
                    if (needToStop) {
                        ttsService.stop();
                        needToStop = false;
                    }
                    Thread.sleep(TIME_BETWEEN_CALLS);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }

        private void threadMessage(String string) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    protected static SpeechSoundManagerInterface instance() {
        if (instance == null) {
            instance = new SpeechSoundManager();
        }
        return instance;
    }

    public void blockWhileSpeaking() {
    }

    public void stopSpeaking() {
        Speak.stop();
    }

    private void init() {
    }

    /** Implementation independent speaking 
     */
    public void speak(final String text) {
        if (talk.isInterrupted()) {
            int i = 0;
        }
        if (!talk.isAlive()) {
            int b = 0;
        }
        Speak.setText(text);
    }

    public void listAllVoices() {
    }
}
