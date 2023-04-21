package com.google.code.xbeejavaapi.api;

import com.google.code.xbeejavaapi.api.LocalXBee.RemoteXBee;
import com.google.code.xbeejavaapi.api.Constants.Device_Type;
import org.apache.log4j.Logger;

/**
 *
 * @author David Miguel Antunes <davidmiguel [ at ] antunes.net>
 */
public class DiscoveredNode {

    private static final Logger logger = Logger.getLogger(DiscoveredNode.class);

    private XBeeAddress address;

    private String nodeIdentifier;

    private long parentNetworkAddress;

    private Device_Type deviceType;

    private long status;

    private long profileId;

    private long manufacturerId;

    private LocalXBee.RemoteXBee xbee;

    public DiscoveredNode(XBeeAddress address, String nodeIdentifier, long parentNetworkAddress, Device_Type deviceType, long status, long profileId, long manufacturerId, RemoteXBee xbee) {
        this.address = address;
        this.nodeIdentifier = nodeIdentifier;
        this.parentNetworkAddress = parentNetworkAddress;
        this.deviceType = deviceType;
        this.status = status;
        this.profileId = profileId;
        this.manufacturerId = manufacturerId;
        this.xbee = xbee;
    }

    public XBeeAddress getAddress() {
        return address;
    }

    public Device_Type getDeviceType() {
        return deviceType;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    public long getParentNetworkAddress() {
        return parentNetworkAddress;
    }

    public long getProfileId() {
        return profileId;
    }

    public long getStatus() {
        return status;
    }

    public RemoteXBee getXbee() {
        return xbee;
    }

    @Override
    public String toString() {
        return "address=" + address + "\n" + "nodeIdentifier=" + nodeIdentifier + "\n" + "parentNetworkAddress=" + "0x" + Long.toHexString(parentNetworkAddress).toUpperCase() + "\n" + "deviceType=" + deviceType + "\n" + "status=" + "0x" + Long.toHexString(status).toUpperCase() + "\n" + "profileId=" + "0x" + Long.toHexString(profileId).toUpperCase() + "\n" + "manufacturerId=" + "0x" + Long.toHexString(manufacturerId).toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiscoveredNode other = (DiscoveredNode) obj;
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }
}
