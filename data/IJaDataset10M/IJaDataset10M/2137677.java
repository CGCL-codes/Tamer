package com.anthonyeden.lib.config.sax;

import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.ConfigurationBase;
import com.anthonyeden.lib.config.ConfigurationException;
import com.anthonyeden.lib.config.ConfigurationFactory;
import com.anthonyeden.lib.util.ClassUtilities;
import com.anthonyeden.lib.util.IOUtilities;
import com.anthonyeden.lib.util.MessageUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.*;
import java.util.Properties;

/**
 * Implementation of the ConfigurationFactory interface which generates a
 * a configuration tree using a SAX parser.
 * <p/>
 * -fixed the unicode parsing [Florin]
 *
 * @author Anthony Eden
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since 2.0
 */
public class SAXConfigurationFactory implements ConfigurationFactory {

    public static final String CONFIG_FILE = "edenlib-config.properties";

    public static final String DEFAULT_CONFIG_FILE = "/edenlib-default-config.properties";

    private static final Log log = LogFactory.getLog(SAXConfigurationFactory.class);

    private static final SAXConfigurationFactory INSTANCE = new SAXConfigurationFactory();

    public static final String PKG = "com.anthonyeden.lib";

    public static final int BUFFER_SIZE = 4096;

    private static String className;

    private static boolean ns = false;

    private static boolean fallback = false;

    private SAXConfigurationFactory() {
        Properties properties = new Properties();
        InputStream in;
        try {
            in = ClassUtilities.getResourceAsStream(CONFIG_FILE);
        } catch (Exception e) {
            log.warn("Could not load " + CONFIG_FILE + " file. Falling back to defaults.");
            in = getClass().getResourceAsStream(DEFAULT_CONFIG_FILE);
        }
        try {
            properties.load(in);
            className = properties.getProperty("org.xml.sax.parser");
            ns = properties.getProperty("namespaces").equalsIgnoreCase("on");
            try {
                XMLReader xr = null;
                if (!fallback) {
                    try {
                        xr = (XMLReader) ClassUtilities.loadClass(className).newInstance();
                    } catch (Exception e) {
                        fallback = true;
                    }
                }
                if (fallback) {
                    xr = XMLReaderFactory.createXMLReader();
                }
                if (xr != null) {
                    log.info("XML Parser used: " + xr.getClass().getName());
                }
                if (xr == null) throw new ConfigurationException("No XML parsers available in the System???");
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SAXConfigurationFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get the root Configuration object from the specified InputStream.
     *
     * @param id The id of the configuration (file path, URL, etc)
     * @param in The InputStream
     * @return The Configuration object
     * @throws ConfigurationException
     */
    public Configuration getConfigurationX(String id, InputStream in) throws ConfigurationException {
        try {
            return getConfiguration(id, new InputStreamReader(in, ConfigurationBase.ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Old method used just to maintain the backward compatibility
     *
     * @param id
     * @param in
     * @return
     * @throws ConfigurationException
     * @deprecated
     */
    public Configuration getConfiguration(String id, Reader in) throws ConfigurationException {
        try {
            return getConfiguration(id, new ByteArrayInputStream(IOUtilities.copyToString(in).getBytes("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        }
    }

    /**
     * Get the root Configuration object from the specified InputStream.
     *
     * @param id The id of the configuration (file path, URL, etc)
     * @param in The InputStream
     * @return The Configuration object
     * @throws ConfigurationException
     */
    public Configuration getConfiguration(String id, InputStream in) throws ConfigurationException {
        SAXConfigurationHandler handler = new SAXConfigurationHandler(this, id);
        XMLReader xr = null;
        try {
            if (!fallback) {
                try {
                    xr = (XMLReader) ClassUtilities.loadClass(className).newInstance();
                } catch (Exception e) {
                    fallback = true;
                }
            }
            if (fallback) {
                xr = XMLReaderFactory.createXMLReader();
            }
            if (xr == null) throw new ConfigurationException("No XML parsers available in the System???");
            xr.setErrorHandler(handler);
            xr.setContentHandler(handler);
            try {
                xr.setFeature("http://xml.org/sax/features/namespaces", ns);
                xr.setFeature("http://xml.org/sax/features/namespace-prefixes", !ns);
            } catch (SAXException e) {
                log.info("Warning: could not set namespace feature to " + ns);
                try {
                    boolean currentValue;
                    log.info("Current value is " + xr.getFeature("http://xml.org/sax/features/namespaces"));
                } catch (SAXException x) {
                    log.info("Current value is unknown but probably false.");
                }
            }
            xr.parse(new InputSource(in));
            return handler.getConfiguration();
        } catch (SAXException e) {
            Object[] args = { id, e.getMessage() };
            throw new ConfigurationException(MessageUtilities.getMessage(getClass(), PKG, "parseSAXError", args));
        } catch (IOException e) {
            Object[] args = { id, e.getMessage() };
            throw new ConfigurationException(MessageUtilities.getMessage(getClass(), PKG, "parseIOError", args));
        }
    }
}
