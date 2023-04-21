package net.assimilator.examples.sca.web.tomcat.installer;

import net.assimilator.examples.sca.web.tomcat.TomcatSCA;
import net.assimilator.substrates.sca.utils.EnvironmentVariables;
import net.assimilator.substrates.sca.utils.FileEditor;
import java.io.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Completes configuration of the Tomcat server.
 *
 * @author Kevin Hartig
 * @version $Id: Configuration.java 174 2007-07-25 21:26:10Z mtm $
 */
public class Configuration implements Serializable {

    private String sep = null;

    private String userHome = null;

    private String javaHome = null;

    private String catalinaHome = null;

    private String installDir = null;

    private String ext = ".sh";

    private FileEditor fileEditor = new FileEditor();

    private String startScript = null;

    private String stopScript = null;

    private boolean windowsOS = false;

    private final Logger logger = Logger.getLogger("net.assimilator.examples.sca.web.tomcat");

    private static final long serialVersionUID = 5993256176739771021L;

    /**
     * Default constructor.
     *
     * @param installRoot         root directory name defined for the Tomcat installation.
     * @param tomcatDirectoryName Top level Tomcat directory name.
     */
    public Configuration(String installRoot, String tomcatDirectoryName) {
        sep = System.getProperty("file.separator");
        userHome = System.getProperty("user.home");
        javaHome = System.getProperty("java.home");
        catalinaHome = EnvironmentVariables.getProperty("CATALINA_HOME");
        String osName = System.getProperty("os.name").toLowerCase();
        if ((osName.indexOf("windows") > -1) || (osName.indexOf("nt") > -1)) {
            ext = "bat";
            windowsOS = true;
        }
        setInstallDirectory(installRoot, tomcatDirectoryName);
    }

    private void setInstallDirectory(String installRoot, String tomcatDirectoryName) {
        if (windowsOS) {
            userHome = fileEditor.checkForSpaces(userHome);
        }
        if (catalinaHome == null) {
            installDir = userHome + sep + ".assimilator" + sep + "external" + sep + installRoot + sep + tomcatDirectoryName;
        } else {
            installDir = catalinaHome;
        }
        logger.info("Installation directory = " + installDir);
    }

    public String getInstallDirectory() throws Exception {
        if (installDir == null) throw new Exception("Install directory name not set during initialization.");
        return installDir;
    }

