package com.jcorporate.expresso.kernel.digester;

import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Digester class representing the entire expresso-services.xml file
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class ExpressoServicesConfig implements java.io.Serializable {

    /**
     * This constant tells the limit to which we allow nesting of components.
     * Thanks to digester not being recursive, and not wanting to write our own
     * SAX parser, we use this kludge instead.
     */
    private static final int MAX_NESTING = 20;

    /**
     * The log4j Logger [which is initialized by the time we get here]
     */
    private Logger log = Logger.getLogger(ExpressoServicesConfig.class);

    /**
     * The name of the expresso config file name
     */
    private String fileName = null;

    /**
     * The URL of expresso config file
     */
    private URL fileURL = null;

    /**
     * The root of the Component configuration tree
     */
    private ComponentConfig root;

    /**
     * A utility class to set the configuration of the SAX Parser
     */
    SaxParserConfigurer saxParserConfig;

    /**
     * Default Constructor
     */
    public ExpressoServicesConfig() {
        saxParserConfig = new SaxParserConfigurer();
    }

    public static ExpressoServicesConfig newBlankContainer() {
        ExpressoServicesConfig esc = new ExpressoServicesConfig();
        esc.initBlankContainer();
        return esc;
    }

    public void initBlankContainer() {
        root = new ComponentConfig();
    }

    /**
     * Loads the ExpressoServices file.  When complete, call getRootConfig() to
     * get the results of the Digesting of the configuration file.
     */
    public void loadExpressoServices() {
        try {
            javax.xml.parsers.SAXParserFactory spf = javax.xml.parsers.SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser sp = spf.newSAXParser();
            Digester digester = new Digester(sp);
            URL url = this.getClass().getResource("/com/jcorporate/expresso/kernel/expresso-services_5_1.dtd");
            if (url != null) {
                digester.register("-//Jcorporate Ltd//DTD Expresso Services Configuration 5.1//EN", url.toString());
            } else {
                throw new IllegalArgumentException("Unable to locate " + "expresso-services_5_1.dtd in component package");
            }
            setDigesterRules(digester);
            InputStream is;
            if (fileName != null) {
                File f = new File(fileName);
                is = new FileInputStream(f);
            } else if (fileURL != null) {
                is = fileURL.openStream();
            } else {
                throw new IllegalArgumentException("Must set the filename to parse before loading Expresso Services");
            }
            digester.parse(is);
        } catch (FactoryConfigurationError ex) {
            log.error("Fatal error trying to find a suitable Digester compatible parser.", ex);
        } catch (SAXException ex) {
            log.error("Fatal error trying to digest expresso-services.xml file", ex);
        } catch (ParserConfigurationException ex) {
            log.error("Fatal error trying to find a suitable Digester compatible parser.", ex);
        } catch (java.io.IOException ex) {
            log.error("Fatal IO error parsing input.", ex);
        }
    }

    /**
     * Set the filename of the expresso services file to load
     * @param fileName the file name to load the Expresso services file from
     */
    public void setExpressoServicesFile(String fileName) {
        if (log.isDebugEnabled()) {
            log.debug("Setting up to load Expresso Services file from: " + fileName);
        }
        this.fileName = fileName;
    }

    /**
     * Retrieve the currently set Expresso Services file.
     * @return java.lang.String
     */
    public String getExpressoServicesFile() {
        return this.fileName;
    }

    /**
     * Set the URL of the ExpressoServices File to load.
     * @param url the new URL for the services file.
     */
    public void setExpressoServicesFile(URL url) {
        if (log.isDebugEnabled()) {
            log.debug("Setting up to load Expresso Services file from: " + url.toString());
        }
        this.fileURL = url;
    }

    /**
     * Retrieve the URL of the file that currently is set for the Expresso Services
     * file.
     * @return java.net.URL the location of the Expresso services file
     */
    public URL getExpressoServicesFileURL() {
        return this.fileURL;
    }

    /**
     * <p>Set the rules for the digester<p>
     * <p><b>TOTAL HACK WARNING!</b> Since Digester doesn't seem to allow recursive
     * rules, we dynamically write rules for up to a maximum component depth of
     * 20.</p>
     * @param digester the digester instance to use.
     */
    protected void setDigesterRules(Digester digester) {
        root = new ComponentConfig();
        digester.push(root);
        digester.addSetProperties("expresso-services");
        digester.addCallMethod("expresso-services/property", "addProperty", 2);
        digester.addCallParam("expresso-services/property", 0, "name");
        digester.addCallParam("expresso-services/property", 1, "value");
        digester.addCallMethod("expresso-services/mapped-property", "addMappedProperty", 3);
        digester.addCallParam("expresso-services/mapped-property", 0, "name");
        digester.addCallParam("expresso-services/mapped-property", 1, "key");
        digester.addCallParam("expresso-services/mapped-property", 2, "value");
        digester.addCallMethod("expresso-services/indexed-property", "addMappedProperty", 3);
        digester.addCallParam("expresso-services/indexed-property", 0, "name");
        digester.addCallParam("expresso-services/indexed-property", 1, "index");
        digester.addCallParam("expresso-services/indexed-property", 2, "value");
        for (int i = 0; i < MAX_NESTING; i++) {
            String componentDepthString = getComponentDepthString(i);
            digester.addObjectCreate("expresso-services/" + componentDepthString + "component", com.jcorporate.expresso.kernel.digester.ComponentConfig.class);
            digester.addSetProperties("expresso-services/" + componentDepthString + "component", new String[] { "name", "class-name" }, new String[] { "name", "className" });
            digester.addCallMethod("expresso-services/" + componentDepthString + "component/property", "addProperty", 2);
            digester.addCallParam("expresso-services/" + componentDepthString + "component/property", 0, "name");
            digester.addCallParam("expresso-services/" + componentDepthString + "component/property", 1, "value");
            digester.addCallMethod("expresso-services/" + componentDepthString + "component/mapped-property", "addMappedProperty", 3);
            digester.addCallParam("expresso-services/" + componentDepthString + "component/mapped-property", 0, "name");
            digester.addCallParam("expresso-services/" + componentDepthString + "component/mapped-property", 1, "key");
            digester.addCallParam("expresso-services/" + componentDepthString + "component/mapped-property", 2, "value");
            digester.addCallMethod("expresso-services/" + componentDepthString + "component/indexed-property", "addMappedProperty", 3);
            digester.addCallParam("expresso-services/" + componentDepthString + "component/indexed-property", 0, "name");
            digester.addCallParam("expresso-services/" + componentDepthString + "component/indexed-property", 1, "index");
            digester.addCallParam("expresso-services/" + componentDepthString + "component/indexed-property", 2, "value");
            digester.addSetNext("expresso-services/" + componentDepthString + "component", "addChildComponent");
        }
    }

    private String getComponentDepthString(int index) {
        FastStringBuffer fsb = FastStringBuffer.getInstance();
        for (int i = 0; i < index; i++) {
            fsb.append("component/");
        }
        String returnValue = fsb.toString();
        fsb.release();
        return returnValue;
    }

    /**
     * Returns the Root of the ComponentConfig tree generated by the Digester.
     * May be null if there was a complete error
     * @return base of the ComponentConfig tree.
     */
    public ComponentConfig getRootConfig() {
        return root;
    }

    /**
     * Test for equality... two ExpressoServiceConfig objets are equal if all
     * the components are the same and each ComponentConfig is completely equal.
     * The files that are the sources DO NOT have to be equal.
     * @param parm1 the object to compare against.
     * @return boolean true if the two objects are indeed equal
     */
    public boolean equals(Object parm1) {
        if (parm1 == null) {
            return false;
        }
        if (!(parm1 instanceof ExpressoServicesConfig)) {
            return false;
        }
        ExpressoServicesConfig other = (ExpressoServicesConfig) parm1;
        boolean returnValue = true;
        ComponentConfig root = getRootConfig();
        ComponentConfig otherRoot = other.getRootConfig();
        if ((root == null) ^ (otherRoot == null)) {
            return false;
        }
        if (root == null && otherRoot == null) {
            return true;
        }
        return root.equals(otherRoot);
    }

    /**
     * Retrieve The 'name' of the runtime configuration.  This is gleaned
     * from the root configuration element who's name is set during the
     * xml digestion.
     * @return java.lang.String
     */
    public String getName() {
        return root.getName();
    }
}
