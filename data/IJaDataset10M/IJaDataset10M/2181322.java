package com.intel.gui.controls;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Document class only for double values.
 * 
 * @author Ralf Ratering
 * @version $Id$
 * @see DoubleTextField
 */
class DoubleDocument extends PlainDocument {

    private double min, max;

    /**
     * Constructor initializes with min and max
     * 
     * @param min
     *            minimal value
     * @param max
     *            maximal value
     */
    public DoubleDocument(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Overwrites the function "insertString" of class PlainDocument for int
     * only input.
     * 
     * @param offset
     *            >=0 where String should be inserted
     * @param str
     *            String to insert
     * @param a
     *            attributes for inserted content
     * @exception BadLocationException
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        boolean valid = true;
        String tmp = getText(0, offset) + str + getText(offset, getLength() - offset);
        if (min >= 0 || (!tmp.equals("-"))) {
            try {
                double i = Double.parseDouble(tmp);
                if (i > max) valid = false;
            } catch (NumberFormatException e) {
                valid = false;
            }
        }
        if (valid) {
            super.insertString(offset, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Set Maximum
     * 
     * @param max
     *            maximum
     */
    public void setMaximum(double max) {
        this.max = max;
    }

    /**
     * Set Minimum
     * 
     * @param min
     *            minimum
     */
    public void setMinimum(double min) {
        this.min = min;
    }
}
