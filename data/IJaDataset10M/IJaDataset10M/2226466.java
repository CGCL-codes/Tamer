package net.sf.gpproxy.ui2;

import javax.swing.text.*;

/**
 *
 * @author  red
 */
public class HexDocumentFilter extends DocumentFilter {

    /** Creates a new instance of HexDocumentFilter */
    public HexDocumentFilter() {
    }

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, string, attr);
    }

    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        super.replace(fb, offset, length, string, attr);
    }
}
