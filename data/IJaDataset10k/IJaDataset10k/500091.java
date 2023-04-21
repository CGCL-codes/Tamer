package net.sf.wmutils.is.packages.streams;

import org.apache.log4j.Logger;
import com.wm.app.b2b.server.HTTPState;
import com.wm.app.b2b.server.InvokeState;
import com.wm.app.b2b.server.ProtocolInfoIf;
import com.wm.app.b2b.server.ProtocolState;
import com.wm.net.HttpHeader;

/**
 * Helper class providing static utility methods.
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class);

    private static boolean tracing;

    public static boolean isTracing() {
        return tracing;
    }

    public static void setTracing(boolean pTracing) {
        tracing = pTracing;
    }

    /**
	 * Extracts the SOAP action from the given request.
	 */
    public static String getSOAPAction(final InvokeState pState) {
        final InvokeState state = pState == null ? InvokeState.getCurrentState() : pState;
        if (state != null) {
            final ProtocolInfoIf protocolInfoIf = state.getProtocolInfoIf();
            if (protocolInfoIf != null && protocolInfoIf instanceof ProtocolState) {
                final ProtocolState protocolState = (ProtocolState) protocolInfoIf;
                final HttpHeader header = protocolState.getReqHdr();
                String value = header.getFieldValue("SOAPAction");
                return parseFieldValue(value);
            }
        }
        return null;
    }

    /**
	 * Sets the HTTP response code.
	 */
    public static void setHttpResponseCode(final InvokeState pState, int pStatusCode, String pStatusMessage) {
        final InvokeState state = pState == null ? InvokeState.getCurrentState() : pState;
        if (state != null) {
            final ProtocolInfoIf protocolInfoIf = state.getProtocolInfoIf();
            if (protocolInfoIf != null && protocolInfoIf instanceof HTTPState) {
                HTTPState httpState = (HTTPState) protocolInfoIf;
                httpState.setResponse(pStatusCode, pStatusMessage == null ? ("HTTP Status: " + pStatusCode) : pStatusMessage);
                log.debug("setHTTPResponseCode: " + pStatusCode + ", " + pStatusMessage);
                return;
            }
        }
        log.warn("setHTTPResponseCode: Unable to set status code " + pStatusCode + " (" + pStatusMessage + "); state=" + (state == null ? "null" : state.getClass().getName()) + ", protocolInfoIf=" + (state == null || state.getProtocolInfoIf() == null ? "null" : state.getProtocolInfoIf().getClass().getName()));
    }

    static String parseFieldValue(String pValue) {
        if (pValue == null) {
            return null;
        }
        final String value = pValue.trim();
        if (value.length() == 0) {
            return null;
        }
        char c = value.charAt(0);
        if (c == '\'' || c == '\"') {
            int offset = value.indexOf(c, 1);
            if (offset == -1) {
                return null;
            }
            return value.substring(1, offset);
        }
        return value;
    }
}
