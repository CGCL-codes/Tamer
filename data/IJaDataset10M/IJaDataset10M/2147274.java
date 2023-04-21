package com.bitgate.util.services.protocol;

import java.util.HashMap;
import java.util.Iterator;
import com.bitgate.server.Server;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.node.NodeUtil;
import com.bitgate.util.services.KeepaliveException;
import com.bitgate.util.services.WorkerContext;
import com.bitgate.util.services.engine.DocumentEngine;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.socket.SocketTools;

/**
 * This class is used to generate response codes, used by the <code>WebWorker</code> class.  These response codes can also
 * be used by other classes, to generate response codes where they see fit.
 *
 * @version $Id: //depot/nuklees/util/services/protocol/WebResponseCodes.java#23 $
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 */
public class WebResponseCodes {

    public static final int HTTP_CONTINUE = 100;

    public static final int HTTP_OK = 200;

    public static final int HTTP_CREATED = 201;

    public static final int HTTP_ACCEPTED = 202;

    public static final int HTTP_NOT_AUTHORITATIVE = 203;

    public static final int HTTP_NO_CONTENT = 204;

    public static final int HTTP_RESET = 205;

    public static final int HTTP_PARTIAL = 206;

    public static final int HTTP_LOW_STORAGE_SPACE = 250;

    public static final int HTTP_MULT_CHOICE = 300;

    public static final int HTTP_MOVED_PERM = 301;

    public static final int HTTP_MOVED_TEMP = 302;

    public static final int HTTP_SEE_OTHER = 303;

    public static final int HTTP_NOT_MODIFIED = 304;

    public static final int HTTP_USE_PROXY = 305;

    public static final int HTTP_BAD_REQUEST = 400;

    public static final int HTTP_UNAUTHORIZED = 401;

    public static final int HTTP_PAYMENT_REQUIRED = 402;

    public static final int HTTP_FORBIDDEN = 403;

    public static final int HTTP_NOT_FOUND = 404;

    public static final int HTTP_BAD_METHOD = 405;

    public static final int HTTP_NOT_ACCEPTABLE = 406;

    public static final int HTTP_PROXY_AUTH = 407;

    public static final int HTTP_CLIENT_TIMEOUT = 408;

    public static final int HTTP_CONFLICT = 409;

    public static final int HTTP_GONE = 410;

    public static final int HTTP_LENGTH_REQUIRED = 411;

    public static final int HTTP_PRECON_FAILED = 412;

    public static final int HTTP_ENTITY_TOO_LARGE = 413;

    public static final int HTTP_REQ_TOO_LONG = 414;

    public static final int HTTP_UNSUPPORTED_TYPE = 415;

    public static final int HTTP_PARAMETER_NOT_UNDERSTOOD = 451;

    public static final int HTTP_CONFERENCE_NOT_FOUND = 452;

    public static final int HTTP_NOT_ENOUGH_BANDWIDTH = 453;

    public static final int HTTP_SESSION_NOT_FOUND = 454;

    public static final int HTTP_METHOD_NOT_VALID_STATE = 455;

    public static final int HTTP_HEADER_FIELD_NOT_VALID = 456;

    public static final int HTTP_INVALID_RANGE = 457;

    public static final int HTTP_PARAMETER_IS_READONLY = 458;

    public static final int HTTP_AGGREGATE_NOT_ALLOWED = 459;

    public static final int HTTP_ONLY_AGGREGATE_ALLOWED = 460;

    public static final int HTTP_UNSUPPORTED_TRANSPORT = 461;

    public static final int HTTP_DESTINATION_UNAVAILABLE = 462;

    public static final int HTTP_SERVER_ERROR = 500;

    public static final int HTTP_INTERNAL_ERROR = 500;

    public static final int HTTP_BAD_GATEWAY = 502;

    public static final int HTTP_UNAVAILABLE = 503;

    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    public static final int HTTP_VERSION = 505;

    public static final int HTTP_BANDWIDTH_EXCEEDED = 509;

