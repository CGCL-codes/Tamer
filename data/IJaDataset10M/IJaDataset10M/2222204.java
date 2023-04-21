package xapc.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class DecimalDocument extends javax.swing.text.PlainDocument {

    private static final long serialVersionUID = -7082396939344559239L;

    private PalastPanel palastPanel = null;

    public DecimalDocument(PalastPanel pp) {
        palastPanel = pp;
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        String valid = "0123456789";
        if (getLength() < 7) {
            for (int i = 0; i < str.length(); i++) {
                if (valid.indexOf(str.charAt(i)) == -1) {
                    palastPanel.calculateHords();
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    return;
                }
                super.insertString(offset, str, a);
            }
        }
    }
}
