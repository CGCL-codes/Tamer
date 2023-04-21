package org.edkits.latex;

import java.awt.Toolkit;
import javax.swing.text.*;

public class LaTeXDocument extends DefaultStyledDocument {

    public static final char[] secureChars = { '�', '�', '!', '\"', '%', '&', '/', '(', ')', '=', '?', '@', '�', '$', '{', '[', ']', '}', '+', '\\', '\'', '�', '`', '^', '�', '~', '<', '>', '|', '-', '_', ',', ';', '.', ':', '*' };

    /**
     * Inserts some content into the document.
     * Inserting content causes a write lock to be held while the
     * actual changes are taking place, followed by notification
     * to the observers on the thread that grabbed the write lock.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.
     *
     * @param offs the starting offset >= 0
     * @param str the string to insert; does nothing with null/empty strings
     * @param a the attributes for the inserted content
     * @exception BadLocationException  the given insert position is not a valid
     *   position within the document
     * @see Document#insertString
     */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, removeNotSupported(str), a);
    }

    private String removeNotSupported(String in) {
        String tmp;
        if (in == null) return null;
        for (int i = 0; i >= 0 && i < in.length(); i++) {
            if (!isSupportedInLaTeX(in.charAt(i))) {
                if (in.length() == 1) {
                    in = "";
                } else {
                    tmp = in.substring(0, i);
                    in = tmp + in.substring(i + 1, in.length());
                    i--;
                }
                Toolkit.getDefaultToolkit().beep();
            }
        }
        return in;
    }

    private boolean isSupportedInLaTeX(char ch) {
        if (Character.isLetterOrDigit(ch)) return true;
        if (Character.isWhitespace(ch)) return true;
        return isSecure(ch);
    }

    private boolean isSecure(char ch) {
        for (int i = 0; i < secureChars.length; i++) {
            if (ch == secureChars[i]) return true;
        }
        return false;
    }
}
