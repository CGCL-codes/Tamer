package org.coolreader.crengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Locale;
import android.content.Context;

/**
 * Wrapper for android.speech.tts.TextToSpeech
 * 
 * For compatibility with Android 1.5
 */
public class TTS {

    public static final Logger log = L.create("tts");

    public static final String ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";

    public static final int ERROR = 1;

    public static final int LANG_AVAILABLE = 0;

    public static final int LANG_COUNTRY_AVAILABLE = 1;

    public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;

    public static final int LANG_MISSING_DATA = -1;

    public static final int LANG_NOT_SUPPORTED = -2;

    public static final int QUEUE_ADD = 1;

    public static final int QUEUE_FLUSH = 0;

    public static final int SUCCESS = 0;

    public static final String KEY_PARAM_UTTERANCE_ID = "utteranceId";

    private static Class<?> textToSpeechClass;

    private static Constructor<?> textToSpeech_constructor;

    private static Class<?> onInitListenerClass;

    private static Class<?> onUtteranceCompletedListenerClass;

    private static Method textToSpeech_addEarcon;

    private static Method textToSpeech_addEarcon2;

    private static Method textToSpeech_addSpeech;

    private static Method textToSpeech_addSpeech2;

    private static Method textToSpeech_areDefaultsEnforced;

    private static Method textToSpeech_getDefaultEngine;

    private static Method textToSpeech_getLanguage;

    private static Method textToSpeech_isLanguageAvailable;

    private static Method textToSpeech_isSpeaking;

    private static Method textToSpeech_playEarcon;

    private static Method textToSpeech_playSilence;

    private static Method textToSpeech_setEngineByPackageName;

    private static Method textToSpeech_setLanguage;

    private static Method textToSpeech_setOnUtteranceCompletedListener;

    private static Method textToSpeech_setPitch;

    private static Method textToSpeech_setSpeechRate;

    private static Method textToSpeech_shutdown;

    private static Method textToSpeech_speak;

    private static Method textToSpeech_stop;

    private static Method textToSpeech_synthesizeToFile;

    private Object tts;

    private boolean initialized;

