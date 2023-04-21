package org.itsnat.impl.core.css.lex;

import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class Identifier extends Token {

    protected String value = "";

    /** Creates a new instance of Identifier */
    public Identifier(String code, Cursor cursor) {
        super(cursor.getPos());
        parse(code, cursor);
    }

    public static boolean isIdentifierStart(char c) {
        return Character.isJavaIdentifierStart(c);
    }

    public static boolean isIdentifierPart(char c) {
        if (Character.isJavaIdentifierPart(c)) return true;
        if (c == '-') return true;
        return false;
    }

    public String toString() {
        return value;
    }

    public void parse(String code, Cursor cursor) {
        StringBuffer valueTmp = new StringBuffer();
        valueTmp.append(code.charAt(cursor.getPos()));
        int i = cursor.inc();
        while ((i < code.length()) && isIdentifierPart(code.charAt(i))) {
            valueTmp.append(code.charAt(i));
            i = cursor.inc();
        }
        this.value = valueTmp.toString();
        cursor.dec();
        this.end = cursor.getPos();
    }
}
