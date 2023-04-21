package view.gui.model;

import java.awt.event.KeyEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PlainDocumentFloat extends PlainDocument {

    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String ALPHA = LOWERCASE + UPPERCASE;

    public static final String NUMERIC = "0123456789";

    public static final String FLOAT = NUMERIC + ".";

    public static final String SPECIAL = "@#$%&^*()-";

    public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;

    public static final String ALPHA_NUMERIC_SIN_ESPACIOS = ALPHA + NUMERIC + ".";

    public static final String ALPHA_NUMERIC_ESPECIALES = ALPHA + NUMERIC + SPECIAL;

    protected String acceptedChars = null;

    protected boolean negativeAccepted = false;

    protected boolean especialAccepted = false;

    public PlainDocumentFloat() {
        this(FLOAT);
    }

    public PlainDocumentFloat(String acceptedchars) {
        acceptedChars = acceptedchars;
        if ((acceptedChars.equals(ALPHA_NUMERIC))) {
            acceptedChars += (char) KeyEvent.VK_SPACE;
            acceptedChars += (char) KeyEvent.VK_COMMA;
            acceptedChars += String.valueOf("#");
            acceptedChars += String.valueOf("@");
            acceptedChars += String.valueOf(".");
        }
    }

    public void setNegativeAccepted(boolean negativeaccepted) {
        if (acceptedChars.equals(NUMERIC) || acceptedChars.equals(FLOAT) || acceptedChars.equals(ALPHA_NUMERIC)) {
            negativeAccepted = negativeaccepted;
            acceptedChars += "-";
        }
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if (acceptedChars.equals(UPPERCASE)) str = str.toUpperCase(); else if (acceptedChars.equals(LOWERCASE)) str = str.toLowerCase();
        for (int i = 0; i < str.length(); i++) {
            if (acceptedChars.indexOf(str.valueOf(str.charAt(i))) == -1) return;
        }
        if (acceptedChars.equals(FLOAT) || (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
            if (str.indexOf(".") != -1) {
                if (getText(0, getLength()).indexOf(".") != -1) {
                    return;
                }
            }
        }
        if (negativeAccepted && str.indexOf("-") != -1) {
            if (str.indexOf("-") != 0 || offset != 0) {
                return;
            }
        }
        super.insertString(offset, str, attr);
    }
}
