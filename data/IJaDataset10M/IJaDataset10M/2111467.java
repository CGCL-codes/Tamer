package socksviahttp.client.engine;

import java.net.*;
import common.log.*;
import socksviahttp.core.util.*;

public class Configuration extends LogConfiguration {

    public static final int SPY_MODE_NONE = 0;

    public static final int SPY_MODE_CLIENT = 1;

    public static final int SPY_MODE_SERVER = 2;

    public static final int SPY_MODE_BOTH = 3;

    private int spyMode = SPY_MODE_NONE;

    private int port = 1080;

    private boolean listenOnlyLocalhost = true;

    private ServerInfo[] servers = new ServerInfo[0];

    private int delay = 20;

    private boolean requestOnlyIfClientActivity = false;

    private long dontTryToMinimizeTrafficBefore = 10000;

    private long forceRequestAfter = 3000;

    private long continueRequestingAfterDataReceivedDuring = 5000;

    private long continueRequestingAfterDataSentDuring = 5000;

    private int maxRetries = 0;

    private long delayBetweenTries = 3000;

    Tunnel[] tunnels = new Tunnel[0];

    private boolean useProxy = false;

    private String proxyHost = null;

    private String proxyPort = null;

    private boolean proxyNeedsAuthentication = false;

    private String proxyUser = null;

    private String proxyPassword = null;

    public Configuration() {
        super();
    }

    public void load(String file) throws MalformedURLException {
        fileLoggingLevel = (int) PropertiesFileReader.getPropertyLongValue(file, "client.logfile.level");
        logFileName = PropertiesFileReader.getPropertyStringValue(file, "client.logfile.name");
        spyMode = (int) PropertiesFileReader.getPropertyLongValue(file, "client.spymode");
        String sAllServers = PropertiesFileReader.getPropertyStringValue(file, "socks.httpservers");
        String[] serverNames = StringUtils.stringSplit(sAllServers, ",", true);
        servers = new ServerInfo[serverNames.length];
        for (int i = 0; i < serverNames.length; i++) {
            String serverName = serverNames[i];
            String sUrl = PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".url");
            URL url = new URL(sUrl);
            String user = PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".user").trim();
            String password = PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".password").trim();
            long timeout = PropertiesFileReader.getPropertyLongValue(file, "socks." + serverName + ".timeout");
            boolean zipData = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".zipdata")).booleanValue();
            boolean encryptData = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".encryptdata")).booleanValue();
            String encryptionKey = PropertiesFileReader.getPropertyStringValue(file, "socks." + serverName + ".encryptionkey").trim();
            ServerInfo srv = new ServerInfo();
            srv.setServerName(serverName);
            srv.setUrl(url);
            srv.setUser(user);
            srv.setPassword(password);
            srv.setTimeout(timeout);
            srv.setZipData(zipData);
            srv.setEncryptData(encryptData);
            srv.setEncryptionKey(encryptionKey);
            servers[i] = srv;
        }
        port = (int) PropertiesFileReader.getPropertyLongValue(file, "socks.server.port");
        listenOnlyLocalhost = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks.listen.localhost")).booleanValue();
        delay = (int) PropertiesFileReader.getPropertyLongValue(file, "socks.delay");
        requestOnlyIfClientActivity = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks.requestonlyifclientactivity")).booleanValue();
        dontTryToMinimizeTrafficBefore = PropertiesFileReader.getPropertyLongValue(file, "socks.donttrytominimizetrafficbefore");
        forceRequestAfter = PropertiesFileReader.getPropertyLongValue(file, "socks.forcerequestafter");
        continueRequestingAfterDataReceivedDuring = PropertiesFileReader.getPropertyLongValue(file, "socks.continuerequestingafterdatareceivedduring");
        continueRequestingAfterDataSentDuring = PropertiesFileReader.getPropertyLongValue(file, "socks.continuerequestingafterdatasentduring");
        maxRetries = (int) PropertiesFileReader.getPropertyLongValue(file, "socks.maxretries");
        delayBetweenTries = PropertiesFileReader.getPropertyLongValue(file, "socks.delaybetweenretries");
        String sAllActivePorts = PropertiesFileReader.getPropertyStringValue(file, "tunnel.ports.active");
        String[] sActivePorts = StringUtils.stringSplit(sAllActivePorts, ",", true);
        tunnels = new Tunnel[sActivePorts.length];
        for (int i = 0; i < sActivePorts.length; i++) {
            int localPort = Integer.parseInt(sActivePorts[i]);
            String destinationUri = PropertiesFileReader.getPropertyStringValue(file, "tunnel.localport." + localPort);
            tunnels[i] = new Tunnel(localPort, destinationUri);
        }
        useProxy = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks.proxy")).booleanValue();
        proxyHost = PropertiesFileReader.getPropertyStringValue(file, "socks.proxy.host");
        proxyPort = PropertiesFileReader.getPropertyStringValue(file, "socks.proxy.port");
        proxyNeedsAuthentication = Boolean.valueOf(PropertiesFileReader.getPropertyStringValue(file, "socks.proxy.authentication")).booleanValue();
        proxyUser = PropertiesFileReader.getPropertyStringValue(file, "socks.proxy.user");
        proxyPassword = PropertiesFileReader.getPropertyStringValue(file, "socks.proxy.password");
    }

    public int getDelay() {
        return delay;
    }

    public long getForceRequestAfter() {
        return forceRequestAfter;
    }

    public boolean isListenOnlyLocalhost() {
        return listenOnlyLocalhost;
    }

    public int getPort() {
        return port;
    }

    public boolean isProxyNeedsAuthentication() {
        return proxyNeedsAuthentication;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public boolean isRequestOnlyIfClientActivity() {
        return requestOnlyIfClientActivity;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public long getDelayBetweenTries() {
        return delayBetweenTries;
    }

    public Tunnel[] getTunnels() {
        return tunnels;
    }

    public int getSpyMode() {
        return spyMode;
    }

    public long getDontTryToMinimizeTrafficBefore() {
        return dontTryToMinimizeTrafficBefore;
    }

    public ServerInfo[] getServers() {
        return servers;
    }

    public long getContinueRequestingAfterDataReceivedDuring() {
        return continueRequestingAfterDataReceivedDuring;
    }

    public long getContinueRequestingAfterDataSentDuring() {
        return continueRequestingAfterDataSentDuring;
    }
}
