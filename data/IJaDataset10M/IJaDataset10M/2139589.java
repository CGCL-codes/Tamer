package br.org.databasetools.core.view.fields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TIntegerPlainDocument extends PlainDocument {

    private static final long serialVersionUID = 1L;

    private int maxlength = 500;

    private boolean negative = false;

    public TIntegerPlainDocument() {
        maxlength = 30;
    }

    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    private int getTotalLength(String str) {
        if (isNegative()) {
            return (getLength() - 1) + str.length();
        } else {
            return getLength() + str.length();
        }
    }

    public boolean verifyLength(String str) {
        if (getTotalLength(str) <= this.maxlength) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNegative() {
        return negative;
    }

    private void setNegative(boolean b) {
        negative = b;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (verifyLength(str)) {
            if (getLength() == 0) {
                setNegative(false);
                if (str.equals("-")) {
                    setNegative(true);
                    super.insertString(offset, str, attr);
                } else if (!str.equals(null)) {
                    try {
                        @SuppressWarnings("unused") int number = Integer.parseInt(str);
                        super.insertString(offset, str, attr);
                    } catch (NumberFormatException ne) {
                        return;
                    }
                }
            } else if (!str.equals(null)) {
                try {
                    @SuppressWarnings("unused") int number = Integer.parseInt(str);
                    super.insertString(offset, str, attr);
                } catch (NumberFormatException ne) {
                    return;
                }
            }
        }
    }
}
