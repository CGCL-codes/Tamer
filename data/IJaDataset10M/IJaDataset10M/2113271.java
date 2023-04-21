package org.jmeld.util;

import java.util.*;
import java.util.regex.*;

public class WordTokenizer {

    private Pattern p;

    public WordTokenizer(String pattern) {
        p = Pattern.compile(pattern);
    }

    /** Get the tokens of the text.
   *   All tokens and non-tokens are returned in the result.
   *   So that the length of the text is the same length as
   *   the length of all tokens.
   */
    public List<String> getTokens(String text) {
        Matcher m;
        List<String> result;
        int index;
        String s;
        result = new ArrayList<String>();
        index = 0;
        m = p.matcher(text);
        while (m.find()) {
            s = text.substring(index, m.start());
            if (s.length() > 0) {
                result.add(s);
                index += s.length();
            }
            s = text.substring(m.start(), m.end());
            if (s.length() > 0) {
                result.add(s);
                index += s.length();
            }
        }
        if (index < text.length()) {
            s = text.substring(index, text.length());
            if (s.length() > 0) {
                result.add(s);
            }
        }
        return result;
    }
}
