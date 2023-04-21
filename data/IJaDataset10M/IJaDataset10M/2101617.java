package org.bitdrive.jlan.impl;

import org.alfresco.jlan.app.DriveMappingsConfigSection;
import org.alfresco.jlan.app.XMLServerConfiguration;
import org.alfresco.jlan.debug.Debug;
import org.alfresco.jlan.debug.DebugConfigSection;
import org.alfresco.jlan.ftp.FTPConfigSection;
import org.alfresco.jlan.netbios.server.NetBIOSNameServer;
import org.alfresco.jlan.netbios.win32.Win32NetBIOS;
import org.alfresco.jlan.oncrpc.nfs.NFSConfigSection;
import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.server.ServerListener;
import org.alfresco.jlan.server.config.ServerConfiguration;
import org.alfresco.jlan.smb.SMBErrorText;
import org.alfresco.jlan.smb.SMBStatus;
import org.alfresco.jlan.smb.server.CIFSConfigSection;
import org.alfresco.jlan.smb.server.SMBServer;
import org.alfresco.jlan.smb.util.DriveMapping;
import org.alfresco.jlan.smb.util.DriveMappingList;
import org.alfresco.jlan.util.ConsoleIO;
import org.alfresco.jlan.util.Platform;
import org.alfresco.jlan.util.win32.Win32Utils;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * JLAN File Server Application
 *
 * @author gkspencer
 */
public class JlanServer implements ServerListener {

    public static final int CheckPointStarting = 0;

    public static final int CheckPointConfigLoading = 1;

    public static final int CheckPointConfigLoaded = 2;

    public static final int CheckPointCheckIPAddress = 3;

    public static final int CheckPointCreateSMBServer = 4;

    public static final int CheckPointCreateFTPServer = 5;

    public static final int CheckPointCreateNFSServer = 6;

    public static final int CheckPointServersStart = 7;

    public static final int CheckPointServersStarted = 8;

    public static final int CheckPointRunning = 9;

    public static final int CheckPointServersStop = 10;

    public static final int CheckPointServersStopped = 11;

    public static final int CheckPointFinished = 12;

    private static final String DEFAULT_CONFIGFILENAME = "jlanserver.xml";

    private static final boolean CheckLocalIPAddress = false;

    protected static boolean m_shutdown = false;

    protected static boolean m_restart = false;

    protected static boolean m_allowShutViaConsole = true;

    protected static boolean m_dumpStackOnError = true;

    private ServerConfiguration m_srvConfig;

    /**
     * Class constructor
     */
    protected JlanServer() {
    }

    /**
     * Set/clear the allow shutdown via console flag
     *
     * @param consoleShut boolean
     */
    public static final void setAllowConsoleShutdown(boolean consoleShut) {
        m_allowShutViaConsole = consoleShut;
    }

    /**
     * Enable/disable exception stack dumps
     *
     * @param ena boolean
     */
    protected final void enableExceptionStackDump(boolean ena) {
        m_dumpStackOnError = ena;
    }

