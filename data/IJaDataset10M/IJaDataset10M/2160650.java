package org.jsresources.apps.jam.util;

public class XmlError {

    public static final int NONE = 0;

    public static final int PARSE_ERROR = 1;

    /**	A number denoting the occured error.
	 *	This must be one of the constants defined above.
	 *	All other variables are only valid if this is not equal to zero.
	 */
    public static int nErrorCode;

    public static int nLineNumber;

    /**	The entity (the file) in which the error occured.
	 */
    public static String sEntity;

    /**	A human-readable description of the error.
	 *	This is non-localized.
	 */
    public static String sDescription;
}
