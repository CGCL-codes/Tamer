package com.android.internal.policy.impl;

import android.content.Context;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.telephony.IccCard;
import com.android.internal.widget.LinearLayoutWithDefaultTouchRecepient;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockPatternView.Cell;
import java.util.List;
import java.util.Date;

/**
 * This is the screen that shows the 9 circle unlock widget and instructs
 * the user how to unlock their device, or make an emergency call.
 */
class PatternUnlockScreen extends LinearLayoutWithDefaultTouchRecepient implements KeyguardScreen, KeyguardUpdateMonitor.InfoCallback, KeyguardUpdateMonitor.SimStateCallback {

    private static final boolean DEBUG = false;

    private static final String TAG = "UnlockScreen";

    private static final int PATTERN_CLEAR_TIMEOUT_MS = 2000;

    private static final int UNLOCK_PATTERN_WAKE_INTERVAL_MS = 7000;

    private static final int UNLOCK_PATTERN_WAKE_INTERVAL_FIRST_DOTS_MS = 2000;

    private static final int MIN_PATTERN_BEFORE_POKE_WAKELOCK = 2;

    private int mFailedPatternAttemptsSinceLastTimeout = 0;

    private int mTotalFailedPatternAttempts = 0;

    private CountDownTimer mCountdownTimer = null;

    private final LockPatternUtils mLockPatternUtils;

    private final KeyguardUpdateMonitor mUpdateMonitor;

    private final KeyguardScreenCallback mCallback;

    /**
     * whether there is a fallback option available when the pattern is forgotten.
     */
    private boolean mEnableFallback;

    private String mDateFormatString;

    private TextView mCarrier;

    private TextView mDate;

    private boolean mShowingBatteryInfo = false;

    private boolean mPluggedIn = false;

    private int mBatteryLevel = 100;

    private String mNextAlarm = null;

    private String mInstructions = null;

    private TextView mStatus1;

    private TextView mStatusSep;

    private TextView mStatus2;

    private LockPatternView mLockPatternView;

    private ViewGroup mFooterNormal;

    private ViewGroup mFooterForgotPattern;

    /**
     * Keeps track of the last time we poked the wake lock during dispatching
     * of the touch event, initalized to something gauranteed to make us
     * poke it when the user starts drawing the pattern.
     * @see #dispatchTouchEvent(android.view.MotionEvent)
     */
    private long mLastPokeTime = -UNLOCK_PATTERN_WAKE_INTERVAL_MS;

