package org.dasein.cloud.openstack.swift;

public class AuthenticationContext {

    private String authToken;

    private String myRegion;

    private String storageToken;

    private String cdnUrl;

    private String serverUrl;

    private String storageUrl;

    public AuthenticationContext() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getStorageToken() {
        if (storageToken == null) {
            return getAuthToken();
        }
        return storageToken;
    }

    public void setStorageToken(String storageToken) {
        this.storageToken = storageToken;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public String getLoadBalancerUrl(String regionId) {
        if (serverUrl == null) {
            return null;
        }
        regionId = regionId.toLowerCase();
        if (regionId.equals("lon")) {
            return serverUrl.replaceAll("servers", "loadbalancers");
        } else {
            return serverUrl.replaceAll("servers", regionId.toLowerCase() + ".loadbalancers");
        }
    }

    public String getMyRegion() {
        return myRegion;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getStorageUrl() {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl) {
        if (storageUrl != null) {
            String tmp = storageUrl.toLowerCase();
            if (tmp.contains(".dfw")) {
                myRegion = "DFW";
            } else if (tmp.contains(".ord")) {
                myRegion = "ORD";
            } else if (tmp.contains(".lon")) {
                myRegion = "LON";
            } else {
                myRegion = "ORD";
            }
        }
        this.storageUrl = storageUrl;
    }
}
