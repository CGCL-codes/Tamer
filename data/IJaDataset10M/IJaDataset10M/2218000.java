package self.text;

import java.util.*;

public final class CommaDelimitedUtils {

    public static final String COMMA = ", ";

    public static void intSeriesToArray(String commaDelimInts, int[] store) {
        StringTokenizer tok = new StringTokenizer(commaDelimInts, COMMA, false);
        for (int cntr = 0; tok.hasMoreElements(); cntr++) store[cntr] = Integer.parseInt((String) tok.nextElement());
    }

    public static void intSeriesToList(String commaDelimInts, java.util.List store) {
        StringTokenizer tok = new StringTokenizer(commaDelimInts, COMMA, false);
        for (int cntr = 0; tok.hasMoreElements(); cntr++) store.add(tok.nextElement());
    }

    public static String objectsToSeries(java.util.List store) {
        StringBuffer commaDelimInts = new StringBuffer();
        int max = store.size();
        for (int cntr = 0; cntr < max; cntr++) {
            if (cntr > 0) commaDelimInts.append(COMMA);
            commaDelimInts.append(store.get(cntr).toString());
        }
        return commaDelimInts.toString();
    }
}
