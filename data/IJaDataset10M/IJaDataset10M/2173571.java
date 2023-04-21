package jportforwarder;

public class ConfigInfo {

    public String protocol;

    public String localhost;

    public String remotehost;

    public String application;

    public ConfigInfo() {
    }

    public ConfigInfo(String protocol, String localhost, String remotehost, String application) {
        this.protocol = protocol;
        this.localhost = localhost;
        this.remotehost = remotehost;
        this.application = application;
    }

    public String toString() {
        String ret = protocol + " " + localhost + " " + remotehost;
        if (application != null) {
            ret += " " + application;
        }
        return ret;
    }
}
