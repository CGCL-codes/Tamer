package universe.common.gui;

import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.text.*;

public class FloatTextField extends JTextField {

    public static final String INT_POS = "01234556789";

    public static final int INT_POS_LEN = 10;

    boolean allowNeg = true;

    public FloatTextField(int columns) {
        super(columns);
    }

    public FloatTextField(int columns, boolean allowNegative) {
        super(columns);
        allowNeg = allowNegative;
    }

    public FloatTextField(int columns, int alignment, boolean allowNegative) {
        super(columns);
        setHorizontalAlignment(alignment);
        allowNeg = allowNegative;
    }

    public void setFloatValue(float value) {
        super.setText(String.valueOf(value));
    }

    public void setText(String text) {
        try {
            Float f = new Float(text);
            super.setText(text);
        } catch (NumberFormatException e) {
            throw new RuntimeException("GUIFloatTextFieldJFC: setText() called with illegal float value");
        }
    }

    public float getFloatValue() {
        String text = this.getText();
        if (text.equals("") || text.equals(".") || text.equals("-")) {
            return 0f;
        }
        try {
            Float f = new Float(text);
            return f.floatValue();
        } catch (NumberFormatException e) {
            throw new RuntimeException("GUIFloatTextFieldJFC: Internal Error");
        }
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    protected Document createDefaultModel() {
        return new FloatDocument();
    }

    class FloatDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            char[] chars = str.toCharArray();
            int count = 0;
            char[] carray = new char[1];
            for (int i = 0; i < chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    carray[0] = chars[i];
                    super.insertString(offs + count, new String(carray), a);
                    count++;
                } else if (allowNeg && (chars[i] == '-') && (offs == 0)) {
                    String text = FloatTextField.this.getText();
                    if (text.indexOf('-') == -1) {
                        carray[0] = chars[i];
                        super.insertString(offs + count, new String(carray), a);
                        count++;
                    }
                } else if (chars[i] == '.') {
                    String text = FloatTextField.this.getText();
                    if (text.indexOf('.') == -1) {
                        carray[0] = chars[i];
                        super.insertString(offs + count, new String(carray), a);
                        count++;
                    }
                }
            }
        }
    }
}
