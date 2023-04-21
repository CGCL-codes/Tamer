package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.widget.TextView;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Displays a given time in a convenient human-readable foramt.
 *
 * @hide
 */
@RemoteView
public class DateTimeView extends TextView {

    private static final String TAG = "DateTimeView";

    private static final long TWELVE_HOURS_IN_MINUTES = 12 * 60;

    private static final long TWENTY_FOUR_HOURS_IN_MILLIS = 24 * 60 * 60 * 1000;

    private static final int SHOW_TIME = 0;

    private static final int SHOW_MONTH_DAY_YEAR = 1;

    Date mTime;

    long mTimeMillis;

    int mLastDisplay = -1;

    DateFormat mLastFormat;

    private boolean mAttachedToWindow;

    private long mUpdateTimeMillis;

    public DateTimeView(Context context) {
        super(context);
    }

    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onDetachedFromWindow();
        registerReceivers();
        mAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceivers();
        mAttachedToWindow = false;
    }

    @android.view.RemotableViewMethod
    public void setTime(long time) {
        Time t = new Time();
        t.set(time);
        t.second = 0;
        mTimeMillis = t.toMillis(false);
        mTime = new Date(t.year - 1900, t.month, t.monthDay, t.hour, t.minute, 0);
        update();
    }

    void update() {
        if (mTime == null) {
            return;
        }
        long start = System.nanoTime();
        int display;
        Date time = mTime;
        Time t = new Time();
        t.set(mTimeMillis);
        t.second = 0;
        t.hour -= 12;
        long twelveHoursBefore = t.toMillis(false);
        t.hour += 12;
        long twelveHoursAfter = t.toMillis(false);
        t.hour = 0;
        t.minute = 0;
        long midnightBefore = t.toMillis(false);
        t.monthDay++;
        long midnightAfter = t.toMillis(false);
        long nowMillis = System.currentTimeMillis();
        t.set(nowMillis);
        t.second = 0;
        nowMillis = t.normalize(false);
        choose_display: {
            if ((nowMillis >= midnightBefore && nowMillis < midnightAfter) || (nowMillis >= twelveHoursBefore && nowMillis < twelveHoursAfter)) {
                display = SHOW_TIME;
                break choose_display;
            }
            display = SHOW_MONTH_DAY_YEAR;
            break choose_display;
        }
        DateFormat format;
        if (display == mLastDisplay && mLastFormat != null) {
            format = mLastFormat;
        } else {
            switch(display) {
                case SHOW_TIME:
                    format = getTimeFormat();
                    break;
                case SHOW_MONTH_DAY_YEAR:
                    format = getDateFormat();
                    break;
                default:
                    throw new RuntimeException("unknown display value: " + display);
            }
            mLastFormat = format;
        }
        String text = format.format(mTime);
        setText(text);
        if (display == SHOW_TIME) {
            mUpdateTimeMillis = twelveHoursAfter > midnightAfter ? twelveHoursAfter : midnightAfter;
        } else {
            if (mTimeMillis < nowMillis) {
                mUpdateTimeMillis = 0;
            } else {
                mUpdateTimeMillis = twelveHoursBefore < midnightBefore ? twelveHoursBefore : midnightBefore;
            }
        }
        if (false) {
            Log.d(TAG, "update needed for '" + time + "' at '" + new Date(mUpdateTimeMillis) + "' - text=" + text);
        }
        long finish = System.nanoTime();
    }

    private DateFormat getTimeFormat() {
        int res;
        Context context = getContext();
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            res = R.string.twenty_four_hour_time_format;
        } else {
            res = R.string.twelve_hour_time_format;
        }
        String format = context.getString(res);
        return new SimpleDateFormat(format);
    }

    private DateFormat getDateFormat() {
        String format = Settings.System.getString(getContext().getContentResolver(), Settings.System.DATE_FORMAT);
        if (format == null || "".equals(format)) {
            return DateFormat.getDateInstance(DateFormat.SHORT);
        } else {
            try {
                return new SimpleDateFormat(format);
            } catch (IllegalArgumentException e) {
                return DateFormat.getDateInstance(DateFormat.SHORT);
            }
        }
    }

    private void registerReceivers() {
        Context context = getContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        context.registerReceiver(mBroadcastReceiver, filter);
        Uri uri = Settings.System.getUriFor(Settings.System.DATE_FORMAT);
        context.getContentResolver().registerContentObserver(uri, true, mContentObserver);
    }

    private void unregisterReceivers() {
        Context context = getContext();
        context.unregisterReceiver(mBroadcastReceiver);
        context.getContentResolver().unregisterContentObserver(mContentObserver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)) {
                if (System.currentTimeMillis() < mUpdateTimeMillis) {
                    return;
                }
            }
            mLastFormat = null;
            update();
        }
    };

    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            mLastFormat = null;
            update();
        }
    };
}
