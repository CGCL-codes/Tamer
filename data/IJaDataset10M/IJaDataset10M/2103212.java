package org.apache.xml.serialize;

/**
 * @version $Revision: 316040 $ $Date: 2000-08-30 20:59:22 +0200 (Mi, 30 Aug 2000) $
 * @author <a href="mailto:arkin@intalio..com">Assaf Arkin</a>
 * @see OutputFormat
 */
public final class LineSeparator {

    /**
     * Line separator for Unix systems (<tt>\n</tt>).
     */
    public static final String Unix = "\n";

    /**
     * Line separator for Windows systems (<tt>\r\n</tt>).
     */
    public static final String Windows = "\r\n";

    /**
     * Line separator for Macintosh systems (<tt>\r</tt>).
     */
    public static final String Macintosh = "\r";

    /**
     * Line separator for the Web (<tt>\n</tt>).
     */
    public static final String Web = "\n";
}