    /**
     * Get the home directory for current user.
     *
     * @return the directory path of the user's home directory.
     */
    public String getUserHome() {
        return userHome;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public String getCatalinaHome() {
        return catalinaHome;
    }

    /**
     * Set the user name and role for the Tomcat manager needed to manage
     * the server.
     *
     * @param user     manager username.
     * @param password manager password.
     * @param role     manager role definition name.
     */
    public void addUser(String user, String password, String role) {
        try {
            String usersFile = installDir + sep + "conf" + sep + "tomcat-users.xml";
            FileEditor editor = new FileEditor();
            String[] strings = { "  <role rolename=\"" + role + "\"/>", "  <user username=\"" + user + "\" password=\"" + password + "\" roles=\"" + role + "\"/>" };
            editor.insertString(usersFile, "<tomcat-users>", strings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Tomcat server port number to use in the server.xml
     * configuration file.
     *
     * @param port Tomcat server port number.
     */
    public void setServerPort(String port) {
        try {
            String serverConfigFile = installDir + sep + "conf" + sep + "server.xml";
            FileEditor editor = new FileEditor();
            editor.replaceOccurrenceOfString(serverConfigFile, "<Server port=\"8005\"", "<Server port=\"" + port + "\" shutdown=\"SHUTDOWN\">", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Tomcat HTTP connector port number to use in the server.xml
     * configuration file.
     *
     * @param port HTTP connector port number.
     */
    public void setHTTPConnectorPort(String port) {
        try {
            String serverConfigFile = installDir + sep + "conf" + sep + "server.xml";
            FileEditor editor = new FileEditor();
            editor.replaceOccurrenceOfString(serverConfigFile, "<Connector port=\"8080\"", "    <Connector port=\"" + port + "\" protocol=\"HTTP/1.1\"", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Apache JServ Protocol connector port number to use in the server.xml
     * Tomcat configuration file.
     *
     * @param port AJP connector port number.
     */
    public void setAJPConnectorPort(String port) {
        try {
            String serverConfigFile = installDir + sep + "conf" + sep + "server.xml";
            FileEditor editor = new FileEditor();
            editor.replaceOccurrenceOfString(serverConfigFile, "<Connector port=\"8009\"", "    <Connector port=\"" + port + "\" protocol=\"AJP/1.3\" redirectPort=\"8443\" />", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Tomcat install directory name in the accesslog servlet
     * web.xml file.
     *
     * @param tomcatDirectoryName Top level Tomcat install directory name.
     */
    public void configureAccessLogMonitor(String tomcatDirectoryName) {
        try {
            String webxmlfilename = getInstallDirectory() + sep + "webapps" + sep + "watches" + sep + "WEB-INF" + sep + "web.xml";
            FileEditor editor = new FileEditor();
            editor.replaceOccurrenceOfString(webxmlfilename, "<param-value>jakarta-tomcat</param-value>", "         <param-value>" + tomcatDirectoryName + "</param-value>", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uncomment the tomcat clustering parameters and set values as necessary.
     */
    public void setClustering() {
        try {
            String serverConfigFile = installDir + sep + "conf" + sep + "server.xml";
            FileEditor editor = new FileEditor();
            editor.deleteLines(serverConfigFile, "<Cluster", -1, 1, true);
            editor.deleteLines(serverConfigFile, "<Cluster", 1, 1, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the startup and shutdown scripts for Tomcat. The script content
     * is dependent on the Tomcat installation location. An existing
     * installation should use the existing directory location and environment
     * variable definitions. A new installation defines the new variables in the
     * scripts.
     */
    public void createScripts() {
        File out;
        BufferedWriter writer = null;
        String newline = "\n";
        setStartScriptName();
        setStopScriptName();
        try {
            out = new File(startScript);
            writer = new BufferedWriter(new FileWriter(out));
            writer.write("@echo off\n");
            writer.write("if not defined CATALINA_HOME set CATALINA_HOME=" + installDir + newline);
            writer.write("if not defined JAVA_HOME set JAVA_HOME=" + javaHome + newline);
            writer.write("echo call %CATALINA_HOME%\\bin\\startup.bat\n");
            writer.write("call %CATALINA_HOME%\\bin\\startup.bat\n");
            writer.close();
            out = new File(stopScript);
            writer = new BufferedWriter(new FileWriter(out));
            writer.write("@echo off\n");
            writer.write("if not defined CATALINA_HOME set CATALINA_HOME=" + installDir + newline);
            writer.write("if not defined JAVA_HOME set JAVA_HOME=" + javaHome + newline);
            writer.write("echo call %CATALINA_HOME%\\bin\\shutdown.bat\n");
            writer.write("call %CATALINA_HOME%\\bin\\shutdown.bat\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Set the file name of the script used to start Tomcat.
     */
    private void setStartScriptName() {
        startScript = installDir + sep + "bin" + sep + "start_tomcat." + ext;
    }

    /**
     * Get the startup script file name for the envionment being used.
     *
     * @return The name of the startup script.
     */
    public String getStartScriptName() {
        if (startScript == null) setStartScriptName();
        return startScript;
    }

    /**
     * Set the file name of the script used to stop the Tomcat process.
     */
    private void setStopScriptName() {
        stopScript = installDir + sep + "bin" + sep + "stop_tomcat." + ext;
    }

    /**
     * Get the shutdown script file name for the currebt environment.
     *
     * @return The name of the shutdown script.
     */
    public String getStopScriptName() {
        if (stopScript == null) setStopScriptName();
        return stopScript;
    }

    /**
     * Remove the comments around the access log valve definition in the
     * default Tomcat server.xml configuration file.
     */
    public void turnOnAccessLogging() {
        try {
            String serverConfigFilename = getInstallDirectory() + sep + "conf" + sep + "server.xml";
            FileEditor editor = new FileEditor();
            editor.deleteLines(serverConfigFilename, "AccessLogValve", -1, 1, true);
            editor.deleteLines(serverConfigFilename, "AccessLogValve", 2, 1, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the Tomcat server for the current installation.
     * Update the rules file for the balancer servlet to reflect the current
     * state of Tomcat servers. Restart the servlet.
     *
     * @param service     Tomcat SCA service interface.
     * @param maxServices The maximum number of services to load balance.
     * @param addService  Defines whether to add or delete a service from the configuration file to be load balanced.
     * @return true if configuration succeeded, false otherwise.
     */
    public boolean configureLoadbalancer(TomcatSCA service, int maxServices, boolean addService) {
        String rulesFilename;
        try {
            rulesFilename = getInstallDirectory() + sep + "webapps" + sep + "balancer" + sep + "WEB-INF" + sep + "config" + sep + "rules.xml";
            if (addService) {
                String hostAddress = service.getHostAddress();
                String HTTPPort = service.getHTTPPort();
                logger.info("Configuring Load Balancer rules file by adding entry with address = " + hostAddress + ":" + HTTPPort);
                String[] replacementStrings = { "\n", "<rule className=\"net.assimilator.examples.sca.web.tomcat.balancer.RoundRobinRule\"", "    serverInstance=\"" + service.getClusterInstance() + "\"", "    maxServerInstances=\"" + maxServices + "\"", "    tcpListenAddress=\"" + hostAddress + "\"", "    tcpListenPort=\"4000\"", "    testWebPage=\"http://" + hostAddress + ":" + HTTPPort + "/clusterapp/test.jsp\"", "    redirectUrl=\"http://" + hostAddress + ":" + HTTPPort + "/clusterapp/sessiondata.jsp\" />" };
                fileEditor.insertString(rulesFilename, "<!--  Redirect to server instance based on RandomRedirectRule  -->", replacementStrings);
            } else {
                logger.info("Tomcat SCA service proxy object from lost connection = " + service);
                String hostAddress = service.toString();
                hostAddress = hostAddress.substring(hostAddress.indexOf("TcpEndpoint[") + 12, hostAddress.indexOf(":"));
                logger.info("HostAddress = " + hostAddress);
                logger.info("Configuring Load Balancer rules file by removing entry with address = " + hostAddress);
                fileEditor.deleteLines(rulesFilename, hostAddress, -3, 7, false);
                for (int i = 1; i < maxServices + 1; i++) {
                    fileEditor.replaceOccurrenceOfString(rulesFilename, "serverInstance=", "serverInstance=\"" + i + "\"", i);
                }
            }
            fileEditor.replaceAllStrings(rulesFilename, "maxServerInstances", "    maxServerInstances=\"" + maxServices + "\"");
            ReloadTask task = new ReloadTask();
            task.setUsername("manager");
            task.setPassword("manager");
            task.setPath("/balancer");
            task.execute();
            return true;
        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (TaskExecutionException tee) {
            tee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean configureClusterApp(TomcatSCA service) {
        try {
            String hostAddress = service.getHostAddress();
            String filename = new StringBuilder().append(getInstallDirectory()).append(sep).append("webapps").append(sep).append("clusterapp").append(sep).append("testLB.jsp").toString();
            return fileEditor.replaceString(filename, "localhost", "<meta http-equiv=\"refresh\" content=\"0; URL=http://" + hostAddress + ":8080/balancer/LoadBalancer\" />", false, 1);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
            e.printStackTrace();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e.getCause());
            e.printStackTrace();
        }
        return false;
    }
}
