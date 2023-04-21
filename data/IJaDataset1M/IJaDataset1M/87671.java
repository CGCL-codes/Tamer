package com.hifiremote.jp1;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public class NumberParameter extends Parameter {

    public NumberParameter(String name, int defaultValue) {
        this(name, defaultValue, 8);
    }

    public NumberParameter(String name, int defaultValue, int bits) {
        this(name, defaultValue, 0, ((1 << bits) - 1));
    }

    public NumberParameter(String name, int defaultValue, int min, int max) {
        super(name);
        this.min = min;
        this.max = max;
        IntOrNullFormatter formatter = new IntOrNullFormatter(min, max);
        formatter.setAllowsInvalid(false);
        tf = new JFormattedTextField(formatter);
        String helpText = "Enter a number in the range " + min + ".." + max + ".";
        if (defaultValue != -1) helpText += "  The default is " + defaultValue + ".";
        tf.setToolTipText(helpText);
        tf.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    }

    public JComponent getComponent() {
        return tf;
    }

    public int getValue() {
        Integer value = (Integer) tf.getValue();
        if (value == null) return -1; else return value.intValue();
    }

    public void setValue(int val) {
        if (val == -1) tf.setValue(null); else tf.setValue(new Integer(val));
    }

    private JFormattedTextField tf = null;

    private int min;

    private int max;

    private static int[] bits2max = { 0, 1, 3, 7, 0xF, 0x1F, 0x3F, 0x7F, 0xFF, 0x1FF, 0x3FF, 0x7FF, 0xFFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF };
}
