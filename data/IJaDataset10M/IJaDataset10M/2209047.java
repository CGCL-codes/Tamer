package server;

public class ServerConfiguration {

    private int serverPort;

    private String serverServiceName;

    private String securityFile;

    private String codeBase;

    private String key;

    private String serverPublicHostname;

    private long lastUID;

    public String getKey() {
        return key;
    }

    public String getServerPublicHostname() {
        return serverPublicHostname;
    }

    public void setServerPublicHostname(String serverPublicHostname) {
        this.serverPublicHostname = serverPublicHostname;
    }

    public void setKey(String uuid) {
        this.key = uuid;
    }

    public int getServerPort() {
        return serverPort;
    }

    public long getLastUID() {
        return lastUID;
    }

    public void setLastUID(long lastUID) {
        this.lastUID = lastUID;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerServiceName() {
        return serverServiceName;
    }

    public void setServerServiceName(String serverServiceName) {
        this.serverServiceName = serverServiceName;
    }

    public String getSecurityFile() {
        return securityFile;
    }

    public void setSecurityFile(String securityFile) {
        this.securityFile = securityFile;
    }

    public String getCodeBase() {
        return codeBase;
    }

    public void setCodeBase(String codeBase) {
        this.codeBase = codeBase;
    }
}
