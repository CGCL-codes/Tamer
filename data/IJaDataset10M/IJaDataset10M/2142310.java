package sagex.remote.rmi;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import sagex.SageAPI;
import sagex.api.Global;
import sagex.plugin.impl.SagexConfiguration;
import sagex.remote.server.DatagramListener;
import sagex.remote.server.DatagramServer;
import sagex.util.ILog;
import sagex.util.LogProvider;

public class SageRMIServer {

    private ILog log = LogProvider.getLogger(SageRMIServer.class);

    public static final String SERVER_BINDING = "SageJavaRPC";

    private static SageRMIServer instance = new SageRMIServer();

    private DatagramServer udpServer = null;

    private Properties serverInfo = null;

    private boolean isRunning = false;

    public SageRMIServer() {
    }

    public synchronized void startServer() {
        if (isRunning) {
            log.warn("RMI Services are already running... will not start again.");
            return;
        }
        isRunning = true;
        SagexConfiguration config = new SagexConfiguration();
        discoverProperties(config);
        if (config.getBoolean(SagexConfiguration.PROP_ENABLE_RMI, true)) {
            System.setProperty("java.rmi.server.hostname", serverInfo.getProperty("server"));
            try {
                log.info("Starting Sage RMI Server: " + this);
                SageRemoteCommandImpl obj = SageRemoteCommandImpl.getInstance();
                SageRemoteCommand stub = (SageRemoteCommand) UnicastRemoteObject.exportObject(obj, 0);
                Registry registry = LocateRegistry.createRegistry(Integer.parseInt(serverInfo.getProperty("rmi.port")));
                registry.rebind(SERVER_BINDING, stub);
                log.info("Sage Java RMI Server ready: " + this);
            } catch (Exception e) {
                log.warn("Failed to start RMI Services", e);
            }
        } else {
            log.debug("RMI server is disabled in the configuration.  If you want RMI services you will need to enable it in the plugin configuration.");
        }
        startRMIBroadcastServer();
    }

    public static SageRMIServer getInstance() {
        return instance;
    }

    private void startRMIBroadcastServer() {
        if (SageAPI.isRemote()) {
            log.warn("Discovery is disabled because this server is remote");
            return;
        }
        udpServer = new DatagramServer(DatagramServer.MULTICAST_GROUP, DatagramServer.MULTICAST_PORT, new DatagramListener() {

            public byte[] onDatagramPacketReceived(DatagramPacket packet) {
                try {
                    String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    if (msg != null && msg.startsWith("Discover")) {
                        log.info("Received Discovery Request: " + msg);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        serverInfo.list(new PrintStream(baos));
                        baos.flush();
                        return baos.toByteArray();
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "".getBytes();
                }
            }

            public void serverStarted(DatagramServer server) {
                log.info("Annoucing Remote Sage API Server availability....");
            }

            public void serverStopped(DatagramServer server) {
                log.info("Stopping Remote Sage API Server....");
            }
        });
        try {
            udpServer.startServer();
        } catch (Exception e) {
            log.warn("Failed to start the RMI Broadcast Server", e);
        }
    }

    public String toString() {
        if (serverInfo == null) {
            return this.getClass().getName() + "[No Info Set]";
        } else {
            return this.getClass().getName() + "[" + serverInfo.getProperty("server") + ":" + serverInfo.getProperty("rmi.port") + "]";
        }
    }

    public synchronized void stopServer() {
        if (isRunning) {
            if (udpServer != null) {
                udpServer.stopServer();
            }
        }
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void discoverProperties(SagexConfiguration plugin) {
        serverInfo = new Properties();
        serverInfo.put("server", Global.GetServerAddress());
        serverInfo.put("rmi.port", plugin.get(SagexConfiguration.PROP_RMI_PORT, "1098"));
        serverInfo.put("http.port", plugin.get(SagexConfiguration.PROP_HTTP_PORT, "8080"));
        SageAPI.setProviderProperties(serverInfo);
    }
}
