package org.wikiup.modules.webdav.util;

import java.util.Hashtable;

public class StatusUtil {

    private static Hashtable mapStatusCodes = new Hashtable();

    public static String getStatusText(int nHttpStatusCode) {
        Integer intKey = new Integer(nHttpStatusCode);
        return (String) mapStatusCodes.get(intKey);
    }

    public static String getStatusResult(int nHttpStatusCode) {
        String status = getStatusText(nHttpStatusCode);
        return status != null ? "HTTP/1.1 " + nHttpStatusCode + " " + status : getStatusResult(SC_INTERNAL_SERVER_ERROR);
    }

    private static void addStatusCodeMap(int nKey, String strVal) {
        mapStatusCodes.put(new Integer(nKey), strVal);
    }

    public static final int SC_CONTINUE = 100;

    public static final int SC_SWITCHING_PROTOCOLS = 101;

    public static final int SC_PROCESSING = 102;

    public static final int SC_OK = 200;

    public static final int SC_CREATED = 201;

    public static final int SC_ACCEPTED = 202;

    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

    public static final int SC_NO_CONTENT = 204;

    public static final int SC_RESET_CONTENT = 205;

    public static final int SC_PARTIAL_CONTENT = 206;

    public static final int SC_MULTI_STATUS = 207;

    public static final int SC_MULTIPLE_CHOICES = 300;

    public static final int SC_MOVED_PERMANENTLY = 301;

    public static final int SC_MOVED_TEMPORARILY = 302;

    public static final int SC_SEE_OTHER = 303;

    public static final int SC_NOT_MODIFIED = 304;

    public static final int SC_USE_PROXY = 305;

    public static final int SC_BAD_REQUEST = 400;

    public static final int SC_UNAUTHORIZED = 401;

    public static final int SC_PAYMENT_REQUIRED = 402;

    public static final int SC_FORBIDDEN = 403;

    public static final int SC_NOT_FOUND = 404;

    public static final int SC_METHOD_NOT_ALLOWED = 405;

    public static final int SC_NOT_ACCEPTABLE = 406;

    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

    public static final int SC_REQUEST_TIMEOUT = 408;

    public static final int SC_CONFLICT = 409;

    public static final int SC_GONE = 410;

    public static final int SC_LENGTH_REQUIRED = 411;

    public static final int SC_PRECONDITION_FAILED = 412;

    public static final int SC_REQUEST_TOO_LONG = 413;

    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    public static final int SC_EXPECTATION_FAILED = 417;

    public static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;

    public static final int SC_METHOD_FAILURE = 420;

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    public static final int SC_LOCKED = 423;

    public static final int SC_FAILED_DEPENDENCY = 424;

    public static final int SC_INTERNAL_SERVER_ERROR = 500;

    public static final int SC_NOT_IMPLEMENTED = 501;

    public static final int SC_BAD_GATEWAY = 502;

    public static final int SC_SERVICE_UNAVAILABLE = 503;

    public static final int SC_GATEWAY_TIMEOUT = 504;

    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

    public static final int SC_INSUFFICIENT_STORAGE = 507;

    static {
        addStatusCodeMap(SC_OK, "OK");
        addStatusCodeMap(SC_CREATED, "Created");
        addStatusCodeMap(SC_ACCEPTED, "Accepted");
        addStatusCodeMap(SC_NO_CONTENT, "No Content");
        addStatusCodeMap(SC_MOVED_PERMANENTLY, "Moved Permanently");
        addStatusCodeMap(SC_MOVED_TEMPORARILY, "Moved Temporarily");
        addStatusCodeMap(SC_NOT_MODIFIED, "Not Modified");
        addStatusCodeMap(SC_BAD_REQUEST, "Bad Request");
        addStatusCodeMap(SC_UNAUTHORIZED, "Unauthorized");
        addStatusCodeMap(SC_FORBIDDEN, "Forbidden");
        addStatusCodeMap(SC_NOT_FOUND, "Not Found");
        addStatusCodeMap(SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        addStatusCodeMap(SC_NOT_IMPLEMENTED, "Not Implemented");
        addStatusCodeMap(SC_BAD_GATEWAY, "Bad Gateway");
        addStatusCodeMap(SC_SERVICE_UNAVAILABLE, "Service Unavailable");
        addStatusCodeMap(SC_CONTINUE, "Continue");
        addStatusCodeMap(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        addStatusCodeMap(SC_NOT_ACCEPTABLE, "Not Acceptable");
        addStatusCodeMap(SC_CONFLICT, "Conflict");
        addStatusCodeMap(SC_PRECONDITION_FAILED, "Precondition Failed");
        addStatusCodeMap(SC_REQUEST_TOO_LONG, "Request Too Long");
        addStatusCodeMap(SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        addStatusCodeMap(SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable");
        addStatusCodeMap(SC_SWITCHING_PROTOCOLS, "Switching Protocols");
        addStatusCodeMap(SC_NON_AUTHORITATIVE_INFORMATION, "Non Authoritative Information");
        addStatusCodeMap(SC_RESET_CONTENT, "Reset Content");
        addStatusCodeMap(SC_GATEWAY_TIMEOUT, "Gateway Timeout");
        addStatusCodeMap(SC_HTTP_VERSION_NOT_SUPPORTED, "Http Version Not Supported");
        addStatusCodeMap(SC_PROCESSING, "Processing");
        addStatusCodeMap(SC_MULTI_STATUS, "Multi-Status");
        addStatusCodeMap(SC_UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        addStatusCodeMap(SC_INSUFFICIENT_SPACE_ON_RESOURCE, "Insufficient Space On Resource");
        addStatusCodeMap(SC_METHOD_FAILURE, "Method Failure");
        addStatusCodeMap(SC_LOCKED, "Locked");
        addStatusCodeMap(SC_INSUFFICIENT_STORAGE, "Insufficient Storage");
        addStatusCodeMap(SC_FAILED_DEPENDENCY, "Failed Dependency");
    }
}
