package misc;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Owner
 */
public class TextFieldLimiter extends PlainDocument {

    private int limit;

    /** Creates a new instance of TextFieldLimiter */
    public TextFieldLimiter(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String string, AttributeSet att) throws BadLocationException {
        if (string == null) return;
        if ((this.getLength() + string.length()) <= limit) {
            super.insertString(offset, string, att);
        }
    }
}
