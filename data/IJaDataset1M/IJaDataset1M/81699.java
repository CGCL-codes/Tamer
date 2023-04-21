package com.sun.org.apache.regexp.internal;

/**
 * This is a class that contains utility helper methods for this package.
 *
 * @author <a href="mailto:jonl@muppetlabs.com">Jonathan Locke</a>
 * @version $Id: REUtil.java,v 1.1.2.1 2005/08/01 00:02:56 jeffsuttor Exp $
 */
public class REUtil {

    /** complex: */
    private static final String complexPrefix = "complex:";

    /**
     * Creates a regular expression, permitting simple or complex syntax 
     * @param expression The expression, beginning with a prefix if it's complex or 
     * having no prefix if it's simple
     * @param matchFlags Matching style flags
     * @return The regular expression object
     * @exception RESyntaxException thrown in case of error
     */
    public static RE createRE(String expression, int matchFlags) throws RESyntaxException {
        if (expression.startsWith(complexPrefix)) {
            return new RE(expression.substring(complexPrefix.length()), matchFlags);
        }
        return new RE(RE.simplePatternToFullRegularExpression(expression), matchFlags);
    }

    /**
     * Creates a regular expression, permitting simple or complex syntax 
     * @param expression The expression, beginning with a prefix if it's complex or 
     * having no prefix if it's simple 
     * @return The regular expression object
     * @exception RESyntaxException thrown in case of error
     */
    public static RE createRE(String expression) throws RESyntaxException {
        return createRE(expression, RE.MATCH_NORMAL);
    }
}
