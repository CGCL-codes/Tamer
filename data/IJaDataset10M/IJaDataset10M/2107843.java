package at.the.gogo.parkoid.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import at.the.gogo.parkoid.R;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {

    private final String TAG = getClass().getName();

    private static final String androidns = "http://schemas.android.com/apk/res/android";

    private static final String seekbarpreference = "org.tschekkomap.seekbarpreference";

    private static final int DEFAULT_VALUE = 50;

    private int mMaxValue = 100;

    private int mMinValue = 0;

    private int mInterval = 1;

    private int mCurrentValue;

    private String mUnits = "";

    private TextView mStatusText;

    public SeekBarPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setValuesFromXml(attrs);
    }

    public SeekBarPreference(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setValuesFromXml(attrs);
    }

    private void setValuesFromXml(final AttributeSet attrs) {
        mMaxValue = attrs.getAttributeIntValue(SeekBarPreference.androidns, "max", 100);
        mMinValue = attrs.getAttributeIntValue(SeekBarPreference.seekbarpreference, "min", 0);
        mUnits = attrs.getAttributeValue(SeekBarPreference.seekbarpreference, "units");
        try {
            final String newInterval = attrs.getAttributeValue(SeekBarPreference.seekbarpreference, "interval");
            if (newInterval != null) {
                mInterval = Integer.parseInt(newInterval);
            }
        } catch (final Exception e) {
            Log.e(TAG, "Invalid interval value", e);
        }
    }

    @Override
    protected View onCreateView(final ViewGroup parent) {
        RelativeLayout layout = null;
        try {
            final LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) mInflater.inflate(R.layout.seek_bar_preference, parent, false);
            final TextView title = (TextView) layout.findViewById(R.id.seekBarPrefTitle);
            title.setText(getTitle());
            final TextView summary = (TextView) layout.findViewById(R.id.seekBarPrefSummary);
            summary.setText(getSummary());
            final SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seekBarPrefBar);
            seekBar.setMax(mMaxValue - mMinValue);
            seekBar.setProgress(mCurrentValue + mMinValue);
            seekBar.setOnSeekBarChangeListener(this);
            mStatusText = (TextView) layout.findViewById(R.id.seekBarPrefValue);
            mStatusText.setText(String.valueOf(mCurrentValue));
            mStatusText.setMinimumWidth(30);
            final TextView units = (TextView) layout.findViewById(R.id.seekBarPrefUnits);
            units.setText(mUnits);
        } catch (final Exception e) {
            Log.e(TAG, "Error building seek bar preference", e);
        }
        return layout;
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        int newValue = progress + mMinValue;
        if (newValue > mMaxValue) {
            newValue = mMaxValue;
        } else if (newValue < mMinValue) {
            newValue = mMinValue;
        } else if ((mInterval != 1) && ((newValue % mInterval) != 0)) {
            newValue = Math.round(((float) newValue) / mInterval) * mInterval;
        }
        if (!callChangeListener(newValue)) {
            seekBar.setProgress(mCurrentValue + mMinValue);
            return;
        }
        mCurrentValue = newValue;
        mStatusText.setText(String.valueOf(newValue));
        persistInt(newValue);
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray ta, final int index) {
        final int defaultValue = ta.getInt(index, SeekBarPreference.DEFAULT_VALUE);
        return defaultValue;
    }

    @Override
    protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        if (restoreValue) {
            mCurrentValue = getPersistedInt(mCurrentValue);
        } else {
            final int temp = (Integer) defaultValue;
            persistInt(temp);
            mCurrentValue = temp;
        }
    }
}
