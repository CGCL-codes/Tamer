package jarden.ws.testing.engine;

import org.apache.xmlbeans.XmlObject;

public class XmlResponseInfo {

    private XmlObject responsePayload;

    private int responseCode;

    private String responseMessage;

    public XmlResponseInfo(XmlObject responsePayload, int responseCode, String responseMessage) {
        this.responsePayload = responsePayload;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public XmlObject getResponsePayload() {
        return responsePayload;
    }
}
