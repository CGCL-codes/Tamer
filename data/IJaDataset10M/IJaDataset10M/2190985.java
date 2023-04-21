package gnu.saw.server.console;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import com.martiansoftware.jsap.CommandLineTokenizer;
import gnu.saw.SAW;
import gnu.saw.graphics.device.SAWUsableGraphicalDeviceResolver;
import gnu.saw.nativeutils.SAWNativeUtils;
import gnu.saw.server.SAWServer;
import gnu.saw.server.connection.SAWServerConnectionHandler;
import gnu.saw.server.connection.SAWServerConnector;
import gnu.saw.server.help.SAWServerHelpManager;
import gnu.saw.terminal.SAWTerminal;

public class SAWServerConsoleReader implements Runnable {

    private String command;

    private String[] splitCommand;

    private DateFormat dateTimeFormat;

    private GregorianCalendar clock;

    private StringBuilder message;

    private SAWServer server;

    public SAWServerConsoleReader(SAWServer server) {
        this.server = server;
        this.dateTimeFormat = new SimpleDateFormat("MM-dd-G][HH:mm:ss:SSS-z]");
        this.clock = new GregorianCalendar();
        this.message = new StringBuilder();
    }

    public void run() {
        SAWTerminal.print("\rSAW>SAWSERVER:Enter '*SAWHELP' to know about the SATAN-ANYWHERE server console\nSAW>");
        while (true) {
            try {
                command = SAWTerminal.readLine(true);
                if (command != null) {
                    if (!(command.length() == 0)) {
                        splitCommand = CommandLineTokenizer.tokenize(command);
                        if (splitCommand.length < 1) {
                            splitCommand = new String[] { command };
                        }
                    } else {
                        splitCommand = new String[] { "" };
                    }
                    if (splitCommand[0].equalsIgnoreCase("*SAWMESSAGE")) {
                        if (command.contains("\"")) {
                            if (splitCommand.length == 2) {
                                List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                                synchronized (connections) {
                                    if (connections.size() > 0) {
                                        for (SAWServerConnectionHandler connectionHandler : connections) {
                                            if (connectionHandler.getSessionHandler().isAuthenticated()) {
                                                if (connectionHandler.getConnection() != null && connectionHandler.getConnection().isConnected()) {
                                                    try {
                                                        connectionHandler.getConnection().getResultWriter().write("\nSAW>SAWMESSAGE:Message from server: \"" + splitCommand[1] + "\"\nSAW>");
                                                        connectionHandler.getConnection().getResultWriter().flush();
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Message sended to clients!\nSAW>");
                                    } else {
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Not connected with clients!\nSAW>");
                                    }
                                }
                            } else if (splitCommand.length == 1) {
                                List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                                synchronized (connections) {
                                    if (connections.size() > 0) {
                                        for (SAWServerConnectionHandler connectionHandler : connections) {
                                            if (connectionHandler.getSessionHandler().isAuthenticated()) {
                                                if (connectionHandler.getConnection() != null && connectionHandler.getConnection().isConnected()) {
                                                    try {
                                                        connectionHandler.getConnection().getResultWriter().write("\nSAW>SAWMESSAGE:Message from server: \"\"\nSAW>");
                                                        connectionHandler.getConnection().getResultWriter().flush();
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Message sended to clients!\nSAW>");
                                    } else {
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Not connected with clients!\nSAW>");
                                    }
                                }
                            }
                        } else {
                            if (command.length() >= 12 && command.charAt(11) == ' ') {
                                List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                                synchronized (connections) {
                                    if (connections.size() > 0) {
                                        for (SAWServerConnectionHandler connectionHandler : connections) {
                                            if (connectionHandler.getSessionHandler().isAuthenticated()) {
                                                if (connectionHandler.getConnection() != null && connectionHandler.getConnection().isConnected()) {
                                                    try {
                                                        connectionHandler.getConnection().getResultWriter().write("\nSAW>SAWMESSAGE:Message from server: \"" + command.substring(12) + "\"\nSAW>");
                                                        connectionHandler.getConnection().getResultWriter().flush();
                                                    } catch (Exception e) {
                                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Error detected when sending message!\nSAW>");
                                                    }
                                                }
                                            }
                                        }
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Message sended to clients!\nSAW>");
                                    } else {
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Not connected with clients!\nSAW>");
                                    }
                                }
                            } else {
                                List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                                synchronized (connections) {
                                    if (connections.size() > 0) {
                                        for (SAWServerConnectionHandler connectionHandler : connections) {
                                            if (connectionHandler.getSessionHandler().isAuthenticated()) {
                                                if (connectionHandler.getConnection() != null && connectionHandler.getConnection().isConnected()) {
                                                    try {
                                                        connectionHandler.getConnection().getResultWriter().write("\nSAW>SAWMESSAGE:Message from server: \"\"\nSAW>");
                                                        connectionHandler.getConnection().getResultWriter().flush();
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Message sended to clients!\nSAW>");
                                    } else {
                                        SAWTerminal.print("\rSAW>SAWMESSAGE:Not connected with clients!\nSAW>");
                                    }
                                }
                            }
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWTERMINATE")) {
                        System.exit(0);
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWCONNECTIONSLIST")) {
                        int i = 0;
                        message.setLength(0);
                        List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                        synchronized (connections) {
                            if (connections.size() > 0) {
                                message.append("\rSAW>SAWCONNECTIONSLIST:Lis of current SATAN-ANYWHERE connections on server:\nSAW>");
                                for (SAWServerConnectionHandler handler : connections) {
                                    message.append("\nSAW>Order: " + i++ + "");
                                    message.append("\nSAW>Authenticated: " + (handler.getSessionHandler().isAuthenticated() ? "Yes" : "No") + "");
                                    message.append("\nSAW>Login: '" + (handler.getSessionHandler().getLogin() != null ? handler.getSessionHandler().getLogin() : "") + "'");
                                    InetAddress address = handler.getConnection().getConnectionSocket().getInetAddress();
                                    if (address != null) {
                                        message.append("\nSAW>Host address: '" + address.getHostAddress() + "'\nSAW>Host name: '" + address.getHostName() + "'\nSAW>Canonical host name: '" + address.getCanonicalHostName() + "'\nSAW>");
                                    }
                                }
                                message.append("\nSAW>SAWCONNECTIONSLIST:End of SATAN-ANYWHERE connections list\nSAW>");
                                SAWTerminal.print(message.toString());
                            } else {
                                SAWTerminal.print("\rSAW>SAWCONNECTIONSLIST:Not connected with clients!\nSAW>");
                            }
                        }
                        SAWTerminal.print("\nSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWHELP")) {
                        if (splitCommand.length == 1) {
                            SAWTerminal.print(SAWServerHelpManager.getMainHelpForServerCommands());
                        } else if (splitCommand.length > 1) {
                            SAWTerminal.print(SAWServerHelpManager.getHelpForServerCommand(splitCommand[1]));
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWCLEAR")) {
                        SAWTerminal.clear();
                        SAWTerminal.print("SAW>SAWSERVER:SATAN-ANYWHERE SERVER V:" + SAW.SAW_VERSION + " - COPYRIGHT (C) " + SAW.SAW_YEAR + "\n" + "SAW>SAWSERVER:This software is under GPL license, see license.txt for details!\n" + "SAW>SAWSERVER:This software comes with no warranty, use at your own risk!\n" + "\rSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWNETWORKINTERFACESLIST")) {
                        message.setLength(0);
                        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                        message.append("\rSAW>SAWNETWORKINTERFACESLIST:Network interfaces on server:\nSAW>");
                        while (networkInterfaces.hasMoreElements()) {
                            NetworkInterface networkInterface = networkInterfaces.nextElement();
                            message.append("\nSAW>Name: '" + networkInterface.getName() + "'\nSAW>Display name: '" + networkInterface.getDisplayName() + "'\nSAW>Addresses: ");
                            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                InetAddress address = addresses.nextElement();
                                message.append("\nSAW>Host address: '" + address.getHostAddress() + "'\nSAW>Host name: '" + address.getHostName() + "'\nSAW>Canonical host name: '" + address.getCanonicalHostName() + "'");
                            }
                            message.append("\nSAW>");
                        }
                        message.append("\nSAW>SAWNETWORKINTERFACESLIST:End of network interfaces list\nSAW>");
                        SAWTerminal.print(message.toString());
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWGRAPHICALDEVICESLIST")) {
                        message.setLength(0);
                        int count = 0;
                        GraphicsDevice[] devices = SAWUsableGraphicalDeviceResolver.getRasterDevices();
                        if (devices != null && devices.length > 0) {
                            message.append("\rSAW>SAWGRAPHICALDEVICESLIST:Graphical devices on server:\nSAW>");
                            for (GraphicsDevice device : devices) {
                                DisplayMode mode = device.getDisplayMode();
                                message.append("\nSAW>Number: " + (++count));
                                message.append("\nSAW>ID: '" + device.getIDstring() + "'");
                                message.append("\nSAW>Mode: " + mode.getWidth() + "x" + mode.getHeight());
                                Rectangle bounds = device.getDefaultConfiguration().getBounds();
                                message.append("\nSAW>Origin: x:" + bounds.x + " y:" + bounds.y);
                                message.append("\nSAW>");
                            }
                            message.append("\nSAW>SAWGRAPHICALDEVICESLIST:End of graphical devices list\nSAW>");
                        } else {
                            message.append("\rSAW>SAWGRAPHICALDEVICESLIST:Graphical devices not found on server!");
                        }
                        SAWTerminal.print(message.toString());
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROPERTIESLIST")) {
                        message.setLength(0);
                        message.append("\rSAW>SAWPROPERTIESLIST:JVM properties on server:\nSAW>");
                        for (Entry<Object, Object> property : System.getProperties().entrySet()) {
                            message.append("\nSAW>[" + property.getKey().toString() + "]=[" + property.getValue().toString() + "]");
                        }
                        message.append("\nSAW>\nSAW>SAWPROPERTIESLIST:End of JVM properties list\nSAW>");
                        SAWTerminal.print(message.toString());
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENVIRONMENTSLIST")) {
                        message.setLength(0);
                        message.append("\rSAW>SAWENVIRONMENTSLIST:Environment variables on server:\nSAW>");
                        for (Entry<String, String> variable : SAWNativeUtils.getvirtualenv().entrySet()) {
                            message.append("\nSAW>[" + variable.getKey() + "]=[" + variable.getValue() + "]");
                        }
                        message.append("\nSAW>\nSAW>SAWENVIRONMENTSLIST:End of environment variables list\nSAW>");
                        SAWTerminal.print(message.toString());
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWFILESYSTEMROOTSLIST")) {
                        message.setLength(0);
                        File[] roots = File.listRoots();
                        message.append("\rSAW>SAWFILESYSTEMROOTSLIST:File system roots on server:\nSAW>");
                        for (File root : roots) {
                            message.append("\nSAW>Canonical path: '" + root.getCanonicalPath() + "'");
                        }
                        message.append("\nSAW>\nSAW>SAWFILESYSTEMROOTSLIST:End of file system roots list\nSAW>");
                        SAWTerminal.print(message.toString());
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPRINTSERVICESLIST")) {
                        message.setLength(0);
                        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                        if (printServices.length > 0) {
                            message.append("\rSAW>SAWPRINTSERVICESLIST:Print services on server:\nSAW>");
                            for (PrintService printService : printServices) {
                                message.append("\nSAW>Name: '" + printService.getName() + "'");
                            }
                            message.append("\nSAW>\nSAW>SAWPRINTSERVICESLIST:End of print services list\nSAW>");
                            SAWTerminal.print(message.toString());
                        } else {
                            SAWTerminal.print("\rSAW>SAWPRINTSERVICESLIST:Print services not found on server!");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWDEFAULTPRINTSERVICE")) {
                        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
                        if (defaultPrintService != null) {
                            SAWTerminal.print("\rSAW>SAWDEFAULTPRINTSERVICE:Name: '" + defaultPrintService.getName() + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWDEFAULTPRINTSERVICE:Default print service not found!" + "\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENVIRONMENT")) {
                        if (splitCommand.length == 2) {
                            String value = SAWNativeUtils.getvirtualenv(splitCommand[1]);
                            if (value != null) {
                                SAWTerminal.print("\rSAW>SAWENVIRONMENT:[" + splitCommand[1] + "]=[" + value + "]\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWENVIRONMENT:Environment variable '" + splitCommand[1] + "' not found on server!\nSAW>");
                            }
                        } else if (splitCommand.length == 3) {
                            if (SAWNativeUtils.putvirtualenv(splitCommand[1], splitCommand[2]) == 0) {
                                SAWTerminal.print("\rSAW>SAWENVIRONMENT:[" + splitCommand[1] + "]=[" + splitCommand[2] + "]\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWENVIRONMENT:Environment variable '" + splitCommand[1] + "' cannot be set to '" + splitCommand[2] + "' on server!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWENVIRONMENT:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROPERTY")) {
                        if (splitCommand.length == 2) {
                            String value = System.getProperty(splitCommand[1]);
                            if (value != null) {
                                SAWTerminal.print("\rSAW>SAWPROPERTY:[" + splitCommand[1] + "]=[" + value + "]\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWPROPERTY:JVM property '" + splitCommand[1] + "' not found on server!\nSAW>");
                            }
                        } else if (splitCommand.length == 3) {
                            try {
                                System.setProperty(splitCommand[1], splitCommand[2]);
                                SAWTerminal.print("\rSAW>SAWPROPERTY:[" + splitCommand[1] + "]=[" + splitCommand[2] + "]\nSAW>");
                            } catch (Exception e) {
                                SAWTerminal.print("\rSAW>SAWPROPERTY:JVM property '" + splitCommand[1] + "' cannot be set to '" + splitCommand[2] + "' on server!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROPERTY:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWSESSIONSLIMIT")) {
                        if (splitCommand.length == 1) {
                            int sessionLimit = server.getServerConnector().getSessionsLimit();
                            SAWTerminal.print("\rSAW>SAWSESSIONSLIMIT:Server sessions limit: '" + sessionLimit + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            int sessionsLimit = Integer.parseInt(splitCommand[1]);
                            if (sessionsLimit >= 1) {
                                SAWServerConnector connector = server.getServerConnector();
                                synchronized (connector) {
                                    connector.setSessionsLimit(sessionsLimit);
                                    connector.interruptConnector();
                                    connector.notify();
                                }
                                SAWTerminal.print("\rSAW>SAWSESSIONSLIMIT:Server sessions limit set to: '" + sessionsLimit + "'\nSAW>");
                                ;
                            } else {
                                SAWTerminal.print("\rSAW>SAWSESSIONSLIMIT:Invalid limit!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWSESSIONSLIMIT:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWCONNECTIONMODE")) {
                        if (splitCommand.length == 1) {
                            if (server.getServerConnector().isPassive()) {
                                SAWTerminal.print("\rSAW>SAWCONNECTIONMODE:Server connection mode: Passive\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWCONNECTIONMODE:Server connection mode: Active\nSAW>");
                            }
                        } else if (splitCommand.length == 2) {
                            boolean passive = !splitCommand[1].toUpperCase().startsWith("A");
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setPassive(passive);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWCONNECTIONMODE:Server connection mode set to: " + (passive ? "Passive" : "Active") + "\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWCONNECTIONMODE:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWTARGETHOST")) {
                        if (splitCommand.length == 1) {
                            String hostAddress = server.getServerConnector().getAddress();
                            SAWTerminal.print("\rSAW>SAWTARGETHOST:Server target host address: '" + hostAddress + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            String hostAddress = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setAddress(hostAddress);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWTARGETHOST:Server target host address set to: '" + hostAddress + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWTARGETHOST:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWTARGETPORT")) {
                        if (splitCommand.length == 1) {
                            Integer port = server.getServerConnector().getPort();
                            SAWTerminal.print("\rSAW>SAWTARGETPORT:Server target host port: '" + port + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            try {
                                int port = Integer.parseInt(splitCommand[1]);
                                if (port < 0 || port > 65535) {
                                    SAWTerminal.print("\rSAW>SAWTARGETPORT:Syntax error detected!\nSAW>");
                                } else {
                                    SAWServerConnector connector = server.getServerConnector();
                                    synchronized (connector) {
                                        connector.setPort(port);
                                        connector.interruptConnector();
                                        connector.notify();
                                    }
                                    SAWTerminal.print("\rSAW>SAWTARGETPORT:Server target host port set to: '" + port + "'\nSAW>");
                                }
                            } catch (NumberFormatException e) {
                                SAWTerminal.print("\rSAW>SAWTARGETPORT:Syntax error detected!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWTARGETPORT:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYTYPE")) {
                        if (splitCommand.length == 1) {
                            String proxyType = server.getServerConnector().getProxyType();
                            if (proxyType.toUpperCase().startsWith("H")) {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type: HTTP\nSAW>");
                            } else if (proxyType.toUpperCase().startsWith("S")) {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type: SOCKS\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type: Disabled\nSAW>");
                            }
                        } else if (splitCommand.length == 2) {
                            String proxyType = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setProxyType(proxyType);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            if (proxyType.toUpperCase().startsWith("H")) {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type set to: HTTP\nSAW>");
                            } else if (proxyType.toUpperCase().startsWith("S")) {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type set to: SOCKS\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWPROXYTYPE:Server proxy type set to: Disabled\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYTYPE:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYHOST")) {
                        if (splitCommand.length == 1) {
                            String proxyAddress = server.getServerConnector().getProxyAddress();
                            SAWTerminal.print("\rSAW>SAWPROXYHOST:Server proxy host address: '" + proxyAddress + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            String proxyAddress = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setProxyAddress(proxyAddress);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWPROXYHOST:Server proxy host address set to: '" + proxyAddress + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYHOST:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYPORT")) {
                        if (splitCommand.length == 1) {
                            Integer proxyPort = server.getServerConnector().getProxyPort();
                            if (proxyPort != null) {
                                SAWTerminal.print("\rSAW>SAWPROXYPORT:Server proxy host port: '" + proxyPort + "'\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWPROXYPORT:Server proxy host port: ''\nSAW>");
                            }
                        } else if (splitCommand.length == 2) {
                            try {
                                int proxyPort = Integer.parseInt(splitCommand[1]);
                                if (proxyPort < 0 || proxyPort > 65535) {
                                    SAWTerminal.print("\rSAW>SAWPROXYPORT:Syntax error detected!\nSAW>");
                                } else {
                                    SAWServerConnector connector = server.getServerConnector();
                                    synchronized (connector) {
                                        connector.setPort(proxyPort);
                                        connector.interruptConnector();
                                        connector.notify();
                                    }
                                    SAWTerminal.print("\rSAW>SAWPROXYPORT:Server proxy host port set to: '" + proxyPort + "'\nSAW>");
                                }
                            } catch (NumberFormatException e) {
                                SAWTerminal.print("\rSAW>SAWPROXYPORT:Syntax error detected!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYPORT:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYSECURITY")) {
                        if (splitCommand.length == 1) {
                            if (!server.getServerConnector().isUseProxySecurity()) {
                                SAWTerminal.print("\rSAW>SAWPROXYSECURITY:Server proxy security: Disabled\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWPROXYSECURITY:Server proxy security: Enabled\nSAW>");
                            }
                        } else if (splitCommand.length == 2) {
                            if (splitCommand[1].toUpperCase().startsWith("E")) {
                                SAWServerConnector connector = server.getServerConnector();
                                synchronized (connector) {
                                    connector.setUseProxySecurity(true);
                                    connector.interruptConnector();
                                    connector.notify();
                                }
                                SAWTerminal.print("\rSAW>SAWPROXYSECURITY:Server proxy security set to: Enabled\nSAW>");
                            } else {
                                SAWServerConnector connector = server.getServerConnector();
                                synchronized (connector) {
                                    connector.setUseProxySecurity(false);
                                    connector.interruptConnector();
                                    connector.notify();
                                }
                                SAWTerminal.print("\rSAW>SAWPROXYSECURITY:Server proxy security set to: Disabled\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYSECURITY:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYUSER")) {
                        if (splitCommand.length == 1) {
                            String proxyUser = server.getServerConnector().getProxyUser();
                            SAWTerminal.print("\rSAW>SAWPROXYUSER:Server proxy user: '" + proxyUser + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            String proxyUser = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setProxyUser(proxyUser);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWPROXYUSER:Server proxy user set to: '" + proxyUser + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYUSER:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROXYPASSWORD")) {
                        if (splitCommand.length == 1) {
                            String proxyPassword = server.getServerConnector().getProxyPassword();
                            SAWTerminal.print("\rSAW>SAWPROXYPASSWORD:Server proxy password: '" + proxyPassword + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            String proxyPassword = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setProxyPassword(proxyPassword);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWPROXYPASSWORD:Server proxy password set to: '" + proxyPassword + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWPROXYPASSWORD:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENCRYPTIONTYPE")) {
                        if (splitCommand.length == 1) {
                            String encryptionType = server.getServerConnector().getEncryptionType();
                            if (encryptionType.toUpperCase().startsWith("R")) {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type: RC4\nSAW>");
                            } else if (encryptionType.toUpperCase().startsWith("A")) {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type: AES\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type: Disabled\nSAW>");
                            }
                        } else if (splitCommand.length == 2) {
                            String encryptionType = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setEncryptionType(encryptionType);
                                connector.interruptConnector();
                                connector.notify();
                            }
                            if (encryptionType.toUpperCase().startsWith("R")) {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type set to: RC4\nSAW>");
                            } else if (encryptionType.toUpperCase().startsWith("A")) {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type set to: AES\nSAW>");
                            } else {
                                SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Server encryption type set to: Disabled\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWENCRYPTIONTYPE:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENCRYPTIONPASSPHRASE")) {
                        if (splitCommand.length == 1) {
                            String passPhrase = "";
                            if (server.getServerConnector().getEncryptionKey() != null) {
                                passPhrase = new String(server.getServerConnector().getEncryptionKey(), "UTF-8");
                            }
                            SAWTerminal.print("\rSAW>SAWENCRYPTIONPASSPHRASE:Server encryption passphrase: '" + passPhrase + "'\nSAW>");
                        } else if (splitCommand.length == 2) {
                            String passPhrase = splitCommand[1];
                            SAWServerConnector connector = server.getServerConnector();
                            synchronized (connector) {
                                connector.setEncryptionKey(passPhrase.getBytes("UTF-8"));
                                connector.interruptConnector();
                                connector.notify();
                            }
                            SAWTerminal.print("\rSAW>SAWENCRYPTIONPASSPHRASE:Server encryption passphrase set to: '" + passPhrase + "'\nSAW>");
                        } else {
                            SAWTerminal.print("\rSAW>SAWENCRYPTIONPASSPHRASE:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWDISCONNECT")) {
                        if (splitCommand.length == 1) {
                            List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                            synchronized (connections) {
                                if (connections.size() > 0) {
                                    SAWTerminal.print("\rSAW>SAWDISCONNECT:Disconnecting all clients from server...\nSAW>");
                                    for (SAWServerConnectionHandler connectionHandler : connections) {
                                        connectionHandler.getConnection().closeSockets();
                                    }
                                    SAWTerminal.print("\rSAW>SAWDISCONNECT:Disconnected all clients from server!\nSAW>");
                                } else {
                                    SAWTerminal.print("\rSAW>SAWDISCONNECT:Not connected with clients!\nSAW>");
                                }
                            }
                        } else if (splitCommand.length == 2) {
                            try {
                                int order = Integer.parseInt(splitCommand[1]);
                                if (order >= 0) {
                                    List<SAWServerConnectionHandler> connections = server.getServerConnector().getConnectionHandlers();
                                    synchronized (connections) {
                                        if (connections.size() > 0) {
                                            if (connections.size() >= order) {
                                                SAWTerminal.print("\rSAW>SAWDISCONNECT:Disconnecting client of order number '" + order + "' from server...\nSAW>");
                                                connections.get(order).getConnection().closeSockets();
                                                SAWTerminal.print("\rSAW>SAWDISCONNECT:Disconnected client of order number '" + order + "' from server!\nSAW>");
                                            } else {
                                                SAWTerminal.print("\rSAW>SAWDISCONNECT:Invalid client order number!\nSAW>");
                                            }
                                        } else {
                                            SAWTerminal.print("\rSAW>SAWDISCONNECT:Not connected with clients!\nSAW>");
                                        }
                                    }
                                } else {
                                    SAWTerminal.print("\rSAW>SAWDISCONNECT:Syntax error detected!\nSAW>");
                                }
                            } catch (NumberFormatException e) {
                                SAWTerminal.print("\rSAW>SAWDISCONNECT:Syntax error detected!\nSAW>");
                            }
                        } else {
                            SAWTerminal.print("\rSAW>SAWDISCONNECT:Syntax error detected!\nSAW>");
                        }
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWSETTINGSLIST")) {
                        message.setLength(0);
                        int sessionsLimit = server.getServerConnector().getSessionsLimit();
                        String hostAddress = server.getServerConnector().getAddress();
                        Integer port = server.getServerConnector().getPort();
                        String proxyType = server.getServerConnector().getProxyType();
                        String proxyAddress = server.getServerConnector().getProxyAddress();
                        Integer proxyPort = server.getServerConnector().getProxyPort();
                        String proxyUser = server.getServerConnector().getProxyUser();
                        String proxyPassword = server.getServerConnector().getProxyPassword();
                        String encryptionType = server.getServerConnector().getEncryptionType();
                        String passPhrase = "";
                        if (server.getServerConnector().getEncryptionKey() != null) {
                            passPhrase = new String(server.getServerConnector().getEncryptionKey(), "UTF-8");
                        }
                        message.append("\rSAW>SAWSETTINGSLIST:List of settings on server:\nSAW>");
                        message.append("\nSAW>SAWSETTINGSLIST:Server sessions limit: '" + sessionsLimit + "'");
                        if (server.getServerConnector().isPassive()) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server connection mode: Passive");
                        } else {
                            message.append("\nSAW>SAWSETTINGSLIST:Server connection mode: Active");
                        }
                        message.append("\nSAW>SAWSETTINGSLIST:Server target host address: '" + hostAddress + "'");
                        message.append("\nSAW>SAWSETTINGSLIST:Server target host port: '" + port + "'");
                        if (proxyType.toUpperCase().startsWith("H")) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy type: HTTP");
                        } else if (proxyType.toUpperCase().startsWith("S")) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy type: SOCKS");
                        } else {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy type: Disabled");
                        }
                        message.append("\nSAW>SAWSETTINGSLIST:Server proxy host address: '" + proxyAddress + "'");
                        if (proxyPort != null) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy host port: '" + proxyPort + "'");
                        } else {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy host port: ''");
                        }
                        if (server.getServerConnector().isUseProxySecurity()) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy security: Enabled");
                        } else {
                            message.append("\nSAW>SAWSETTINGSLIST:Server proxy security: Disabled");
                        }
                        message.append("\nSAW>SAWSETTINGSLIST:Server proxy user: '" + proxyUser + "'");
                        message.append("\nSAW>SAWSETTINGSLIST:Server proxy password: '" + proxyPassword + "'");
                        if (encryptionType.toUpperCase().startsWith("R")) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server encryption type: RC4");
                        } else if (encryptionType.toUpperCase().startsWith("A")) {
                            message.append("\nSAW>SAWSETTINGSLIST:Server encryption type: AES");
                        } else {
                            message.append("\nSAW>SAWSETTINGSLIST:Server encryption type: Disabled");
                        }
                        message.append("\nSAW>SAWSETTINGSLIST:Server encryption passphrase: '" + passPhrase + "'");
                        message.append("\nSAW>\nSAW>SAWSETTINGSLIST:End of list of settings on server\nSAW>");
                        SAWTerminal.print(message.toString());
                        message.setLength(0);
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENVIRONMENTSBACKUP")) {
                        SAWNativeUtils.backupvirtualenvs();
                        SAWTerminal.print("\rSAW>SAWENVIRONMENTSBACKUP:Backed up all environment variables on server!\nSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWENVIRONMENTSRESTORE")) {
                        SAWNativeUtils.restorevirtualenvs();
                        SAWTerminal.print("\rSAW>SAWENVIRONMENTSRESTORE:Restored all environment variables on server!\nSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROPERTIESBACKUP")) {
                        SAWNativeUtils.backupsystemproperties();
                        SAWTerminal.print("\rSAW>SAWPROPERTIESBACKUP:Backed up all JVM properties on server!\nSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWPROPERTIESRESTORE")) {
                        SAWNativeUtils.restoresystemproperties();
                        SAWTerminal.print("\rSAW>SAWPROPERTIESRESTORE:Restored all JVM properties on server!\nSAW>");
                    } else if (splitCommand[0].equalsIgnoreCase("*SAWTIME")) {
                        clock.setTime(Calendar.getInstance().getTime());
                        SAWTerminal.print("\rSAW>SAWTIME:Date/time ([Y-MM-DD-ER][HH:MM:SS:MS-TZ]) on server:\nSAW>[" + clock.get(GregorianCalendar.YEAR) + "-" + dateTimeFormat.format(clock.getTime()) + "\nSAW>");
                    } else {
                        SAWTerminal.print("\rSAW>");
                    }
                } else {
                    System.exit(0);
                }
            } catch (Exception e) {
                SAWTerminal.print("\rSAW>SAWCONSOLE:Error detected when processing command!\nSAW>");
            }
        }
    }
}
