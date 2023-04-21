package com.meisenberger.stealthnet.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.SessionHandler;
import org.tanukisoftware.wrapper.WrapperManager;
import com.meisenberger.stealthnet.servlet.DownloadServlet;
import com.meisenberger.stealthnet.servlet.MyHandler;
import com.meisenberger.utils.Timeout;
import com.meisenberger.utils.UpdateCheck;

public class StealthnetServer extends ConsoleWrapper {

    private static Logger log = Logger.getLogger(StealthnetServer.class.getName());

    private static int PORT = 8765;

    public static int KILL_TIMEOUT = 120;

    public static boolean AUTO_CONNECT = true;

    public static boolean STEALTHNET_IS_RUNNING = false;

    public static String confDirName = "conf", CMD_MONO = null, SHELL_SERVER_CMD_START_LINUX_SHELL = null, SHELL_SERVER_CMD_START_LINUX_SCRIPT = null, SHELL_SERVER_CMD_START_WINDOWS = null, LOG_FILE = null;

    public static String settingsDIR = "conf";

    public static URL[] confSearchPath = null;

    public static String settingsName = "settings";

    public static String WORKING_DIR = null;

    public static String PASSWORD = null;

    public static String HOSTS = null;

    public static boolean isWindows = false;

    public static int CMD_WAIT_TIME = 200, CMD_FLUSH_WAIT_TIME = 100;

