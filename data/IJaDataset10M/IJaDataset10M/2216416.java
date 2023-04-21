package org.formaria.awt;

import java.io.IOException;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import org.formaria.awt.Edit;
import java.awt.Scrollbar;

/**
 * UpDown Control
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p>$Revision: 1.2 $</p>
 */
public class Spinner extends Scrollbar implements AdjustmentListener {

    public Spinner() {
        initialize();
    }

    private void initialize() {
        setOrientation(Scrollbar.VERTICAL);
        addAdjustmentListener(this);
        setRange(0, 100);
    }

    public void setBuddy(Edit je) {
        buddyEdit = je;
    }

    public void setRange(int min, int max) {
        setMinimum(min - 1);
        setMaximum(max + 11);
        maxValue = max;
        minValue = min;
        value = Math.max(value, minValue);
        value = Math.min(value, maxValue);
        setValue((int) value);
        if (buddyEdit != null) buddyEdit.setText(new Double(maxValue - value).toString());
        setBlockIncrement(1);
        setUnitIncrement(1);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (bInAdjustment) return;
        if (buddyEdit != null) {
            double bv = new Double(buddyEdit.getText()).doubleValue();
            double newValue = 0;
            double newScrollValue = e.getValue();
            if (newScrollValue > value) newValue = Math.max(minValue, --bv); else if (newScrollValue < value) newValue = Math.min(maxValue, ++bv);
            bInAdjustment = true;
            buddyEdit.setText(new Double(newValue).toString());
            value = newValue;
            setValue((int) value);
            bInAdjustment = false;
        }
    }

    /**
     * Performs any post creation initialisation of the control.
     */
    public void init() throws IOException {
        doLayout();
    }

    /**
    * Sets the value corresponding to the maximum value of the meter
    */
    public void setMaxValue(double _value) {
        setMaximum((int) _value + 1);
        maxValue = (int) _value;
    }

    /**
    * Sets the value corresponding to the minimum value of the meter
    */
    public void setMinValue(double _value) {
        setMinimum((int) _value - 1);
        minValue = (int) _value;
    }

    private Edit buddyEdit = null;

    private double maxValue = 100;

    private double minValue = 0;

    private double value = 0;

    private static boolean bInAdjustment = false;
}
