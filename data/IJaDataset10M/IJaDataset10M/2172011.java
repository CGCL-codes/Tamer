package org.grailrtls.legacy.gcs.conversion.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class HubConnectionMessage extends ServerMessage {

    public static final byte MESSAGE_TYPE = 7;

    private long connectedSince;

    private long hubId;

    private byte connected;

    private byte[] ipAddress;

    public long getConnectedSince() {
        return connectedSince;
    }

    public void setConnectedSince(long connectedSince) {
        this.connectedSince = connectedSince;
    }

    public long getHubId() {
        return hubId;
    }

    public void setHubId(long hubId) {
        this.hubId = hubId;
    }

    public byte getConnected() {
        return connected;
    }

    public void setConnected(byte connected) {
        this.connected = connected;
    }

    public byte[] getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(byte[] ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean decodeSpecificMessage(byte[] messageBytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(messageBytes));
        this.connectedSince = in.readLong();
        this.hubId = in.readLong();
        this.connected = in.readByte();
        if (this.connected == 1) {
            this.ipAddress = new byte[4];
            in.read(this.ipAddress);
        } else {
            this.ipAddress = null;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString()).append(" | ");
        sb.append("Hub ").append(this.getHubId());
        if (this.connected == 1) {
            sb.append(" connected at ").append(new Date(this.getConnectedSince()));
        } else {
            sb.append(" disconnected.");
        }
        return sb.toString();
    }
}
