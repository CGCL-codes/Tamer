package org.personalsmartspace.onm.api;

/**
 * @author David McKitterick
 *
 */
public class IncomingMessage {

    private String targetServiceID;

    private String sourceServiceID;

    private PSSAdvertisement sourcePSS = null;

    private String sourcePeerID = null;

    private String xmlMessage;

    public IncomingMessage(String targetServiceID, String sourceServiceID, String xmlMessage) {
        this.targetServiceID = targetServiceID;
        this.sourceServiceID = sourceServiceID;
        this.xmlMessage = xmlMessage;
    }

    public IncomingMessage(String targetServiceID, String sourceServiceID, PSSAdvertisement sourcePSS, String xmlMessage) {
        this.targetServiceID = targetServiceID;
        this.sourceServiceID = sourceServiceID;
        this.sourcePSS = sourcePSS;
        this.xmlMessage = xmlMessage;
    }

    public IncomingMessage(String targetServiceID, String sourceServiceID, String sourcePeerID, String xmlMessage) {
        this.targetServiceID = targetServiceID;
        this.sourceServiceID = sourceServiceID;
        this.sourcePeerID = sourcePeerID;
        this.xmlMessage = xmlMessage;
    }

    /**
     * @return the targetServiceID
     */
    public String getTargetServiceID() {
        return targetServiceID;
    }

    /**
     * @return the sourceServiceID
     */
    public String getSourceServiceID() {
        return sourceServiceID;
    }

    /**
     * @return the sourcePSS
     */
    public PSSAdvertisement getSourcePSS() {
        return sourcePSS;
    }

    /**
     * @return the sourcePeerID
     */
    public String getSourcePeerID() {
        return sourcePeerID;
    }

    /**
     * @return the xmlMessage
     */
    public String getXmlMessage() {
        return xmlMessage;
    }
}
