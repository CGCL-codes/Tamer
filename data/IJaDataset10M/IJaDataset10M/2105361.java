package hypercast.util;

import hypercast.util.XmlUtil;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;
import java.io.IOException;
import java.text.ParseException;
import java.io.FileNotFoundException;

/**
 * This class provides functions to create a Properties
 * object from a schema which uses all possible Xpath 
 * string as entry and its default values as value. 
 *
 * @author HyperCast Team
 * @version 2005 (version 3.0)
 */
public class SchemaDefaultValueCalculator {

    /**
	 * debug flag
	 */
    static final boolean debug = false;

    /** The schema file. */
    private File SchemaFile = null;

    /** The schema file name. */
    private String schemaFileName = null;

    /** The Document instance created from the opened schema file. */
    private Document openedSchemaFile = null;

    /**
     * Constructor.
     */
    public SchemaDefaultValueCalculator(String schemafile) throws IOException, ParseException {
        schemaFileName = schemafile;
        SchemaFile = new File(schemafile);
        if (schemaFileName.toLowerCase().endsWith(".xsd")) {
            try {
                openedSchemaFile = XmlUtil.createDocument(schemafile);
            } catch (final IOException ioe) {
                throw new IOException("Cannot open schema file " + schemaFileName + ": " + ioe);
            } catch (final ParseException ioe) {
                throw new ParseException("Failed in parsingschema  file " + schemaFileName + ": " + ioe, 0);
            }
            if (openedSchemaFile == null) {
                throw new IOException("Poor formated schema file: hypercast.xsd");
            }
        } else {
        }
    }

    /**
	 * Constructor.
	 */
    public SchemaDefaultValueCalculator() throws IOException {
        schemaFileName = "hypercast.xsd";
        InputStream fis = getClass().getResourceAsStream(schemaFileName);
        if (fis == null) {
            System.out.println("Connot find default schema file " + schemaFileName + ".");
            return;
        }
        try {
            openedSchemaFile = XmlUtil.createDocument(fis, new String("hypercast.xsd"));
        } catch (final IOException ioe) {
            throw new IOException("Cannot open schema file " + schemaFileName + ": " + ioe);
        }
        if (openedSchemaFile == null) {
            throw new IOException("Poor formated schema file: hypercast.xsd");
        }
    }

    /**
	 * Build the Properties object for all default values of a schema file.
	 */
    public Properties getDefaultValues() throws IOException {
        if (openedSchemaFile == null) {
            schemaFileName = "hypercast.xsd";
            InputStream fis = getClass().getResourceAsStream(schemaFileName);
            if (fis == null) {
                System.out.println("Connot find default schema file " + schemaFileName + ".");
                return new Properties();
            }
            try {
                openedSchemaFile = XmlUtil.createDocument(fis, new String("hypercast.xsd"));
            } catch (final IOException ioe) {
                throw new IOException("Cannot open schema file " + schemaFileName + ": " + ioe);
            }
            if (openedSchemaFile == null) {
                throw new IOException("Poor formated schema file: hypercast.xsd");
            }
        }
        return getDefaultValues(openedSchemaFile);
    }

    /**
	 * Build the Properties object for all default values of a schema file.
	 */
    public static Properties getDefaultValues(String schemafile) throws FileNotFoundException, ParseException, IOException {
        Document schemaDoc = null;
        if (schemafile.toLowerCase().endsWith(".xsd")) {
            schemaDoc = XmlUtil.createDocument(schemafile);
            if (schemaDoc == null) {
                throw new IOException("Poor formated schema file: " + schemafile);
            }
        } else {
            System.err.println("The file " + schemafile + " is not a schema file.");
        }
        return getDefaultValues(schemaDoc);
    }

    /**
     * Build the Properties object for all default values of a schema file.
     */
    public static Properties getDefaultValues(Document schemaDoc) {
        Properties defaultProperties = new Properties();
        if (schemaDoc == null) {
            return defaultProperties;
        }
        Element propertyElement = HypercastConfigurator.findPropertyElement(schemaDoc);
        if (propertyElement == null) {
            System.err.println("Poor formated schema file: " + schemaDoc.getNodeName());
            return null;
        }
        if (debug) System.out.println("find property Element in the given Document: " + schemaDoc.getNodeName());
        recursiveGetDefaultValues("", defaultProperties, propertyElement);
        return defaultProperties;
    }

    /**
	 * Recursively get all default values in the schema file and put them in 
	 * the given Properties instance.
	 * 
	 * @param prefix	the string which represents the xpath string to current Element or Node.
	 * @param p	a Properties instabce in which the default values are stored.
	 * @param xmlElement an Element in the schema DOM which represents an attribute.
	 */
    private static void recursiveGetDefaultValues(String prefix, Properties p, Element xmlElement) {
        if (debug) System.out.println("Process Element: " + xmlElement.getAttribute("name"));
        if (HypercastConfigurator.isComplexElement(xmlElement)) {
            processComplexElement(prefix, p, xmlElement);
        } else {
            processSimpleElement(prefix, p, xmlElement);
        }
    }

    /**
	 * Get the default value of the given complexType Element e and recursively process its children.
	 * 
	 * @param prefix	the string which represents the xpath string to current Element or Node.
	 * @param p	a Properties instabce in which the default values are stored.
	 * @param e an Element in the schema DOM which represents attribute.
	 */
    private static void processComplexElement(String prefix, Properties p, Element e) {
        String name = e.getAttribute("name");
        if (debug) System.out.println("Process complexType Element: " + name);
        String newPrefix = new String(prefix + "/" + name);
        Vector children = HypercastConfigurator.getChildrenOfComplexElement(e);
        if (HypercastConfigurator.hasChoiceOnChildren(e)) {
            String defaultValue = e.getAttribute("default");
            if ((defaultValue == "") || (defaultValue == null)) {
                Element firstChild = (Element) children.elementAt(0);
                if (firstChild != null) {
                    String childName = firstChild.getAttribute("name");
                    if (debug) System.out.println("Entry is added to Properties: " + newPrefix);
                    p.put(new String(newPrefix), childName);
                } else {
                    System.out.println("The complexType Element " + name + " doesn't have child.");
                }
            } else {
                if (debug) System.out.println("Entry is added to Properties: " + newPrefix);
                p.put(new String(newPrefix), defaultValue);
            }
        }
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.elementAt(i);
            if (child == null) {
                continue;
            }
            if (debug) System.out.println("Loop on the children of " + newPrefix);
            recursiveGetDefaultValues(newPrefix, p, child);
        }
    }

    /**
	 * Get the default of given simpleType Element.
	 * 
	 * @param prefix	the string which represents the xpath string to current Element or Node.
	 * @param p	a Properties instabce in which the default values are stored.
	 * @param e an Element in the schema DOM which represents attribute.
	 */
    private static void processSimpleElement(String prefix, Properties p, Element e) {
        String name = e.getAttribute("name");
        if (debug) System.out.println("Process simpleType Element: " + name);
        if (HypercastConfigurator.getRestrictionElement(e) == null) {
            return;
        }
        String newPrefix = new String(prefix + "/" + name);
        String defaultValue = e.getAttribute("default");
        if (defaultValue != null) {
            if (debug) System.out.println("Loop on the children of " + newPrefix);
            p.put(new String(newPrefix), defaultValue);
        }
    }
}
