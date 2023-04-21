package org.schwiet.LincolnLog.transaction;

import java.awt.Toolkit;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.log4j.Logger;

/**
 *
 * @author sethschwiethale
 */
public class AmountColumnEditorFilter extends DocumentFilter {

    JTextField field;

    static Logger logger = Logger.getLogger(AmountColumnEditorFilter.class);

    private static final Pattern MONEY_PATTERN = Pattern.compile("(?:[0-9]*\\.[0-9]{2})|(?:[0-9]*\\.[0-9]{1})|(?:[0-9]*\\.)|(?:[0-9]*)");

    public AmountColumnEditorFilter(JTextField checkField) {
        field = checkField;
    }

    public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if (MONEY_PATTERN.matcher(field.getText() + str).matches()) super.insertString(fb, offs, str, a); else Toolkit.getDefaultToolkit().beep();
    }

    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
        if (MONEY_PATTERN.matcher(fb.getDocument().getText(0, offs) + str).matches()) {
            super.replace(fb, offs, length, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
