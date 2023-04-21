package org.jwebsocket.packetProcessors;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.kit.RawPacket;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;

/**
 * converts CSV formatted data packets into tokens and vice versa.
 * @author aschulze
 */
public class CSVProcessor {

    /**
     * converts a CSV formatted data packet into a token.
     * @param aDataPacket
     * @return
     */
    public static Token packetToToken(WebSocketPacket aDataPacket) {
        Token lToken = TokenFactory.createToken();
        try {
            String aData = aDataPacket.getString("UTF-8");
            String[] lItems = aData.split(",");
            for (int i = 0; i < lItems.length; i++) {
                String[] lKeyVal = lItems[i].split("=", 2);
                if (lKeyVal.length == 2) {
                    String lVal = lKeyVal[1];
                    if (lVal.length() <= 0) {
                        lToken.setValidated(lKeyVal[0], null);
                    } else if (lVal.startsWith("\"") && lVal.endsWith("\"")) {
                        lVal = lVal.replace("\\x2C", ",");
                        lVal = lVal.replace("\\x22", "\"");
                        lToken.setValidated(lKeyVal[0], lVal.substring(1, lVal.length() - 1));
                    } else {
                        lToken.setValidated(lKeyVal[0], lVal);
                    }
                }
            }
        } catch (UnsupportedEncodingException ex) {
        }
        return lToken;
    }

    private static String stringToCSV(String aString) {
        aString = aString.replace(",", "\\x2C");
        aString = aString.replace("\"", "\\x22");
        return ("\"" + aString + "\"");
    }

    private static String collectionToCSV(Collection<Object> aCollection) {
        String lRes = "";
        for (Object lItem : aCollection) {
            String llRes = objectToCSV(lItem);
            lRes += llRes + "|";
        }
        if (lRes.length() > 1) {
            lRes = lRes.substring(0, lRes.length() - 1);
        }
        lRes = "[" + lRes + "]";
        return lRes;
    }

    private static String objectToCSV(Object aObj) {
        String lRes;
        if (aObj == null) {
            lRes = "null";
        } else if (aObj instanceof String) {
            lRes = stringToCSV((String) aObj);
        } else if (aObj instanceof Collection) {
            lRes = collectionToCSV((Collection<Object>) aObj);
        } else {
            lRes = "\"" + aObj.toString() + "\"";
        }
        return lRes;
    }

    /**
     * converts a token into a CSV formatted data packet.
     * @param aToken
     * @return
     */
    public static WebSocketPacket tokenToPacket(Token aToken) {
        String lData = "";
        Iterator<String> lIterator = aToken.getKeyIterator();
        while (lIterator.hasNext()) {
            String lKey = lIterator.next();
            Object lVal = aToken.getString(lKey);
            lData += lKey + "=" + objectToCSV(lVal) + (lIterator.hasNext() ? "," : "");
        }
        WebSocketPacket lPacket = null;
        try {
            lPacket = new RawPacket(lData, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return lPacket;
    }
}
