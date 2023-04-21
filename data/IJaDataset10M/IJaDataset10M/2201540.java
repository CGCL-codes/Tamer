package android.telephony;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.TextView;
import java.util.Locale;

/**
 * Watches a {@link TextView} and if a phone number is entered will format it using
 * {@link PhoneNumberUtils#formatNumber(Editable, int)}. The formatting is based on
 * the current system locale when this object is created and future locale changes
 * may not take effect on this instance.
 */
public class PhoneNumberFormattingTextWatcher implements TextWatcher {

    private static int sFormatType;

    private static Locale sCachedLocale;

    private boolean mFormatting;

    private boolean mDeletingHyphen;

    private int mHyphenStart;

    private boolean mDeletingBackward;

    public PhoneNumberFormattingTextWatcher() {
        if (sCachedLocale == null || sCachedLocale != Locale.getDefault()) {
            sCachedLocale = Locale.getDefault();
            sFormatType = PhoneNumberUtils.getFormatTypeForLocale(sCachedLocale);
        }
    }

    public synchronized void afterTextChanged(Editable text) {
        if (!mFormatting) {
            mFormatting = true;
            if (mDeletingHyphen && mHyphenStart > 0) {
                if (mDeletingBackward) {
                    if (mHyphenStart - 1 < text.length()) {
                        text.delete(mHyphenStart - 1, mHyphenStart);
                    }
                } else if (mHyphenStart < text.length()) {
                    text.delete(mHyphenStart, mHyphenStart + 1);
                }
            }
            PhoneNumberUtils.formatNumber(text, sFormatType);
            mFormatting = false;
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!mFormatting) {
            final int selStart = Selection.getSelectionStart(s);
            final int selEnd = Selection.getSelectionEnd(s);
            if (s.length() > 1 && count == 1 && after == 0 && s.charAt(start) == '-' && selStart == selEnd) {
                mDeletingHyphen = true;
                mHyphenStart = start;
                if (selStart == start + 1) {
                    mDeletingBackward = true;
                } else {
                    mDeletingBackward = false;
                }
            } else {
                mDeletingHyphen = false;
            }
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
