package pl.edu.pjwstk.mteam.core;

import java.io.Serializable;

public class GeneralNodeInfo implements Serializable {

    private String ip;

    private int port;

    public GeneralNodeInfo() {
    }

    @Override
    protected void finalize() throws Throwable {
        this.ip = null;
        super.finalize();
    }

    public GeneralNodeInfo(String ipAddr, int portNum) {
        ip = ipAddr;
        port = portNum;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIP(String ipAddr) {
        ip = ipAddr;
    }

    public void setPort(int portNum) {
        port = portNum;
    }
}
