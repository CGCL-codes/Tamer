package net.sourceforge.squirrel_sql.fw.gui;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * This class is a <CODE>TextField</CODE> that only allows integral
 * values to be entered into it.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class IntegerField extends JTextField {

    /**
     * Default ctor.
     */
    public IntegerField() {
        super();
    }

    /**
     * Ctor specifying the field width.
     */
    public IntegerField(int cols) {
        super(cols);
    }

    public int getInt() {
        final String text = getText();
        if (text == null || text.length() == 0) {
            return 0;
        }
        return Integer.parseInt(text);
    }

    public void setInt(int value) {
        setText("" + value);
    }

    protected Document createDefaultModel() {
        return new IntegerDocument();
    }

    static class IntegerDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str != null) {
                try {
                    Integer.decode(str);
                    super.insertString(offs, str, a);
                } catch (NumberFormatException ex) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
}
