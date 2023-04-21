package org.argouml.language.sql;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class providing access to domain mappings. Domain mappings are required to
 * map uml datatypes (domains in db-language) to datatypes of a target database.
 * This way there can be modelled <code>String</code>s, <code>int</code>s
 * or <code>double</code>s for a database model. The DomainMapper maps
 * <code>String</code> to a <code>VARCHAR(100)</code>, <code>int</code>
 * to <code>INTEGER</code> or <code>double</code> to
 * <code>DOUBLE PRECISION</code>, according its configuration. This
 * configuration can be changed in the settings dialog.
 * 
 * @author drahmann
 */
public class DomainMapper {

    private static final ConfigurationKey MAPPING_KEY = Configuration.makeKey("sql", "domainmapping");

    private static final String ROOT_TAG = "<tns:mappings " + "xmlns:tns=\"http://www.argouml.org/Namespace/argouml-sql\" " + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + "xsi:schemaLocation=\"" + "http://www.argouml.org/Namespace/argouml-sql domainmapping.xsd \">";

    private static final String XML_FILE_NAME = "domainmapping.xml";

    private static final String XML_TAG = "<?xml version=\"1.0\" " + "encoding=\"UTF-8\"?>";

    private Map<String, Map<String, String>> databases;

    private String indent;

    private static final Logger LOG = Logger.getLogger(DomainMapper.class);

    /**
     * Creates a new DomainMapper.
     * 
     */
    public DomainMapper() {
        databases = new HashMap<String, Map<String, String>>();
        load();
    }

    /**
     * Clears all mappings for the specified database code creator.
     * 
     * @param codeCreatorClass The class object of the code creator.
     */
    public void clear(Class codeCreatorClass) {
        getMappingsFor(codeCreatorClass).clear();
    }

    /**
     * Returns the datatype for a given domain and code creator. The domain
     * itself is returned if
     * <ul>
     * <li>there does not exist a mapping for the given code creator or</li>
     * <li>there is no defined mapping for the given domain</li>
     * </ul>
     * 
     * @param codeCreatorClass
     *            The class of the code creator
     * @param domain
     *            The domain
     * @return The database-specific datatype for the given domain
     */
    public String getDatatype(Class codeCreatorClass, String domain) {
        Map<String, String> mappings = getMappingsFor(codeCreatorClass);
        String datatype = domain;
        if (mappings != null) {
            String dt = mappings.get(domain);
            if (dt != null) {
                datatype = dt;
            }
        }
        return datatype;
    }

    private Map<String, String> getMappingsFor(String codeCreatorClassName) {
        Map<String, String> mappings = databases.get(codeCreatorClassName);
        if (mappings == null) {
            mappings = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            databases.put(codeCreatorClassName, mappings);
        }
        return mappings;
    }

    /**
     * 
     * @param codeCreatorClass
     * @return All mappings for the given database code creator class.
     */
    public Map<String, String> getMappingsFor(Class codeCreatorClass) {
        return getMappingsFor(codeCreatorClass.getName());
    }

    private InputStream getDomainMap() {
        String domainMap = Configuration.getString(MAPPING_KEY);
        InputStream is = new StringBufferInputStream(domainMap);
        if ("".equals(domainMap)) {
            try {
                URL url = getClass().getResource(XML_FILE_NAME).toURI().toURL();
                is = url.openStream();
            } catch (URISyntaxException e) {
                LOG.warn("Could not find domainmapping file", e);
            } catch (MalformedURLException e) {
                LOG.warn("Could not find domainmapping file", e);
            } catch (IOException e) {
                LOG.warn("Error reading/fetching domain map", e);
            }
        }
        return is;
    }

    /**
     * Load all mappings from the file domainmapping.xml located in the same
     * directory than the module.
     * 
     * TODO: This should first try to load from user preferences, then a URI.
     */
    public void load() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.parse(getDomainMap());
            Element root = document.getDocumentElement();
            NodeList childs = root.getChildNodes();
            for (int i = 0; i < childs.getLength(); i++) {
                Node child = childs.item(i);
                if (child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NamedNodeMap attributes = child.getAttributes();
                String name = attributes.getNamedItem("name").getTextContent();
                Map<String, String> mappings = getMappingsFor(name);
                readMappings(mappings, child.getChildNodes());
            }
        } catch (ParserConfigurationException e) {
            LOG.error("Exception", e);
        } catch (SAXException e) {
            LOG.error("Exception", e);
        } catch (IOException e) {
            LOG.error("Exception", e);
        }
    }

    /**
     * Save all mappings to the file domainmapping.xml located in the same
     * directory than the module.
     * 
     * TODO: This needs to be modified to save use the user preference store
     */
    public void save() {
        Writer sw = new StringWriter(1024);
        try {
            sw.write(XML_TAG);
            sw.write(GeneratorSql.LINE_SEPARATOR);
            sw.write(ROOT_TAG);
            sw.write(GeneratorSql.LINE_SEPARATOR);
            indent = "\t";
            Set<Entry<String, Map<String, String>>> dbEntries = databases.entrySet();
            for (Iterator<Entry<String, Map<String, String>>> it = dbEntries.iterator(); it.hasNext(); ) {
                Entry<String, Map<String, String>> entry = it.next();
                String className = (String) entry.getKey();
                Map<String, String> mappings = entry.getValue();
                StringBuffer sb = new StringBuffer();
                sb.append(indent);
                sb.append("<tns:database name=\"");
                sb.append(className);
                sb.append("\">").append(GeneratorSql.LINE_SEPARATOR);
                sw.write(sb.toString());
                writeMappings(sw, mappings);
                sw.write("</tns:database>");
            }
            sw.write("</tns:mappings>");
            sw.close();
        } catch (IOException e) {
            LOG.error("Exception", e);
        }
        Configuration.setString(MAPPING_KEY, sw.toString());
    }

    /**
     * Set specified mapping for the given database code creator class.
     * 
     * @param codeCreatorClass
     *            The class of the code creator for which this mapping should be
     *            set.
     * @param domain
     *            The domain (uml datatype).
     * @param datatype
     *            The datatype (database-specific).
     */
    public void setDatatype(Class codeCreatorClass, String domain, String datatype) {
        Map<String, String> mappings = getMappingsFor(codeCreatorClass);
        mappings.put(domain, datatype);
    }

    private void readMappings(Map<String, String> mappings, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node mapping = nodes.item(i);
            if (mapping.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            NamedNodeMap attributes = mapping.getAttributes();
            Node src = attributes.getNamedItem("umltype");
            Node dst = attributes.getNamedItem("dbtype");
            String srcText = src.getTextContent();
            String dstText = dst.getTextContent();
            mappings.put(srcText, dstText);
        }
    }

    private void writeMappings(Writer fw, Map<String, String> mappings) throws IOException {
        String oldIndent = indent;
        indent += "\t";
        Set<Entry<String, String>> entries = mappings.entrySet();
        for (Iterator<Entry<String, String>> it = entries.iterator(); it.hasNext(); ) {
            Entry<String, String> entry = it.next();
            String domain = entry.getKey();
            String datatype = entry.getValue();
            StringBuffer sb = new StringBuffer();
            sb.append(indent);
            sb.append("<tns:mapping umltype=\"");
            sb.append(domain);
            sb.append("\" dbtype=\"");
            sb.append(datatype);
            sb.append("\" />").append(GeneratorSql.LINE_SEPARATOR);
            fw.write(sb.toString());
        }
        indent = oldIndent;
    }
}
