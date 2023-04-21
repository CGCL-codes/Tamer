package benchmark.vocabulary;

import java.util.HashMap;

public class DC {

    public static final String NS = "http://purl.org/dc/elements/1.1/";

    public static final String PREFIX = "dc:";

    private static HashMap<String, String> uriMap = new HashMap<String, String>();

    public static String prefixed(String string) {
        if (uriMap.containsKey(string)) {
            return uriMap.get(string);
        } else {
            String newValue = PREFIX + string;
            uriMap.put(string, newValue);
            return newValue;
        }
    }

    public static String getURI() {
        return NS;
    }

    public static final String publisher = NS + "publisher";

    public static final String date = NS + "date";

    public static final String title = NS + "title";
}
