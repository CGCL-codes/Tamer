package com.avatal;

import java.util.HashMap;

/**
 * @author Olaf Siefart
 */
public class CMIDataModelErrors {

    private static final HashMap ERRORCODES = new HashMap();

    public static final Integer EC0 = new Integer(0);

    public static final Integer EC101 = new Integer(101);

    public static final Integer EC201 = new Integer(201);

    public static final Integer EC202 = new Integer(202);

    public static final Integer EC203 = new Integer(203);

    public static final Integer EC301 = new Integer(301);

    public static final Integer EC302 = new Integer(302);

    public static final Integer EC303 = new Integer(303);

    public static final Integer EC304 = new Integer(304);

    public static final Integer EC305 = new Integer(305);

    public static final Integer EC401 = new Integer(401);

    public static final Integer EC402 = new Integer(402);

    public static final Integer EC403 = new Integer(403);

    public static final Integer EC404 = new Integer(404);

    public static final Integer EC405 = new Integer(405);

    public static final Integer EC406 = new Integer(406);

    public static final Integer EC407 = new Integer(407);

    static {
        ERRORCODES.put(EC0, new Error(EC0, "No error", null));
        ERRORCODES.put(EC101, new Error(EC101, "General Exception", null));
        ERRORCODES.put(EC201, new Error(EC201, "Invalid argument error", null));
        ERRORCODES.put(EC202, new Error(EC202, "Element cannot have children", null));
        ERRORCODES.put(EC203, new Error(EC203, "Element not an array - cannot have count", null));
        ERRORCODES.put(EC301, new Error(EC301, "Not initialized", null));
        ERRORCODES.put(EC302, new Error(EC302, "Finder Exception", null));
        ERRORCODES.put(EC303, new Error(EC303, "EJB Exception", null));
        ERRORCODES.put(EC304, new Error(EC304, "Create Exception", null));
        ERRORCODES.put(EC305, new Error(EC305, "Remote Exception", null));
        ERRORCODES.put(EC401, new Error(EC401, "Not implemented error", null));
        ERRORCODES.put(EC402, new Error(EC402, "Invalid set value, element is a keyword", null));
        ERRORCODES.put(EC403, new Error(EC403, "Element is read only", null));
        ERRORCODES.put(EC404, new Error(EC404, "Element is write only", null));
        ERRORCODES.put(EC405, new Error(EC405, "Incorrect Data Type", null));
        ERRORCODES.put(EC406, new Error(EC406, "Not in exam modus", "You can't write this information in the exam modus"));
        ERRORCODES.put(EC407, new Error(EC407, "Not in browse modus", "You can't write in the browse modus"));
    }

    public static String getErrorString(Integer code) {
        return ((Error) ERRORCODES.get(code)).getErrorString();
    }

    public static String getErrorDescription(Integer code) {
        return ((Error) ERRORCODES.get(code)).getErrorDescription();
    }
}

class Error {

    Integer errorCode;

    String errorString;

    String errorDescription;

    public Error(Integer _errorCode, String _errorString, String _errorDescription) {
        if (null != _errorCode) {
            errorCode = _errorCode;
        }
        if (null != _errorCode) {
            errorString = _errorString;
        } else {
            errorString = new String();
        }
        if (null != _errorCode) {
            errorDescription = _errorDescription;
        } else {
            errorDescription = new String();
        }
    }

    public String getErrorString() {
        return errorString;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