    /**
	 * @param listener is listener to call on init finished
	 * @return proxy
	 */
    private Object createOnInitProxy(final OnInitListener listener) {
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.d("invoking OnInit - " + method.getName());
                if ("onInit".equals(method.getName())) {
                    int status = (Integer) (args[0]);
                    log.i("OnInitListener.onInit() is called: status=" + status);
                    if (status == SUCCESS) initialized = true;
                    listener.onInit(status);
                }
                return null;
            }
        };
        return Proxy.newProxyInstance(onInitListenerClass.getClassLoader(), new Class[] { onInitListenerClass }, handler);
    }

    /**
	 * @param listener is listener to call on init finished
	 * @return proxy
	 */
    private Object createOnUtteranceCompletedListener(final OnUtteranceCompletedListener listener) {
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.d("invoking OnUtteranceCompletedListener - " + method.getName());
                if ("onUtteranceCompleted".equals(method.getName())) {
                    String id = (String) (args[0]);
                    log.d("OnUtteranceCompletedListener.onUtteranceCompleted() is called: id=" + id);
                    listener.onUtteranceCompleted(id);
                }
                return null;
            }
        };
        return Proxy.newProxyInstance(onUtteranceCompletedListenerClass.getClassLoader(), new Class[] { onUtteranceCompletedListenerClass }, handler);
    }

    private static boolean classesFound;

    static {
        try {
            onInitListenerClass = Class.forName("android.speech.tts.TextToSpeech$OnInitListener");
            onUtteranceCompletedListenerClass = Class.forName("android.speech.tts.TextToSpeech$OnUtteranceCompletedListener");
            textToSpeechClass = Class.forName("android.speech.tts.TextToSpeech");
            textToSpeech_constructor = textToSpeechClass.getConstructor(new Class[] { Context.class, onInitListenerClass });
            textToSpeech_addEarcon = textToSpeechClass.getMethod("addEarcon", new Class[] { String.class, String.class });
            textToSpeech_addEarcon2 = textToSpeechClass.getMethod("addEarcon", new Class[] { String.class, String.class, int.class });
            textToSpeech_addSpeech = textToSpeechClass.getMethod("addSpeech", new Class[] { String.class, String.class, int.class });
            textToSpeech_addSpeech2 = textToSpeechClass.getMethod("addSpeech", new Class[] { String.class, String.class });
            textToSpeech_areDefaultsEnforced = textToSpeechClass.getMethod("areDefaultsEnforced", new Class[] {});
            textToSpeech_getDefaultEngine = textToSpeechClass.getMethod("getDefaultEngine", new Class[] {});
            textToSpeech_getLanguage = textToSpeechClass.getMethod("getLanguage", new Class[] {});
            textToSpeech_isLanguageAvailable = textToSpeechClass.getMethod("isLanguageAvailable", new Class[] { Locale.class });
            textToSpeech_isSpeaking = textToSpeechClass.getMethod("isSpeaking", new Class[] {});
            textToSpeech_playEarcon = textToSpeechClass.getMethod("playEarcon", new Class[] { String.class, int.class, HashMap.class });
            textToSpeech_playSilence = textToSpeechClass.getMethod("playSilence", new Class[] { long.class, int.class, HashMap.class });
            textToSpeech_setEngineByPackageName = textToSpeechClass.getMethod("setEngineByPackageName", new Class[] { String.class });
            textToSpeech_setLanguage = textToSpeechClass.getMethod("setLanguage", new Class[] { Locale.class });
            textToSpeech_setOnUtteranceCompletedListener = textToSpeechClass.getMethod("setOnUtteranceCompletedListener", new Class[] { onUtteranceCompletedListenerClass });
            textToSpeech_setPitch = textToSpeechClass.getMethod("setPitch", new Class[] { float.class });
            textToSpeech_setSpeechRate = textToSpeechClass.getMethod("setSpeechRate", new Class[] { float.class });
            textToSpeech_shutdown = textToSpeechClass.getMethod("shutdown", new Class[] {});
            textToSpeech_speak = textToSpeechClass.getMethod("speak", new Class[] { String.class, int.class, HashMap.class });
            textToSpeech_stop = textToSpeechClass.getMethod("stop", new Class[] {});
            textToSpeech_synthesizeToFile = textToSpeechClass.getMethod("synthesizeToFile", new Class[] { String.class, HashMap.class, String.class });
            classesFound = true;
            L.i("TTS classes initialized successfully");
        } catch (Exception e) {
            L.e("Exception while initializing TTS classes: tts will be disabled", e);
            classesFound = false;
        }
    }

    public interface OnInitListener {

        void onInit(int status);
    }

    public interface OnUtteranceCompletedListener {

        /**
		 * Called to signal the completion of the synthesis of the utterance that was identified with the string parameter.
		 * @param utteranceId
		 */
        void onUtteranceCompleted(String utteranceId);
    }

    public interface OnTTSCreatedListener {

        void onCreated(TTS tts);
    }

    public TTS(Context context, OnInitListener listener) {
        if (!classesFound) {
            L.e("Cannot create TTS object : TTS classes not initialized");
            throw new IllegalStateException("Cannot instanciate TextToSpeech");
        }
        try {
            tts = textToSpeech_constructor.newInstance(context, createOnInitProxy(listener));
            L.i("TTS object created successfully");
        } catch (InvocationTargetException e) {
            classesFound = false;
            L.e("Cannot create TTS object", e);
            throw new IllegalStateException("Cannot instanciate TextToSpeech");
        } catch (IllegalArgumentException e) {
            classesFound = false;
            L.e("Cannot create TTS object", e);
            throw new IllegalStateException("Cannot instanciate TextToSpeech");
        } catch (InstantiationException e) {
            classesFound = false;
            L.e("Cannot create TTS object", e);
            throw new IllegalStateException("Cannot instanciate TextToSpeech");
        } catch (IllegalAccessException e) {
            classesFound = false;
            L.e("Cannot create TTS object", e);
            throw new IllegalStateException("Cannot instanciate TextToSpeech");
        }
    }

    public int addEarcon(String earcon, String filename) {
        try {
            return (Integer) textToSpeech_addEarcon.invoke(tts, earcon, filename);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int addEarcon(String earcon, String packagename, int resourceId) {
        try {
            return (Integer) textToSpeech_addEarcon2.invoke(tts, packagename, resourceId);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int addSpeech(String text, String packagename, int resourceId) {
        try {
            return (Integer) textToSpeech_addSpeech.invoke(tts, text, packagename, resourceId);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int addSpeech(String text, String filename) {
        try {
            return (Integer) textToSpeech_addSpeech2.invoke(tts, text, filename);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public boolean areDefaultsEnforced() {
        try {
            return (Boolean) textToSpeech_areDefaultsEnforced.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public String getDefaultEngine() {
        try {
            return (String) textToSpeech_getDefaultEngine.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public Locale getLanguage() {
        try {
            return (Locale) textToSpeech_getLanguage.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int isLanguageAvailable(Locale loc) {
        try {
            return (Integer) textToSpeech_isLanguageAvailable.invoke(tts, loc);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public boolean isSpeaking() {
        try {
            return (Boolean) textToSpeech_isSpeaking.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int playEarcon(String earcon, int queueMode, HashMap<String, String> params) {
        try {
            return (Integer) textToSpeech_playEarcon.invoke(tts, earcon, queueMode, params);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int playSilence(long durationInMs, int queueMode, HashMap<String, String> params) {
        try {
            return (Integer) textToSpeech_playSilence.invoke(tts, durationInMs, queueMode, params);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int setEngineByPackageName(String enginePackageName) {
        try {
            return (Integer) textToSpeech_setEngineByPackageName.invoke(tts, enginePackageName);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int setLanguage(Locale loc) {
        try {
            return (Integer) textToSpeech_setLanguage.invoke(tts, loc);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int setOnUtteranceCompletedListener(OnUtteranceCompletedListener listener) {
        try {
            return (Integer) textToSpeech_setOnUtteranceCompletedListener.invoke(tts, createOnUtteranceCompletedListener(listener));
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int setPitch(float pitch) {
        try {
            return (Integer) textToSpeech_setPitch.invoke(tts, pitch);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int setSpeechRate(float speechRate) {
        try {
            return (Integer) textToSpeech_setSpeechRate.invoke(tts, speechRate);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public void shutdown() {
        if (tts != null && initialized) try {
            initialized = false;
            textToSpeech_shutdown.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int speak(String text, int queueMode, HashMap<String, String> params) {
        try {
            L.v("speak(" + text + ")");
            int res = (Integer) textToSpeech_speak.invoke(tts, text, queueMode, params);
            L.v("speak() returned " + res);
            return res;
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int stop() {
        try {
            return (Integer) textToSpeech_stop.invoke(tts);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public int synthesizeToFile(String text, HashMap<String, String> params, String filename) {
        try {
            return (Integer) textToSpeech_synthesizeToFile.invoke(tts, text, params, filename);
        } catch (Exception e) {
            L.e("Exception while calling tts", e);
            throw new IllegalStateException(e);
        }
    }

    public boolean isInitialized() {
        return false;
    }

    public static boolean isFound() {
        return classesFound;
    }
}
