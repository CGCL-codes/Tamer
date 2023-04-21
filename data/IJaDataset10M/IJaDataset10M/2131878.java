package ar.edu.uade.android.dialogo;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private static final String androidns = "http://schemas.android.com/apk/res/android";

    private SeekBar seekBar;

    private TextView porcentaje;

    private Context context;

    private String sufijo;

    private int valorDefault, valorMaximo, valorActual = 0;

    public SeekBarPreference(Context cntxt, AttributeSet attrs) {
        super(cntxt, attrs);
        context = cntxt;
        sufijo = attrs.getAttributeValue(androidns, "text");
        valorDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        valorMaximo = attrs.getAttributeIntValue(androidns, "max", 100);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);
        porcentaje = new TextView(context);
        porcentaje.setGravity(Gravity.CENTER_HORIZONTAL);
        porcentaje.setTextSize(24);
        layout.addView(porcentaje, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(this);
        layout.addView(seekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (shouldPersist()) valorActual = getPersistedInt(valorDefault);
        seekBar.setMax(valorMaximo);
        seekBar.setProgress(valorActual);
        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        seekBar.setMax(valorMaximo);
        seekBar.setProgress(valorActual);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        super.onSetInitialValue(restore, defaultValue);
        if (restore) valorActual = shouldPersist() ? getPersistedInt(valorDefault) : 0; else valorActual = (Integer) defaultValue;
    }

    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        String t = String.valueOf(value);
        porcentaje.setText(sufijo == null ? t : t.concat(sufijo));
        if (shouldPersist()) persistInt(value);
        callChangeListener(new Integer(value));
    }

    public void onStartTrackingTouch(SeekBar seek) {
    }

    public void onStopTrackingTouch(SeekBar seek) {
    }

    public void setMax(int max) {
        valorMaximo = max;
    }

    public int getMax() {
        return valorMaximo;
    }

    public void setProgress(int progress) {
        valorActual = progress;
        if (seekBar != null) seekBar.setProgress(progress);
    }

    public int getProgress() {
        return valorActual;
    }
}
