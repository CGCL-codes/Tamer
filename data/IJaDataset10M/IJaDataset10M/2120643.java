package org.dcm4chee.usr.ui.util;

/**
 * @author Robert David <robert.david@agfa.com>
 * @version $Revision$ $Date$
 * @since 28.09.2009
 */
public class CSSUtils {

    public static String getRowClass(int rowNumber) {
        return (rowNumber % 2 == 0) ? "even" : "odd";
    }
}
