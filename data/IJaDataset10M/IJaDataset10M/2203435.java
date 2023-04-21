package com.mawell.common.propertymapperswing;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * @version $Revision: 1.2 $
 */
public class IntField extends JTextField {

    public IntField(int cols) {
        super(cols);
    }

    public IntField(String text) {
        super(text);
    }

    public IntField(String text, int cols) {
        super(text, cols);
    }

    protected Document createDefaultModel() {
        return new IntDocument();
    }

    static class IntDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            char[] intString = str.toCharArray();
            char[] result = new char[intString.length];
            int j = 0;
            for (int i = 0; i < intString.length; i++) {
                if ((intString[i] > 47 && intString[i] < 58) || (intString[i] == 8)) {
                    result[j++] = intString[i];
                } else if (j == 0 && intString[i] == 45) {
                    result[j++] = intString[i];
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }
}