    /**
     * Start the JLAN Server
     *
     * @param reader Reader Reader to read XML config from.
     */
    protected void start(Reader reader) {
        PrintStream out = createOutputStream();
        m_shutdown = true;
        m_restart = false;
        checkPoint(out, CheckPointStarting);
        m_srvConfig = null;
        try {
            checkPoint(out, CheckPointConfigLoading);
            m_srvConfig = loadConfiguration(out, reader);
            checkPoint(out, CheckPointConfigLoaded);
        } catch (Exception ex) {
            checkPointError(out, CheckPointConfigLoading, ex);
            return;
        }
        if (CheckLocalIPAddress) {
            try {
                checkPoint(out, CheckPointCheckIPAddress);
                String localAddr = InetAddress.getLocalHost().getHostAddress();
                if (localAddr.equals("127.0.0.1")) {
                    out.println("%% Local IP address resolves to 127.0.0.1, this may be caused by a mis-configured hosts file");
                    return;
                }
            } catch (UnknownHostException ex) {
                checkPointError(out, CheckPointCheckIPAddress, ex);
                return;
            }
        }
        try {
            if (m_srvConfig.hasConfigSection(CIFSConfigSection.SectionName)) {
                checkPoint(out, CheckPointCreateSMBServer);
                CIFSConfigSection cifsConfig = (CIFSConfigSection) m_srvConfig.getConfigSection(CIFSConfigSection.SectionName);
                if (cifsConfig.hasWin32NetBIOS()) Win32NetBIOS.LanaEnumerate();
                if (cifsConfig.hasNetBIOSSMB()) m_srvConfig.addServer(createNetBIOSServer(m_srvConfig));
                m_srvConfig.addServer(createSMBServer(m_srvConfig));
            }
            if (m_srvConfig.hasConfigSection(FTPConfigSection.SectionName)) {
                checkPoint(out, CheckPointCreateFTPServer);
                m_srvConfig.addServer(createFTPServer(m_srvConfig));
            }
            if (m_srvConfig.hasConfigSection(NFSConfigSection.SectionName)) {
                checkPoint(out, CheckPointCreateNFSServer);
                NFSConfigSection nfsConfig = (NFSConfigSection) m_srvConfig.getConfigSection(NFSConfigSection.SectionName);
                if (nfsConfig.hasNFSPortMapper()) m_srvConfig.addServer(createNFSPortMapper(m_srvConfig));
                m_srvConfig.addServer(createNFSMountServer(m_srvConfig));
                m_srvConfig.addServer(createNFSServer(m_srvConfig));
            }
            checkPoint(out, CheckPointServersStart);
            DebugConfigSection dbgConfig = (DebugConfigSection) m_srvConfig.getConfigSection(DebugConfigSection.SectionName);
            for (int i = 0; i < m_srvConfig.numberOfServers(); i++) {
                NetworkServer server = m_srvConfig.getServer(i);
                if (Debug.EnableInfo && dbgConfig != null && dbgConfig.hasDebug()) Debug.println("Starting server " + server.getProtocolName() + " ...");
                m_srvConfig.getServer(i).startServer();
            }
            checkPoint(out, CheckPointServersStarted);
            boolean service = false;
            if (ConsoleIO.isValid() == false) service = true;
            checkPoint(out, CheckPointRunning);
            m_shutdown = false;
        } catch (Exception ex) {
            checkPointError(out, CheckPointServersStarted, ex);
        }
        checkPoint(out, CheckPointFinished);
    }

    public boolean isAlive() {
        boolean result = true;
        if (m_srvConfig == null) return false;
        int idx = m_srvConfig.numberOfServers() - 1;
        while (idx >= 0) {
            NetworkServer server = m_srvConfig.getServer(idx--);
            if (!server.isActive()) result = false;
        }
        return result;
    }

    public void shutdown() {
        if (m_srvConfig == null) return;
        int idx = m_srvConfig.numberOfServers() - 1;
        while (idx >= 0) {
            NetworkServer server = m_srvConfig.getServer(idx--);
            server.shutdownServer(false);
        }
    }

    /**
     * Shutdown the server when running as an NT service
     *
     * @param args String[]
     */
    public static final void shutdownServer(String[] args) {
        m_shutdown = true;
    }

    /**
     * Create the SMB server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createSMBServer(ServerConfiguration config) throws Exception {
        NetworkServer smbServer = new SMBServer(config);
        if (Platform.isPlatformType() == Platform.Type.WINDOWS && config.hasConfigSection(DriveMappingsConfigSection.SectionName)) smbServer.addServerListener(this);
        return smbServer;
    }

    /**
     * Create the NetBIOS name server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createNetBIOSServer(ServerConfiguration config) throws Exception {
        return new NetBIOSNameServer(config);
    }

    /**
     * Create the FTP server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createFTPServer(ServerConfiguration config) throws Exception {
        return createServer("org.alfresco.jlan.ftp.FTPServer", config);
    }

    /**
     * Create the NFS server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createNFSServer(ServerConfiguration config) throws Exception {
        return createServer("org.alfresco.jlan.oncrpc.nfs.NFSServer", config);
    }

    /**
     * Create the NFS mount server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createNFSMountServer(ServerConfiguration config) throws Exception {
        return createServer("org.alfresco.jlan.oncrpc.mount.MountServer", config);
    }

    /**
     * Create the NFS port mapper server
     *
     * @param config ServerConfiguration
     * @return NetworkServer
     */
    protected final NetworkServer createNFSPortMapper(ServerConfiguration config) throws Exception {
        return createServer("org.alfresco.jlan.oncrpc.portmap.PortMapperServer", config);
    }

