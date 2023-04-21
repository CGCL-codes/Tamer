package quietcoffee.ssh;

import java.io.*;
import java.util.StringTokenizer;

/**
 *  Configuration options for SSH connections.
 *
 * @author  Brett Porter
 * @version $Header: /cvsroot/quietcoffee/quietcoffee/src/quietcoffee/ssh/Options.java,v 1.1.1.1 2002/05/13 03:49:13 brettporter Exp $
 */
public final class Options {

    /** The system /etc directory. For most, /etc is ok. For Cygwin, it'll need to be
     * c:\cygwin\etc. For other non-Unices, it can be wherever you like.
     * TODO: way to configure through system property. */
    private static final String ETCDIR = "c:\\cygwin\\etc";

    /** The SSH configuration directory. */
    private static final String SSHDIR = ETCDIR + "/ssh";

    /** The host configuration file path. */
    public static final String PATH_HOST_CONFIG_FILE = SSHDIR + "/ssh_config";

    /** The path used under the user's home directory for configuration files. */
    public static final String PATH_SSH_USER_CONFFILE = ".ssh/config";

    /** Cipher to use. */
    private int cipher = CipherDetails.SSH_CIPHER_NOT_SET;

    /** SSH2 ciphers in order of preference. */
    private String ciphers = null;

    /** Max attempts (seconds) before giving up */
    private int connectionAttempts = -1;

    /** Protocol in order of preference. */
    private int protocol = -1;

    /** Set SO_KEEPALIVE. */
    private boolean keepalives = true;

    /** Level for logging. */
    private int logLevel = Log.SYSLOG_LEVEL_NOT_SET;

    /** Port to connect. */
    private int port = -1;

    /** Compress packets in both directions. */
    private boolean compression = false;

    /** Compression level 1 (fast) to 9 (best). */
    private int compressionLevel = -1;

    /** Proxy command for connecting the host. */
    private String proxyCommand = null;

    /** Forward authentication agent. */
    private boolean forwardAgent = false;

    /** Whether to regurgitate packet debug information. */
    private boolean packetDebug = false;

    /** The username to connect as. */
    private String user = null;

    /** The hostname to connect to. */
    private String hostname = null;

    /** The preset keyword list for options. */
    private static java.util.Map keywords;

    /** Keyword tokens. */
    private static final int oBadOption = 0;

    private static final int oForwardAgent = 1;

    private static final int oForwardX11 = 2;

    private static final int oGatewayPorts = 3;

    private static final int oRhostsAuthentication = 4;

    private static final int oPasswordAuthentication = 5;

    private static final int oRSAAuthentication = 6;

    private static final int oFallBackToRsh = 7;

    private static final int oUseRsh = 8;

    private static final int oChallengeResponseAuthentication = 9;

    private static final int oXAuthLocation = 10;

    private static final int oIdentityFile = 14;

    private static final int oHostName = 15;

    private static final int oPort = 16;

    private static final int oCipher = 17;

    private static final int oRemoteForward = 18;

    private static final int oLocalForward = 19;

    private static final int oUser = 20;

    private static final int oHost = 21;

    private static final int oEscapeChar = 22;

    private static final int oRhostsRSAAuthentication = 23;

    private static final int oProxyCommand = 24;

    private static final int oGlobalKnownHostsFile = 25;

    private static final int oUserKnownHostsFile = 26;

    private static final int oConnectionAttempts = 27;

    private static final int oBatchMode = 28;

    private static final int oCheckHostIP = 29;

    private static final int oStrictHostKeyChecking = 30;

    private static final int oCompression = 31;

    private static final int oCompressionLevel = 32;

    private static final int oKeepAlives = 33;

    private static final int oNumberOfPasswordPrompts = 34;

    private static final int oUsePrivilegedPort = 35;

    private static final int oLogLevel = 36;

    private static final int oCiphers = 37;

    private static final int oProtocol = 38;

    private static final int oMacs = 39;

    private static final int oGlobalKnownHostsFile2 = 40;

    private static final int oUserKnownHostsFile2 = 41;

