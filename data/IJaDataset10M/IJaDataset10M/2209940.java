package irc;

/**
 * Startup configuration.
 */
public class StartupConfiguration {

    private String _nick;

    private String _altNick;

    private String _name;

    private String[] _pass;

    private String[] _host;

    private String _alias;

    private int[] _port;

    private String[] _commands;

    private String[] _plugins;

    private boolean _smileysupport;

    /**
   * Create a new StartupConfiguration.
   * @param nick claimed nickname.
   * @param altNick claimed alternate nickname.
   * @param name full user name.
   * @param pass user server passwords.
   * @param host IRC server hosts.
   * @param port IRC server ports.
   * @param alias IRC server alias.
   * @param commands initial server commands.
   * @param plugins autoloaded plugins.
   * @param smileysupport flag for smileys support.
   */
    public StartupConfiguration(String nick, String altNick, String name, String pass[], String host[], int port[], String alias, String[] commands, String[] plugins, boolean smileysupport) {
        _nick = nick;
        _altNick = altNick;
        _name = name;
        _pass = pass;
        _host = host;
        _port = port;
        _alias = alias;
        _commands = commands;
        _plugins = plugins;
        _smileysupport = smileysupport;
    }

    /**
   * Get the nickname.
   * @return the nickname.
   */
    public String getNick() {
        return _nick;
    }

    /**
   * Get the alternate nickname.
   * @return the alternate nickname.
   */
    public String getAltNick() {
        return _altNick;
    }

    /**
   * Get the full name.
   * @return the full name.
   */
    public String getName() {
        return _name;
    }

    /**
   * Get the server passwords.
   * @return the server passwords.
   */
    public String[] getPass() {
        return _pass;
    }

    /**
   * Get the server host names.
   * @return the server host names.
   */
    public String[] getHost() {
        return _host;
    }

    /**
   * Get the server ports.
   * @return the server ports.
   */
    public int[] getPort() {
        return _port;
    }

    /**
   * Get the server alias.
   * @return the server alias.
   */
    public String getAlias() {
        return _alias;
    }

    /**
   * Get initial server commands.
   * @return initial on-connect server commands.
   */
    public String[] getCommands() {
        return _commands;
    }

    /**
   * Get the autoloaded scripts.
   * @return autoloaded scripts.
   */
    public String[] getPlugins() {
        return _plugins;
    }

    /**
   * Get the boolean value for smileys.
   * @return enabled/disabled.
   */
    public boolean getSmileysSupport() {
        return _smileysupport;
    }
}
