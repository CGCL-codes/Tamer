package net.sourceforge.x360mediaserve.plugins.flickr.impl.config;

import java.io.Serializable;

public class FlickrConfigModel implements Serializable {

    Boolean wantAuth = false;

    String authURL = "";

    String status = "";

    public Boolean getWantAuth() {
        return wantAuth;
    }

    public void setWantAuth(Boolean wantAuth) {
        this.wantAuth = wantAuth;
    }

    public String getAuthURL() {
        return authURL;
    }

    public void setAuthURL(String authURL) {
        this.authURL = authURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("FlickrConfigModel:");
        buffer.append(" Want auth:");
        buffer.append(wantAuth != null ? wantAuth : "");
        buffer.append(" Auth URL:");
        buffer.append(authURL != null ? authURL : "");
        buffer.append(" Status:");
        buffer.append(status != null ? status : "");
        return buffer.toString();
    }
}
