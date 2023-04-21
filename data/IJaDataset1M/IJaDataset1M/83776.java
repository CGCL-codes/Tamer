package net.jawe.scriptbot;

import java.net.InetAddress;
import java.util.Set;

/**
 * Bot configuration.
 * 
 * @author jawe
 */
public interface Config {

    /**
     * Unique name for this config. Recommended form is "network.nick".
     * @return the id
     */
    public String getId();

    /**
     * Unique name for this config. Recommended form is "network.nick".
     * @param id the id.
     */
    public void setId(String id);

    /**
     * Filename of the init script.
     * 
     * @return Returns the initScriptName.
     */
    public String getInitScriptName();

    /**
     * Filename of the init script.
     * 
     * @param initScriptName
     *            The initScriptName to set.
     */
    public void setInitScriptName(String initScriptName);

    /**
     * Data directory for the persistence manager.
     * 
     * @return Returns the dataDir.
     */
    public String getDataDir();

    /**
     * Data directory for the persistence manager.
     * 
     * @param dataDir
     *            The dataDir to set.
     */
    public void setDataDir(String dataDir);

    /**
     * The configured nicks.
     * 
     * @return Returns the nicks.
     */
    public Set<String> getNicks();

    /**
     * The nicks to use as an array. Convenience method for use by scripts.
     * 
     * @return the nicks as array.
     */
    public String[] getNicksArray();

    /**
     * The scripts to be executed after connecting.
     * 
     * @return Returns the perform scripts.
     */
    public Set<String> getPerformScripts();

    /**
     * The scripts to be executed after connecting as an array. Convenience
     * method for use by scripts.
     * 
     * @return the perform scripts as array.
     */
    public String[] getPerformScriptsArray();

    /**
     * The configured modules.
     * 
     * @return Returns the modules.
     */
    public Set<String> getModules();

    /**
     * The configured modules as an array. Convenience method for use by
     * scripts.
     * 
     * @return the modules as array.
     */
    public String[] getModulesArray();

    /**
     * The configured servers.
     * 
     * @return Returns the servers.
     */
    public Set<Server> getServers();

    /**
     * @see org.jibble.pircbot.PircBot#getDccInetAddress()
     */
    public String getDccInetAddress();

    /**
     * @see org.jibble.pircbot.PircBot#setDccInetAddress(InetAddress)
     */
    public void setDccInetAddress(String dccInetAddress);

    /**
     * Set of DCC listening ports to use for DCC chats and filetransfers.
     * @see org.jibble.pircbot.PircBot#getDccPorts() 
     * @see org.jibble.pircbot.PircBot#setDccPorts(int[])
     * @return the dcc ports
     */
    public Set<Integer> getDccPorts();

    /**
     * Convenience method to add a range of ports to use for DCC chats and
     * filetransfers.
     * @param from the lowest port to use.
     * @param to the highest port to use.
     */
    public void addDccPortRange(int from, int to);

    /**
     * Convenience method to remove a range of ports to use for DCC chats and
     * filetransfers.
     * @param from the lowest port not to use anymore.
     * @param to the highest port not to use anymore.
     */
    public void removeDccPortRange(int from, int to);

    /**
     * The maximum number of attempts the bot will try to connect to a server.
     * 
     * @return Returns the maxConnectionAttempts.
     */
    public int getMaxConnectionAttempts();

    /**
     * The maximum number of attempts the bot will try to connect to a server.
     * 
     * @param maxConnectionAttempts
     *            The maxConnectionAttempts to set.
     */
    public void setMaxConnectionAttempts(int maxConnectionAttempts);

    /**
     * The time in milliseconds the bot will wait between connection attempts.
     * 
     * @return Returns the connectionRetryDelay.
     */
    public long getConnectionRetryDelay();

    /**
     * The time in milliseconds the bot will wait between connection attempts.
     * 
     * @param connectionRetryDelay
     *            The connectionRetryDelay to set.
     */
    public void setConnectionRetryDelay(long connectionRetryDelay);

    /**
     * Hide PING and PONG messages in verbose output?
     * 
     * @return Returns the hidePingPong.
     */
    public boolean isHidePingPong();

    /**
     * Hide PING and PONG messages in verbose output?
     * 
     * @param hidePingPong
     *            The hidePingPong to set.
     */
    public void setHidePingPong(boolean hidePingPong);

    /**
     * Add a nick.
     * 
     * @param nick
     * @return true if the nick was not already configured.
     */
    public boolean addNick(String nick);

    /**
     * Add a server.
     * 
     * @param hostname
     *            the server's hostname or IP.
     * @return the newly created server object.
     */
    public Server addServer(String hostname);

    /**
     * Add a server.
     * 
     * @param hostname
     *            the server's hostname or IP.
     * @param port
     *            the server port.
     * @return the newly created server object.
     */
    public Server addServer(String hostname, int port);

    /**
     * Add a server.
     * 
     * @param hostname
     *            the server's hostname or IP.
     * @param port
     *            the server port.
     * @param password
     *            the server password.
     * @return the newly created server object.
     */
    public Server addServer(String hostname, int port, String password);

    /**
     * Add a module.
     * 
     * @param name
     *            the module name.
     * @return true if the module was not already added.
     */
    public boolean addModule(String name);

