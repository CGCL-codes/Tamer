package editor.util;

import java.util.StringTokenizer;

/**
 *
 * @author mitja
 */
class StringSplitter {

    public StringSplitter() {
    }

    public String[] splitStringBySpace(String s) {
        StringTokenizer st;
        String[] tokens;
        int count = 0;
        st = new StringTokenizer(s);
        if (st.countTokens() == 0) {
            return null;
        }
        tokens = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            tokens[count] = st.nextToken();
            count++;
        }
        return tokens;
    }

    public String[] splitStringByDot(String s) {
        StringTokenizer st;
        String[] tokens;
        int count = 0;
        st = new StringTokenizer(s, ".");
        if (st.countTokens() == 0) {
            return null;
        }
        tokens = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            tokens[count] = st.nextToken();
            count++;
        }
        return tokens;
    }

    public String[] splitByFileSeparator(String s) {
        StringTokenizer st;
        String[] tokens;
        int count = 0;
        st = new StringTokenizer(s, System.getProperty("file.separator"));
        if (st.countTokens() == 0) {
            return null;
        }
        tokens = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            tokens[count] = st.nextToken();
            count++;
        }
        return tokens;
    }
}