    public static final int HTTP_OPTION_NOT_SUPPORTED = 551;

    /**
     * This returns the name of the response that HTTP/1.0 codes require.
     *
     * @param code The numeric code of the error.
     * @return String containing the error message in plain-text.
     */
    public static String get(int code) {
        switch(code) {
            case HTTP_CONTINUE:
                return "Continue";
            case HTTP_OK:
                return "OK";
            case HTTP_CREATED:
                return "Created";
            case HTTP_ACCEPTED:
                return "Accepted";
            case HTTP_NOT_AUTHORITATIVE:
                return "Not Authoritative";
            case HTTP_NO_CONTENT:
                return "No Content";
            case HTTP_RESET:
                return "Reset";
            case HTTP_PARTIAL:
                return "Partial";
            case HTTP_LOW_STORAGE_SPACE:
                return "Low On Storage Space";
            case HTTP_MULT_CHOICE:
                return "Multiple Choice";
            case HTTP_MOVED_PERM:
                return "Moved Permanently";
            case HTTP_MOVED_TEMP:
                return "Moved Temporarily";
            case HTTP_SEE_OTHER:
                return "See Other";
            case HTTP_NOT_MODIFIED:
                return "Not Modified";
            case HTTP_USE_PROXY:
                return "Use Proxy";
            case HTTP_PARAMETER_NOT_UNDERSTOOD:
                return "Parameter Not Understood";
            case HTTP_CONFERENCE_NOT_FOUND:
                return "Conference Not Found";
            case HTTP_NOT_ENOUGH_BANDWIDTH:
                return "Not Enough Bandwidth";
            case HTTP_SESSION_NOT_FOUND:
                return "Session Not Found";
            case HTTP_METHOD_NOT_VALID_STATE:
                return "Method Not Valid In This State";
            case HTTP_HEADER_FIELD_NOT_VALID:
                return "Header Field Not Valid For This Resource";
            case HTTP_INVALID_RANGE:
                return "Invalid Range";
            case HTTP_PARAMETER_IS_READONLY:
                return "Parameter Is Read-Only";
            case HTTP_AGGREGATE_NOT_ALLOWED:
                return "Aggregate operation not allowed";
            case HTTP_ONLY_AGGREGATE_ALLOWED:
                return "Only aggregate operation allowed";
            case HTTP_UNSUPPORTED_TRANSPORT:
                return "Unsupported transport";
            case HTTP_DESTINATION_UNAVAILABLE:
                return "Destination unreachable";
            case HTTP_BAD_REQUEST:
                return "Bad Request";
            case HTTP_UNAUTHORIZED:
                return "Unauthorized";
            case HTTP_PAYMENT_REQUIRED:
                return "Payment Required";
            case HTTP_FORBIDDEN:
                return "Forbidden";
            case HTTP_NOT_FOUND:
                return "Not Found";
            case HTTP_BAD_METHOD:
                return "Bad Method";
            case HTTP_NOT_ACCEPTABLE:
                return "Not Acceptable";
            case HTTP_PROXY_AUTH:
                return "Proxy Authentication";
            case HTTP_CLIENT_TIMEOUT:
                return "Client Timed Out";
            case HTTP_CONFLICT:
                return "Conflict";
            case HTTP_GONE:
                return "Gone";
            case HTTP_LENGTH_REQUIRED:
                return "Length Required";
            case HTTP_PRECON_FAILED:
                return "Precondition Failed";
            case HTTP_ENTITY_TOO_LARGE:
                return "Entity Too Large";
            case HTTP_REQ_TOO_LONG:
                return "Request Too Long";
            case HTTP_UNSUPPORTED_TYPE:
                return "Unsupported Type";
            case HTTP_SERVER_ERROR:
                return "Internal Server Error";
            case HTTP_BAD_GATEWAY:
                return "Bad Gateway";
            case HTTP_UNAVAILABLE:
                return "Unavailable";
            case HTTP_GATEWAY_TIMEOUT:
                return "Gateway Timeout";
            case HTTP_VERSION:
                return "Version";
            case HTTP_BANDWIDTH_EXCEEDED:
                return "Bandwidth Limit Exceeded";
            case HTTP_OPTION_NOT_SUPPORTED:
                return "Option not supported";
            default:
                return "Unknown Error";
        }
    }

