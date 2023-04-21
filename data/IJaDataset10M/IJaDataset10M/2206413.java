package com.prolixtech.jaminid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * This class captures the essential messages in the HTTP protocol. It behaves
 * as a singleton.
 * 
 * @author Constantinos Michael
 * 
 */
public class Protocol {

    public static final String MIMEFILE = "config/MIME.XML";

    public static final String HTTP_VERSION = "HTTP/1.1";

    public static final int CONTINUE = 100;

    public static final int SWITCHING = 101;

    public static final int OK = 200;

    public static final int CREATED = 201;

    public static final int ACCEPTED = 202;

    public static final int PROVISIONAL_INFORMATION = 203;

    public static final int NO_CONTENT = 204;

    public static final int RESET_CONTENT = 205;

    public static final int PARTIAL_CONTENT = 206;

    public static final int MULTIPLE_CHOICE = 300;

    public static final int MOVED_PERMANENTLY = 301;

    public static final int MOVED_TEMPORARILY = 302;

    public static final int SEE_OTHER = 303;

    public static final int NOT_MODIFIED = 304;

    public static final int USE_PROXY = 305;

    public static final int BAD_REQUEST = 400;

    public static final int UNAUTHORIZED = 401;

    public static final int PAYMENT_REQUIRED = 402;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND = 404;

    public static final int NOT_ALLOWED = 405;

    public static final int NONE_ACCEPTABLE = 406;

    public static final int PROXY_AUTH_REQUIRED = 407;

    public static final int REQUEST_TIMEOUT = 408;

    public static final int CONFLICT = 409;

    public static final int GONE = 410;

    public static final int LENGTH_REQUIRED = 411;

    public static final int PRECONDITION_FAILED = 412;

    public static final int REQUEST_ENTITY_TOO_LARGE = 413;

    public static final int REQUEST_URI_TOO_LONG = 414;

    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    public static final int EXPECTATION_FAILED = 417;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int NOT_IMPLEMENTED = 501;

    public static final int BAD_GATEWAY = 502;

    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int GATEWAY_TIMEOUT = 504;

    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    public static final String HQ_IFMODSINCE = "If-Modified-Since";

    private static Properties MIME = new Properties();

    private static HashMap<String, Boolean> headerRequest = new HashMap<String, Boolean>();

    private static HashMap<String, String> headerResponseStatus = new HashMap<String, String>();

    private static HashMap<String, Boolean> requestMethod = new HashMap<String, Boolean>();

    private static Protocol theInstance;

    public static Protocol Instance() {
        if (theInstance == null) theInstance = new Protocol();
        return theInstance;
    }

