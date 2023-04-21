package rj.tools.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <code>DecimalNumberDocument</code> only accepts deciaml characters (0-9).
 * This document is intendet to be used with a JTextField or similia object.
 *
 * <code>
 * private JTextField textFied = new JTextField(new DecimalNumberDocument(), "0", 3);
 * </code>
 *
 * @author Ralph Jocham
 * @version __0.98.2__
 */
public class DecimalNumberDocument extends PlainDocument {

    /**
    * <code>insertString</code>
    *
    * @param offs an <code>int</code> value
    * @param str a <code>String</code> value
    * @param a an <code>AttributeSet</code> value
    * @exception BadLocationException if an error occurs
    * @see javax.swing.text.PlainDocument
    */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0, count = str.length(); i < count; i++) {
            char c = str.charAt(i);
            if (c >= 0x30 && c <= 0x39) {
                sb.append(c);
            }
        }
        super.insertString(offs, sb.toString(), a);
    }
}
