package org.newsml.toolkit.conformance;

import gnu.regexp.RE;
import gnu.regexp.RESyntax;
import gnu.regexp.REException;

/**
 * Convenient static utility methods for NewsML conformance tests.
 *
 * <p>Many of the methods in this class depend on the Gnu Regexp
 * Java library.</p>
 *
 * @author Reuters PLC
 * @version 2.0
 */
public final class Util {

    /**
     * Regular expression to validate an ISO 8601 date.
     *
     * <p>This matches only the NewsML subset of the Basic Format.</p>
     */
    public static final String DATE_REGEXP = "^\\d{8}(T\\d{6}([\\+\\-]\\d{4}|Z))?$";

    public static final String URL_REGEXP = "^[a-zA-Z]+:.+$";

    /**
     * Regular expression to validate RFC 2141 URN syntax.
     */
    public static final String URN_REGEXP = "^urn:[a-zA-Z0-9][a-zA-Z0-9-]{1,31}:([a-zA-Z0-9()+,.:=@;$_!*'-]|%[0-9A-Fa-f]{2})+$";

    /**
     * Match a string against a regular expression.
     *
     * @param regexp The regular expression, in Perl5 syntax.
     * @param data The string to match.
     * @return true if the string matches, false otherwise.
     */
    public static boolean matches(String regexp, String data) {
        try {
            return new RE(regexp).isMatch(data);
        } catch (REException e) {
            throw new RuntimeException("Bad regular expression: " + e.getMessage());
        }
    }

    /**
     * Test whether a string is an ISO 8601 date (NewsML subset).
     */
    public static boolean isISODate(String s) {
        return matches(DATE_REGEXP, s);
    }

    /**
     * Test whether a string matches IETF RFC 1766 language syntax.
     * See NewsML 1.04, section 5.4.2 -- it must be ll-CC or ll-CCC.
     */
    public static boolean isISOLang(String s) {
        return matches("^[a-zA-Z]{2}(-[a-zA-Z]{2,3})?$", s);
    }

    /**
     * Test whether a string matches Duid reference syntax.
     */
    public static boolean isDuidRef(String ref) {
        return (ref.startsWith("#"));
    }

    /**
     * Test whether a string matches NewsML URN syntax.
     */
    public static boolean isNewsMLUrn(String urn) {
        return (isUrn(urn) && matches("^urn:newsml:[^:]*:\\d{8}:[^:]*:\\d+[UNA]$", urn));
    }

    /**
     * Test whether a string matches HTTP URL syntax.
     */
    public static boolean isHttpUrl(String url) {
        return (isUrl(url) && matches("^[hH][tT][tT][pP]:.+", url));
    }

    /**
     * Test whether a string matches general URL syntax.
     */
    public static boolean isUrl(String url) {
        return matches(URL_REGEXP, url);
    }

    /**
     * Test whether a string matches general URN syntax.
     */
    public static boolean isUrn(String urn) {
        return matches(URN_REGEXP, urn);
    }
}
