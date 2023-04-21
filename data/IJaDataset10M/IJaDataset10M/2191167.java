package sc.fgrid.gui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import sc.fgrid.common.FGException;

/**
 * contains the configuration of a client. Configuration is maintained by
 * java.util.prefs.Preferences. Could be anywhere $HOME/.java or anything else.
 */
public class Config {

    private final String config_version = "0.40001";

    private Map<String, ServerConnection> connections = null;

    private boolean stacktrace = true;

    private int look = 1;

    /**
     * @return the stacktrace
     */
    public boolean isStacktrace() {
        return stacktrace;
    }

    /**
     * @param stacktrace the stacktrace to set
     */
    public void setStacktrace(boolean stacktrace) throws FGException {
        this.stacktrace = stacktrace;
        store();
    }

    /**
     * @return the look
     */
    public int getLook() {
        return look;
    }

    /**
     * @param look the look to set
     */
    public void setLook(int look) throws FGException {
        this.look = look;
        store();
    }

    public Config() throws FGException {
        connections = (Map<String, ServerConnection>) new HashMap<String, ServerConnection>();
        load();
    }

    /**
     * Set a a new connection or overwrite an existing one. nickname is the key
     * of the map, so unique.
     *
     * @param user
     *            can be null
     * @param sessionkey
     *            can be null
     */
    public void setRemoteConnection(String nickname, URL url, String user, String sessionkey, boolean autoconnect) throws FGException {
        ServerConnection sc = new ServerConnection();
        sc.internal = false;
        sc.url = url;
        sc.fgRoot = null;
        sc.user = user;
        sc.sessionkey = sessionkey;
        sc.autoconnect = autoconnect;
        connections.put(nickname, sc);
        store();
    }

    /**
     * Set a a new connection or overwrite an existing one. nickname is the key
     * of the map, so unique.
     *
     * @param user
     *            can be null
     */
    public void setLocalConnection(String nickname, String fgRoot, String user, boolean autoconnect) throws FGException {
        ServerConnection sc = new ServerConnection();
        sc.internal = true;
        sc.url = null;
        sc.fgRoot = fgRoot;
        sc.user = user;
        sc.sessionkey = null;
        sc.autoconnect = autoconnect;
        connections.put(nickname, sc);
        store();
    }

    /** On the long run not needed. */
    public String getAnyNickname() throws FGException {
        if (connections.isEmpty()) {
            throw new FGException("config is empty");
        }
        String[] sc = (String[]) connections.keySet().toArray(new String[0]);
        return sc[0];
    }

    public void deleteConnection(String nickname) throws FGException {
        connections.remove(nickname);
        store();
    }

    /** Get all connection nicknames. */
    public Set<String> getConnections() {
        return connections.keySet();
    }

    /** Whether such a key exists in the connections map */
    public boolean containsConnection(String nickname) {
        return connections.containsKey(nickname);
    }

    public boolean isInternal(String nickname) {
        return connections.get(nickname).internal;
    }

    /** Get url from connection */
    public URL getURL(String nickname) {
        return connections.get(nickname).url;
    }

    /** Get fgRoot from connection */
    public String getFgRoot(String nickname) {
        return connections.get(nickname).fgRoot;
    }

    /** get user from connection */
    public String getUser(String nickname) {
        return connections.get(nickname).user;
    }

    public boolean isAutoconnect(String nickname) {
        return connections.get(nickname).autoconnect;
    }

    public void setAutoconnect(String nickname, boolean autoconnect) throws FGException {
        connections.get(nickname).autoconnect = autoconnect;
        store();
    }

    /** get sessionkey from connection */
    public String getSessionkey(String nickname) {
        return connections.get(nickname).sessionkey;
    }

    public void setSessionkey(String nickname, String sessionkey) throws FGException {
        connections.get(nickname).sessionkey = sessionkey;
        store();
    }

