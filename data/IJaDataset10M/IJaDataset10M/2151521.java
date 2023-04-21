package com.aol.services;

import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ApplicationManager {

    private static HashMap apps = new HashMap();

    private static Application defaultApp = null;

    private static String appWorkDir = null;

    private static Logger logger = null;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: ApplicationManager <config file dir> <config file name> {<wurfl file>}");
            System.exit(1);
        }
        java.util.Properties p = new java.util.Properties();
        p.setProperty("log4j.logger.am", "INFO, AM");
        p.setProperty("log4j.appender.AM", "org.apache.log4j.ConsoleAppender");
        p.setProperty("log4j.appender.AM.layout", "org.apache.log4j.PatternLayout");
        org.apache.log4j.PropertyConfigurator.configure(p);
        if (args.length > 2) net.sourceforge.wurfl.wurflapi.ObjectsManager.initFromWebApplication(args[2]);
        String loggerName = "am";
        String configFileDir = args[0];
        String appConfigFile = args[1];
        int status = ApplicationManager.init(configFileDir, appConfigFile, loggerName);
        System.exit(status);
    }

    public static int init(String configFileDir, String configFile, String loggerName) {
        int status = 0;
        try {
            logger = Logger.getLogger(loggerName);
            System.setProperty("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeParserConfiguration");
            appWorkDir = configFileDir + "/work";
            String workFiles[] = new java.io.File(appWorkDir).list();
            for (int wf = 0; workFiles != null && wf < workFiles.length; wf++) new java.io.File(appWorkDir, workFiles[wf]).delete();
            Iterator i = parseConfigFile(configFileDir + "/" + configFile);
            while (i.hasNext()) {
                HashMap map = (HashMap) i.next();
                if (!map.containsKey("configDir")) map.put("configDir", configFileDir);
                Application app = Application.compileInstance(map, appWorkDir, loggerName);
                if (app != null) {
                    apps.put(app.getName(), app);
                    if (map.containsKey("defaultApp")) {
                        if (Boolean.valueOf((String) map.get("defaultApp")).booleanValue()) defaultApp = app;
                    }
                    logger.info("Loaded application " + app.toString() + " [" + app.getClass() + "]");
                } else {
                    logger.warn("Error loading application '" + map.get("name") + "'!");
                    status = 1;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            status = 1;
        }
        return status;
    }

    public static Application getApplication(final String appName) {
        if (appName == null) return defaultApp; else return (Application) apps.get(appName);
    }

    public static void destroy() {
        defaultApp = null;
        Iterator i = apps.keySet().iterator();
        while (i.hasNext()) {
            Application app = (Application) apps.get(i.next());
            app.destroy();
            logger.info("Destroyed application " + app.getName());
            app = null;
        }
        apps = null;
        logger = null;
    }

    private static void set(String name, String value) {
    }

    private static Iterator parseConfigFile(String configFile) {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            XMLHandler handler = new XMLHandler();
            xr.setContentHandler(handler);
            xr.parse(new InputSource(configFile));
            return handler.iterator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final class XMLHandler extends DefaultHandler {

        private String objTag = "application";

        private LinkedList appList = new LinkedList();

        private HashMap appProps = null;

        private StringBuffer contents = new StringBuffer();

        public XMLHandler() {
        }

        public XMLHandler(String objTag) {
            this.objTag = objTag;
        }

        public void startElement(String ns, String localName, String qName, Attributes attr) throws SAXException {
            contents.delete(0, contents.length());
            if ("application".equals(localName)) {
                appProps = new HashMap();
                appList.add(appProps);
            }
        }

        public void endElement(String ns, String localName, String qName) throws SAXException {
            if ("application".equals(localName)) appProps = null; else if (appProps != null) appProps.put(localName, contents.toString()); else {
                ApplicationManager.set(localName, contents.toString());
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            contents.append(ch, start, length);
        }

        public Iterator iterator() {
            return appList.iterator();
        }
    }
}
