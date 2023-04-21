package tiiger.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class extends PlainDocument associated to swing components and verifies
 * the length of the string inserted in the component.
 */
public class LengthFieldVerifier extends PlainDocument {

    private int length;

    public LengthFieldVerifier(int length) {
        this.length = length;
    }

    /**
	 * Verifies the length of the string before to insert it in the Document
	 * */
    public void insertString(int offset, String str, AttributeSet attSet) throws BadLocationException {
        if (str == null) {
            return;
        }
        String old = getText(0, getLength());
        String newStr = old.substring(0, offset) + str + old.substring(offset);
        if (this.length > newStr.length()) super.insertString(offset, str, attSet);
    }
}
