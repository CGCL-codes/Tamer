package ru.spb.leti.gr6351.rjap.var14.lexer;

import ru.spb.leti.gr6351.rjap.common.lang.IsToken;

/**
 * @author nikita
 * 
 */
public final class LexerUtils {

    public static void error(String text, int line, int column) {
        error(-1, text, line, column);
    }

    public static void error(int tokenClass, String text, int line, int column) {
        StringBuffer buf = new StringBuffer();
        buf.append("Error: ");
        buf.append("line " + line);
        buf.append(" column " + column);
        buf.append(" text: \"" + text + "\"");
        System.out.println(buf.toString());
    }

    public static IsToken token(Controller tables, int tokenClass, String text, int line, int column) {
        IsToken token = new AToken(tokenClass, text, line, column);
        tables.putToken(token);
        return token;
    }
}
