package com.loribel.commons.swing;

import java.awt.Color;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JProgressBar;
import com.loribel.commons.util.GB_NumberTools;

/**
 * Component ProgressBar with value.
 * <p>
 * This progressBar manages itselt the value witch it represents.
 *
 * @author Gregory Borelli
 */
public class GB_ProgressBarWithValue extends JProgressBar {

    /**
     * Attribute value. <br />.
     */
    private int value = 0;

    private boolean useLabelMax = true;

    /**
     * Attribute maxValue.
     */
    public static int MAX_VALUE = 100;

    private int maxValue;

    private String maxValueStr;

    /**
     * Constructor of GB_ProgressBarWithValue without parameter.
     */
    public GB_ProgressBarWithValue() {
        this(MAX_VALUE);
    }

    /**
     * Constructor of GB_ProgressBarWithValue with parameter(s).
     *
     * @param a_maxValue int -
     */
    public GB_ProgressBarWithValue(int a_maxValue) {
        super(0, a_maxValue);
        setMaxValue(a_maxValue);
        init();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getValue() {
        return value;
    }

    /**
     * Increments the value of this progress.
     */
    public void incValue() {
        incValue(1);
    }

    /**
     * Increments the value of this progress.
     *
     * @param a_inc int -
     */
    public void incValue(int a_inc) {
        value = value + a_inc;
        this.setValue(value);
    }

    /**
     * Method init. <br />
     */
    private void init() {
        this.setString("");
        this.setStringPainted(true);
        this.setForeground(new Color(0x5D, 0x74, 0xB0));
    }

    public void setMaxValue(int a_maxValue) {
        if (maxValue == a_maxValue) {
            return;
        }
        maxValue = a_maxValue;
        if (maxValue > 0) {
            this.setIndeterminate(false);
            maxValueStr = GB_NumberTools.applyPattern(maxValue, "#,###");
            setModel(new DefaultBoundedRangeModel(0, 0, 0, maxValue));
        } else {
            this.setIndeterminate(true);
            this.setString("");
        }
    }

    public void setValue(int a_value) {
        if (maxValue < 1) {
            return;
        }
        value = a_value;
        int v = value;
        if (v > maxValue) {
            v = v % maxValue;
        }
        String l_str = GB_NumberTools.applyPattern(value, "#,###");
        if (useLabelMax && (value <= maxValue)) {
            l_str += "/" + maxValueStr;
            int l_pourcent = ((value * 100) / maxValue);
            l_str += " (" + l_pourcent + "%)";
        }
        this.setString(l_str);
        super.setValue(v);
    }
}
