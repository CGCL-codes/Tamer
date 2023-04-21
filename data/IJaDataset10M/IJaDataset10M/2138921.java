package edu.mit.lcs.haystack.adenine.parser;

import edu.mit.lcs.haystack.adenine.AdenineException;
import edu.mit.lcs.haystack.adenine.SyntaxException;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class URIToken extends Token {

    public URIToken(int line) {
        m_line = line;
    }

    public int processToken(String str, int i) throws AdenineException {
        while (i < str.length()) {
            char ch = str.charAt(i++);
            if (ch == '>') {
                return i;
            }
            m_token = m_token + ch;
        }
        throw new SyntaxException("< without matching >", m_line);
    }

    public String toString() {
        return "<" + m_token + ">";
    }

    public String prettyPrint(int tablevel) {
        return "<" + m_token + ">";
    }
}
