package com.fanfq.iflytek.voice;

import java.util.ArrayList;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IatActivity extends Activity implements OnClickListener, RecognizerDialogListener {

    private EditText mEditText;

    private Button mButton;

    private SharedPreferences mSharedPreferences;

    private RecognizerDialog iatDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iat);
        mEditText = (EditText) findViewById(R.id.output);
        mButton = (Button) findViewById(R.id.input);
        mButton.setOnClickListener(this);
        iatDialog = new RecognizerDialog(this, "appid=" + getString(R.string.app_id));
        iatDialog.setListener(this);
        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String engine = mSharedPreferences.getString(getString(R.string.preference_key_iat_engine), getString(R.string.preference_default_iat_engine));
        String[] engineEntries = getResources().getStringArray(R.array.preference_entries_iat_engine);
        String[] engineValues = getResources().getStringArray(R.array.preference_values_iat_engine);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.input:
                showIatDialog();
                break;
            case R.id.setting:
                startActivity(new Intent(this, IatPreferenceActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onEnd(SpeechError arg0) {
    }

    @Override
    public void onResults(ArrayList<RecognizerResult> results, boolean arg1) {
        StringBuilder builder = new StringBuilder();
        for (RecognizerResult recognizerResult : results) {
            builder.append(recognizerResult.text);
        }
        mEditText.append(builder);
        mEditText.setSelection(mEditText.length());
    }

    public void showIatDialog() {
        String engine = mSharedPreferences.getString(getString(R.string.preference_key_iat_engine), getString(R.string.preference_default_iat_engine));
        String area = null;
        if (IatPreferenceActivity.ENGINE_POI.equals(engine)) {
            final String defaultProvince = getString(R.string.preference_default_poi_province);
            String province = mSharedPreferences.getString(getString(R.string.preference_key_poi_province), defaultProvince);
            final String defaultCity = getString(R.string.preference_default_poi_city);
            String city = mSharedPreferences.getString(getString(R.string.preference_key_poi_city), defaultCity);
            if (!defaultProvince.equals(province)) {
                area = "area=" + province;
                if (!defaultCity.equals(city)) {
                    area += city;
                }
            }
        }
        iatDialog.setEngine(engine, area, null);
        String rate = mSharedPreferences.getString(getString(R.string.preference_key_iat_rate), getString(R.string.preference_default_iat_rate));
        if (rate.equals("rate8k")) iatDialog.setSampleRate(RATE.rate8k); else if (rate.equals("rate11k")) iatDialog.setSampleRate(RATE.rate11k); else if (rate.equals("rate16k")) iatDialog.setSampleRate(RATE.rate16k); else if (rate.equals("rate22k")) iatDialog.setSampleRate(RATE.rate22k);
        mEditText.setText(null);
        iatDialog.show();
    }
}