    private static final int oPubkeyAuthentication = 42;

    private static final int oKbdInteractiveAuthentication = 43;

    private static final int oKbdInteractiveDevices = 44;

    private static final int oHostKeyAlias = 45;

    private static final int oDynamicForward = 46;

    private static final int oPreferredAuthentications = 47;

    private static final int oHostbasedAuthentication = 48;

    private static final int oHostKeyAlgorithms = 49;

    private static final int oBindAddress = 50;

    private static final int oSmartcardDevice = 51;

    private static final int oClearAllForwardings = 52;

    private static final int oNoHostAuthenticationForLocalhost = 53;

    private static final int oPacketDebug = 54;

    static {
        keywords = new java.util.HashMap();
        keywords.put("forwardagent", new Integer(oForwardAgent));
        keywords.put("forwardx11", new Integer(oForwardX11));
        keywords.put("xauthlocation", new Integer(oXAuthLocation));
        keywords.put("gatewayports", new Integer(oGatewayPorts));
        keywords.put("useprivilegedport", new Integer(oUsePrivilegedPort));
        keywords.put("rhostsauthentication", new Integer(oRhostsAuthentication));
        keywords.put("passwordauthentication", new Integer(oPasswordAuthentication));
        keywords.put("kbdinteractiveauthentication", new Integer(oKbdInteractiveAuthentication));
        keywords.put("kbdinteractivedevices", new Integer(oKbdInteractiveDevices));
        keywords.put("rsaauthentication", new Integer(oRSAAuthentication));
        keywords.put("pubkeyauthentication", new Integer(oPubkeyAuthentication));
        keywords.put("dsaauthentication", new Integer(oPubkeyAuthentication));
        keywords.put("rhostsrsaauthentication", new Integer(oRhostsRSAAuthentication));
        keywords.put("hostbasedauthentication", new Integer(oHostbasedAuthentication));
        keywords.put("challengeresponseauthentication", new Integer(oChallengeResponseAuthentication));
        keywords.put("skeyauthentication", new Integer(oChallengeResponseAuthentication));
        keywords.put("tisauthentication", new Integer(oChallengeResponseAuthentication));
        keywords.put("fallbacktorsh", new Integer(oFallBackToRsh));
        keywords.put("usersh", new Integer(oUseRsh));
        keywords.put("identityfile", new Integer(oIdentityFile));
        keywords.put("identityfile2", new Integer(oIdentityFile));
        keywords.put("hostname", new Integer(oHostName));
        keywords.put("hostkeyalias", new Integer(oHostKeyAlias));
        keywords.put("proxycommand", new Integer(oProxyCommand));
        keywords.put("port", new Integer(oPort));
        keywords.put("cipher", new Integer(oCipher));
        keywords.put("ciphers", new Integer(oCiphers));
        keywords.put("macs", new Integer(oMacs));
        keywords.put("protocol", new Integer(oProtocol));
        keywords.put("remoteforward", new Integer(oRemoteForward));
        keywords.put("localforward", new Integer(oLocalForward));
        keywords.put("user", new Integer(oUser));
        keywords.put("host", new Integer(oHost));
        keywords.put("escapechar", new Integer(oEscapeChar));
        keywords.put("globalknownhostsfile", new Integer(oGlobalKnownHostsFile));
        keywords.put("userknownhostsfile", new Integer(oUserKnownHostsFile));
        keywords.put("globalknownhostsfile2", new Integer(oGlobalKnownHostsFile2));
        keywords.put("userknownhostsfile2", new Integer(oUserKnownHostsFile2));
        keywords.put("connectionattempts", new Integer(oConnectionAttempts));
        keywords.put("batchmode", new Integer(oBatchMode));
        keywords.put("checkhostip", new Integer(oCheckHostIP));
        keywords.put("stricthostkeychecking", new Integer(oStrictHostKeyChecking));
        keywords.put("compression", new Integer(oCompression));
        keywords.put("compressionlevel", new Integer(oCompressionLevel));
        keywords.put("keepalive", new Integer(oKeepAlives));
        keywords.put("numberofpasswordprompts", new Integer(oNumberOfPasswordPrompts));
        keywords.put("loglevel", new Integer(oLogLevel));
        keywords.put("dynamicforward", new Integer(oDynamicForward));
        keywords.put("preferredauthentications", new Integer(oPreferredAuthentications));
        keywords.put("hostkeyalgorithms", new Integer(oHostKeyAlgorithms));
        keywords.put("bindaddress", new Integer(oBindAddress));
        keywords.put("smartcarddevice", new Integer(oSmartcardDevice));
        keywords.put("clearallforwardings", new Integer(oClearAllForwardings));
        keywords.put("nohostauthenticationforlocalhost", new Integer(oNoHostAuthenticationForLocalhost));
        keywords.put("packetdebug", new Integer(oPacketDebug));
    }

