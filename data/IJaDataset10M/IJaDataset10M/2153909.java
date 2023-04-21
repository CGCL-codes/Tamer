package org.gjt.btools.ext;

import java.awt.*;
import javax.swing.text.*;

/**
 * A document class used to enter an identifier into a text component.
 * An identifier contains only alphanumeric characters and does not
 * begin with a digit.
 */
public class IdentifierDocument extends PlainDocument {

    /**
     * Attempts to insert a string into the document.
     */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        boolean error = false;
        for (int i = 0; i < str.length(); i++) if (!Character.isLetterOrDigit(str.charAt(i))) {
            error = true;
            break;
        }
        if (!error) if (offs == 0 && str.length() > 0) if (Character.isDigit(str.charAt(0))) error = true;
        if (error) Toolkit.getDefaultToolkit().beep(); else super.insertString(offs, str, a);
    }

    /**
     * Attempts to remove a portion of the document.
     */
    public void remove(int offs, int len) throws BadLocationException {
        boolean error = false;
        if (offs == 0 && len > 0 && len < getLength()) if (Character.isDigit(getText(len, 1).charAt(0))) error = true;
        if (error) Toolkit.getDefaultToolkit().beep(); else super.remove(offs, len);
    }
}
