package org.gnf.oracle;

import java.util.StringTokenizer;

/**
 *
 * <p>Title: KCStringTokenizer</p>
 * <p>Description: Keith Ching's dream tokenizer
 *  it gives you an empty string between delmiters
 *  and empty string for delimiters at beginning and/or end of line
 *  and to top it off.. it dumps it all into an array
 *  "like its supposed to"</p>
 * <p>Copyright: May 2002 Copyright (c) 2002</p>
 * <p>Company: GNF</p>
 * @author Keith Ching
 * @version 1.0
 */
public class KCStringTokenizer {

    /**
 * split string into an array by delimiters
 *
 * @param delim delimiter to split on
 * @param str input string
 * @return array of split values
 */
    public static String[] split(String delim, String str) {
        int count = 0;
        boolean firstTime = true;
        String last = "";
        String current = "";
        StringTokenizer st = new StringTokenizer(str, delim, true);
        String[] tokens = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            current = st.nextToken();
            if (firstTime) {
                firstTime = false;
                last = current;
            }
            if (current.equals(delim) && (last.equals(delim) || !st.hasMoreTokens())) {
                tokens[count] = "";
                count++;
            } else if (!current.equals(delim)) {
                tokens[count] = current;
                count++;
            }
            last = current;
        }
        String[] fields = new String[count];
        for (int i = 0; i < count; i++) {
            fields[i] = tokens[i];
        }
        return (fields);
    }
}
