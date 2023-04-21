package org.oclc.da.ndiipp.spider;

import java.util.Hashtable;

/**
 * SpiderStatus
 *
 * This class defines constants for status conditions that can be used by
 * various pieces of the spider package. 
 * 
 * @author JCG
 * @version 1.0, 
 * @created 11/18/2004
 */
public class SpiderStatus {

    /** Status is currently unknown */
    public static final int UNKNOWN = -1;

    /** Process has completed successfully */
    public static final int SUCCESS = 0;

    /** Process has completed successfully */
    public static final int PARTIAL_SUCCESS = 1;

    /** Process has failed */
    public static final int FAILURE = 2;

    /** Process is currently pending, waiting to be started */
    public static final int PENDING = 3;

    /** Process is currently running */
    public static final int RUNNING = 4;

    /** Process is currently running */
    public static final int NOT_RUNNING = 5;

    /** Process was canceled */
    public static final int CANCELED = 6;

    /** An invalid URL was specified. */
    public static final int INVALID_URL = 7;

    /** An unsupported or unknown protocol was specified in the URL. */
    public static final int UNSUPPORTED_PROTOCOL = 8;

    /** Resource is inaccessible */
    public static final int INACCESSIBLE = 9;

    /** Network error occurred */
    public static final int NET_ERROR = 10;

    /** Hastable containing status mappings. */
    private static final Hashtable<Integer, String> MAPPINGS = new Hashtable<Integer, String>();

    static {
        MAPPINGS.put(new Integer(UNKNOWN), "Unknown");
        MAPPINGS.put(new Integer(SUCCESS), "Success");
        MAPPINGS.put(new Integer(PARTIAL_SUCCESS), "Partial Success");
        MAPPINGS.put(new Integer(FAILURE), "Failed");
        MAPPINGS.put(new Integer(PENDING), "Pending");
        MAPPINGS.put(new Integer(RUNNING), "Running");
        MAPPINGS.put(new Integer(NOT_RUNNING), "Not Running");
        MAPPINGS.put(new Integer(CANCELED), "Canceled");
        MAPPINGS.put(new Integer(INVALID_URL), "Invalid URL");
        MAPPINGS.put(new Integer(UNSUPPORTED_PROTOCOL), "Unsupported Protocol");
        MAPPINGS.put(new Integer(INACCESSIBLE), "Inaccessible");
        MAPPINGS.put(new Integer(NET_ERROR), "Network Error");
    }

    /** Encode the status specified. 
     * <P>
     * @param status    The status to encode into a string.
     * @return  The status in text form.
     */
    public static String encode(int status) {
        String encoded = (String) MAPPINGS.get(new Integer(status));
        if (encoded == null) {
            encoded = encode(UNKNOWN);
        }
        return encoded;
    }
}
