package net.jonbuck.tassoo.persistence.nl;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * 
 * @since 1.0.0
 */
public class Messages extends NLS {

    public static String attachmenttypes_openofficewriter;

    private static final String BUNDLE_NAME = "net.jonbuck.tassoo.persistence.nl.messages";

    public static String exception_getactivetasks_noresults;

    public static String exception_getactivetasks_unknownexception;

    public static String exception_getcategorybyvalue_noresults;

    public static String exception_getcategorybyvalue_toomanyresults;

    public static String exception_getcategorybyvalue_unknownexception;

    public static String exception_getcontainerbyvalue_noresults;

    public static String exception_getcontainerbyvalue_toomanyresults;

    public static String exception_getcontainerbyvalue_unknownexception;

    public static String exception_getprioritybyvalue_noresults;

    public static String exception_getprioritybyvalue_toomanyresults;

    public static String exception_getprioritybyvalue_unknownexception;

    public static String exception_getstatusbyvalue_noresults;

    public static String exception_getstatusbyvalue_toomanyresults;

    public static String exception_getstatusbyvalue_unknownexception;

    public static String exception_gettaskbyvalue_noresults;

    public static String exception_gettaskbyvalue_toomanyresults;

    public static String exception_gettaskbyvalue_unknownexception;

    public static String exception_gettypebyvalue_noresults;

    public static String exception_gettypebyvalue_toomanyresults;

    public static String exception_gettypebyvalue_unknownexception;

    static {
        initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
