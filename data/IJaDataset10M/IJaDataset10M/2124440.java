package net.sf.rcpforms.experimenting.base.util;

/**
 * The Class <code><i>MessageUtil</i></code> helps filling up properties in Message strings
 * 
 * <p>
 * 
 */
public class MessageUtil {

    private static final String[] PLACE_HOLDER = new String[256];

    static {
        for (int i = 0; i < PLACE_HOLDER.length; i++) {
            PLACE_HOLDER[i] = "{" + i + "}";
        }
    }

    public static String formatMessage(final String message, final Object... parameters) {
        final StringBuilder result = new StringBuilder();
        result.append(message);
        String parameter;
        final int max = parameters.length > 256 ? 256 : parameters.length;
        int offset = 0;
        int index;
        for (int i = 0; i < max; i++) {
            parameter = null;
            offset = 0;
            do {
                index = result.indexOf(PLACE_HOLDER[i], offset);
                if (index >= 0) {
                    if (parameter == null) {
                        parameter = parameters[i] == null ? "null" : parameters[i].toString();
                    }
                    result.replace(index, index + PLACE_HOLDER[i].length(), parameter);
                    offset = index + PLACE_HOLDER[i].length();
                }
            } while (index >= 0);
        }
        return result.toString();
    }

    public static String gluePathPieces(final String... parts) {
        final StringBuilder result = new StringBuilder();
        boolean endsWithSlash = false;
        boolean startWithSlash = false;
        for (final String part : parts) {
            startWithSlash = part != null && part.length() > 0 && part.charAt(0) == '/';
            if (result.length() > 0 && !startWithSlash && !endsWithSlash) {
                result.append('/');
            }
            if (endsWithSlash && startWithSlash && part != null && part.length() > 0) {
                result.append(part.substring(1));
            } else {
                result.append(part);
            }
            endsWithSlash = result.length() > 0 && result.charAt(result.length() - 1) == '/';
        }
        return result.toString();
    }

    public static void main(final String[] args) {
        System.out.println("" + gluePathPieces(null, null));
        System.out.println("" + gluePathPieces(null, "eins"));
        System.out.println("" + gluePathPieces("eins", null));
        System.out.println("" + gluePathPieces(null, "eins"));
        System.out.println("" + gluePathPieces("/", "/a", "b", "c", "d/", "/e/"));
    }
}