    /**
     * Useful for clearing out the wrong pattern after a delay
     */
    private Runnable mCancelPatternRunnable = new Runnable() {

        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    private Button mForgotPatternButton;

    private Button mEmergencyAlone;

    private Button mEmergencyTogether;

    private int mCreationOrientation;

    enum FooterMode {

        Normal, ForgotLockPattern, VerifyUnlocked
    }

    private void updateFooter(FooterMode mode) {
        switch(mode) {
            case Normal:
                mFooterNormal.setVisibility(View.VISIBLE);
                mFooterForgotPattern.setVisibility(View.GONE);
                break;
            case ForgotLockPattern:
                mFooterNormal.setVisibility(View.GONE);
                mFooterForgotPattern.setVisibility(View.VISIBLE);
                mForgotPatternButton.setVisibility(View.VISIBLE);
                break;
            case VerifyUnlocked:
                mFooterNormal.setVisibility(View.GONE);
                mFooterForgotPattern.setVisibility(View.GONE);
        }
    }

    /**
     * @param context The context.
     * @param configuration
     * @param lockPatternUtils Used to lookup lock pattern settings.
     * @param updateMonitor Used to lookup state affecting keyguard.
     * @param callback Used to notify the manager when we're done, etc.
     * @param totalFailedAttempts The current number of failed attempts.
     * @param enableFallback True if a backup unlock option is available when the user has forgotten
     *        their pattern (e.g they have a google account so we can show them the account based
     *        backup option).
     */
    PatternUnlockScreen(Context context, Configuration configuration, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor updateMonitor, KeyguardScreenCallback callback, int totalFailedAttempts) {
        super(context);
        mLockPatternUtils = lockPatternUtils;
        mUpdateMonitor = updateMonitor;
        mCallback = callback;
        mTotalFailedPatternAttempts = totalFailedAttempts;
        mFailedPatternAttemptsSinceLastTimeout = totalFailedAttempts % LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT;
        if (DEBUG) Log.d(TAG, "UnlockScreen() ctor: totalFailedAttempts=" + totalFailedAttempts + ", mFailedPat...=" + mFailedPatternAttemptsSinceLastTimeout);
        mCreationOrientation = configuration.orientation;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (mCreationOrientation != Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.keyguard_screen_unlock_portrait, this, true);
        } else {
            inflater.inflate(R.layout.keyguard_screen_unlock_landscape, this, true);
        }
        mCarrier = (TextView) findViewById(R.id.carrier);
        mDate = (TextView) findViewById(R.id.date);
        mDateFormatString = getContext().getString(R.string.full_wday_month_day_no_year);
        refreshTimeAndDateDisplay();
        mStatus1 = (TextView) findViewById(R.id.status1);
        mStatusSep = (TextView) findViewById(R.id.statusSep);
        mStatus2 = (TextView) findViewById(R.id.status2);
        resetStatusInfo();
        mLockPatternView = (LockPatternView) findViewById(R.id.lockPattern);
        mFooterNormal = (ViewGroup) findViewById(R.id.footerNormal);
        mFooterForgotPattern = (ViewGroup) findViewById(R.id.footerForgotPattern);
        final OnClickListener emergencyClick = new OnClickListener() {

            public void onClick(View v) {
                mCallback.takeEmergencyCallAction();
            }
        };
        mEmergencyAlone = (Button) findViewById(R.id.emergencyCallAlone);
        mEmergencyAlone.setFocusable(false);
        mEmergencyAlone.setOnClickListener(emergencyClick);
        mEmergencyTogether = (Button) findViewById(R.id.emergencyCallTogether);
        mEmergencyTogether.setFocusable(false);
        mEmergencyTogether.setOnClickListener(emergencyClick);
        refreshEmergencyButtonText();
        mForgotPatternButton = (Button) findViewById(R.id.forgotPattern);
        mForgotPatternButton.setText(R.string.lockscreen_forgot_pattern_button_text);
        mForgotPatternButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mCallback.forgotPattern(true);
            }
        });
        setDefaultTouchRecepient(mLockPatternView);
        mLockPatternView.setSaveEnabled(false);
        mLockPatternView.setFocusable(false);
        mLockPatternView.setOnPatternListener(new UnlockPatternListener());
        mLockPatternView.setInStealthMode(!mLockPatternUtils.isVisiblePatternEnabled());
        mLockPatternView.setTactileFeedbackEnabled(mLockPatternUtils.isTactileFeedbackEnabled());
        updateFooter(FooterMode.Normal);
        updateMonitor.registerInfoCallback(this);
        updateMonitor.registerSimStateCallback(this);
        setFocusableInTouchMode(true);
        mCarrier.setSelected(true);
        mCarrier.setTextColor(0xffffffff);
        mCarrier.setText(LockScreen.getCarrierString(mUpdateMonitor.getTelephonyPlmn(), mUpdateMonitor.getTelephonySpn()));
    }

    private void refreshEmergencyButtonText() {
        mLockPatternUtils.updateEmergencyCallButtonState(mEmergencyAlone);
        mLockPatternUtils.updateEmergencyCallButtonState(mEmergencyTogether);
    }

    public void setEnableFallback(boolean state) {
        if (DEBUG) Log.d(TAG, "setEnableFallback(" + state + ")");
        mEnableFallback = state;
    }

    private void resetStatusInfo() {
        mInstructions = null;
        mShowingBatteryInfo = mUpdateMonitor.shouldShowBatteryInfo();
        mPluggedIn = mUpdateMonitor.isDevicePluggedIn();
        mBatteryLevel = mUpdateMonitor.getBatteryLevel();
        mNextAlarm = mLockPatternUtils.getNextAlarm();
        updateStatusLines();
    }

    private void updateStatusLines() {
        if (mInstructions != null) {
            mStatus1.setText(mInstructions);
            if (TextUtils.isEmpty(mInstructions)) {
                mStatus1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                mStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_lock, 0, 0, 0);
            }
            mStatus1.setVisibility(View.VISIBLE);
            mStatusSep.setVisibility(View.GONE);
            mStatus2.setVisibility(View.GONE);
        } else if (mShowingBatteryInfo && mNextAlarm == null) {
            if (mPluggedIn) {
                if (mBatteryLevel >= 100) {
                    mStatus1.setText(getContext().getString(R.string.lockscreen_charged));
                } else {
                    mStatus1.setText(getContext().getString(R.string.lockscreen_plugged_in, mBatteryLevel));
                }
            } else {
                mStatus1.setText(getContext().getString(R.string.lockscreen_low_battery));
            }
            mStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_charging, 0, 0, 0);
            mStatus1.setVisibility(View.VISIBLE);
            mStatusSep.setVisibility(View.GONE);
            mStatus2.setVisibility(View.GONE);
        } else if (mNextAlarm != null && !mShowingBatteryInfo) {
            mStatus1.setText(mNextAlarm);
            mStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_alarm, 0, 0, 0);
            mStatus1.setVisibility(View.VISIBLE);
            mStatusSep.setVisibility(View.GONE);
            mStatus2.setVisibility(View.GONE);
        } else if (mNextAlarm != null && mShowingBatteryInfo) {
            mStatus1.setText(mNextAlarm);
            mStatusSep.setText("|");
            mStatus2.setText(getContext().getString(R.string.lockscreen_battery_short, Math.min(100, mBatteryLevel)));
            mStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_alarm, 0, 0, 0);
            if (mPluggedIn) {
                mStatus2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_charging, 0, 0, 0);
            } else {
                mStatus2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            mStatus1.setVisibility(View.VISIBLE);
            mStatusSep.setVisibility(View.VISIBLE);
            mStatus2.setVisibility(View.VISIBLE);
        } else {
            mStatus1.setText(R.string.lockscreen_pattern_instructions);
            mStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_idle_lock, 0, 0, 0);
            mStatus1.setVisibility(View.VISIBLE);
            mStatusSep.setVisibility(View.GONE);
            mStatus2.setVisibility(View.GONE);
        }
    }

    private void refreshTimeAndDateDisplay() {
        mDate.setText(DateFormat.format(mDateFormatString, new Date()));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final boolean result = super.dispatchTouchEvent(ev);
        if (result && ((SystemClock.elapsedRealtime() - mLastPokeTime) > (UNLOCK_PATTERN_WAKE_INTERVAL_MS - 100))) {
            mLastPokeTime = SystemClock.elapsedRealtime();
        }
        return result;
    }

    /** {@inheritDoc} */
    public void onRefreshBatteryInfo(boolean showBatteryInfo, boolean pluggedIn, int batteryLevel) {
        mShowingBatteryInfo = showBatteryInfo;
        mPluggedIn = pluggedIn;
        mBatteryLevel = batteryLevel;
        updateStatusLines();
    }

    /** {@inheritDoc} */
    public void onTimeChanged() {
        refreshTimeAndDateDisplay();
    }

    /** {@inheritDoc} */
    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn) {
        mCarrier.setText(LockScreen.getCarrierString(plmn, spn));
    }

    /** {@inheritDoc} */
    public void onRingerModeChanged(int state) {
    }

    /** {@inheritDoc} */
    public void onSimStateChanged(IccCard.State simState) {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (LockPatternKeyguardView.DEBUG_CONFIGURATION) {
            Log.v(TAG, "***** PATTERN ATTACHED TO WINDOW");
            Log.v(TAG, "Cur orient=" + mCreationOrientation + ", new config=" + getResources().getConfiguration());
        }
        if (getResources().getConfiguration().orientation != mCreationOrientation) {
            mCallback.recreateMe(getResources().getConfiguration());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (LockPatternKeyguardView.DEBUG_CONFIGURATION) {
            Log.v(TAG, "***** PATTERN CONFIGURATION CHANGED");
            Log.v(TAG, "Cur orient=" + mCreationOrientation + ", new config=" + getResources().getConfiguration());
        }
        if (newConfig.orientation != mCreationOrientation) {
            mCallback.recreateMe(newConfig);
        }
    }

    /** {@inheritDoc} */
    public void onKeyboardChange(boolean isKeyboardOpen) {
    }

    /** {@inheritDoc} */
    public boolean needsInput() {
        return false;
    }

    /** {@inheritDoc} */
    public void onPause() {
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
            mCountdownTimer = null;
        }
    }

    /** {@inheritDoc} */
    public void onResume() {
        resetStatusInfo();
        mLockPatternView.enableInput();
        mLockPatternView.setEnabled(true);
        mLockPatternView.clearPattern();
        mForgotPatternButton.setVisibility(mCallback.doesFallbackUnlockScreenExist() ? View.VISIBLE : View.INVISIBLE);
        long deadline = mLockPatternUtils.getLockoutAttemptDeadline();
        if (deadline != 0) {
            handleAttemptLockout(deadline);
        }
        if (mCallback.isVerifyUnlockOnly()) {
            updateFooter(FooterMode.VerifyUnlocked);
        } else if (mEnableFallback && (mTotalFailedPatternAttempts >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)) {
            updateFooter(FooterMode.ForgotLockPattern);
        } else {
            updateFooter(FooterMode.Normal);
        }
        refreshEmergencyButtonText();
    }

    /** {@inheritDoc} */
    public void cleanUp() {
        mUpdateMonitor.removeCallback(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            onResume();
        }
    }

    private class UnlockPatternListener implements LockPatternView.OnPatternListener {

        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mCancelPatternRunnable);
        }

        public void onPatternCleared() {
        }

        public void onPatternCellAdded(List<Cell> pattern) {
            if (pattern.size() > MIN_PATTERN_BEFORE_POKE_WAKELOCK) {
                mCallback.pokeWakelock(UNLOCK_PATTERN_WAKE_INTERVAL_MS);
            } else {
                mCallback.pokeWakelock(UNLOCK_PATTERN_WAKE_INTERVAL_FIRST_DOTS_MS);
            }
        }

        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if (mLockPatternUtils.checkPattern(pattern)) {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                mInstructions = "";
                updateStatusLines();
                mCallback.keyguardDone(true);
                mCallback.reportSuccessfulUnlockAttempt();
            } else {
                if (pattern.size() > MIN_PATTERN_BEFORE_POKE_WAKELOCK) {
                    mCallback.pokeWakelock(UNLOCK_PATTERN_WAKE_INTERVAL_MS);
                }
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mTotalFailedPatternAttempts++;
                    mFailedPatternAttemptsSinceLastTimeout++;
                    mCallback.reportFailedUnlockAttempt();
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    long deadline = mLockPatternUtils.setLockoutAttemptDeadline();
                    handleAttemptLockout(deadline);
                } else {
                    mInstructions = getContext().getString(R.string.lockscreen_pattern_wrong);
                    updateStatusLines();
                    mLockPatternView.postDelayed(mCancelPatternRunnable, PATTERN_CLEAR_TIMEOUT_MS);
                }
            }
        }
    }

    private void handleAttemptLockout(long elapsedRealtimeDeadline) {
        mLockPatternView.clearPattern();
        mLockPatternView.setEnabled(false);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        mCountdownTimer = new CountDownTimer(elapsedRealtimeDeadline - elapsedRealtime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                mInstructions = getContext().getString(R.string.lockscreen_too_many_failed_attempts_countdown, secondsRemaining);
                updateStatusLines();
            }

            @Override
            public void onFinish() {
                mLockPatternView.setEnabled(true);
                mInstructions = getContext().getString(R.string.lockscreen_pattern_instructions);
                updateStatusLines();
                mFailedPatternAttemptsSinceLastTimeout = 0;
                if (mEnableFallback) {
                    updateFooter(FooterMode.ForgotLockPattern);
                } else {
                    updateFooter(FooterMode.Normal);
                }
            }
        }.start();
    }

    public void onPhoneStateChanged(String newState) {
        refreshEmergencyButtonText();
    }
}
