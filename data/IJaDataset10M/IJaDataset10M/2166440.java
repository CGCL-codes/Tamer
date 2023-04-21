package com.android.mms.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.NumberPicker;
import com.android.mms.R;

/**
 * A dialog that prompts the user for the message deletion limits.
 */
public class NumberPickerDialog extends AlertDialog implements OnClickListener {

    private int mInitialNumber;

    private static final String NUMBER = "number";

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnNumberSetListener {

        /**
         * @param number The number that was set.
         */
        void onNumberSet(int number);
    }

    private final NonWrapNumberPicker mNumberPicker;

    private final OnNumberSetListener mCallback;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param number The initial number.
     */
    public NumberPickerDialog(Context context, OnNumberSetListener callBack, int number, int rangeMin, int rangeMax, int title, int desc) {
        this(context, com.android.internal.R.style.Theme_Dialog_Alert, callBack, number, rangeMin, rangeMax, title, desc);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param number The initial number.
     */
    public NumberPickerDialog(Context context, int theme, OnNumberSetListener callBack, int number, int rangeMin, int rangeMax, int title, int desc) {
        super(context, theme);
        mCallback = callBack;
        mInitialNumber = number;
        setTitle(title);
        setButton(context.getText(R.string.set), this);
        setButton2(context.getText(R.string.no), (OnClickListener) null);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker, null);
        setView(view);
        mNumberPicker = (NonWrapNumberPicker) view.findViewById(R.id.number_picker);
        ((TextView) view.findViewById(R.id.desc)).setText(desc);
        mNumberPicker.setRange(rangeMin, rangeMax);
        mNumberPicker.setCurrent(number);
        mNumberPicker.setSpeed(150);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mNumberPicker.clearFocus();
            mCallback.onNumberSet(mNumberPicker.getCurrent());
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(NUMBER, mNumberPicker.getCurrent());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int number = savedInstanceState.getInt(NUMBER);
        mNumberPicker.setCurrent(number);
    }

    public static class NonWrapNumberPicker extends NumberPicker {

        public NonWrapNumberPicker(Context context) {
            this(context, null);
        }

        public NonWrapNumberPicker(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        @SuppressWarnings({ "UnusedDeclaration" })
        public NonWrapNumberPicker(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs);
        }

        @Override
        protected void changeCurrent(int current) {
            if (current > mEnd) {
                current = mEnd;
            } else if (current < mStart) {
                current = mStart;
            }
            super.changeCurrent(current);
        }
    }
}