    public void load() throws FGException {
        synchronized (connections) {
            try {
                Preferences root_pref = Preferences.userNodeForPackage(getClass());
                Preferences prefs = root_pref.node(config_version);
                connections.clear();
                int nServer = prefs.getInt("server.n", 0);
                for (int i = 0; i < nServer; i++) {
                    ServerConnection si = new ServerConnection();
                    String nickname = prefs.get(new String("server." + i + ".nickname"), "");
                    if (nickname.equals("")) {
                        throw new FGException("no nickname");
                    }
                    String internal_string = new String("server." + i + ".internal");
                    si.internal = Boolean.parseBoolean(prefs.get(internal_string, "false"));
                    if (si.internal) {
                        si.fgRoot = prefs.get(new String("server." + i + ".fgRoot"), null);
                        if (si.fgRoot == null) {
                            throw new FGException("Missing server." + i + ".fgRoot");
                        }
                        si.url = null;
                        si.sessionkey = null;
                    } else {
                        String url_string = prefs.get(new String("server." + i + ".URL"), null);
                        if (url_string == null) {
                            throw new FGException("Missing server." + i + ".URL");
                        }
                        si.url = new URL(url_string);
                        si.sessionkey = prefs.get(new String("server." + i + ".sessionkey"), "");
                        si.fgRoot = null;
                    }
                    si.user = prefs.get(new String("server." + i + ".user"), "");
                    String ac_string = new String("server." + i + ".autoconnect");
                    si.autoconnect = Boolean.parseBoolean(prefs.get(ac_string, "false"));
                    connections.put(nickname, si);
                }
                stacktrace = Boolean.parseBoolean(prefs.get("stacktrace", "true"));
                look = Integer.parseInt(prefs.get("look", "1"));
            } catch (IOException e2) {
                throw new FGException("while reading preferences ", e2);
            }
        }
    }

    /** Write to file */
    public void store() throws FGException {
        synchronized (connections) {
            try {
                Preferences root_pref = Preferences.userNodeForPackage(getClass());
                Preferences prefs = root_pref.node(config_version);
                prefs.putInt("server.n", connections.size());
                int i = 0;
                for (String nickname : connections.keySet()) {
                    ServerConnection si = connections.get(nickname);
                    prefs.put(new String("server." + i + ".nickname"), nickname);
                    String internal_string = new String("server." + i + ".internal");
                    prefs.put(internal_string, Boolean.toString(si.internal));
                    if (si.internal) {
                        prefs.put(new String("server." + i + ".fgRoot"), si.fgRoot);
                    } else {
                        prefs.put(new String("server." + i + ".URL"), si.url.toString());
                        String keystring = si.sessionkey;
                        prefs.put(new String("server." + i + ".sessionkey"), keystring);
                    }
                    String user = si.user;
                    prefs.put(new String("server." + i + ".user"), user);
                    String ac_string = new String("server." + i + ".autoconnect");
                    prefs.put(ac_string, Boolean.toString(si.autoconnect));
                    i++;
                }
                prefs.put("stacktrace", Boolean.toString(stacktrace));
                prefs.put("look", Integer.toString(look));
                prefs.flush();
            } catch (Exception e) {
                throw new FGException("while writing preferences", e);
            }
        }
    }

    /**
     * The information of the Server objects, which is stored in the config. The
     * goal is to have as much as possible on the Server itself, so only
     * connection information needs to be stored.
     */
    private class ServerConnection {

        /** Is it an internal server or a remote server? */
        boolean internal = false;

        /** The endpoint, null in case internal==true */
        URL url = null;

        /** The home directory of the server, null in case internal==false */
        String fgRoot = null;

        String user = null;

        /**
         * This can be empty if there is no session. For more then one session,
         * use several objects of this class.
         */
        String sessionkey = null;

        /**
         * Autoconnect indicates the users wishes to automatically connect
         * to the server, starting first time at login (which might cause
         * undesired error messages at login) end eventually later.
         */
        boolean autoconnect = false;
    }
}
