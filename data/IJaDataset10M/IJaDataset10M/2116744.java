package com.jabberwookie.ns.jabber;

import com.ssttr.xml.XMLElement;

/**
 * This class is pretty useless for recieving error messages since they don't
 * have a namespace that this can be mapped to.  However, it's very usefull
 * for sending error messages.
 *<pre>
 *    Code   Description
 *    400    Bad Request
 *    401    Unauthorized
 *    402    Payment Required
 *    403    Forbidden
 *    404    Not Found
 *    405    Not Allowed
 *    406    Not Acceptable
 *    407    Registration Required
 *    408    Request Timeout
 *    409    Username Not Available
 *    500    Internal Server Error
 *    501    Not Implemented
 *    502    Remote Server Error
 *    503    Service Unavailable
 *    504    Remote Server Timeout
 *</pre>
 * @author  smeiners
 */
public class Error extends XMLElement {

    public static final int BAD_REQUEST = 400;

    public static final int UNAUTHORIZED = 401;

    public static final int PAYMENT_REQUIRED = 402;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND = 404;

    public static final int NOT_ALLOWED = 405;

    public static final int NOT_ACCEPTABLE = 406;

    public static final int REGISTRATION_REQUIRED = 407;

    public static final int REQUEST_TIMEOUT = 408;

    public static final int USERNAME_NOT_AVAILABLE = 409;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int NOT_IMPLEMENTED = 501;

    public static final int REMOTE_SERVER_ERROR = 502;

    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int REMOTE_SERVER_TIMEOUT = 504;

    /** Creates a new instance of Error */
    public Error() {
        super(Const.ERROR);
    }

    /** Creates a new instance of Error */
    public Error(int code) {
        super(Const.ERROR);
        setCode(code);
        setMessage(code);
    }

    /** Creates a new instance of Error */
    public Error(int code, String message) {
        super(Const.ERROR);
        setCode(code);
        setMessage(message);
    }

    public Error(XMLElement error) {
        super(error);
    }

    public void setCode(int code) {
        setAttribute("code", Integer.toString(code));
    }

    public int getCode() {
        return (int) getAttributeLong("code", -1);
    }

    public void setMessage(String message) {
        setValue(message);
    }

    public void setMessage(int code) {
        switch(code) {
            case BAD_REQUEST:
                setMessage("Bad Request");
                break;
            case UNAUTHORIZED:
                setMessage("Unauthorized");
                break;
            case PAYMENT_REQUIRED:
                setMessage("Payment Required");
                break;
            case FORBIDDEN:
                setMessage("Forbidden");
                break;
            case NOT_FOUND:
                setMessage("Not Found");
                break;
            case NOT_ALLOWED:
                setMessage("Not Allowed");
                break;
            case NOT_ACCEPTABLE:
                setMessage("Not Acceptable");
                break;
            case REGISTRATION_REQUIRED:
                setMessage("Registration Required");
                break;
            case REQUEST_TIMEOUT:
                setMessage("Request Timeout");
                break;
            case USERNAME_NOT_AVAILABLE:
                setMessage("Username Not Available");
                break;
            case INTERNAL_SERVER_ERROR:
                setMessage("Internal Server Error");
                break;
            case NOT_IMPLEMENTED:
                setMessage("Not Impelmented");
                break;
            case REMOTE_SERVER_ERROR:
                setMessage("Remote Server Error");
                break;
            case SERVICE_UNAVAILABLE:
                setMessage("Service Unavailable");
                break;
            case REMOTE_SERVER_TIMEOUT:
                setMessage("Remote Server Timeout");
                break;
            default:
                setMessage("Unknown");
                break;
        }
    }

    public String getMessage() {
        return getValue();
    }
}