    /**
     *  Set the port from a string.
     *      @param port this port
     */
    public void setPort(String port) {
        this.port = Integer.valueOf(port).intValue();
    }

    /**
     *  Get the port in options.
     *      @returns the port
     */
    public int getPort() {
        return port;
    }

    /**
     *  Set the compression status.
     *      @param compression the compression status
     */
    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    /**
     *  Set the protocol.
     *      @param protocol the protocol
     */
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    /**
     *  Set the level of logging.
     *      @param logLevel the level of logging
     */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /**
     *  Get the level of logging.
     *      @returns the logging level
     */
    public int getLogLevel() {
        return logLevel;
    }

    /**
     *  Check whether keepalives are enabled.
     *      @returns keepalives
     */
    public boolean isKeepalives() {
        return keepalives;
    }

    /**
     *  Check whether compression is enabled.
     *      @returns compression flag
     */
    public boolean isCompression() {
        return compression;
    }

    /**
     *  Get the number of connection attempts.
     *      @returns the number of connection attempts
     */
    public int getConnectionAttempts() {
        return connectionAttempts;
    }

    /**
     *  Get the cipher.
     *      @returns the cipher.
     */
    public int getCipher() {
        return cipher;
    }

    /**
     *  Get the compression level
     *      @returns the compression level
     */
    public int getCompressionLevel() {
        return compressionLevel;
    }

    /**
     *  Get the proxy command.
     *      @returns the proxy command
     */
    public String getProxyCommand() {
        return proxyCommand;
    }

    /** Setter for property cipher.
     * @param cipher New value of property cipher.
     */
    public void setCipher(int cipher) {
        this.cipher = cipher;
    }

    /** Getter for property protocol.
     * @return Value of property protocol.
     */
    public int getProtocol() {
        return protocol;
    }

    /** Getter for property forwardAgent.
     * @return Value of property forwardAgent.
     */
    public boolean isForwardAgent() {
        return forwardAgent;
    }

    /** Setter for property forwardAgent.
     * @param forwardAgent New value of property forwardAgent.
     */
    public void setForwardAgent(boolean forwardAgent) {
        this.forwardAgent = forwardAgent;
    }

    /** Getter for property ciphers.
     * @return Value of property ciphers.
     */
    public java.lang.String getCiphers() {
        return ciphers;
    }

    /** Setter for property ciphers.
     * @param ciphers New value of property ciphers.
     */
    public void setCiphers(java.lang.String ciphers) {
        this.ciphers = ciphers;
    }

    /**
     *  Read the configuration file.
     *      @param configFile the configuration file
     *      @param host the host to connect to
     *      @throws IOException TODO
     */
    public void readConfigFile(String filename, String host) throws java.io.IOException {
        int badOptions = 0;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            Log.getLogInstance().debug("Reading configuration data " + filename);
            boolean active = true;
            int lineNum = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                lineNum++;
                if (processConfigLine(host, line, filename, lineNum, active) == false) {
                    badOptions++;
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (in != null) {
                in.close();
            }
        }
        if (badOptions > 0) {
            Log.getLogInstance().fatal(filename + ": termination, " + badOptions + " bad configuration options");
        }
    }