    /**
     * Creates a body for the generic response.
     *
     * @param ww The <code>ClientContext</code> class of the current client session.
     * @param code The return code to generate.
     * @param message The message to put in the body of the response.
     */
    public static StringBuffer createGenericResponse(WorkerContext ww, int code, String message) {
        StringBuffer msg = new StringBuffer();
        String errorPage = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.template']/property[@type='engine.error']/@value");
        if (errorPage != null) {
            HashMap vars = new HashMap();
            vars.put("error_code", "" + code);
            vars.put("error_message_simple", get(code));
            vars.put("error_message_complex", message);
            String sspExtensions[] = { "ssp" };
            String str[] = { "*" };
            RenderEngine engine = new RenderEngine(ww);
            try {
                DocumentEngine docEngine = new DocumentEngine(ww, errorPage, str, sspExtensions);
                engine.preloadVariables(vars);
                engine.loadPostVariables(vars);
                engine.getVariableContainer().setVariable("ERROR_CODE", "" + code);
                engine.getVariableContainer().setVariable("ERROR_MESSAGE_SIMPLE", get(code));
                engine.getVariableContainer().setVariable("ERROR_MESSAGE_COMPLEX", message);
                engine.setDocumentEngine(docEngine);
                engine.getRenderContext().setCurrentDocroot(".");
                docEngine.setCurrentDocroot(".");
                docEngine.setRenderedFilename(errorPage);
                docEngine.setProcServerName(engine.getRenderContext().getCurrentProcServerName());
                docEngine.setProcHostName(engine.getRenderContext().getCurrentProcHostName());
                msg = docEngine.render(engine);
            } catch (Exception e) {
                msg = new StringBuffer();
                Debug.log("Unable to transform error page from XSL and XML to HTML: " + e.getMessage());
                msg.append("<TITLE>" + code + " - " + get(code) + "</TITLE>\r\n");
                msg.append("<BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\">\r\n\r\n");
                msg.append("<TABLE CELLPADDING=5 WIDTH=100%>\r\n");
                msg.append("<TR><TD BGCOLOR=\"#CCCCCC\"><FONT SIZE=+2>" + get(code) + "</FONT></TD></TR>\r\n");
                msg.append("</TABLE>\r\n\r\n");
                msg.append("<BR>\r\n");
                msg.append(message);
            }
        } else {
            msg.append("<TITLE>" + code + " - " + get(code) + "</TITLE>\r\n");
            msg.append("<BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\">\r\n\r\n");
            msg.append("<TABLE CELLPADDING=5 WIDTH=100%>\r\n");
            msg.append("<TR><TD BGCOLOR=\"#CCCCCC\"><FONT SIZE=+2>" + get(code) + "</FONT></TD></TR>\r\n");
            msg.append("</TABLE>\r\n\r\n");
            msg.append("<BR>\r\n");
            msg.append(message);
        }
        return msg;
    }

    /**
     * This generates a "generic" response back to the client, based on the message code - the best thing this could be used
     * for is like a "404" message, or really simple, quick messages.  Also uses an internal XML/XSLT file to transform the 
     * error output and display it back to the client.  If this fails, it uses a very generic error page as a last resort.
     *
     * @param ww The <code>ClientContext</code> class of the current client session.
     * @param code The return code to generate.
     * @param message The message to put in the body of the response.
     */
    public static void genericResponse(WorkerContext ww, int code, String message) {
        StringBuffer msg = createGenericResponse(ww, code, message);
        StringBuffer header = ww.createHeader(null, code, msg.toString().length(), 0L, "text/html", false, false);
        try {
            SocketTools.sendBuffer(ww.getClientContext(), header, msg);
        } catch (Exception e) {
            Debug.debug("Unable to send data to client: " + e);
            return;
        }
        msg = null;
        header = null;
    }