    public static void main(String[] args) {
        try {
            startupStealthnetWebUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startupStealthnetWebUI() throws Exception {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().indexOf("win") == 0) isWindows = true; else isWindows = false;
        System.out.println("OS:" + os + "; windows detected:" + isWindows);
        Runtime.getRuntime().addShutdownHook(new Shutdown());
        UpdateCheck.startUpdateCheck();
        startTheWholeThing();
    }

    public static void doStopTheWholeServer() {
        new Thread() {

            public void run() {
                boolean wasRunning = STEALTHNET_IS_RUNNING;
                STEALTHNET_IS_RUNNING = false;
                try {
                    Thread.sleep(500);
                    if (server != null) {
                        server.stop();
                        server.destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                server = null;
                stopStealthNet();
                WrapperManager.stopAndReturn(0);
            }
        }.start();
    }

    /**
	 * startet alles neu in einem background thread
	 *
	 */
    public static void reStartTheWholeThing() {
        new Thread() {

            public void run() {
                boolean wasRunning = STEALTHNET_IS_RUNNING;
                STEALTHNET_IS_RUNNING = false;
                try {
                    Thread.sleep(500);
                    if (server != null) {
                        server.stop();
                        server.destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                server = null;
                stopStealthNet();
                WrapperManager.restartAndReturn();
                if (true) return;
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (wasRunning && !STEALTHNET_IS_RUNNING) startStealthNet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static ResourceBundle bundle;

    /**
	 * startet alles
	 *
	 */
    public static void startTheWholeThing() throws Exception {
        String sc = System.getProperty("settingsDIR");
        if (!(sc == null || sc.length() <= 0)) settingsDIR = sc;
        if (settingsDIR.charAt(settingsDIR.length() - 1) != File.separatorChar) settingsDIR += File.separatorChar;
        String filepre = "file://";
        if (isWindows) filepre += "/";
        confSearchPath = new URL[] { new URL(filepre + settingsDIR), new URL(filepre + sc), new URL(filepre + System.getProperty("user.dir") + File.separatorChar + confDirName + File.separatorChar), new URL(filepre + System.getProperty("user.dir") + File.separatorChar) };
        System.out.println(settingsDIR);
        for (URL u : confSearchPath) {
            System.out.println(u);
        }
        bundle = (ResourceBundle) ResourceBundle.getBundle(settingsName, new Locale("en"), new URLClassLoader(confSearchPath));
        PORT = Integer.parseInt(bundle.getString("PORT"));
        CMD_WAIT_TIME = Integer.parseInt(bundle.getString("CMD_WAIT_TIME"));
        CMD_FLUSH_WAIT_TIME = Integer.parseInt(bundle.getString("CMD_FLUSH_WAIT_TIME"));
        try {
            KILL_TIMEOUT = Integer.parseInt(bundle.getString("KILL_TIMEOUT"));
        } catch (Exception e) {
        }
        CMD_MONO = bundle.getString("SHELL_SERVER_CMD_START_MONO");
        SHELL_SERVER_CMD_START_LINUX_SHELL = bundle.getString("SHELL_SERVER_CMD_START_LINUX_SHELL");
        SHELL_SERVER_CMD_START_LINUX_SCRIPT = bundle.getString("SHELL_SERVER_CMD_START_LINUX_SCRIPT");
        SHELL_SERVER_CMD_START_WINDOWS = bundle.getString("SHELL_SERVER_CMD_START_WINDOWS");
        LOG_FILE = bundle.getString("LOG_FILE");
        WORKING_DIR = bundle.getString("WORKING_DIR");
        PASSWORD = bundle.getString("PASSWORD");
        HOSTS = bundle.getString("HOSTS");
        AUTO_CONNECT = HelperStd.parseBoolean(bundle.getString("AUTO_CONNECT"), AUTO_CONNECT);
        if (AUTO_CONNECT) startStealthNet();
        doStartServletContainer();
    }

    public static void stopStealthNet() {
        STEALTHNET_IS_RUNNING = false;
        System.out.println("sending exit command to StealthNet");
        doCustomCommand("exit");
        Timeout timeout = new Timeout(process);
        timeout.sleep = KILL_TIMEOUT;
        timeout.start();
        try {
            process.child.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeout.running = false;
        System.out.println("stoping StealthNet shell");
        process.stop();
    }

    public static void startStealthNet() throws Exception {
        STEALTHNET_IS_RUNNING = false;
        System.out.println("Starting StealthNet:");
        File workingDir = new File(WORKING_DIR);
        if (!workingDir.exists() || !workingDir.isDirectory()) throw new Exception("Invalid working directory, change the property WORKING_DIR in the conf/settings.properties file!");
        String output = "";
        try {
            if (isWindows) {
                System.out.println("Starting up:" + "\"" + workingDir.getAbsolutePath() + File.separatorChar + SHELL_SERVER_CMD_START_WINDOWS + "\"; in working dir:" + workingDir);
                process = ProcessHelper.doShellProcess("\"" + workingDir.getAbsolutePath() + File.separatorChar + SHELL_SERVER_CMD_START_WINDOWS + "\"", null, workingDir, true);
            } else {
                System.out.println("Starting up:" + CMD_MONO + ";in working dir:" + workingDir);
                process = ProcessHelper.doShellProcess(CMD_MONO, null, workingDir, true);
            }
            Thread.sleep(2000);
            output = process.readFullOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (output.length() <= 0 || output.indexOf("ealth") < 0) {
            System.out.println("first start up failed!");
            if (process != null) process.child.destroy();
            if (!isWindows) {
                System.out.println("trying other start-up:" + SHELL_SERVER_CMD_START_LINUX_SHELL + " " + SHELL_SERVER_CMD_START_LINUX_SCRIPT + ";in working dir:" + workingDir);
                process = ProcessHelper.doShellProcess(SHELL_SERVER_CMD_START_LINUX_SHELL + " " + SHELL_SERVER_CMD_START_LINUX_SCRIPT, null, workingDir, true);
                Thread.sleep(2000);
                output = process.readFullOutput();
            } else {
                System.out.println("trying start-up with:" + SHELL_SERVER_CMD_START_WINDOWS + "; in working dir:" + workingDir);
                process = ProcessHelper.doShellProcess(SHELL_SERVER_CMD_START_WINDOWS, null, workingDir, true);
                Thread.sleep(2000);
                output = process.readFullOutput();
            }
        }
        if (output.length() <= 0 || output.indexOf("ealth") < 0) {
            throw new Exception("StealthNet could not be started!\nOutput:" + output + ";");
        }
        System.out.println(output);
        if (HelperStd.isEmpty(FINISHED_DOWNLOAD_FOLDER)) FINISHED_DOWNLOAD_FOLDER = getSettingsValue("IncomingDirectory");
        System.out.println(FINISHED_DOWNLOAD_FOLDER);
        STEALTHNET_IS_RUNNING = true;
    }

    public static Server server;

    /**
	 * starts the Jetty web server and blocks the main thread
	 * @throws Exception
	 */
    public static void doStartServletContainer() {
        try {
            server = new Server(PORT);
            MyHandler contexts = new MyHandler(HOSTS.split(";"), PASSWORD, server);
            server.setHandler(contexts);
            Context root = new Context(contexts, "/", Context.REQUEST);
            root.addServlet(new ServletHolder(new DownloadServlet()), "/bin/");
            String[] contextR = { "/index.jsp", "/", "/index.html", "/index.htm", "/index.php", "/index" };
            for (String c : contextR) {
                root.addServlet(new ServletHolder(new org.apache.jsp.index_jsp()), c);
            }
            root.addServlet(new ServletHolder(new org.apache.jsp.downloads_jsp()), "/downloads.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.search_jsp()), "/search.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.uploads_jsp()), "/uploads.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.finished_jsp()), "/finished.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.settings_jsp()), "/settings.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.sharedfiles_jsp()), "/sharedfiles.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.log_jsp()), "/log.jsp");
            root.addServlet(new ServletHolder(new org.apache.jsp.collection_jsp()), "/collection.jsp");
            server.start();
        } catch (Exception e) {
            log.log(Level.SEVERE, "error in doStartServletContainer() method", e);
            e.printStackTrace();
        }
    }

    public static String getWebUILog() {
        try {
            File workingDir = new File(WORKING_DIR);
            File log = new File(workingDir, LOG_FILE);
            FileInputStream in = new FileInputStream(log);
            byte[] b = new byte[(int) log.length()];
            in.read(b);
            in.close();
            return new String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error reading log file:" + e.getLocalizedMessage();
        }
    }
}
