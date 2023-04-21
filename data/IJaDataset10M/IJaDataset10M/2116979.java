package org.naftulin.configmgr.parsers;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.log4j.Logger;
import org.naftulin.configmgr.ConfigurationManagementEntryImpl;
import org.naftulin.configmgr.ConfigurationManagerException;
import org.naftulin.configmgr.ConfigurationManagementEntry;
import org.naftulin.configmgr.ConfigurationType;
import org.xml.sax.SAXException;

/**
 * Configuration entry parser converts xml configuration record into supplied class instance configuration management entry.
 * It parses the property file that the record describes, and stores the configuration
 * as a properties in content of an {@link org.naftulin.configmgr.ConfigurationManagementEntry entry}.
 * 
 * @author Henry Naftulin
 * @since 1.0
 */
public class XmlFileParserImpl implements ConfigEntryParser {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(XmlFileParserImpl.class);

    private final String className;

    public XmlFileParserImpl(final String className) {
        this.className = className;
    }

    /**
	 * Returns configuration management entry by parsing the record passed in.
	 * Note xml file has to match the class passed in, per betwix specfications. 
	 * @param key the key configuration entry will be assigned
	 * @param fileUrl the file URL to be parsed.
	 * @return a configuration managment entry by parsing the record passed in.
	 * @throws ConfigurationManagerException if an error occurs while parsing an entry.
	 */
    public ConfigurationManagementEntry getConfigurationManagementEntry(final String key, final URL fileUrl) throws ConfigurationManagerException {
        validateParameters(key, fileUrl);
        Class<Object> beanClass;
        try {
            beanClass = (Class<Object>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationManagerException("className " + className + " could not be found in the classpath.", e);
        }
        final String fileName = fileUrl.getFile();
        Object content = null;
        try {
            final BeanReader beanReader = new BeanReader();
            log.debug("class name " + getSimpleName(beanClass));
            beanReader.registerBeanClass(getSimpleName(beanClass), beanClass);
            content = beanReader.parse(fileUrl);
            log.debug("xml object parsed by XML Parser is: " + content);
        } catch (IOException e) {
            log.warn("Error while reading property file", e);
            throw new ConfigurationManagerException("Error while reading property file", e);
        } catch (IntrospectionException e) {
            log.warn("Error while parsing xml.", e);
            throw new ConfigurationManagerException("Error while parsing xml.", e);
        } catch (SAXException e) {
            log.warn("Error while parsing xml.", e);
            throw new ConfigurationManagerException("Error while parsing xml.", e);
        }
        ConfigurationManagementEntry entry = new ConfigurationManagementEntryImpl(key, fileName, content, this, ConfigurationType.XML);
        return entry;
    }

    private void validateParameters(final String key, final URL fileUrl) throws ConfigurationManagerException {
        if (fileUrl == null) {
            throw new ConfigurationManagerException("file URL is null");
        }
        if (fileUrl.getFile() == null) {
            throw new ConfigurationManagerException("file name passed in the URL " + fileUrl + " is null");
        }
        if (key == null) {
            throw new ConfigurationManagerException("key is null for file " + fileUrl);
        }
        if (className == null) {
            throw new ConfigurationManagerException("className is null for file " + fileUrl + " and key " + key);
        }
    }

    /**
	 * Immitates the JDK 1.5 API to get the simple class name. Class is not null.
	 * @param cl class
	 * @return simple class name as defined in JDK 1.5
	 */
    private String getSimpleName(final Class<Object> cl) {
        final int lastDot = cl.getName().lastIndexOf(".");
        return cl.getName().substring(lastDot + 1);
    }

    /**
	 * Returns a string representation of this parser.
	 * @return a string representation of this parser.
	 */
    public String toString() {
        return "Xml file parser";
    }
}
