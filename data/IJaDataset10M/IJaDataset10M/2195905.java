package com.finalist.jaggenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The ConfigManager deals with loading / saving JAG configuraton information
 * to/from an XML config file.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class ConfigManager {

    private static final String GRAG_CONFIG = "grag-config";

    private static final File GRAG_CONFIG_FILE = new File("grag-config.xml");

    private static final String[] STRING_ARRAY = new String[0];

    private static final File DATABASE_DRIVERS_CLASSPATH_FILE = new File("set_database_drivers_classpath.bat");

    private static final String SET_COMMAND = "set DATABASE_DRIVERS_CLASSPATH=";

    private static final char SEMICOLON = ';';

    /**
    * The name of the XML tag that contains the GUI configuration properties.
    */
    protected static final String XMLTAG_GUI = "gui";

    private static ConfigManager ourInstance;

    private Document doc;

    private ConfigManager() {
        load();
    }

    /**
    * The ConfigManager is a singleton - this method obtains the one and only instance.
    *
    * @return
    */
    public static synchronized ConfigManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ConfigManager();
        }
        return ourInstance;
    }

    /**
    * Gets the config information, parsed as an XML document.
    * <p/>
    * <b>NOTE:</b> this Document is provided READ-ONLY - any changes made to the doc will NOT be persisted!
    *
    * @return the doc.
    */
    public Document getDocument() {
        return doc;
    }

    /**
    * Creates a Map that contains key-value pairs representing the XML elements / text nodes
    * that fall underneath the node with the supplied name. If there are more than one node with the supplied
    * name, only the first of these is translated into a Map.
    * <p/>
    * Property values are of type String[] : if a given property within the Map occurs more than
    * once in the XML there will be more than one String in the array.
    *
    * @param rootElementName The name of the parent node whose children we want making into a Properties object.
    *
    * @return a Map whose keys are Strings and whose values are String[].
    *         The map will be empty (but never <code>null</code>) if there are no nodes with the name
    *         specified in <code>rootElementName</code>.
    */
    public Map retrievePropertiesFromXML(String rootElementName) {
        HashMap props = new HashMap();
        Element propsRoot = (Element) doc.getElementsByTagName(rootElementName).item(0);
        if (propsRoot != null) {
            NodeList children = propsRoot.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j) instanceof Element) {
                    Element child = (Element) children.item(j);
                    String key = child.getNodeName();
                    String[] existingValues = (String[]) props.get(key);
                    if (existingValues == null) {
                        props.put(key, new String[] { child.getFirstChild().getNodeValue() });
                    } else {
                        ArrayList newValues = new ArrayList(Arrays.asList(existingValues));
                        newValues.add(child.getFirstChild().getNodeValue());
                        props.put(key, newValues.toArray(STRING_ARRAY));
                    }
                }
            }
        }
        return props;
    }

    /**
    * Saves the configurations to the XML JAG_CONFIG_FILE.
    */
    public void save() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document newDoc = builder.newDocument();
            Element root = newDoc.createElement(GRAG_CONFIG);
            Element dbRoot = DatabaseManager.getInstance().appendXML(root);
            root.appendChild(dbRoot);
            newDoc.appendChild(root);
            HashMap mappingsMap = new HashMap();
            mappingsMap.put(DatabaseManager.NAME, DatabaseManager.getInstance().getTypeMappings());
            Element mappingsRoot = appendPropertiesAsXML(root, mappingsMap, DatabaseManager.APPSERVER_TYPEMAPPINGS);
            root.appendChild(mappingsRoot);
            String XMLDoc = com.finalist.jaggenerator.GragGenerator.outXML(newDoc);
            FileWriter fw = new FileWriter(GRAG_CONFIG_FILE);
            fw.write(XMLDoc);
            fw.close();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveDatabaseDriversClasspath();
    }

    /**
    * Reads in JAG's configuration from the XML JAG_CONFIG_FILE.
    */
    private void load() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
            doc = builder.parse(GRAG_CONFIG_FILE);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            GragGenerator.kickTheBucket("The XML parser can't even start up!");
        } catch (SAXException e) {
            e.printStackTrace();
            GragGenerator.kickTheBucket("JAG's config JAG_CONFIG_FILE (" + GRAG_CONFIG_FILE + ") is invalid!");
        } catch (IOException e) {
            e.printStackTrace();
            GragGenerator.kickTheBucket("JAG's can't access the config JAG_CONFIG_FILE (" + GRAG_CONFIG_FILE + ")!");
        }
    }

    /**
    * Temporary work-around for dynamic loading of database driver classes..
    * <p/>
    * Write the database drivers classpath to a JAG_CONFIG_FILE, which is used by the startup scripts to build
    * the JAG classpath.
    */
    private void saveDatabaseDriversClasspath() {
        try {
            DATABASE_DRIVERS_CLASSPATH_FILE.delete();
            StringBuffer temp = new StringBuffer(SET_COMMAND);
            Database[] databases = DatabaseManager.getInstance().getSupportedDatabases();
            for (int i = 0; i < databases.length; i++) {
                temp.append(databases[i].getFilename());
                temp.append(SEMICOLON);
            }
            FileWriter fw = new FileWriter(DATABASE_DRIVERS_CLASSPATH_FILE);
            fw.write(temp.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Converts a Properties object into XML and appends it to the supplied root element.
    *
    * @param root            The root element to attach the properties XML to.
    * @param props           The Map containing the properties to be XML-ised.  Must be a mapping of
    *                        String to String[].
    * @param rootElementName The name for the base XML tag.
    */
    private Element appendPropertiesAsXML(Element root, Map props, String rootElementName) {
        Document doc = root.getOwnerDocument();
        Element propsRoot = doc.createElement(rootElementName);
        Iterator i = props.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String propertyName = (String) entry.getKey();
            String[] value = (String[]) entry.getValue();
            for (int j = 0; j < value.length; j++) {
                Element nelly = doc.createElement(propertyName);
                if (value[j] != null) {
                    nelly.appendChild(doc.createTextNode(value[j]));
                }
                propsRoot.appendChild(nelly);
            }
        }
        return propsRoot;
    }
}
