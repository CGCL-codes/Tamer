package visitpc.destclient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import visitpc.UserOutput;
import visitpc.VisitPCException;
import visitpc.lib.gui.MessageFrame;
import visitpc.messages.ConnectFromDestClient;
import visitpc.messages.ConnectedFromDestClient;
import visitpc.messages.DataMessage;
import visitpc.messages.DisconnectFromDestClient;
import visitpc.messages.ErrorMessage;
import visitpc.messages.KeepAliveMessage;
import visitpc.server.Server;
import java.io.*;
import visitpc.lib.io.FileIO;
import visitpc.lib.io.ShellCmd;
import visitpc.lib.io.SimpleConfig;
import visitpc.lib.io.SimpleConfigHelper;
import visitpc.lib.gui.Dialogs;
import visitpc.destclient.gui.VNCDestConfigDialog;
import visitpc.destclient.vncserverdetector.*;
import java.util.*;
import visitpc.lib.io.*;
import visitpc.VisitPCConstants;

public class VNCDestClient extends DestClient {

    public static final String WIN_VNC_SERVER_EXE_LOCATION = "/visitpc/destclient/vncserver/win/ultra_winvnc.exe";

    public static final String WIN_VNC_SERVER_CFG_LOCATION = "/visitpc/destclient/vncserver/win/ultravnc.ini";

    public static File WINDOWS_VNC_SERVER_EXE = new File("ultravnc", "ultra_winvnc.exe");

    public static File WINDOWS_VNC_SERVER_CFG = new File("ultravnc", "ultravnc.ini");

    public static final String SERVER_NAME_ARG = "--server_name";

    public static final String SERVER_PORT_ARG = "--server_port";

    public static final String USERNAME_ARG = "--username";

    public static final String PASSWORD_ARG = "--password";

    public static final String PC_NAME_ARG = "--pc_name";

    public static final String EXPERT_MODE_ARG = "--expert_mode";

    public static final String GUI_ARG = "--gui";

    public static final String DISABLE_ANONYMOUS_SSL_ARG = "--disable_anonymous_ssl";

    public static final String VNC_SERVER_PORT_ARG = "--vnc_server_port";

    public static final String START_VNC_SERVER_ARG = "--start_vnc_server";

    public static final String TRUSTSTORE_ARG = "--truststore_file";

    public static final String CONFIG_PASSWORD_ARG = "--config_password";

    public static final String RECONNECT_DELAY_SECONDS_ARG = "--reconnect_delay";

    public VNCDestClientConfig vncDestClientConfig;

    private VNCDestConfigDialog vncDestConfigDialog;

    private MessageFrame messageFrame;

    private String osName = System.getProperty("os.name");

    private static File VncServerDir;

    private boolean useJavaHomePath = true;

    private ShellCmd shellCmd = new ShellCmd(false);

    private boolean loadedConfigFromJar = false;

    private SimpleConfigHelper simpleConfigHelper;

    public static void main(String args[]) {
        boolean readNextArg = false;
        for (String arg : args) {
            if (readNextArg) {
                readNextArg = false;
            }
            if (arg.toLowerCase().equals("--java_properties_file")) {
                readNextArg = true;
            }
        }
        Server.CheckJava6();
        try {
            new VNCDestClient(args);
        } catch (Exception e) {
            UserOutput uo = new UserOutput();
            uo.error(e);
        }
        System.exit(10);
    }

