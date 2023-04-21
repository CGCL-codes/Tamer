package org.apache.harmony.x.swing.text.html.cssparser;

public class TokenResolver {

    private static final String SEPARATOR = " ";

    public static String resolve(final Token token) {
        return token.image;
    }

    public static String resolve(final Token start, final Token end, final boolean allowSeparation) {
        StringBuilder result = new StringBuilder();
        Token nextToken = start;
        do {
            if (allowSeparation && result.length() != 0) {
                result.append(SEPARATOR);
            }
            result.append(resolve(nextToken));
            if (nextToken == end) {
                break;
            }
            nextToken = nextToken.next;
        } while (nextToken != null);
        return result.toString();
    }
}
