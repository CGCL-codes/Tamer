package org.nbnResolving.resolver;

/**
 * Class for all Constant values that should be used for better readability
 * and easier modification of code.
 * 
 * @author Kadir Karaca Kocer
 */
public class Constants {

    /** URI for Institutions, Autors, ... in German National Library */
    public static final String GND_URI_ROOT = "http://d-nb.info/gnd";

    /** URN is marked as inactive */
    public static final short URN_STATUS_INACTIVE = 0;

    /** URN is active */
    public static final short URN_STATUS_ACTIVE = 1;

    /** URN is reserved but not yet registered */
    public static final short URN_STATUS_RESERVED = 2;

    /** Return all URLs with all information available */
    public static final short RESOLVE_FULL_INFO = 10;

    /** Return only the most privileged URL to redirect */
    public static final short RESOLVE_FIRST_URL = 11;

    /** Only the archive copy (ies) */
    public static final short RESOLVE_ONLY_ARCHIVE = 12;

    /** Only the frontpage(s) */
    public static final short RESOLVE_ONLY_FRONTPAGE = 13;

    /** There is no registered URL for this URN */
    public static final short URN_RESOLVING_OK = 700;

    /** URN is NULL*/
    public static final short URN_IS_NULL = 701;

    /** No active URL exists in RDBMS */
    public static final short NO_ACTIVE_URL = 702;

    /** This URN is replaced by a newer version*/
    public static final short URN_HAS_NEWER_VERSION = 703;

    /** Wrong URN format*/
    public static final short WRONG_URN_FORMAT = 705;

    /** There is no registered URL for this URN */
    public static final short NO_REGISTERED_URL = 712;

    /** The requested URN is marked inactive*/
    public static final short REQUESTED_URN_IS_INACTIVE = 713;

    /** Requested URN is marked reserved*/
    public static final short REQUESTED_URN_IS_RESERVED = 714;

    /** Not defined URN STATUS code. Should not occur. */
    public static final short NO_SUCH_URN_STATUS = 715;

    /** URN is not in database */
    public static final short NO_SUCH_URN_IN_DATABASE = 741;

    /** There was an exception during resolving process*/
    public static final short ERROR_DURING_RESOLVING = 745;

    /** URN information comes from a foreign data source */
    public static final short REMOTE_RESOLVER_INFORMATION = 750;

    /** A registered URN for the given URL is found and listed (success) */
    public static final short URN_FOR_URL_FOLLOWS = 850;

    /** This URL is not in database */
    public static final short NO_URN_WITH_THIS_URL = 802;

    /** An URN_ID could be found but it is not valid */
    public static final short PROBLEM_WITH_URN_ID = 803;

    /** All in system registered namespaces are listed */
    public static final short NAMESPACE_LISTING_SUCCESSFUL = 810;

    /** No namespace is registered in database (error condition) */
    public static final short NO_NAMESPACE_FOUND = 811;

    /** All in system registered institutions are listed */
    public static final short INSTITUTION_LISTING_SUCCESSFUL = 820;

    /** No institution is registered in database (error condition) */
    public static final short NO_INSTITUTION_FOUND = 821;

    /** HTTP 1.1 response codes */
    public static final short HTTP_STATUS_OK = 200;
}
