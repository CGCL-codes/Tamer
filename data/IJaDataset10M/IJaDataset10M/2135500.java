package com.microfly.core;

import java.io.Serializable;

public class FtpHost implements Serializable {

    protected String hostname;

    protected String remotedir;

    protected int remoteport;

    protected String uname;

    protected String upasswd;

    public FtpHost(String hostname, String remotedir, int remoteport, String uname, String upasswd) {
        this.hostname = hostname;
        this.remotedir = remotedir;
        this.remoteport = remoteport == 0 ? 21 : remoteport;
        this.uname = uname;
        this.upasswd = upasswd;
    }

    public FtpHost(String hostname, String uname, String upasswd) {
        this.hostname = hostname;
        this.remotedir = "/";
        this.remoteport = 21;
        this.uname = uname;
        this.upasswd = upasswd;
    }

    public boolean equals(FtpHost ahost) {
        if (ahost == null) return false;
        if (hostname == null && ahost.GetHostname() != null) return false;
        if (!ahost.GetHostname().equals(hostname)) return false;
        if (remotedir == null && ahost.GetRemotedir() != null) return false;
        if (!ahost.GetRemotedir().equals(remotedir)) return false;
        if (remoteport == ahost.GetRemoteport()) return false;
        if (uname == null && ahost.GetUsername() != null) return false;
        if (!ahost.GetUsername().equals(uname)) return false;
        if (upasswd == null && ahost.GetUserpassword() != null) return false;
        if (!ahost.GetUserpassword().equals(upasswd)) return false;
        return true;
    }

    public String GetHostname() {
        return hostname;
    }

    public String GetRemotedir() {
        return remotedir;
    }

    public int GetRemoteport() {
        return remoteport;
    }

    public String GetUsername() {
        return uname;
    }

    public String GetUserpassword() {
        return upasswd;
    }
}