    /**
     *  Process a single configuration option.
     *      @param host the host to connect to
     *      @param line the configuration line
     *      @param filename the filename read from
     *      @param lineNum the filename line number
     *      @returns whether the line is valid or not   // TODO: return active
     */
    public boolean processConfigLine(String host, String line, String filename, int lineNum, boolean active) {
        java.util.StringTokenizer tok = new java.util.StringTokenizer(line, " \t\n\r");
        String keyword = tok.nextToken("= \n\t\r");
        if (keyword != null) {
            keyword = keyword.trim();
        }
        if (keyword == null || keyword.length() == 0 || keyword.charAt(0) == '#') {
            return true;
        }
        int opcode = parseToken(keyword, filename, lineNum);
        try {
            switch(opcode) {
                case oBadOption:
                    return false;
                case oCompression:
                    compression = getBooleanTokenValue(tok.nextToken());
                    break;
                case oCompressionLevel:
                    if (compressionLevel < 0) {
                        compressionLevel = getIntTokenValue(tok.nextToken());
                    }
                    break;
                case oKeepAlives:
                    keepalives = getBooleanTokenValue(tok.nextToken());
                    break;
                case oUser:
                    if (user == null) {
                        user = getStringTokenValue(tok.nextToken());
                    }
                    break;
                case oHostName:
                    if (hostname == null) {
                        hostname = getStringTokenValue(tok.nextToken());
                    }
                    break;
                case oPort:
                    if (port < 0) {
                        port = getIntTokenValue(tok.nextToken());
                    }
                    break;
                case oConnectionAttempts:
                    if (connectionAttempts < 0) {
                        connectionAttempts = getIntTokenValue(tok.nextToken());
                    }
                    break;
                case oProxyCommand:
                    if (proxyCommand == null) {
                        StringBuffer string = new StringBuffer();
                        while (tok.hasMoreTokens()) {
                            string.append(getStringTokenValue(tok.nextToken()));
                            string.append(" ");
                        }
                        proxyCommand = string.toString();
                    }
                    return true;
                case oCipher:
                    if (cipher < 0) {
                        String arg = getStringTokenValue(tok.nextToken());
                        int value = CipherFactory.getCipherNumber(arg);
                        if (value == CipherDetails.SSH_CIPHER_NOT_SET) {
                            Log.getLogInstance().fatal(filename + " line " + lineNum + ": Bad cipher '" + arg + "'.");
                        }
                        cipher = value;
                    }
                    break;
                case oCiphers:
                    if (ciphers == null) {
                        String arg = getStringTokenValue(tok.nextToken());
                        if (CipherFactory.valid(arg) == false) {
                            Log.getLogInstance().fatal(filename + " line " + lineNum + ": Bad SSH2 cipher spec '" + arg + "'.");
                        }
                        ciphers = arg;
                    }
                    break;
                case oLogLevel:
                    if (logLevel == Log.SYSLOG_LEVEL_NOT_SET) {
                        String arg = getStringTokenValue(tok.nextToken());
                        int value = Log.getLevelNumber(arg);
                        if (value == Log.SYSLOG_LEVEL_NOT_SET) {
                            Log.getLogInstance().fatal(filename + " line " + lineNum + ": unsupported log level '" + arg + "'.");
                        }
                        logLevel = value;
                    }
                    break;
                case oForwardAgent:
                    forwardAgent = getBooleanTokenValue(tok.nextToken());
                    break;
                case oPacketDebug:
                    packetDebug = getBooleanTokenValue(tok.nextToken());
                    break;
                default:
                    Log.getLogInstance().fatal("Options.parseConfigLine: Unimplemented opcode " + opcode);
            }
        } catch (ConfigException e) {
            Log.getLogInstance().fatal(filename + " line " + lineNum + ": " + e.getMessage());
        }
        while (tok.hasMoreTokens()) {
            String arg = tok.nextToken().trim();
            if (arg.length() > 0) {
                Log.getLogInstance().fatal(filename + " line " + lineNum + ": garbage at end of line; \"" + arg + "\".");
            }
        }
        return true;
    }

