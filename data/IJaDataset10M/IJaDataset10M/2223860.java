package org.bissa;

@Deprecated
public class NodeInfo {

    private static volatile NodeInfo nodeInfo;

    private String bootAddress;

    private int bootPort;

    private String localAddress;

    private int localPort;

    private String configurationFile;

    private boolean natSearch = false;

    private boolean firewall = false;

    private NodeInfo() {
    }

    public static NodeInfo getInstance() {
        if (nodeInfo == null) {
            synchronized (NodeInfo.class) {
                if (nodeInfo == null) {
                    nodeInfo = new NodeInfo();
                }
            }
        }
        return nodeInfo;
    }

    public String getBootAddress() {
        return bootAddress;
    }

    public void setBootAddress(String bootAddress) {
        this.bootAddress = bootAddress;
    }

    public int getBootPort() {
        return bootPort;
    }

    public void setBootPort(int bootPort) {
        if (bootPort >= 0) {
            this.bootPort = bootPort;
        }
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        if (localPort >= 0) {
            this.localPort = localPort;
        }
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public boolean isNatSearch() {
        return natSearch;
    }

    public void setNatSearch(boolean natSearch) {
        this.natSearch = natSearch;
    }

    public boolean isFirewall() {
        return firewall;
    }

    public void setFirewall(boolean firewall) {
        this.firewall = firewall;
    }
}