    /**
     * Create a network server using reflection
     *
     * @param className String
     * @param config    ServerConfiguration
     * @return NetworkServer
     * @throws Exception
     */
    protected final NetworkServer createServer(String className, ServerConfiguration config) throws Exception {
        NetworkServer srv = null;
        Class<?>[] classes = new Class[1];
        classes[0] = ServerConfiguration.class;
        Constructor<?> srvConstructor = Class.forName(className).getConstructor(classes);
        Object[] args = new Object[1];
        args[0] = config;
        srv = (NetworkServer) srvConstructor.newInstance(args);
        return srv;
    }

    /**
     * Load the server configuration, default is to load using an XML configuration file.
     *
     * @param out    PrintStream
     * @param reader Reader Reader to read the XML config from.
     * @return ServerConfiguration
     * @throws Exception
     */
    protected ServerConfiguration loadConfiguration(PrintStream out, Reader reader) throws Exception {
        XMLServerConfiguration srvConfig = null;
        srvConfig = new XMLServerConfiguration();
        srvConfig.loadConfiguration(reader);
        return srvConfig;
    }

    /**
     * Create the output stream for logging
     *
     * @return PrintStream
     */
    protected PrintStream createOutputStream() {
        return System.out;
    }

    /**
     * Checkpoint method, called at various points of the server startup and shutdown
     *
     * @param out   PrintStream
     * @param check int
     */
    protected void checkPoint(PrintStream out, int check) {
    }

    /**
     * Checkpoint error method, called if an error occurs during server startup/shutdown
     *
     * @param out   PrintStream
     * @param check int
     * @param ex    Exception
     */
    protected void checkPointError(PrintStream out, int check, Exception ex) {
        String msg = "%% Error occurred";
        switch(check) {
            case CheckPointConfigLoading:
                msg = "%% Failed to load server configuration";
                break;
            case CheckPointCheckIPAddress:
                msg = "%% Failed to get local IP address details";
                break;
            case CheckPointServersStarted:
                msg = "%% Server error";
                break;
        }
        out.println(msg);
        if (m_dumpStackOnError) ex.printStackTrace(out);
    }

    /**
     * Handle server startup/shutdown events
     *
     * @param server NetworkServer
     * @param event  int
     */
    public void serverStatusEvent(NetworkServer server, int event) {
        if (server instanceof SMBServer) {
            DriveMappingsConfigSection mapConfig = (DriveMappingsConfigSection) m_srvConfig.getConfigSection(DriveMappingsConfigSection.SectionName);
            if (mapConfig == null) return;
            if (event == ServerListener.ServerStartup) {
                DriveMappingList mapList = mapConfig.getMappedDrives();
                for (int i = 0; i < mapList.numberOfMappings(); i++) {
                    DriveMapping driveMap = mapList.getMappingAt(i);
                    if (Debug.EnableInfo && mapConfig.hasDebug()) Debug.println("Mapping drive " + driveMap.getLocalDrive() + " to " + driveMap.getRemotePath() + " ...");
                    int sts = Win32Utils.MapNetworkDrive(driveMap.getRemotePath(), driveMap.getLocalDrive(), driveMap.getUserName(), driveMap.getPassword(), driveMap.hasInteractive(), driveMap.hasPrompt());
                    if (sts != 0) Debug.println("Failed to map drive " + driveMap.getLocalDrive() + " to " + driveMap.getRemotePath() + ", status = " + SMBErrorText.ErrorString(SMBStatus.Win32Err, sts));
                }
            } else if (event == ServerListener.ServerShutdown) {
                DriveMappingList mapList = mapConfig.getMappedDrives();
                for (int i = 0; i < mapList.numberOfMappings(); i++) {
                    DriveMapping driveMap = mapList.getMappingAt(i);
                    if (Debug.EnableInfo && mapConfig.hasDebug()) Debug.println("Removing mapped drive " + driveMap.getLocalDrive() + " to " + driveMap.getRemotePath() + " ...");
                    int sts = Win32Utils.DeleteNetworkDrive(driveMap.getLocalDrive(), false, true);
                    if (sts != 0) Debug.println("Failed to delete mapped drive " + driveMap.getLocalDrive() + " from " + driveMap.getRemotePath() + ", status = " + SMBErrorText.ErrorString(SMBStatus.Win32Err, sts));
                }
            }
        }
    }
}
