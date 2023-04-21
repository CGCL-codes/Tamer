package org.boffyflow.ru.util;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class WholeNumberField extends JTextField {

    private Toolkit toolkit;

    private NumberFormat integerFormatter;

    public WholeNumberField(int value, int columns) {
        super(columns);
        toolkit = Toolkit.getDefaultToolkit();
        integerFormatter = NumberFormat.getNumberInstance(Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        setValue(value);
    }

    public int getValue() {
        int retVal = 0;
        String s = getText();
        if (s.length() == 0) {
            s += "0";
        }
        try {
            retVal = integerFormatter.parse(s).intValue();
        } catch (ParseException e) {
            System.out.println("Error in WholeNumberField.getValue(): " + e.toString());
            toolkit.beep();
        }
        return retVal;
    }

    public void setValue(int value) {
        setText(Integer.toString(value));
    }

    protected Document createDefaultModel() {
        return new WholeNumberDocument();
    }

    protected class WholeNumberDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;
            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i])) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                    System.err.println("WholeNumberField: insertString: " + source[i]);
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }
}
