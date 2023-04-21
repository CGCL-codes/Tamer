package com.cidero.upnp;

/**
 *  Class for use with AVTransport service.  Container for information 
 *  returned by the AVTransport GetCurrentConnectionInfo action
 *
 *  TODO: implementation not yet complete! (only needed IDs)
 */
public class AVConnectionInfo {

    int connectionID;

    int avTransportID;

    int renderingControlID;

    String protocolInfo;

    String peerConnectionManager;

    int peerConnectionID;

    String direction;

    String status;

    public AVConnectionInfo(int connectionID, int avTransportID, int renderingControlID) {
        this.connectionID = connectionID;
        this.avTransportID = avTransportID;
        this.renderingControlID = renderingControlID;
    }

    public void setConnectionID(int id) {
        connectionID = id;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void setAVTransportID(int id) {
        avTransportID = id;
    }

    public int getAVTransportID() {
        return avTransportID;
    }

    public void setRenderingControlID(int id) {
        renderingControlID = id;
    }

    public int getRenderingControlID() {
        return renderingControlID;
    }

    public void setProtocolInfo(String protocolInfo) {
        this.protocolInfo = protocolInfo;
    }

    public String getProtocolInfo() {
        return protocolInfo;
    }
}
