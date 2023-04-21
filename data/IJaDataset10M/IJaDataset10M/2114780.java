package org.jwebsocket.kit;

import java.util.Map;
import javolution.util.FastMap;
import org.jwebsocket.config.JWebSocketCommonConstants;

/**
 * Holds the header of the initial WebSocket request from the client
 * to the server. The RequestHeader internally maintains a FastMap to store
 * key/values pairs.
 * @author aschulze
 * @author jang
 * @version $Id: RequestHeader.java 596 2010-06-22 17:09:54Z fivefeetfurther $
 */
public final class RequestHeader {

    private Map<String, Object> mFields = new FastMap<String, Object>();

    public static final String WS_PROTOCOL = "subprot";

    public static final String WS_DRAFT = "draft";

    public static final String WS_ORIGIN = "origin";

    public static final String WS_LOCATION = "location";

    public static final String WS_PATH = "path";

    public static final String WS_SEARCHSTRING = "searchString";

    public static final String WS_HOST = "host";

    public static final String WS_SECKEY1 = "secKey1";

    public static final String WS_SECKEY2 = "secKey2";

    public static final String URL_ARGS = "args";

    public static final String TIMEOUT = "timeout";

    /**
	 * Puts a new object value to the request header.
	 * @param aKey
	 * @param aValue
	 */
    public void put(String aKey, Object aValue) {
        mFields.put(aKey, aValue);
    }

    /**
	 * Returns the object value for the given key or {@code null} if the
	 * key does not exist in the header.
	 * @param aKey
	 * @return object value for the given key or {@code null}.
	 */
    public Object get(String aKey) {
        return mFields.get(aKey);
    }

    /**
	 * Returns the string value for the given key or {@code null} if the
	 * key does not exist in the header.
	 * @param aKey
	 * @return String value for the given key or {@code null}.
	 */
    public String getString(String aKey) {
        return (String) mFields.get(aKey);
    }

    /**
	 * Returns a Map of the optional URL arguments passed by the client.
	 * @return Map of the optional URL arguments.
	 */
    public Map getArgs() {
        return (Map) mFields.get(URL_ARGS);
    }

    /**
	 * Returns the sub protocol passed by the client or a default value
	 * if no sub protocol has been passed either in the header or in the
	 * URL arguments.
	 * @return Sub protocol passed by the client or default value.
	 */
    public String getSubProtocol() {
        return resolveSubprotocol()[0];
    }

    /**
	 * Returns the subprotocol format in which messages are exchanged between client and server.
	 * @return subprotocol format passed by the client or default value
	 */
    public String getFormat() {
        return resolveSubprotocol()[1];
    }

    /**
	 * Tries to resolve correct subprotocol & format regardless of
	 * client version (old, new, hixie, hybi, browser, java).
	 * TODO: deprecate this method once majority of clients switch to new 'subprotocol/format' scheme
	 * @return array with two members: protocol and format
	 */
    private String[] resolveSubprotocol() {
        String lSubProt = (String) mFields.get(WS_PROTOCOL);
        if (lSubProt == null) {
            return new String[] { JWebSocketCommonConstants.WS_SUBPROTOCOL_DEFAULT, JWebSocketCommonConstants.WS_FORMAT_DEFAULT };
        } else {
            if (lSubProt.indexOf('/') != -1) {
                return lSubProt.split("/");
            } else {
                String format = JWebSocketCommonConstants.WS_FORMAT_DEFAULT;
                if (JWebSocketCommonConstants.WS_SUBPROT_JSON.equals(lSubProt) || JWebSocketCommonConstants.SUB_PROT_JSON.equals(lSubProt)) {
                    format = JWebSocketCommonConstants.WS_FORMAT_JSON;
                } else if (JWebSocketCommonConstants.WS_SUBPROT_XML.equals(lSubProt) || JWebSocketCommonConstants.SUB_PROT_XML.equals(lSubProt)) {
                    format = JWebSocketCommonConstants.WS_FORMAT_XML;
                } else if (JWebSocketCommonConstants.WS_SUBPROT_CSV.equals(lSubProt) || JWebSocketCommonConstants.SUB_PROT_CSV.equals(lSubProt)) {
                    format = JWebSocketCommonConstants.WS_FORMAT_CSV;
                } else if (JWebSocketCommonConstants.WS_SUBPROT_CUSTOM.equals(lSubProt) || JWebSocketCommonConstants.SUB_PROT_CUSTOM.equals(lSubProt)) {
                    format = JWebSocketCommonConstants.WS_FORMAT_CUSTOM;
                }
                return new String[] { lSubProt, format };
            }
        }
    }

    /**
	 * Returns the session timeout passed by the client or a default value
	 * if no session timeout has been passed either in the header or in the
	 * URL arguments.
	 * @param aDefault
	 * @return Session timeout passed by the client or default value.
	 */
    public Integer getTimeout(Integer aDefault) {
        Map lArgs = getArgs();
        Integer lTimeout = null;
        if (lArgs != null) {
            try {
                lTimeout = Integer.parseInt((String) (lArgs.get(TIMEOUT)));
            } catch (Exception lEx) {
            }
        }
        return (lTimeout != null ? lTimeout : aDefault);
    }

    public String getDraft() {
        return (String) mFields.get(WS_DRAFT);
    }
}
