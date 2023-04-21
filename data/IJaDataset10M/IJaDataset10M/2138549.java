package org.imogene.android.widget;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import org.imogene.android.template.R;
import org.imogene.android.widget.NumberPicker.OnChangedListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * A view for selecting a month / year / day based on a calendar like layout.
 *
 * For a dialog using this view, see {@link android.app.DatePickerDialog}.
 */
public class DateTimePicker extends FrameLayout {

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
     */
    private static final OnDateTimeChangedListener NO_OP_CHANGE_LISTENER = new OnDateTimeChangedListener() {

        public void onDateTimeChanged(DateTimePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        }
    };

    private final NumberPicker mDayPicker;

    private final NumberPicker mMonthPicker;

    private final NumberPicker mYearPicker;

    private final NumberPicker mHourPicker;

    private final NumberPicker mMinutePicker;

    private final Button mAmPmButton;

    private final String mAmText;

    private final String mPmText;

    /**
     * How we notify users the date has changed.
     */
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    private int mDay;

    private int mMonth;

    private int mYear;

    private int mCurrentHour = 0;

    private int mCurrentMinute = 0;

    private Boolean mIs24HourView = false;

    private boolean mIsAm;

    /**
     * The callback used to indicate the user changes the date.
     */
    public interface OnDateTimeChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onDateTimeChanged(DateTimePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
    }

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ig_date_time_picker, this, true);
        mDayPicker = (NumberPicker) findViewById(R.id.ig_day);
        mDayPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mDayPicker.setSpeed(100);
        mDayPicker.setOnChangeListener(new OnChangedListener() {

            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                onDateTimeChanged();
            }
        });
        mMonthPicker = (NumberPicker) findViewById(R.id.ig_month);
        mMonthPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        DateFormatSymbols dfs = new DateFormatSymbols();
        mMonthPicker.setRange(1, 12, dfs.getShortMonths());
        mMonthPicker.setSpeed(200);
        mMonthPicker.setOnChangeListener(new OnChangedListener() {

            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal - 1;
                onDateTimeChanged();
                updateDaySpinner();
            }
        });
        mYearPicker = (NumberPicker) findViewById(R.id.ig_year);
        mYearPicker.setSpeed(100);
        mYearPicker.setOnChangeListener(new OnChangedListener() {

            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                onDateTimeChanged();
            }
        });
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker);
        int mStartYear = a.getInt(R.styleable.DateTimePicker_startYear, DEFAULT_START_YEAR);
        int mEndYear = a.getInt(R.styleable.DateTimePicker_endYear, DEFAULT_END_YEAR);
        mYearPicker.setRange(mStartYear, mEndYear);
        a.recycle();
        mHourPicker = (NumberPicker) findViewById(R.id.ig_hour);
        mHourPicker.setOnChangeListener(new NumberPicker.OnChangedListener() {

            public void onChanged(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentHour = newVal;
                if (!mIs24HourView) {
                    if (mCurrentHour == 12) {
                        mCurrentHour = 0;
                    }
                    if (!mIsAm) {
                        mCurrentHour += 12;
                    }
                }
                onDateTimeChanged();
            }
        });
        mMinutePicker = (NumberPicker) findViewById(R.id.ig_minute);
        mMinutePicker.setRange(0, 59);
        mMinutePicker.setSpeed(100);
        mMinutePicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mMinutePicker.setOnChangeListener(new NumberPicker.OnChangedListener() {

            public void onChanged(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentMinute = newVal;
                onDateTimeChanged();
            }
        });
        mAmPmButton = (Button) findViewById(R.id.ig_amPm);
        configurePickerRanges();
        mIsAm = (mCurrentHour < 12);
        String[] dfsAmPm = dfs.getAmPmStrings();
        mAmText = dfsAmPm[Calendar.AM];
        mPmText = dfsAmPm[Calendar.PM];
        mAmPmButton.setText(mIsAm ? mAmText : mPmText);
        mAmPmButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                requestFocus();
                if (mIsAm) {
                    if (mCurrentHour < 12) {
                        mCurrentHour += 12;
                    }
                } else {
                    if (mCurrentHour >= 12) {
                        mCurrentHour -= 12;
                    }
                }
                mIsAm = !mIsAm;
                mAmPmButton.setText(mIsAm ? mAmText : mPmText);
                onDateTimeChanged();
            }
        });
        Calendar cal = Calendar.getInstance();
        init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), NO_OP_CHANGE_LISTENER);
        if (!isEnabled()) {
            setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDayPicker.setEnabled(enabled);
        mMonthPicker.setEnabled(enabled);
        mYearPicker.setEnabled(enabled);
        mMinutePicker.setEnabled(enabled);
        mHourPicker.setEnabled(enabled);
        mAmPmButton.setEnabled(enabled);
    }

    public void updateDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        setCurrentHour(hourOfDay);
        setCurrentMinute(minute);
        updateSpinners();
    }

    private static class SavedState extends BaseSavedState {

        private final int mYear;

        private final int mMonth;

        private final int mDay;

        private final int mHour;

        private final int mMinute;

        /**
         * Constructor called from {@link DateTimePicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int year, int month, int day, int hour, int minute) {
            super(superState);
            mYear = year;
            mMonth = month;
            mDay = day;
            mHour = hour;
            mMinute = minute;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mYear = in.readInt();
            mMonth = in.readInt();
            mDay = in.readInt();
            mHour = in.readInt();
            mMinute = in.readInt();
        }

        public int getYear() {
            return mYear;
        }

        public int getMonth() {
            return mMonth;
        }

        public int getDay() {
            return mDay;
        }

        public int getHour() {
            return mHour;
        }

        public int getMinute() {
            return mMinute;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mYear);
            dest.writeInt(mMonth);
            dest.writeInt(mDay);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * Override so we are in complete control of save / restore for this widget.
     */
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mYear, mMonth, mDay, mCurrentHour, mCurrentMinute);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mYear = ss.getYear();
        mMonth = ss.getMonth();
        mDay = ss.getDay();
        mCurrentHour = ss.getHour();
        mCurrentMinute = ss.getMinute();
    }

    /**
     * Initialize the state.
     * @param year The initial year.
     * @param monthOfYear The initial month.
     * @param dayOfMonth The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth, int hour, int minute, OnDateTimeChangedListener onDateTimeChangedListener) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        mOnDateTimeChangedListener = onDateTimeChangedListener;
        setCurrentHour(hour);
        setCurrentMinute(minute);
        updateSpinners();
    }

    private void updateSpinners() {
        updateDaySpinner();
        mYearPicker.setCurrent(mYear);
        mMonthPicker.setCurrent(mMonth + 1);
    }

    private void updateDaySpinner() {
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth, mDay);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayPicker.setRange(1, max);
        mDayPicker.setCurrent(mDay);
    }

    private void configurePickerRanges() {
        if (mIs24HourView) {
            mHourPicker.setRange(0, 23);
            mHourPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
            mAmPmButton.setVisibility(View.GONE);
        } else {
            mHourPicker.setRange(1, 12);
            mHourPicker.setFormatter(null);
            mAmPmButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return The current hour (0-23).
     */
    public Integer getCurrentHour() {
        return mCurrentHour;
    }

    /**
     * Set the current hour.
     */
    public void setCurrentHour(Integer currentHour) {
        this.mCurrentHour = currentHour;
        updateHourDisplay();
    }

    /**
     * Set whether in 24 hour or AM/PM mode.
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    public void setIs24HourView(Boolean is24HourView) {
        if (mIs24HourView.equals(is24HourView)) {
            mIs24HourView = is24HourView;
            configurePickerRanges();
            updateHourDisplay();
        }
    }

    /**
     * @return true if this is in 24 hour view else false.
     */
    public boolean is24HourView() {
        return mIs24HourView;
    }

    /**
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
        return mCurrentMinute;
    }

    /**
     * Set the current minute (0-59).
     */
    public void setCurrentMinute(Integer currentMinute) {
        this.mCurrentMinute = currentMinute;
        updateMinuteDisplay();
    }

    @Override
    public int getBaseline() {
        return mHourPicker.getBaseline();
    }

    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentHour;
        if (!mIs24HourView) {
            if (currentHour > 12) currentHour -= 12; else if (currentHour == 0) currentHour = 12;
        }
        mHourPicker.setCurrent(currentHour);
        mIsAm = mCurrentHour < 12;
        mAmPmButton.setText(mIsAm ? mAmText : mPmText);
        onDateTimeChanged();
    }

    /**
     * Set the state of the spinners appropriate to the current minute.
     */
    private void updateMinuteDisplay() {
        mMinutePicker.setCurrent(mCurrentMinute);
        mOnDateTimeChangedListener.onDateTimeChanged(this, mYear, mMonth, mDay, mCurrentHour, mCurrentMinute);
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, mYear, mMonth, mDay, mCurrentHour, mCurrentMinute);
        }
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDayOfMonth() {
        return mDay;
    }
}
