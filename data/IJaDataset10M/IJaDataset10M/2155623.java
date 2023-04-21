package net.sf.katta.util;

public class LoadTestNodeConfiguration extends KattaConfiguration {

    private static final String LOADTESTNODE_START_PORT = "loadtestnode.server.port.start";

    public LoadTestNodeConfiguration() {
        super("/katta.loadtestnode.properties");
    }

    public int getStartPort() {
        return getInt(LOADTESTNODE_START_PORT);
    }

    public void setStartPort(int startPort) {
        setProperty(LOADTESTNODE_START_PORT, startPort + "");
    }
}
