package cdox.util;

import javax.swing.*;
import javax.swing.text.*;

/**
 * This is an extended JTextField that accepts only one char as uppercase.
 * @author <a href="mailto:cdox@gmx.net">Rutger Bezema, Andreas Schmitz</a>
 * @version May 22nd 2002
 */
public class JCharField extends JTextField {

    private JCharField itsme = this;

    /**
     * Constructor whom you must provide the initial value.
     *@param val the initial value.
     */
    public JCharField(char val) {
        setValue(val);
    }

    /**
     * Overrides something in JTextField; it just returns an
     * CharDocument (This <i>IS</i> a JCharField...) (This <i>IS</i> stolen from
     * JCharField...)
     *@return returns the CharDocument.
     */
    protected Document createDefaultModel() {
        return new CharDocument();
    }

    /**
     * Returns the actual value.
     *@return the value.
     */
    public char getValue() {
        return getText().charAt(0);
    }

    /**
     * Sets the actual value.
     *@param val the value
     */
    public void setValue(char val) {
        setText("" + val);
    }

    private class CharDocument extends PlainDocument {

        public void insertString(int offs, String s, AttributeSet a) throws BadLocationException {
            super.insertString(0, s.charAt(0) + "", a);
            if (itsme.getText().length() > 1) itsme.setValue(itsme.getText().toUpperCase().charAt(0));
        }
    }
}