    /**
     *  Fill any unset options with defaults.
     */
    public void fillDefaultOptions() {
        if (compressionLevel == -1) {
            compressionLevel = 6;
        }
        if (port == -1) {
            port = 0;
        }
        if (connectionAttempts == -1) {
            connectionAttempts = 1;
        }
        if (cipher == -1) {
            cipher = CipherDetails.SSH_CIPHER_NOT_SET;
        }
        if (protocol == Compatibility.SSH_PROTO_UNKNOWN) {
            protocol = Compatibility.SSH_PROTO_1 | Compatibility.SSH_PROTO_2;
        }
        if (logLevel == Log.SYSLOG_LEVEL_NOT_SET) {
            logLevel = Log.SYSLOG_LEVEL_INFO;
        }
    }

    /** Getter for property user.
     * @return Value of property user.
     */
    public java.lang.String getUser() {
        return user;
    }

    /** Setter for property user.
     * @param user New value of property user.
     */
    public void setUser(java.lang.String user) {
        this.user = user;
    }

    /** Getter for property hostname.
     * @return Value of property hostname.
     */
    public java.lang.String getHostname() {
        return hostname;
    }

    /** Setter for property hostname.
     * @param hostname New value of property hostname.
     */
    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    /**
     * Returns the number of the token pointed to by cp or oBadOption.
     *  @returns the opcode
     */
    private int parseToken(String cp, String filename, int lineNum) {
        String lcp = cp.toLowerCase();
        if (keywords.containsKey(lcp)) {
            Integer value = (Integer) keywords.get(lcp);
            return value.intValue();
        } else {
            Log.getLogInstance().error(filename + ": line " + lineNum + ": Bad configuration option: " + cp);
            return oBadOption;
        }
    }

    /**
     *  Get the token value (yes/no or true/false)
     *      @param arg the argument
     *      @returns whether yes/no or true/false was found
     *      @throws ConfigException if it is not a valid argument
     */
    private boolean getBooleanTokenValue(String arg) throws ConfigException {
        arg = getArgument(arg);
        if (arg == null || arg.length() == 0) {
            throw new ConfigException("Missing yes/no argument.");
        }
        if (arg.equals("yes") || arg.equals("true")) {
            return true;
        } else if (arg.equals("no") || arg.equals("false")) {
            return false;
        } else {
            throw new ConfigException("Bad yes/no argument: '" + arg + "'.");
        }
    }

    /**
     *  Get the token value
     *      @param arg the argument
     *      @returns the value
     *      @throws ConfigException if it is not a valid argument
     */
    private int getIntTokenValue(String arg) throws ConfigException {
        arg = getArgument(arg);
        if (arg == null || arg.length() == 0) {
            throw new ConfigException("Missing argument.");
        }
        try {
            return Integer.valueOf(arg).intValue();
        } catch (NumberFormatException e) {
            throw new ConfigException("Bad number.");
        }
    }

    /**
     *  Get the token value
     *      @param arg the argument
     *      @returns the value
     *      @throws ConfigException if it is not a valid argument
     */
    private String getStringTokenValue(String arg) throws ConfigException {
        arg = getArgument(arg);
        if (arg == null || arg.length() == 0) {
            throw new ConfigException("Missing argument.");
        }
        return arg.trim();
    }

    /** Getter for property packetDebug.
     * @return Value of property packetDebug.
     */
    public boolean isPacketDebug() {
        return packetDebug;
    }

    /** Setter for property packetDebug.
     * @param packetDebug New value of property packetDebug.
     */
    public void setPacketDebug(boolean packetDebug) {
        this.packetDebug = packetDebug;
    }

    /**
     *  Get the argument.
     *      @param the argument to clean
     *      @returns the cleaned argument
     */
    private String getArgument(String arg) {
        if (arg != null) {
            arg = arg.trim();
            if (arg.startsWith("=")) {
                arg = arg.substring(1).trim();
            }
        }
        return arg;
    }
}
