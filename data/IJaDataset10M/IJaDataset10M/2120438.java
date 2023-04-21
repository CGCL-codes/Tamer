package edu.mit.lcs.haystack.adenine;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class SyntaxException extends AdenineException {

    public SyntaxException(String s) {
        super(s);
    }

    public SyntaxException(String s, int line) {
        super(s);
        m_line = line;
    }

    public SyntaxException(String s, Exception e) {
        super(s, e);
    }
}
