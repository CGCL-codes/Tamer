package org.dasein.cloud.network;

import java.io.Serializable;

public class Vlan implements Serializable {

    private static final long serialVersionUID = -3730962221402134025L;

    private String cidr;

    private String description;

    private String[] dnsServers;

    private String gateway;

    private String name;

    private String providerDataCenterId;

    private String providerOwnerId;

    private String providerRegionId;

    private String providerVlanId;

    public Vlan() {
    }

    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (ob == this) {
            return true;
        }
        if (ob instanceof Vlan) {
            Vlan v = (Vlan) ob;
            if (!providerOwnerId.equals(v.providerOwnerId)) {
                return false;
            }
            return providerVlanId.equals(v.providerVlanId);
        }
        return false;
    }

    public int hashCode() {
        return (providerOwnerId + providerVlanId).hashCode();
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderDataCenterId() {
        return providerDataCenterId;
    }

    public void setProviderDataCenterId(String providerDataCenterId) {
        this.providerDataCenterId = providerDataCenterId;
    }

    public String getProviderOwnerId() {
        return providerOwnerId;
    }

    public void setProviderOwnerId(String providerOwnerId) {
        this.providerOwnerId = providerOwnerId;
    }

    public String getProviderRegionId() {
        return providerRegionId;
    }

    public void setProviderRegionId(String providerRegionId) {
        this.providerRegionId = providerRegionId;
    }

    public String getProviderVlanId() {
        return providerVlanId;
    }

    public void setProviderVlanId(String providerVlanId) {
        this.providerVlanId = providerVlanId;
    }

    public String toString() {
        return cidr + " [" + providerOwnerId + "]";
    }

    public void setDnsServers(String[] dnsServers) {
        this.dnsServers = dnsServers;
    }

    public String[] getDnsServers() {
        return dnsServers;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGateway() {
        return gateway;
    }
}
