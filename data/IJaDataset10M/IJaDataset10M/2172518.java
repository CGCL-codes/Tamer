package org.isakiev.xl.view.editor;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Document with fixed max length
 * 
 * @author Ruslan Isakiev
 */
class LimitedLengthDocument extends PlainDocument {

    private static final long serialVersionUID = 1L;

    private int maxLength;

    public LimitedLengthDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if ((getLength() + str.length()) <= maxLength) {
            super.insertString(offset, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
