package com.threerings.gwt.ui;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A text box that will only accept numbers.
 */
public class NumberTextBox extends TextBox {

    /**
     * Creates a text box that only accepts integers and has no max or visible character length.
     */
    public static NumberTextBox newIntBox() {
        return new NumberTextBox(false, 0, 0);
    }

    /**
     * Creates a text box that only accepts integers and uses the supplied length as its max and
     * visible character length.
     */
    public static NumberTextBox newIntBox(int length) {
        return new NumberTextBox(false, length, length);
    }

    /**
     * Creates a text box that only accepts integers and has the specified max and visible
     * character length.
     */
    public static NumberTextBox newIntBox(int maxLength, int visibleLength) {
        return new NumberTextBox(false, maxLength, visibleLength);
    }

    /**
     * Creates a text box that accepts floating point numbers and has no max or visible character
     * length.
     */
    public static NumberTextBox newFloatBox() {
        return new NumberTextBox(false, 0, 0);
    }

    /**
     * Creates a text box that accepts floating point numbers and uses the supplied length as its
     * max and visible character length.
     */
    public static NumberTextBox newFloatBox(int length) {
        return new NumberTextBox(false, length, length);
    }

    /**
     * Creates a text box that accepts floating point numbers and has the specified max and visible
     * character length.
     */
    public static NumberTextBox newFloatBox(int maxLength, int visibleLength) {
        return new NumberTextBox(false, maxLength, visibleLength);
    }

    /**
     * @param allowFloatingPoint If true, a single decimal point is part of the allowed character
     * set.  Otherwise, only [0-9]* is accepted.
     */
    public NumberTextBox(final boolean allowFloatingPoint, int maxLength, int visibleLength) {
        _allowFloatingPoint = allowFloatingPoint;
        addKeyUpHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                if (event.isShiftKeyDown() || event.getNativeKeyCode() > '9' || event.getNativeKeyCode() < '0') {
                    String text = getText();
                    boolean foundDecimal = !allowFloatingPoint;
                    for (int ii = 0; ii < text.length(); ii++) {
                        if (text.charAt(ii) > '9' || text.charAt(ii) < '0') {
                            if (text.charAt(ii) == '.' && !foundDecimal) {
                                foundDecimal = true;
                            } else {
                                text = text.substring(0, ii) + text.substring(ii + 1);
                                ii--;
                            }
                        }
                    }
                    setText(text);
                }
            }
        });
        if (maxLength > 0) {
            setMaxLength(maxLength);
        }
        if (visibleLength > 0) {
            setVisibleLength(visibleLength);
        }
    }

    /**
     * Sets the numeric contents of this text box. Passing null will clear the box.
     */
    public void setNumber(Number value) {
        setText(value == null ? "" : value.toString());
    }

    /**
     * Get the numberic value of this box. Returns 0 if the box is empty.
     */
    public Number getNumber() {
        String valstr = getText().length() == 0 ? "0" : getText();
        return _allowFloatingPoint ? (Number) (new Double(valstr)) : (Number) (new Long(valstr));
    }

    /**
     * Convenience method for setting a number and returning the text box in one step.
     */
    public NumberTextBox withValue(Number value) {
        setNumber(value);
        return this;
    }

    protected boolean _allowFloatingPoint;
}
