package be.vds.jtbdive.view.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RegexPlainDocument extends PlainDocument {

    private String[] regex;

    public static final String INTEGER = "[\\d]+";

    public static final String DOUBLE = "[\\d]+([\\,\\.][\\d]+)?";

    public RegexPlainDocument(String[] regex) {
        super();
        this.regex = regex;
    }

    /**
	 * Redefini la methode de la classe PlainDocument permttant ainsi
	 * d'autoriser uniquement les caracteres desires
	 */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        boolean b = false;
        for (int i = 0; i < regex.length; i++) {
            Pattern p = Pattern.compile(regex[i]);
            Matcher m = p.matcher(str);
            b = m.matches();
            if (b) {
                break;
            }
        }
        if (!b) {
            int end = 0;
            if (str.length() > 1) {
                end = 1;
            }
            super.insertString(offs, str.substring(0, str.length() - end - 1), a);
        } else {
            super.insertString(offs, str, a);
        }
    }
}
