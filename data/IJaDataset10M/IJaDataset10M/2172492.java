package net.sourceforge.xjftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import net.sourceforge.xjftp.protocol.handlers.ControlConnectionHandler;
import net.sourceforge.xjftp.utils.ConfigUtils;
import net.sourceforge.xjftp.utils.PortTracker;
import net.sourceforge.xjftp.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the main class for XJFTP. It creates the server socket and loops
 * forever accepting user connections and creating ServerPI threads to handle
 * them.
 * <p>
 * Copyright &copy; 2005 <a href="http://xjftp.sourceforge.net/">XJFTP Team</a>.
 * All rights reserved. Use is subject to <a href="http://xjftp.sourceforge.net/LICENSE.TXT">licence terms</a> (<a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License v2.0</a>)<br/>
 * <p>
 * Last modified: $Date: 2005/10/06 12:06:09 $, by $Author: mmcnamee $
 * <p>
 * @author Mark McNamee (<a href="mailto:mmcnamee@users.sourceforge.net">mmcnamee at users.sourceforge.net</a>)
 * @version $Revision: 1.10 $
 */
public class Server implements Configuration {

    /**
     * The default port this server binds to.
     */
    public static final int DEFAULT_PORT = 21;

    /**
     * The default data port (active transfers)
     */
    public static final int DEFAULT_DATA_PORT = 20;

    /**
     * Spring Bean Factory for loading services
     */
    private static ConfigurableBeanFactory beanFactory;

    private static final Log LOG = LogFactory.getLog(Server.class);

    /**
     * The port this server is listening on.
     */
    private int port = DEFAULT_PORT;

    private int dataPort = DEFAULT_DATA_PORT;

    private ServerSocket serverSocket;

    /**
     * Create an FTP server to run on the specified port.
     */
    public Server() throws Exception {
        super();
        configureServer();
        configureSpring();
    }

    public static void main(String[] args) throws Exception {
        String props = DEFAULT_FTPSVR_CONFIG;
        boolean overrideFile = false;
        if (args.length == 1) {
            props = System.getProperty("user.dir") + File.separator + args[0];
            overrideFile = true;
        } else if (args.length > 1) {
            sysout("**********************************************************************");
            sysout("  Usage: java net.sourceforge.xjftp.Server [properties file]          ");
            sysout("**********************************************************************");
            sysout("  By default the properties file inside the jar will be used!         ");
            System.exit(1);
        }
        if (overrideFile) {
            System.getProperties().load(new FileInputStream(props));
        } else {
            InputStream propertiesIn = Server.class.getResourceAsStream(props);
            if (propertiesIn == null) {
                throw new FileNotFoundException(props);
            }
            System.getProperties().load(propertiesIn);
        }
        Server server = new Server();
        server.startSocket();
        server.start();
    }

    public int getPort() {
        return this.port;
    }

    public int getDataPort() {
        return this.dataPort;
    }

    public static PortTracker getPortTracker() {
        return (PortTracker) beanFactory.getBean(PortTracker.class.getName());
    }

    public static ConfigurableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected void startSocket() throws Exception {
        try {
            this.serverSocket = new ServerSocket(getPort());
            sysout("Server Started and awaiting connections on port " + this.port);
        } catch (BindException e) {
            sysout("XJFTP Cannot bind to port " + getPort() + " on this computer. It is quite likely there is already an FTP server running, please stop it and try again...");
            throw e;
        }
    }

    /**
     * Starts the FTP server. This method listens for connections on the FTP
     * server port (usually port 21), and spawns a new thread to handle each
     * connection.
     */
    protected void start() throws Exception {
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            handleClient(clientSocket);
        }
    }

    protected void stop() throws Exception {
        this.serverSocket.close();
    }

    protected void handleClient(Socket clientSocket) throws IOException {
        StringBuffer sb = new StringBuffer("C:");
        sb.append(clientSocket.getInetAddress().getHostAddress());
        sb.append(':');
        sb.append(clientSocket.getPort());
        sb.append(" to me on:");
        sb.append(clientSocket.getLocalAddress().getHostAddress());
        sb.append(':');
        sb.append(clientSocket.getLocalPort());
        LOG.info("[Connection] " + sb.toString());
        ControlConnectionHandler pi = getControlConnectionHandler();
        pi.initialise(clientSocket);
        new Thread(pi).start();
    }

    protected ControlConnectionHandler getControlConnectionHandler() {
        return (ControlConnectionHandler) beanFactory.getBean(ControlConnectionHandler.class.getName());
    }

    public static int getSystemProperty(String prop, int defaultValue) {
        return ConfigUtils.getSystemProperty(prop, defaultValue);
    }

    public static boolean getSystemProperty(String prop, boolean defaultVal) {
        return ConfigUtils.getSystemProperty(prop, defaultVal);
    }

    /**
     * Configures the ftp server from the configuration properties.
     */
    protected synchronized void configureServer() {
        this.port = getSystemProperty(PROP_PORT, DEFAULT_PORT);
        this.dataPort = getSystemProperty(PROP_DATA_PORT, DEFAULT_DATA_PORT);
        String crlfOverride = System.getProperty(PROP_CRLF_OVERRIDE, "\r\n");
        sysout("Setting System.property for \"line.separator\" to be (HEX):" + StringUtils.toHex(crlfOverride));
        System.setProperty("line.separator", crlfOverride);
        String baseDir = System.getProperty(PROP_BASE_DIR, System.getProperty("user.dir"));
        boolean perUserHomeDir = getSystemProperty(PROP_USE_PER_USER_HOME, true);
        ServerConfig.setup(baseDir, perUserHomeDir);
    }

    protected synchronized void configureSpring() {
        String springConfigFiles[] = null;
        if (ConfigUtils.getSystemProperty(PROP_SPRING_CONFIG_OVERRIDE, false)) {
            String springConfig = System.getProperty(PROP_SPRING_CONFIG, DEFAULT_SPRING_CONFIG);
            springConfigFiles = new String[] { DEFAULT_SPRING_CONFIG, springConfig };
        } else {
            springConfigFiles = new String[] { DEFAULT_SPRING_CONFIG };
        }
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(springConfigFiles);
        beanFactory = ctx.getBeanFactory();
    }

    private static void sysout(String msg) {
        System.out.println(msg);
    }
}
