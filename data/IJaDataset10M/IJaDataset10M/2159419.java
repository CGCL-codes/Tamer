package org.skunk.dav.client.gui;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import org.skunk.config.LocalConfigStore;
import org.skunk.config.Configurator;
import org.skunk.dav.client.DAVConstants;

/**
 *  encapsulates data about a DAV server.
 */
public class ServerData implements Cloneable, Serializable {

    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServerData.class);

    static final long serialVersionUID = -6873822752692653196L;

    protected static ServerDataContainer container = new ServerDataContainer();

    public static void init() {
        try {
            loadServerList();
        } catch (Exception e) {
            log.error("Exception", e);
            container.setServerDataVector(new Vector());
        }
    }

    private String host, initialPath, username, password;

    private int port;

    private boolean permitsLockStealing = true;

    private boolean https = false;

    private boolean rememberPassword = false;

    private String owner = null;

    private boolean touched = false;

    public ServerData(boolean https, String host, int port, String initialPath, String username, String password) {
        this(https, host, port, initialPath, username, password, false);
    }

    public ServerData(boolean https, String host, int port, String initialPath, String username, String password, boolean rememberPassword) {
        this(https, host, port, initialPath, username, password, rememberPassword, true);
    }

    public ServerData(boolean https, String host, int port, String initialPath, String username, String password, boolean rememberPassword, boolean autotouch) {
        this.https = https;
        this.host = host;
        this.port = port;
        this.initialPath = correctPath(initialPath);
        this.username = username;
        this.password = password;
        this.rememberPassword = rememberPassword;
        if (autotouch) try {
            touch(this);
        } catch (IOException oyVeh) {
            log.error("Exception", oyVeh);
        }
    }

    public ServerData(String host, int port, String initialPath, String username, String password) {
        this(false, host, port, initialPath, username, password, false);
    }

    public boolean usesSSL() {
        return https;
    }

    public void setUsesSSL(boolean https) {
        this.https = https;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInitialPath() {
        return initialPath;
    }

    public void setInitialPath(String initialPath) {
        this.initialPath = correctPath(initialPath);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * whether or not password information for the server should be
     * saved.
     * @return whether or not to remember the password.
     */
    public boolean getRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        this.rememberPassword = rememberPassword;
    }

    public boolean getPermitsLockStealing() {
        return this.permitsLockStealing;
    }

    public void setPermitsLockStealing(boolean permitsLockStealing) {
        this.permitsLockStealing = permitsLockStealing;
        log.trace("permitsLockStealing set to " + permitsLockStealing + " for {0}", this);
        try {
            saveServers();
        } catch (IOException oyVeh) {
            log.error("Exception", oyVeh);
        }
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String toString() {
        return makeString(https, host, port, initialPath);
    }

    private static String makeString(String host, int port, String initialPath) {
        return new StringBuffer(host).append(':').append(port).append(initialPath).toString();
    }

    private static String makeString(boolean https, String host, int port, String initialPath) {
        return new StringBuffer((https) ? DAVConstants.HTTPS : DAVConstants.HTTP).append("://").append(host).append(':').append(port).append(initialPath).toString();
    }

    private static String correctPath(String path) {
        if (path == null) return DAVConstants.DAV_FILE_SEPARATOR; else if (path.endsWith(DAVConstants.DAV_FILE_SEPARATOR)) return path; else return path + DAVConstants.DAV_FILE_SEPARATOR;
    }

    /**
     * makes this serverData the last selected one.
     */
    protected static void touch(ServerData sd) throws IOException {
        Vector servers = getServers();
        servers.remove(sd);
        addNewServer(sd);
        saveServers();
        sd.touched = true;
    }

    public static Vector getServers() {
        return container.getServerDataVector();
    }

    public static ServerData getLastSelectedServer() {
        Vector servers = getServers();
        int size = servers.size();
        return (size > 0) ? (ServerData) servers.elementAt(size - 1) : null;
    }

    private static void addNewServer(ServerData sd) {
        Vector servers = getServers();
        for (Enumeration eenum = servers.elements(); eenum.hasMoreElements(); ) {
            ServerData next = (ServerData) eenum.nextElement();
            if (next.getHost().equals(sd.getHost()) && next.getPort() == sd.getPort() && next.getInitialPath().equals(sd.getInitialPath())) {
                servers.remove(next);
                break;
            }
        }
        servers.addElement(sd);
    }

    protected static void saveServers() throws IOException {
        Configurator.getConfigurator().getStore().setConfigValue(ServerDataContainer.class, ServerDataContainer.SERVER_DATA_VECTOR_PROPERTY, getServers());
    }

    public static void removeServer(ServerData sd) throws IOException {
        Vector servers = getServers();
        if (servers.contains(sd)) {
            servers.remove(sd);
            saveServers();
        }
    }

    private static void loadServerList() throws IOException, ClassNotFoundException {
        Configurator.getConfigurator().configure(container);
        log.trace("ServerDataContainer loaded; serverdata is {0}", getServers());
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException inconceivable) {
            return null;
        }
    }

    public static ServerData getServer(String serverString) {
        if (!serverString.startsWith(DAVConstants.HTTP) && !serverString.startsWith(DAVConstants.HTTPS)) {
            serverString = new StringBuffer(DAVConstants.HTTP).append("://").append(serverString).toString();
        }
        for (Enumeration eenum = getServers().elements(); eenum.hasMoreElements(); ) {
            Object nextObj = eenum.nextElement();
            if (serverString.equals(nextObj.toString())) return (ServerData) nextObj;
        }
        log.trace("server {0} not found", new Object[] { serverString });
        return null;
    }

    public static ServerData getServer(String host, int port, String initialPath) {
        return getServer(makeString(false, host, port, initialPath));
    }

    public static ServerData getServer(boolean https, String host, int port, String initialPath) {
        return getServer(makeString(https, host, port, initialPath));
    }
}
