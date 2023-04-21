package org.skunk.dav.client;

/**
 * Object wrapper for the various DAV methods.
 */
public class DAVMethodName {

    private String methodName;

    private boolean isExtension;

    public static final DAVMethodName GET = new DAVMethodName(DAVConstants.GET_METHOD, false);

    public static final DAVMethodName POST = new DAVMethodName(DAVConstants.POST_METHOD, false);

    public static final DAVMethodName HEAD = new DAVMethodName(DAVConstants.HEAD_METHOD, false);

    public static final DAVMethodName PUT = new DAVMethodName(DAVConstants.PUT_METHOD, false);

    public static final DAVMethodName DELETE = new DAVMethodName(DAVConstants.DELETE_METHOD, false);

    public static final DAVMethodName OPTIONS = new DAVMethodName(DAVConstants.OPTIONS_METHOD, false);

    public static final DAVMethodName TRACE = new DAVMethodName(DAVConstants.TRACE_METHOD, false);

    public static final DAVMethodName MOVE = new DAVMethodName(DAVConstants.MOVE_METHOD, true);

    public static final DAVMethodName MKCOL = new DAVMethodName(DAVConstants.MKCOL_METHOD, true);

    public static final DAVMethodName PROPFIND = new DAVMethodName(DAVConstants.PROPFIND_METHOD, true);

    public static final DAVMethodName PROPPATCH = new DAVMethodName(DAVConstants.PROPPATCH_METHOD, true);

    public static final DAVMethodName COPY = new DAVMethodName(DAVConstants.COPY_METHOD, true);

    public static final DAVMethodName LOCK = new DAVMethodName(DAVConstants.LOCK_METHOD, true);

    public static final DAVMethodName UNLOCK = new DAVMethodName(DAVConstants.UNLOCK_METHOD, true);

    private DAVMethodName(String methodName, boolean isExtension) {
        this.methodName = methodName;
    }

    public String toString() {
        return methodName;
    }

    public boolean isExtensionMethod() {
        return isExtension;
    }
}
