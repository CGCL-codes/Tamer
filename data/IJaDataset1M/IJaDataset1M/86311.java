package net.sourceforge.xmote;

/**
 * @author Jason Rush
 */
public class XmoteProperties {

    private String serverHost;

    private int serverPort;

    private String proxyHost;

    private int proxyPort;

    private boolean compress;

    public XmoteProperties() {
        serverHost = "0.0.0.0";
        serverPort = 0;
        proxyHost = null;
        proxyPort = 0;
        compress = false;
    }

    /**
   * @return Returns the serverHost.
   */
    public String getServerHost() {
        return serverHost;
    }

    /**
   * @param serverHost The serverHost to set.
   */
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    /**
   * @return Returns the serverPort.
   */
    public int getServerPort() {
        return serverPort;
    }

    /**
   * @param serverPort The serverPort to set.
   */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
   * @return Returns the proxyHost.
   */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
   * @param proxyHost The proxyHost to set.
   */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
   * @return Returns the proxyPort.
   */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
   * @param proxyPort The proxyPort to set.
   */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
   * @return Returns the compress.
   */
    public boolean isCompress() {
        return compress;
    }

    /**
   * @param compress The compress to set.
   */
    public void setCompress(boolean compress) {
        this.compress = compress;
    }
}
