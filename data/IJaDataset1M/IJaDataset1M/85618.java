package javax.swing.beaninfo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A text document which will reject any characters that are not digits.
 * 
 * @version 1.1 09/23/99
 * @author Mark Davidson
 */
public class NumberDocument extends PlainDocument {

    public void insertString(int offs, String str, AttributeSet atts) throws BadLocationException {
        if (!Character.isDigit(str.charAt(0))) {
            return;
        }
        super.insertString(offs, str, atts);
    }
}
