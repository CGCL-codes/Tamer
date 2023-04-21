package org.p4pp.proxy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Hashtable;

/**
 * Represents HTTP/1.1 GET-requests.
 *
 * @author Sebastian Kamp
 */
public class GETRequest extends HttpRequest {

    /**
     * Constructs a <CODE>GETRequest</CODE> object from a <CODE>RequestLine</CODE> object
     * and a <CODE>String</CODE> that holds the message headers of the request.
     * <CODE>GETRequest</CODE> are the most usual <CODE>HttpRequest</CODE>s.
     * 
     * @param requestLine a <CODE>RequestLine</CODE> object
     * @param headerString a <CODE>String</CODE> that holds the message headers of the request.
     * @see HttpRequest#parseHeaderString
     */
    GETRequest(RequestLine requestLine, String headerString) {
        this.requestLine = requestLine;
        this.headers = new Hashtable();
        this.parseHeaderString(headerString);
        this.cookies = new Hashtable();
        this.parseCookies();
    }

    /**
     * @see HttpRequest
     */
    public void callRequestHandleMethodFor(Switch theSwitch) throws SwitchException, IOException {
        theSwitch.handleGETRequest();
    }

    /**
     * "GET" is the default case anyway, we don't have to do anything here.
     * This is a method with an empty body.
     * 
     * @param connection a <CODE>HttpURLConnection</CODE> object
     */
    public void setSpecificRequestProperties(HttpURLConnection connection) {
    }
}
