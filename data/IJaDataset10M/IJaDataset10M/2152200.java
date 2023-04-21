package tufts.vue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
* NumericField
* This class is a subclass of JTextField that supports
* numeric inputs for integers or real decimal numbers.
* 
* The input is set on construction to be one of four modes:
*   NUMERIC - any decimal number
*   POSITIVE_NUMERIC - only positive real numbers
*   INTEGER - any integer value
*   POSITIVE_INTEGER - only positive integers.
*
* It uses the java Document override to deal with the filtering.
**/
public class NumericField extends JTextField {

    /** Mode supports numeric values **/
    public static final int NUMERIC = 1;

    /** Mode supports integer values **/
    public static final int INTEGER = 2;

    /** Mode isupports positive numeric values **/
    public static final int POSITIVE_NUMERIC = 3;

    /** Mode supports positive, integer values (whole numbers) **/
    public static final int POSITIVE_INTEGER = 4;

    private static final String sValidCharsNumeric = "0123456789-.";

    /** the mode for this editor field **/
    protected int mMode;

    /** the valid chars allowed in the field **/
    protected String mValidChars;

    /**
	* Create a new NumericField with the specified mode
 	**/
    public NumericField(int pMode) {
        super();
        setDocument(new NumericDocument(this));
        setMode(pMode);
    }

    /**
	 * Create a new NumericField with the specified mode and width
	 **/
    public NumericField(int pMode, int pColumns) {
        this(pMode);
        setColumns(pColumns);
    }

    /**
	 * setValue
	 * Sets the value of this field from an integer.
	 *
	 **/
    public void setValue(int pValue) {
        if ((pValue < 0) && (mMode == POSITIVE_INTEGER || mMode == POSITIVE_NUMERIC)) {
            throw new IllegalArgumentException("NumericFieldF - Requires Positive Value");
        } else {
            super.setText(Integer.toString(pValue));
        }
    }

    /**
	 * setValue
	 * Set the value of this field from a double.
	 *
	 **/
    public void setValue(double pValue) {
        if (mMode == INTEGER || mMode == POSITIVE_INTEGER) {
            throw new IllegalArgumentException("NumericField - Requires Integer");
        } else if (mMode == POSITIVE_NUMERIC) {
            throw new IllegalArgumentException("NumericField - Requires Positive Value");
        } else {
            setText(Double.toString(pValue));
        }
    }

    /**
	 * getValue
	 * This gets the value as a double
	 **/
    public double getValue() {
        String val = getText();
        if (val == null || val.length() == 0) {
            return 0.0;
        }
        return Double.valueOf(val).doubleValue();
    }

    /**
	 * setMode
	 * This sets the mode of this NumericField
	 * to the type specified.  It also sets the
	 * valid charas that are allowed from the keyboard.
	 **/
    private void setMode(int pMode) {
        mMode = pMode;
        switch(pMode) {
            case NUMERIC:
                mValidChars = "0123456789-.";
                break;
            case INTEGER:
                mValidChars = "0123456789-";
                break;
            case POSITIVE_NUMERIC:
                mValidChars = "0123456789-.";
                break;
            case POSITIVE_INTEGER:
                mValidChars = "0123456789";
                break;
        }
    }

    /**
	* NumericDocument is a document class that accepts only numeric
	* input and is sensitive to the mode setting of the NumericField
	* that owns the document.
	**/
    private class NumericDocument extends PlainDocument {

        NumericField mDocNumericField = null;

        private NumericDocument(NumericField pField) {
            mDocNumericField = pField;
        }

        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            String text;
            for (int n = 0; n < source.length; n++) {
                char c = source[n];
                if (mValidChars.indexOf(c) == -1) return;
                if (c == '-' && offset > 0) return;
                if (c == '.' && ((text = mDocNumericField.getText()) != null) && text.indexOf('.') != -1) return;
            }
            super.insertString(offset, str, a);
        }
    }
}
