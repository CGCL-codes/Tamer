package com.googlecode.jwsm;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;
import org.jcommon.j2ee.*;
import org.jdom.*;
import org.jdom.input.*;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.config.QueryEvaluationMode;
import com.googlecode.jwsm.security.SecurityFactory;
import com.googlecode.jwsm.security.SecurityManager;
import com.googlecode.jwsm.security.basic.BasicSecurityFactory;
import com.googlecode.jwsm.security.bogus.*;
import contexthelp.*;

public class Uploader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String VERSION = "v1.0.2";

    public static Throwable initStackTrace;

    public static final HashMap progressCache = new HashMap();

    public static final HashMap updateCache = new HashMap();

    public static Preferences prefs;

    private static String defaultDirectory;

    private static String directoryString;

    public static File directory;

    public static SAXBuilder builder;

    private static DatabaseLogHandler handler;

    public static boolean initted;

    private static boolean threadRunning;

    private static String securityFactory;

    public static String getDirectoryString() {
        if (directoryString == null) return defaultDirectory;
        return directoryString;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initted = true;
        defaultDirectory = config.getServletContext().getInitParameter("deployments");
        String backgroundColor = config.getServletContext().getInitParameter("backgroundColor");
        if ((backgroundColor != null) && (backgroundColor.trim().length() > 0)) {
            CSSDefaults.BG_COLOR = backgroundColor;
        }
        securityFactory = config.getServletContext().getInitParameter("securityFactory");
        Thread t = new Thread() {

            public void run() {
                threadRunning = true;
                reinit(true);
                threadRunning = false;
            }
        };
        t.start();
    }

    public static synchronized void reinit(boolean firstTime) {
        if ((!firstTime) && (threadRunning)) return;
        initStackTrace = null;
        try {
            Thread.currentThread().setName("Initialization");
            prefs = Preferences.userRoot().node("/com/googlecode/jwsm");
            prefs.sync();
            builder = new SAXBuilder();
            long time = configureDeployments();
            ServiceManager.getInstance().stopAll();
            try {
                Class<?> factoryClass = Class.forName(securityFactory);
                SecurityManager.setSecurityFactory((SecurityFactory) factoryClass.newInstance());
                Log.get().log(Level.INFO, "Configured SecurityFactory instantiated: " + SecurityManager.getSecurityFactory().getClass());
            } catch (Throwable t) {
                Log.get().log(Level.WARNING, "Unable to load specified security factory: " + securityFactory + ", reverting to default security (BasicSecurityFactory).");
                SecurityManager.setSecurityFactory(new BasicSecurityFactory());
            }
            if ((directory == null) || (!directory.exists()) || (!directory.isDirectory())) {
                initStackTrace = new RuntimeException("Deployment directory does not exist! This must be configured.");
                return;
            }
            configureLogging();
            Log.info("Configured deployments directory (took " + time + "ms)...");
            if (!firstTime) Log.info("Configuration changed, restarting all services...");
            loadServices();
            Thread.currentThread().setName("Initialization");
            loadXSLTs();
            loadHelp();
            if (firstTime) {
                Log.info("Initialization completed successfully.");
            } else {
                Log.info("Reinitialization completed successfully.");
            }
        } catch (Throwable exc) {
            initStackTrace = exc;
        }
    }

    public static void loadServices() {
        File root = Uploader.directory;
        File[] directories = root.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".bin")) {
                    pathname.delete();
                    return false;
                }
                if (pathname.isDirectory()) return true;
                return false;
            }
        });
        for (int i = 0; i < directories.length; i++) {
            if (!directories[i].getName().startsWith(".")) {
                loadService(directories[i]);
            }
        }
    }

    public static void loadXSLTs() {
        File root = new File(Uploader.directory, ".xslts");
        if (root.isDirectory()) {
            File[] files = root.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    if (pathname.getName().toLowerCase().endsWith(".config")) {
                        return true;
                    }
                    return false;
                }
            });
            for (int i = 0; i < files.length; i++) {
                try {
                    XSLT xslt = XSLT.load(files[i]);
                    if (xslt.isIncoming()) {
                        ServiceManager.getInstance().registerIncomingXSLT(xslt);
                    } else {
                        ServiceManager.getInstance().registerOutgoingXSLT(xslt);
                    }
                } catch (Exception exc) {
                    Log.get().throwing(Uploader.class.getName(), "loadXSLTs", exc);
                }
            }
        }
    }

    public static void loadHelp() throws ServletException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Uploader.class.getClassLoader().getResourceAsStream("resource/context_help.txt")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String key = line.substring(0, line.indexOf(';'));
                String value = line.substring(line.indexOf(';') + 1);
                HelpServlet.help.put(key, value);
            }
        } catch (IOException exc) {
            throw new ServletException(exc);
        }
    }

    public static File[] getWSARs() {
        if (directory == null) return new File[0];
        return directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().toLowerCase().endsWith(".wsar")) {
                    return true;
                }
                return false;
            }
        });
    }

    public static long configureDeployments() {
        Log.info("Initializing deployment directory: " + directoryString);
        long time = System.currentTimeMillis();
        directoryString = prefs.get("deploymentDirectory", defaultDirectory);
        if (directoryString != null) {
            directory = new File(directoryString);
            directory.mkdirs();
        }
        return System.currentTimeMillis() - time;
    }

    public static void configureLogging() {
        try {
            shutdownLogger();
            String serverName = prefs.get("serverName", "DefaultServer");
            boolean loggingEnabled = prefs.getBoolean("loggingEnabled", false);
            if (loggingEnabled) {
                Log.get().setLevel(Level.ALL);
                Log.info("Initializing database support...");
                Configuration config = Db4o.newConfiguration();
                config.queries().evaluationMode(QueryEvaluationMode.LAZY);
                ObjectContainer db = Db4o.openFile(config, directory.getCanonicalPath().replace('\\', '/') + "/.db");
                handler = new DatabaseLogHandler(serverName, db);
                handler.setLevel(Level.ALL);
                Log.get().addHandler(handler);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            initStackTrace = t;
        }
    }

    public static void loadService(File directory) {
        File configFile = new File(directory, "configuration.xml");
        if (!configFile.exists()) {
            Service service = new Service(directory.getName(), null, null, null, null, false, false, true);
            ServiceManager.getInstance().registerService(service);
            return;
        }
        Thread.currentThread().setName(directory.getName());
        try {
            Document document = builder.build(configFile);
            String mainClass = null;
            String defaultMethod = "";
            String instantiation = null;
            boolean instanceInstantiation = false;
            boolean debugMode = false;
            boolean interceptWSDL = true;
            ArrayList visibleMethods = new ArrayList();
            Iterator nodes = document.getRootElement().getDescendants();
            HashMap outgoingXSLTMappings = new HashMap();
            HashMap incomingXSLTMappings = new HashMap();
            while (nodes.hasNext()) {
                Content content = (Content) nodes.next();
                if (content instanceof Element) {
                    Element e = (Element) content;
                    if (e.getName().equals("main-class")) {
                        mainClass = e.getTextTrim();
                        if ((e.getAttribute("instanceInstantiation") != null) && (e.getAttributeValue("instanceInstantiation").trim().equals("true"))) {
                            instanceInstantiation = true;
                        }
                        if ((e.getAttribute("debug") != null) && (e.getAttributeValue("debug").trim().equals("true"))) {
                            debugMode = true;
                        }
                        if ((e.getAttribute("interceptWSDL") != null) && (e.getAttributeValue("interceptWSDL").trim().equals("false"))) {
                            interceptWSDL = false;
                        }
                        if (e.getAttribute("defaultMethod") != null) {
                            defaultMethod = e.getAttributeValue("defaultMethod");
                        }
                    } else if (e.getName().equals("visible-method")) {
                        visibleMethods.add(e.getTextTrim());
                        if (e.getAttributeValue("outgoingXSLT") != null) {
                            outgoingXSLTMappings.put(e.getTextTrim(), e.getAttributeValue("outgoingXSLT"));
                        }
                        if (e.getAttributeValue("incomingXSLT") != null) {
                            incomingXSLTMappings.put(e.getTextTrim(), e.getAttributeValue("incomingXSLT"));
                        }
                    } else if (e.getName().equals("instantiation")) {
                        instantiation = e.getTextTrim();
                    }
                }
            }
            if (mainClass != null) {
                Service service = new Service(directory.getName(), mainClass, instantiation, defaultMethod, (String[]) visibleMethods.toArray(new String[visibleMethods.size()]), instanceInstantiation, debugMode, interceptWSDL);
                service.setOutgoingXSLTMappings(outgoingXSLTMappings);
                service.setIncomingXSLTMappings(incomingXSLTMappings);
                ServiceManager.getInstance().registerService(service);
                service.start();
                Log.info("Service: " + service.getName() + " successfully registered and started.");
            } else {
                System.err.println("Unable to find main-class for " + directory.getName());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            Log.get().log(Level.SEVERE, "Exception during initialization of service", t);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Thread.currentThread().setName("Uploader");
        int currentPlace = 0;
        int contentLength = request.getContentLength();
        if (request.getMethod().equals("POST")) {
            progressCache.put(request.getSession(), "0");
            MultipartReader reader = new MultipartReader(request.getInputStream());
            PartInputStream part;
            while ((part = reader.nextPart()) != null) {
                if ((part.isFile()) && (part.getFilename().trim().length() > 0)) {
                    List updateList;
                    if (updateCache.get(request.getSession()) == null) {
                        updateList = Collections.synchronizedList(new ArrayList());
                        updateCache.put(request.getSession(), updateList);
                    } else {
                        updateList = (List) updateCache.get(request.getSession());
                    }
                    updateList.add("Uploading " + part.getFilenameShort());
                    if ((!directory.exists()) || (!directory.isDirectory())) {
                        directory.mkdirs();
                    }
                    File file = File.createTempFile("file", ".bin", directory);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] b = new byte[512];
                    int len;
                    int previousPercentage = -1;
                    int percentage;
                    while ((len = part.read(b)) > -1) {
                        percentage = (int) (((float) currentPlace / (float) contentLength) * 100);
                        progressCache.put(request.getSession(), String.valueOf(percentage));
                        if (percentage % 10 == 0) {
                            if (previousPercentage != percentage) {
                                previousPercentage = percentage;
                            }
                        }
                        fos.write(b, 0, len);
                        currentPlace += len;
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException exc) {
                        }
                    }
                    updateList.add(new String[] { part.getName().substring(3), part.getFilenameShort(), file.getName() });
                    fos.flush();
                    fos.close();
                }
                part.close();
            }
            response.setContentType("text/html");
            progressCache.put(request.getSession(), "Finished");
        } else if ("progress".equals(request.getParameter("request"))) {
            PrintWriter writer = response.getWriter();
            List updateList = (List) updateCache.get(request.getSession());
            while ((updateList != null) && (updateList.size() > 0)) {
                if (updateList.get(0) instanceof String[]) {
                    String[] parsed = (String[]) updateList.get(0);
                    writer.println("top.finishedUpload('" + parsed[0] + "', '" + parsed[1] + "', '" + parsed[2] + "');");
                } else {
                    writer.println("top.updateUploadText('" + updateList.get(0) + "');");
                }
                updateList.remove(0);
            }
            if (!((String) progressCache.get(request.getSession())).equals("Finished")) {
                writer.println("top.updateProgress('" + progressCache.get(request.getSession()) + "');");
                writer.println("top.setTimeout('updateUploads()', 200);");
            } else {
                writer.println("top.updateProgress('100');");
            }
            writer.flush();
            writer.close();
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
        shutdownLogger();
    }

    public static final void shutdownLogger() {
        if (handler != null) {
            handler.close();
            Log.get().removeHandler(handler);
            handler = null;
        }
    }
}