    /**
     * initiates our definition of HTTP
     * 
     */
    private Protocol() {
        File mf = new File(MIMEFILE);
        if (mf.exists() == false) {
            makeNewMIME();
        } else {
            try {
                MIME.loadFromXML(new FileInputStream(mf));
            } catch (InvalidPropertiesFormatException e) {
                makeNewMIME();
            } catch (FileNotFoundException e) {
                makeNewMIME();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        requestMethod.put("OPTIONS", false);
        requestMethod.put("GET", true);
        requestMethod.put("HEAD", false);
        requestMethod.put("POST", true);
        requestMethod.put("PUT", false);
        requestMethod.put("DELETE", false);
        requestMethod.put("TRACE", false);
        requestMethod.put("CONNECT", false);
        headerRequest.put("Accept", true);
        headerRequest.put("Accept-Charset", true);
        headerRequest.put("Accept-Encoding", true);
        headerRequest.put("Accept-Language", true);
        headerRequest.put("Authorization", true);
        headerRequest.put("Expect", true);
        headerRequest.put("From", true);
        headerRequest.put("Host", true);
        headerRequest.put("If-Match", true);
        headerRequest.put("If-Modified-Since", true);
        headerRequest.put("If-None-Match", true);
        headerRequest.put("If-Range", true);
        headerRequest.put("If-Unmodified-Since", true);
        headerRequest.put("Max-Forwards", true);
        headerRequest.put("Proxy-Authorization", true);
        headerRequest.put("Range", true);
        headerRequest.put("Referer", true);
        headerRequest.put("TE", true);
        headerRequest.put("User-Agent", true);
        headerRequest.put("Cookie", true);
        headerResponseStatus.put("100", "Continue");
        headerResponseStatus.put("101", "Switching Protocols");
        headerResponseStatus.put("200", "OK");
        headerResponseStatus.put("201", "Created");
        headerResponseStatus.put("202", "Accepted");
        headerResponseStatus.put("203", "Non-Authoritative Information");
        headerResponseStatus.put("204", "No Content");
        headerResponseStatus.put("205", "Reset Content");
        headerResponseStatus.put("206", "Partial Content");
        headerResponseStatus.put("300", "Multiple Choices");
        headerResponseStatus.put("301", "Moved Permanently");
        headerResponseStatus.put("302", "Found");
        headerResponseStatus.put("303", "See Other");
        headerResponseStatus.put("304", "Not Modified");
        headerResponseStatus.put("305", "Use Proxy");
        headerResponseStatus.put("307", "Temporary Redirect");
        headerResponseStatus.put("400", "Bad Request");
        headerResponseStatus.put("401", "Unauthorized");
        headerResponseStatus.put("402", "Payment Required");
        headerResponseStatus.put("403", "Forbidden");
        headerResponseStatus.put("404", "Not Found");
        headerResponseStatus.put("405", "Method Not Allowed");
        headerResponseStatus.put("406", "Not Acceptable");
        headerResponseStatus.put("407", "Proxy Authentication Required");
        headerResponseStatus.put("408", "Request Time-out");
        headerResponseStatus.put("409", "Conflict");
        headerResponseStatus.put("410", "Gone");
        headerResponseStatus.put("411", "Length Required");
        headerResponseStatus.put("412", "Precondition Failed");
        headerResponseStatus.put("413", "Request Entity Too Large");
        headerResponseStatus.put("414", "Request-URI Too Large");
        headerResponseStatus.put("415", "Unsupported Media Type");
        headerResponseStatus.put("416", "Requested range not satisfiable");
        headerResponseStatus.put("417", "Expectation Failed");
        headerResponseStatus.put("500", "Internal Server Error");
        headerResponseStatus.put("501", "Not Implemented");
        headerResponseStatus.put("502", "Bad Gateway");
        headerResponseStatus.put("503", "Service Unavailable");
        headerResponseStatus.put("504", "Gateway Time-out");
        headerResponseStatus.put("505", "HTTP Version not supported");
    }

    /**
     * MIME type accessor. By using this, we understand that sometimes the wrong
     * headers might go out for a badly named file (e.g. an mp3 file with
     * extension .txt but this is how all the major HTTP servers do it anyway.
     * 
     * @param extension
     *            the extension of the file
     * @return the MIME type of the file according to the extension
     */
    public static String getMIME(String extension) {
        return MIME.getProperty(extension.toLowerCase());
    }

    /**
     * gets a String description of the status code e.g (200 OK) or (404 FnF)
     * 
     * @param statusCode
     *            the integer status code
     * @return the String status code
     */
    public static String getHeaderResponseStatus(int statusCode) {
        return headerResponseStatus.get(Integer.toString(statusCode));
    }

    /**
     * checks if a request header exists in the protocol
     * 
     * @param requestHeader
     *            the request headcer to check
     * @return true if it exists, false otherwise
     */
    public static boolean requestHeaderExists(String requestHeader) {
        return headerRequest.containsKey(requestHeader);
    }

    /**
     * Creates a new mime file with the bare basic defaults
     *
     */
    public static void makeNewMIME() {
        MIME.clear();
        MIME.setProperty(".gif", "image/gif");
        MIME.setProperty(".png", "image/png");
        MIME.setProperty(".jpeg", "image/jpeg");
        MIME.setProperty(".jpg", "image/jpeg");
        MIME.setProperty(".html", "text/html");
        MIME.setProperty(".htm", "text/html");
        MIME.setProperty(".css", "text/css");
        MIME.setProperty(".mp3", "audio/mp3");
        MIME.setProperty(".txt", "text/plain");
        try {
            OutputStream nmim = new FileOutputStream(Protocol.MIMEFILE);
            MIME.storeToXML(nmim, "\nJAMINID MIMETYPES\n\nThese are the default MIME types for the jaminid daemon. If you had a modified but invalid MIME file here, it may have been reverted to this.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