    /**
     * This generates a "generic" response back to the client, based on the message code - the best thing this could be used
     * for is like a "404" message, or really simple, quick messages.  This function allows you to add extra headers to the
     * message that is sent.  Also uses an internal XML/XSLT file to transform the error output and display it back to the
     * client.  If this fails, it uses a very generic error page as a last resort.
     *
     * @param ww The <code>ClientContext</code> class of the current client session.
     * @param code The return code to generate.
     * @param message The message to put in the body of the response.
     * @param extraHeaders Extra list of headers to add.
     */
    public static void genericResponseWithHeaders(WorkerContext ww, int code, String message, HashMap extraHeaders) {
        StringBuffer header;
        StringBuffer msg = new StringBuffer();
        String errorPage = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.template']/property[@type='engine.error']/@value");
        if (errorPage != null) {
            HashMap vars = new HashMap();
            vars.put("error_code", "" + code);
            vars.put("error_message_simple", get(code));
            vars.put("error_message_complex", message);
            String sspExtensions[] = { "ssp" };
            String str[] = { "*" };
            RenderEngine engine = new RenderEngine(ww);
            try {
                DocumentEngine docEngine = new DocumentEngine(ww, errorPage, str, sspExtensions);
                engine.preloadVariables(vars);
                engine.loadPostVariables(vars);
                engine.getVariableContainer().setVariable("ERROR_CODE", "" + code);
                engine.getVariableContainer().setVariable("ERROR_MESSAGE_SIMPLE", get(code));
                engine.getVariableContainer().setVariable("ERROR_MESSAGE_COMPLEX", message);
                engine.setDocumentEngine(docEngine);
                engine.getRenderContext().setCurrentDocroot(".");
                docEngine.setCurrentDocroot(".");
                docEngine.setRenderedFilename(errorPage);
                docEngine.setProcServerName(engine.getRenderContext().getCurrentProcServerName());
                docEngine.setProcHostName(engine.getRenderContext().getCurrentProcHostName());
                msg = docEngine.render(engine);
            } catch (Exception e) {
                msg = new StringBuffer();
                Debug.log("Unable to transform error page from XSL and XML to HTML: " + e.getMessage());
                msg.append("<TITLE>" + code + " - " + get(code) + "</TITLE>\r\n");
                msg.append("<BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\">\r\n\r\n");
                msg.append("<TABLE CELLPADDING=5 WIDTH=100%>\r\n");
                msg.append("<TR><TD BGCOLOR=\"#CCCCCC\"><FONT SIZE=+2>" + get(code) + "</FONT></TD></TR>\r\n");
                msg.append("</TABLE>\r\n\r\n");
                msg.append("<BR>\r\n");
                msg.append(message);
            }
        } else {
            msg.append("<TITLE>" + code + " - " + get(code) + "</TITLE>\r\n");
            msg.append("<BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\">\r\n\r\n");
            msg.append("<TABLE CELLPADDING=5 WIDTH=100%>\r\n");
            msg.append("<TR><TD BGCOLOR=\"#CCCCCC\"><FONT SIZE=+2>" + get(code) + "</FONT></TD></TR>\r\n");
            msg.append("</TABLE>\r\n\r\n");
            msg.append("<BR>\r\n");
            msg.append(message);
        }
        header = ww.createHeader(null, code, msg.toString().length(), 0L, "text/html", false, false);
        if (extraHeaders != null) {
            Iterator elements = extraHeaders.keySet().iterator();
            while (elements.hasNext()) {
                String key = (String) elements.next();
                String val = (String) extraHeaders.get(key);
                header.append(key + ": " + val + "\r\n");
            }
            elements = null;
        }
        try {
            SocketTools.sendBuffer(ww.getClientContext(), header, msg);
        } catch (KeepaliveException e) {
            Debug.debug("Keepalive timeout during write: " + e.getMessage());
        }
        header = null;
        msg = null;
    }
}
