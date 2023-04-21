package com.mgnutella.util.Parsers;

import com.mgnutella.core.constants.HttpConstants;
import com.mgnutella.core.model.bootstrapAndconnection.Ip;
import com.mgnutella.util.DB.IpDB;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author canavar
 */
public class HandShakeParser {

    private static String response;

    private static IpDB ipDB = IpDB.getInstance();

    /**
     *Parses the response, and if it is a "200" Ok message, returns true
     * @param  aResponse StringBuffer
     *@return boolean return true if it is a "200" OK message,false if "503" or anything else
     */
    public static int handShakeInitialParser(StringBuffer aResponse) {
        response = aResponse.toString();
        if (response.indexOf(HttpConstants.SERVENT_STATUS_OK_STR) != -1) {
            return HttpConnection.HTTP_OK;
        }
        if (response.indexOf(HttpConstants.SERVENT_STATUS_BUSY_STR) != -1) {
            return HttpConnection.HTTP_UNAVAILABLE;
        } else {
            return HttpConstants.SERVENT_STATUS_BROKEN_INT;
        }
    }

    /**
     *Parses X-Try-Ultrapeer response, and if the response has got  X-Try-Ultrapeers header,
     * then adds to IpDB
     * 
     */
    public static void xTryParser() {
        int xUltrapeerIndex = response.indexOf("X-Try-Ultrapeers:");
        if (xUltrapeerIndex == -1) {
            System.out.println("no XUltrapeerss");
            return;
        }
        response = response.substring(xUltrapeerIndex + 18, response.indexOf("\r\n\r\n", xUltrapeerIndex));
        int tempInt = 0;
        String ipAddress = new String();
        while (10 <= response.length()) {
            tempInt = response.indexOf(",");
            if (tempInt < 0) {
                break;
            }
            ipAddress = response.substring(0, tempInt);
            response = response.substring(tempInt + 1, response.length());
            ipDB.addRecord(new Ip(ipAddress));
            if (response.length() <= 21) {
                ipDB.addRecord(new Ip(response));
            }
        }
    }

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String aResponse) {
        response = aResponse;
    }
}
