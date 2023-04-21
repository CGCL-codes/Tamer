package com.mainatom.ui.editors;

import com.mainatom.utils.*;

/**
 * Поле ввода для long
 */
public class EditorLong extends EditorCustomnum {

    private long _minValue;

    private long _maxValue;

    protected void onInit() throws Exception {
        super.onInit();
        _minValue = Long.MIN_VALUE;
        _maxValue = Long.MAX_VALUE;
    }

    /**
     * Максимально допустимое значение при вводе
     */
    public void setMaxValue(long maxValue) {
        _maxValue = maxValue;
    }

    public long getMaxValue() {
        return _maxValue;
    }

    /**
     * Минимально допустимое значение при вводе
     */
    public void setMinValue(long minValue) {
        _minValue = minValue;
    }

    public long getMinValue() {
        return _minValue;
    }

    protected long strToLong(String s) {
        return StringConvert.toLong(s, false);
    }

    protected boolean validateString(String s) {
        boolean valid = true;
        long value;
        try {
            value = strToLong(s);
        } catch (Exception e) {
            value = 0;
            valid = false;
        }
        if (!valid) {
            if (s.length() == 0) {
                valid = true;
            } else if (s.equals("-")) {
                valid = true;
            }
        }
        if (valid) {
            if (value < getMinValue() || value > getMaxValue()) {
                valid = false;
            }
        }
        return valid;
    }

    protected String doInsertString(String cur, int offs, String str) throws Exception {
        String s = cur.substring(0, offs) + str + cur.substring(offs);
        if (validateString(s)) {
            return str;
        } else {
            return "";
        }
    }
}
