package jamesreneau.mmTagAndroid;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TTSHelper extends Activity {

    private boolean ttsInitialized = false;

    private TextToSpeech ttsEngine;

    private final int CHECK_TTSDATA_ACTIVITY = 9999;

    private final int LOAD_TTSDATA_ACTIVITY = 9998;

    public TTSHelper() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, CHECK_TTSDATA_ACTIVITY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TTSDATA_ACTIVITY) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsEngine = new TextToSpeech(this, onInitListener);
                ttsInitialized = true;
            } else {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivityForResult(installIntent, LOAD_TTSDATA_ACTIVITY);
            }
        }
        if (requestCode == LOAD_TTSDATA_ACTIVITY) {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, CHECK_TTSDATA_ACTIVITY);
        }
    }

    private OnInitListener onInitListener = new TextToSpeech.OnInitListener() {

        public void onInit(int stuff) {
            ttsInitialized = true;
        }
    };

    public void say(String text) {
        if (ttsInitialized) {
            ttsEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
