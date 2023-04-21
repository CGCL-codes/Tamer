package de.searchworkorange.indexcrawler.remoteCommandServ;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class Configuration {

    private int port;

    private int maxThreads;

    private String serverVersion;

    public Configuration(int port, int maxThreads, String serverVersion) {
        this.port = port;
        this.maxThreads = maxThreads;
        this.serverVersion = serverVersion;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public int getPort() {
        return port;
    }

    public String getServerVersion() {
        return serverVersion;
    }
}