    /**
     * Add a script to be executed after connecting.
     * 
     * @param filename
     *            the script filename.
     * @return true if the script was not already added.
     */
    public boolean addPerformScript(String filename);

    /**
     * The bot's login (eg. username or ident).
     * 
     * @return Returns the login.
     */
    public String getLogin();

    /**
     * The bot's login (eg. username or ident).
     * 
     * @param login
     *            The login to set.
     */
    public void setLogin(String login);

    /**
     * Append random numbers to the nick if the nick is already in use? If this
     * is set to <tt>true</tt> only the first configured nick will be used and
     * modified, all other nicks will be ignored.
     * 
     * @return Returns the autoNickChange.
     */
    public boolean isAutoNickChange();

    /**
     * Append random numbers to the nick if the nick is already in use? If this
     * is set to <tt>true</tt> only the first configured nick will be used and
     * modified, all other nicks will be ignored.
     * 
     * @param autoNickChange
     *            The autoNickChange to set.
     */
    public void setAutoNickChange(boolean autoNickChange);

    /**
     * The CTCP finger reply.
     * 
     * @return Returns the finger.
     */
    public String getFinger();

    /**
     * The CTCP finger reply.
     * 
     * @param finger
     *            The finger to set.
     */
    public void setFinger(String finger);

    /**
     * Start an ident server on startup?
     * 
     * @return Returns the startIdentServer.
     */
    public boolean isStartIdentServer();

    /**
     * Start an ident server on startup?
     * 
     * @param startIdentServer
     *            The startIdentServer to set.
     */
    public void setStartIdentServer(boolean startIdentServer);

    /**
     * The encoding to use when communicating with the IRC server.
     * 
     * @return the encoding;
     */
    public String getEncoding();

    /**
     * The encoding to use when communicating with the IRC server.
     * 
     * @param encoding
     *            the encoding to set.
     */
    public void setEncoding(String encoding);

    /**
     * Prefix for all commands issued in a channel.
     * 
     * @return Returns the channelCommandPrefix.
     */
    public String getChannelCommandPrefix();

    /**
     * Prefix for all commands issued in a channel.
     * 
     * @param channelCommandPrefix
     *            The channelCommandPrefix to set.
     */
    public void setChannelCommandPrefix(String channelCommandPrefix);

    /**
     * Prefix for all commands issued as action.
     * 
     * @return Returns the actionCommandPrefix.
     */
    public String getActionCommandPrefix();

    /**
     * Prefix for all commands issued as action.
     * 
     * @param actionCommandPrefix
     *            The actionCommandPrefix to set.
     */
    public void setActionCommandPrefix(String actionCommandPrefix);

    /**
     * Prefix for all commands issued in a DCC chat.
     * 
     * @return Returns the dccCommandPrefix.
     */
    public String getDccCommandPrefix();

    /**
     * Prefix for all commands issued in a DCC chat.
     * 
     * @param dccCommandPrefix
     *            The dccCommandPrefix to set.
     */
    public void setDccCommandPrefix(String dccCommandPrefix);

    /**
     * Prefix for all commands issued in a notice.
     * 
     * @return Returns the _noticeCommandPrefix.
     */
    public String getNoticeCommandPrefix();

    /**
     * Prefix for all commands issued in a notice.
     * 
     * @param noticeCommandPrefix
     *            The noticeCommandPrefix to set.
     */
    public void setNoticeCommandPrefix(String noticeCommandPrefix);

    /**
     * Prefix for all commands issued in a private message (eg. msg or query).
     * 
     * @return Returns the privateCommandPrefix.
     */
    public String getPrivateCommandPrefix();

    /**
     * Prefix for all commands issued in a private message (eg. msg or query).
     * 
     * @param privateCommandPrefix
     *            The privateCommandPrefix to set.
     */
    public void setPrivateCommandPrefix(String privateCommandPrefix);

    /**
     * Get the configured prefix for the input type.
     * 
     * @param inputType
     *            the input type.
     * @return the configured prefix.
     */
    public String getPrefix(MessageType inputType);

    /**
     * Should hostmasks be automatically added on auth?
     * @return The configured auto adding of hostmasks setting
     */
    public boolean isAutoAddHostmasks();

    /**
     * Set wether hostmasks should be automatically added on auth.
     * @param autoAddHostmasks Enable or disable auto adding of hostmasks
     */
    public void setAutoAddHostmasks(boolean autoAddHostmasks);

    /**
     * The hilite color, a constant from {@link org.jibble.pircbot.Colors}
     * @return the hilite color
     */
    public String getHiliteColor();

    /**
     * The hilite color, a constant from {@link org.jibble.pircbot.Colors}
     * @param hiliteColor the hilite color
     */
    public void setHiliteColor(String hiliteColor);

    /**
     * Issue a log message whenever a user executes a command.
     * Commands invocations will be logged at the INFO level and will include
     * the user's nick, login and hostname as well as the command and all parameters.
     * Be careful as this could include sensitive information such as bot user passwords. 
     * @return audit commands.
     */
    public boolean isAuditCommands();

    /**
     * Issue a log message whenever a user executes a command.
     * Commands invocations will be logged at the INFO level and will include
     * the user's nick, login and hostname as well as the command and all parameters.
     * Be careful as this could include sensitive information such as bot user passwords. 
     * @param auditCommands audit commands
     */
    public void setAuditCommands(boolean auditCommands);
}