    public VNCDestClient(String args[]) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        addArgument(VNCDestClient.SERVER_NAME_ARG, "Followed by the DNS name or IP address of the visitpc server.");
        addArgument(VNCDestClient.SERVER_PORT_ARG, "Followed by the TCP port of the visitpc server (default=" + Server.DEFAULT_SERVER_PORT + ")");
        addArgument(VNCDestClient.USERNAME_ARG, "Followed by your group access username.");
        addArgument(VNCDestClient.PASSWORD_ARG, "Followed by your group access password.");
        addArgument(VNCDestClient.PC_NAME_ARG, "Followed by the name of your PC.");
        addArgument(VNCDestClient.VNC_SERVER_PORT_ARG, "Followed by the local vnc server port to use (search for vnc server if not provided)");
        addArgument(VNCDestClient.EXPERT_MODE_ARG, "Turn on expert mode.");
        addArgument(VNCDestClient.GUI_ARG, "Run in GUI mode.");
        addArgument(VNCDestClient.DISABLE_ANONYMOUS_SSL_ARG, "Disable anonymous SSL server connections.");
        addArgument(VNCDestClient.START_VNC_SERVER_ARG, "Attempt to start VNC server (Windows platforms only).");
        addArgument(VNCDestClient.TRUSTSTORE_ARG, "Followed by the truststore file.");
        addArgument(VNCDestClient.CONFIG_PASSWORD_ARG, "Followed by the password for reading/decrypting config files.");
        addArgument(VNCDestClient.RECONNECT_DELAY_SECONDS_ARG, "Followed by server reconnect delay in seconds).");
        simpleConfigHelper = new SimpleConfigHelper();
        loadedConfigFromJar = simpleConfigHelper.isConfigFromJar();
        if (loadedConfigFromJar) {
            String jarArgs[] = simpleConfigHelper.getArgsFromJar();
            this.addArgs(jarArgs);
        } else {
            addArgs(args);
        }
    }

    public static File GetVNCServerEXE() {
        String homeDir = System.getProperty("user.home");
        VncServerDir = new File(homeDir, ".uvnc");
        return new File(VncServerDir, "ultra_winvnc.exe");
    }

    public static File GetVNCServerINI() {
        String homeDir = System.getProperty("user.home");
        VncServerDir = new File(homeDir, ".uvnc");
        return new File(VncServerDir, "ultravnc.ini");
    }

    /**
   * Load the attributes from the current argument list
   */
    @Override
    public void loadAttributes() {
        class VncServerStarterThread extends Thread {

            File winVNCFile;

            public VncServerStarterThread(File winVNCFile) {
                this.winVNCFile = winVNCFile;
                this.setDaemon(true);
            }

            @Override
            public void run() {
                shellCmd = new ShellCmd(false);
                try {
                    String args[] = new String[1];
                    args[0] = winVNCFile.getAbsoluteFile().toString();
                    shellCmd.runSysCmd(args);
                } catch (Exception e) {
                    uo.error(e);
                }
            }
        }
        vncDestClientConfig = new VNCDestClientConfig();
        vncDestClientConfig.destClientType = GenericDestClientConfig.DEST_CLIENT_TYPES.GENERIC;
        try {
            String homeDir = System.getProperty("user.home");
            VncServerDir = new File(homeDir, ".uvnc");
            WINDOWS_VNC_SERVER_EXE = GetVNCServerEXE();
            WINDOWS_VNC_SERVER_CFG = GetVNCServerINI();
            uo.info(Server.PROGRAM_NAME + " verison " + Server.VERSION);
            String encryptKey = getStringValue(Server.CONFIG_PASSWORD_ARG, defaultEncryptKey);
            vncDestClientConfig.guiMode = getBooleanValue(VNCDestClient.GUI_ARG, vncDestClientConfig.guiMode);
            if (loadedConfigFromJar) {
                encryptKey = SimpleConfigHelper.OBFUSRACTION_KEY;
            }
            if (!defaultEncryptKey.equals(encryptKey)) {
                useJavaHomePath = false;
            }
            try {
                vncDestClientConfig.load(VisitPCConstants.VNC_DEST_CLIENT_CONFIG_FILE, useJavaHomePath, encryptKey);
                uo.info("Loaded previous config from " + SimpleConfig.GetConfigFile(VisitPCConstants.VNC_DEST_CLIENT_CONFIG_FILE, true));
            } catch (IncorrectKeyException e) {
                throw e;
            } catch (Exception e) {
            }
            vncDestClientConfig.serverName = getStringValue(VNCDestClient.SERVER_NAME_ARG, vncDestClientConfig.serverName);
            vncDestClientConfig.serverPort = (int) getLongValue(VNCDestClient.SERVER_PORT_ARG, vncDestClientConfig.serverPort);
            vncDestClientConfig.username = getStringValue(VNCDestClient.USERNAME_ARG, vncDestClientConfig.username);
            vncDestClientConfig.password = getStringValue(VNCDestClient.PASSWORD_ARG, vncDestClientConfig.password);
            vncDestClientConfig.pcName = getStringValue(VNCDestClient.PC_NAME_ARG, vncDestClientConfig.pcName);
            vncDestClientConfig.expertMode = getBooleanValue(VNCDestClient.EXPERT_MODE_ARG, vncDestClientConfig.expertMode);
            vncDestClientConfig.guiMode = getBooleanValue(VNCDestClient.GUI_ARG, vncDestClientConfig.guiMode);
            vncDestClientConfig.destClientType = DestClientConfig.DEST_CLIENT_TYPES.VNC;
            vncDestClientConfig.vncServerPort = (int) getLongValue(VNCDestClient.VNC_SERVER_PORT_ARG, vncDestClientConfig.vncServerPort);
            vncDestClientConfig.sslTrustStoreFile = getStringValue(VNCDestClient.TRUSTSTORE_ARG, vncDestClientConfig.sslTrustStoreFile);
            vncDestClientConfig.serverReconnectDelaySeconds = getLongValue(VNCDestClient.RECONNECT_DELAY_SECONDS_ARG, vncDestClientConfig.serverReconnectDelaySeconds);
            if (vncDestClientConfig.pcName == null || vncDestClientConfig.pcName.length() == 0) {
                vncDestClientConfig.pcName = DestClientConfig.GetLocalHostName();
            }
            if (getBooleanValue(VNCDestClient.START_VNC_SERVER_ARG, vncDestClientConfig.expertMode)) {
                vncDestClientConfig.startVNCServer = true;
            }
            boolean disableAnonymousSSL = getBooleanValue(VNCDestClient.DISABLE_ANONYMOUS_SSL_ARG);
            if (disableAnonymousSSL) {
                vncDestClientConfig.allowAnonymousSSL = !disableAnonymousSSL;
            }
            uo.setExpertMode(vncDestClientConfig.expertMode);
            attemptVNCServerExtraction();
            if (vncDestClientConfig.guiMode) {
                messageFrame = new MessageFrame(Server.PROGRAM_NAME + " (" + Server.VERSION + ") VNC Destination client");
                if ((loadedConfigFromJar && simpleConfigHelper.allowUserConfigChange()) || !loadedConfigFromJar) {
                    vncDestConfigDialog = new VNCDestConfigDialog(null, VNCDestConfigDialog.DLG_TITLE, vncDestClientConfig);
                    vncDestConfigDialog.setVisible(true);
                    if (!vncDestConfigDialog.isOkSelected()) {
                        System.exit(0);
                    }
                    vncDestClientConfig = vncDestConfigDialog.getConfig(vncDestClientConfig);
                    vncDestClientConfig.checkValid();
                }
                uo.setUserOutputObject(messageFrame);
                messageFrame.setVisible(true);
            }
            if (vncDestClientConfig.username == null || vncDestClientConfig.username.length() == 0 || vncDestClientConfig.password == null || vncDestClientConfig.password.length() == 0) {
                String credentials[] = SimpleConfigHelper.GetCredentials(vncDestClientConfig.guiMode, null);
                vncDestClientConfig.username = credentials[0];
                vncDestClientConfig.password = credentials[1];
            }
            if (!vncDestClientConfig.guiMode && simpleConfigHelper.allowUserConfigChange()) {
                vncDestClientConfig.pcName = DestClientConfig.GetPCName(vncDestClientConfig.pcName);
            }
            if (!loadedConfigFromJar) {
                vncDestClientConfig.save(VisitPCConstants.VNC_DEST_CLIENT_CONFIG_FILE, useJavaHomePath, encryptKey);
            }
            if (loadedConfigFromJar) {
                String tmpDir = System.getProperty("java.io.tmpdir");
                String trustStoreFile = simpleConfigHelper.extractTrustStoreTo(tmpDir);
                vncDestClientConfig.sslTrustStoreFile = trustStoreFile;
            }
            if (vncDestClientConfig.sslTrustStoreFile != null && vncDestClientConfig.sslTrustStoreFile.length() > 0) {
                System.setProperty("javax.net.ssl.trustStore", vncDestClientConfig.sslTrustStoreFile);
                if (vncDestClientConfig.sslTrustStoreFile == null) {
                    vncDestClientConfig.sslTrustStoreFile = "";
                }
            }
            checkForInvalidArgs();
            int numberOfPortsToCheck = 1;
            int connectTimeoutMS = 1000;
            if (vncDestClientConfig.startVNCServer) {
                if (!osName.startsWith("Windows")) {
                    uo.error("VisitPC does not contain a VNC server for non Windows platforms and so can't");
                    uo.error("start a VNC server as requested.");
                    if (osName.startsWith("Linux")) {
                        uo.info("Most current Linux distributions come with a Remote Desktop program. This");
                        uo.info("remote desktop is usually provided using a VNC server. Please enable a ");
                        uo.info("Remote Desktop on this computer. You may need to consult the documentation");
                        uo.info("for your Linux distribution for details on how to do this.");
                        uo.info("Please also disable the Start VNC Server checkbox in the previous");
                        uo.info("configuration dialog.");
                    } else {
                        uo.info("Please install a VNC server on this computer and disable the Start VNC Server");
                        uo.info("checkbox in the previous configuration dialog.");
                    }
                    uo.info("VisitPC will then attempt to use this VNC server.");
                    block(vncDestClientConfig.guiMode);
                    return;
                } else {
                    ServerDetector serverDetector = new ServerDetector("127.0.0.1", 5900, numberOfPortsToCheck, connectTimeoutMS);
                    int port = serverDetector.detect();
                    if (port != ServerDetector.NO_SERVER_PORTS_FOUND) {
                        uo.info("A VNC server is running on port " + 5900 + ", using this VNC server");
                        vncDestClientConfig.vncServerPort = 5900;
                    } else {
                        uo.info("Checking you don't already have a VNC server on localhost port " + vncDestClientConfig.vncServerPort);
                        serverDetector = new ServerDetector("127.0.0.1", vncDestClientConfig.vncServerPort, numberOfPortsToCheck, connectTimeoutMS);
                        port = serverDetector.detect();
                        if (port != ServerDetector.NO_SERVER_PORTS_FOUND) {
                            uo.info("A VNC server is already running on port " + vncDestClientConfig.vncServerPort + ", using this VNC server");
                        } else {
                            if (!VncServerDir.isDirectory() || !WINDOWS_VNC_SERVER_EXE.isFile()) {
                                uo.error(WINDOWS_VNC_SERVER_EXE.getAbsolutePath() + " file not found");
                                uo.error("Unable to start a local VNC server.");
                                block(vncDestClientConfig.guiMode);
                                return;
                            }
                            ensureVncServerCfgFileServerPort(vncDestClientConfig.vncServerPort);
                            uo.info("Starting a VNC server on local port " + vncDestClientConfig.vncServerPort);
                            VncServerStarterThread vncServerStarterThread = new VncServerStarterThread(WINDOWS_VNC_SERVER_EXE);
                            vncServerStarterThread.start();
                            Thread.sleep(1000);
                            if (!loadedConfigFromJar) {
                                vncDestClientConfig.save(VisitPCConstants.VNC_DEST_CLIENT_CONFIG_FILE, useJavaHomePath, encryptKey);
                            }
                            uo.info("Checking that the VNC server is now running on localhost port " + vncDestClientConfig.vncServerPort);
                            port = serverDetector.detect();
                            if (port == ServerDetector.NO_SERVER_PORTS_FOUND) {
                                uo.error("Failed to connect to the VNC server just started on " + vncDestClientConfig.serverName + ":" + vncDestClientConfig.vncServerPort);
                                block(vncDestClientConfig.guiMode);
                                return;
                            }
                        }
                    }
                }
            } else {
                uo.info("Checking that this computer is running a VNC server on TCP port " + vncDestClientConfig.vncServerPort);
                ServerDetector serverDetector = new ServerDetector("127.0.0.1", vncDestClientConfig.vncServerPort, 1, connectTimeoutMS);
                int port = serverDetector.detect();
                if (port == ServerDetector.NO_SERVER_PORTS_FOUND) {
                    uo.error("!!! Failed to detect a VNC server.");
                    uo.info("Please enable or install a VNC server (external to VisitPC) software onto this computer.");
                    block(vncDestClientConfig.guiMode);
                    return;
                }
            }
            vncDestClientConfig.checkValid();
            while (true) {
                try {
                    connectToServer(vncDestClientConfig);
                } catch (Exception e) {
                    uo.error(e);
                }
                if (vncDestClientConfig.serverReconnectDelaySeconds > 0) {
                    uo.info("Waiting " + vncDestClientConfig.serverReconnectDelaySeconds + " seconds before attempting to reconnect to " + vncDestClientConfig.serverName + ":" + vncDestClientConfig.serverPort);
                    Thread.sleep(vncDestClientConfig.serverReconnectDelaySeconds * 1000);
                }
            }
        } catch (Exception e) {
            if (vncDestClientConfig.guiMode) {
                Dialogs.showErrorDialog(messageFrame, "Error", e.getMessage());
            } else {
                uo.error(e);
            }
        } finally {
            stopHandlingConnection();
        }
    }

    /**
   * If in GUI mode then block until the message window is closed.
   * 
   * @param guiMmode
   */
    private void block(boolean guiMmode) {
        if (guiMmode) {
            while (messageFrame.isVisible()) {
                ServerDetector.Delay(100);
            }
        }
    }

    /**
   * Check the the VNC server config file has the correct VNC server port set.
   * 
   * @param vncServerPort
   * @throws IOException
   */
    private void ensureVncServerCfgFileServerPort(int vncServerPort) {
        StringTokenizer strTok;
        boolean updateFile = false;
        try {
            String lines[] = FileIO.GetLines(WINDOWS_VNC_SERVER_CFG.getAbsolutePath());
            String modifiedLines[] = new String[lines.length];
            int lineIndex = 0;
            for (String line : lines) {
                if (line.toLowerCase().startsWith("portnumber=")) {
                    modifiedLines[lineIndex] = "PortNumber=" + vncServerPort;
                    strTok = new StringTokenizer(line, "=");
                    if (strTok.countTokens() > 1) {
                        strTok.nextToken();
                        String portStr = strTok.nextToken();
                        if (!portStr.equals("" + vncServerPort)) {
                            updateFile = true;
                        }
                    }
                } else if (line.toLowerCase().startsWith("autoportselect=")) {
                    modifiedLines[lineIndex] = "AutoPortSelect=0";
                    strTok = new StringTokenizer(line, "=");
                    if (strTok.countTokens() > 1) {
                        strTok.nextToken();
                        String enabledStr = strTok.nextToken();
                        if (enabledStr.equals("1")) {
                            updateFile = true;
                        }
                    }
                } else {
                    modifiedLines[lineIndex] = line;
                }
                lineIndex++;
            }
            if (updateFile) {
                FileIO.SetLines(WINDOWS_VNC_SERVER_CFG.getAbsolutePath(), modifiedLines);
            }
        } catch (Exception e) {
            uo.error(e);
        }
    }

    /**
   * Attempt to extract the VNC server from the jar file and save it to the
   * required path. This will only occur on a Winodws platform as the jar file
   * currently only includes exe and config files for a Windows platform.
   * 
   */
    private void attemptVNCServerExtraction() {
        FileOutputStream fos = null;
        if (osName.startsWith("Windows") && !WINDOWS_VNC_SERVER_EXE.isFile()) {
            if (!VncServerDir.isDirectory()) {
                if (!VncServerDir.mkdirs()) {
                    return;
                }
            }
            try {
                byte[] vncServerExeContents = readBinaryFile(WIN_VNC_SERVER_EXE_LOCATION);
                try {
                    fos = new FileOutputStream(WINDOWS_VNC_SERVER_EXE);
                    fos.write(vncServerExeContents);
                    uo.info("Created " + WINDOWS_VNC_SERVER_EXE.getAbsolutePath());
                } finally {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                }
                byte[] vncServerCfgContents = readBinaryFile(WIN_VNC_SERVER_CFG_LOCATION);
                try {
                    fos = new FileOutputStream(WINDOWS_VNC_SERVER_CFG);
                    fos.write(vncServerCfgContents);
                    uo.info("Created " + WINDOWS_VNC_SERVER_CFG.getAbsolutePath());
                } finally {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (osName.startsWith("Windows") && WINDOWS_VNC_SERVER_EXE.isFile() && WINDOWS_VNC_SERVER_EXE.isFile()) {
            try {
                byte[] vncServerExeContents = readBinaryFile(WIN_VNC_SERVER_EXE_LOCATION);
                if (vncServerExeContents.length != WINDOWS_VNC_SERVER_EXE.length()) {
                    uo.info("Ultra VNC server upgrade required");
                    try {
                        fos = new FileOutputStream(WINDOWS_VNC_SERVER_EXE);
                        fos.write(vncServerExeContents);
                        uo.info("Upgraded the Ultra VNC server (" + WINDOWS_VNC_SERVER_EXE.getAbsolutePath() + ")");
                    } finally {
                        if (fos != null) {
                            fos.close();
                            fos = null;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Read the contents of a file.
   * 
   * @param fileName
   * @return
   * @throws IOException
   */
    private byte[] readBinaryFile(String fileName) throws IOException {
        InputStream input = getClass().getResourceAsStream(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[512];
        int bytes;
        while ((bytes = input.read(buffer)) > 0) {
            output.write(buffer, 0, bytes);
        }
        input.close();
        return output.toByteArray();
    }

    /**
   * Handle an object received from the server.
   * 
   * @param object
   * @throws IOException
   */
    @Override
    public void handleObject(Object object) throws IOException, VisitPCException {
        if (object instanceof ConnectFromDestClient) {
            connectToLocalVNCServer((ConnectFromDestClient) object);
        } else if (object instanceof DataMessage) {
            sendToServer((DataMessage) object);
        } else if (object instanceof KeepAliveMessage) {
        } else if (object instanceof DisconnectFromDestClient) {
            closeDestConnections((DisconnectFromDestClient) object);
        }
    }

    /**
   * Attempt to make a connection to the local VNC server
   */
    public void connectToLocalVNCServer(ConnectFromDestClient connectFromDestClient) throws IOException {
        connectFromDestClient.destAddress = "127.0.0.1";
        connectFromDestClient.destPort = vncDestClientConfig.vncServerPort;
        DestConnectionHandler destConnectionHandler = new DestConnectionHandler(uo, connectFromDestClient, this);
        try {
            destConnectionHandler.connect();
            ConnectedFromDestClient connectedFromDestClient = new ConnectedFromDestClient();
            connectedFromDestClient.sessionID = connectFromDestClient.sessionID;
            send(connectedFromDestClient);
            destConnectionHandler.start();
            destConnectionHandlers.add(destConnectionHandler);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.message = e.getMessage();
            send(errorMessage);
        }
    }
}
