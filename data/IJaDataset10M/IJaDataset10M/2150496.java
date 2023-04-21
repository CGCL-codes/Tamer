package org.breport.breport;

import java.io.*;
import java.util.HashMap;
import java.util.Stack;
import org.w3c.dom.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.log4j.Logger;

/**
 * Configuration loader
 * @author Scott Peshak (scott@sourceallies.com)
 */
public class Config {

    /**
	 * Instance variable used to implement the singleton
	 */
    private static Config _instance = null;

    /**
	 * HashMap of Catalog objects
	 */
    protected HashMap<String, Catalog> catalogs = new HashMap<String, Catalog>();

    /**
	 * Flag of whether or not to include instrumentation statistics in output email.
	 */
    protected boolean includeStats = false;

    /**
	 * Search path for reports
	 */
    protected Stack<String> reportSearchPath = new Stack<String>();

    /**
	 * Name of report to run
	 */
    protected String reportName;

    /**
	 * Catalog name requested.
	 */
    protected String catalogName;

    /**
	 * Email configuration
	 */
    protected EmailConfig emailConfig = new EmailConfig();

    /**
	 * Logger object
	 */
    private static Logger logger = Logger.getLogger("org.breport.breport.Config");

    /**
	 * Regex that defines a "true" string
	 */
    protected static final String truePattern = "^(true|yes|on)$";

    /**
	 * Regex that defines a "false" string
	 */
    protected static final String falsePattern = "^(false|no|off)$";

    /**
	 * Construct a new config from a filename
	 * @param filename Filename of config file to load.
	 */
    protected Config(String filename) {
        this.parseConf(filename);
    }

    /**
	 * Get an instance of config
	 */
    public static Config getInstance() throws IOException {
        if (null == _instance) {
            _instance = new Config(AppInfo.getInstance().getConfigFile());
        }
        return _instance;
    }

    /**
	 * Parse the config file
	 */
    private void parseConf(String filename) {
        logger.info("Parsing config from file: '" + filename + "'");
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(filename));
            doc.getDocumentElement().normalize();
            NodeList catList = doc.getElementsByTagName("catalog");
            for (int i = 0; i < catList.getLength(); i++) {
                Catalog cat = new Catalog(catList.item(i));
                this.catalogs.put(cat.getName(), cat);
            }
            NodeList emailList = doc.getElementsByTagName("mail");
            if (emailList.getLength() > 0) this.emailConfig.loadXml((Element) emailList.item(0));
            this.includeStats = this.checkBoolTag(doc.getDocumentElement(), "includeInstrumentation", false);
            NodeList searchPathList = doc.getElementsByTagName("rptSearchPath");
            if (searchPathList.getLength() > 0) {
                for (int i = 0; i < searchPathList.getLength(); i++) {
                    Node pathNode = searchPathList.item(i);
                    if (pathNode.getNodeType() != Node.ELEMENT_NODE) continue;
                    NamedNodeMap attrs = pathNode.getAttributes();
                    this.addReportPath(attrs.getNamedItem("path").getNodeValue());
                }
            }
        } catch (SAXParseException e) {
            System.out.println("Parse error: " + e.getSystemId() + ":" + e.getLineNumber());
            System.out.println("\t" + e.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
	 * Add search path to the list for reports
	 * @param path Path to add
	 */
    public void addReportPath(String path) {
        if (path == null || path.equals("")) return;
        if (!path.endsWith("/")) path = path + "/";
        this.reportSearchPath.push(path);
    }

    /**
	 * Get a catalog object by name
	 * @param name Catalog name to retreve
	 */
    public Catalog getNamedCatalog(String name) throws CatalogNotFoundException {
        Catalog cat = (Catalog) this.catalogs.get(name);
        if (cat == null) {
            throw new CatalogNotFoundException(name);
        }
        return cat;
    }

    /**
	 * Get the catalog named in catalogName
	 */
    public Catalog getCatalog() throws CatalogNotFoundException {
        return this.getNamedCatalog(this.getCatalogName());
    }

    /**
	 * Get the email configuration.
	 */
    public EmailConfig getEmailConfig() {
        return this.emailConfig;
    }

    /**
	 * Check if the configuration requests that the instrumentation data to be
	 * added to the outgoing email.
	 */
    public boolean includeInstrumentation() {
        return this.includeStats;
    }

    /**
	 * Get the configured search path for reports
	 */
    public Stack<String> getReportSearchPath() {
        return this.reportSearchPath;
    }

    /**
	 * Get the name of the report that is requested.
	 */
    public String getReportName() {
        return this.reportName;
    }

    /**
	 * Set the report name
	 */
    public void setReportName(String name) {
        this.reportName = name;
    }

    /**
	 * Get the name of the catalog that is requested.
	 */
    public String getCatalogName() {
        return this.catalogName;
    }

    /**
	 * Set the catalog name
	 */
    public void setCatalogName(String name) {
        this.catalogName = name;
    }

    /**
	 * Check the value of a "boolean" tag.
	 * A boolean tag has to have a attribute called "value"
	 * @param elem Element to check under
	 * @param name Name of tag to check
	 * @param defaultValue Value to return if the tag isn't found
	 */
    protected boolean checkBoolTag(Element elem, String name, boolean defaultValue) {
        NodeList list = elem.getElementsByTagName(name);
        if (list.getLength() != 1) return defaultValue;
        Node node = list.item(0);
        if (node.getNodeType() != Node.ELEMENT_NODE) return defaultValue;
        NamedNodeMap attrs = node.getAttributes();
        if (attrs.getNamedItem("value") == null) return defaultValue;
        String value = attrs.getNamedItem("value").getNodeValue();
        if (value.matches(Config.truePattern)) return true; else if (value.matches(Config.falsePattern)) return false; else return defaultValue;
    }
}
