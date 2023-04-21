package org.primordion.xholon.io.xml;

import org.primordion.xholon.base.Xholon;

/**
 * This factory creates new instances of XmlReader.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on August 1, 2009)
 */
public class XmlReaderFactory extends Xholon {

    /** The singleton instance of this class. */
    private static XmlReaderFactory xmlReaderFactory = null;

    /**
	 * Get a concrete instance of XMLReader.
	 * @param in An input file, stream, string, or other type of reader.
	 * @return An instance of XMLReader, or null.
	 */
    public static IXmlReader getXmlReader(Object in) {
        IXmlReader xmlReader = null;
        if (xmlReaderFactory == null) {
            xmlReaderFactory = new XmlReaderFactory();
        }
        if (xmlReader == null) {
            xmlReader = xmlReaderFactory.newXppInstance();
            if (xmlReader != null) {
                xmlReader.createNew(in);
            }
        }
        if (xmlReader == null) {
            xmlReader = xmlReaderFactory.newStaxInstance();
            if (xmlReader != null) {
                xmlReader.createNew(in);
            }
        }
        if (xmlReader == null) {
        }
        return xmlReader;
    }

    /**
	 * Create a new instance of an XML pull parser (XPP).
	 * @return A new instance, or null.
	 */
    protected IXmlReader newXppInstance() {
        if (!exists("org.xmlpull.v1.XmlPullParserFactory")) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = Class.forName("org.primordion.xholon.io.xml.XmlXppReader", false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            getLogger().warn("Unable to find xpp XML reader.");
            return null;
        }
        try {
            if (clazz != null) {
                return (IXmlReader) clazz.newInstance();
            }
        } catch (IllegalAccessException e) {
            getLogger().warn("Illegal access trying to instantiate xpp XML reader.");
            return null;
        } catch (InstantiationException e) {
            getLogger().warn("Unable to instantiate xpp XML reader.");
            return null;
        }
        return null;
    }

    /**
	 * Create a new instance of a Stax reader.
	 * @return A new instance, or null.
	 */
    protected IXmlReader newStaxInstance() {
        if (!exists("javax.xml.stream.XMLInputFactory")) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = Class.forName("org.primordion.xholon.io.xml.XmlStaxReader", false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            getLogger().warn("Unable to find Stax XML reader.");
            return null;
        }
        try {
            if (clazz != null) {
                return (IXmlReader) clazz.newInstance();
            }
        } catch (IllegalAccessException e) {
            getLogger().warn("Illegal access trying to instantiate Stax XML reader.");
            return null;
        } catch (InstantiationException e) {
            getLogger().warn("Unable to instantiate Stax XML reader.");
            return null;
        }
        return null;
    }

    /**
	 * Create a new instance of an XML "DOM" parser.
	 * @return A new instance, or null.
	 */
    protected IXmlReader newDomInstance() {
        if (!exists("org.w3c.dom.Document")) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = Class.forName("org.primordion.xholon.io.xml.XmlDomReader", false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            getLogger().warn("Unable to find DOM XML reader.");
            return null;
        }
        try {
            if (clazz != null) {
                return (IXmlReader) clazz.newInstance();
            }
        } catch (IllegalAccessException e) {
            getLogger().warn("Illegal access trying to instantiate DOM XML reader.");
            return null;
        } catch (InstantiationException e) {
            getLogger().warn("Unable to instantiate DOM XML reader.");
            return null;
        }
        return null;
    }

    /**
	 * Do the Reader classes (XPP or StAX) exist?
	 * @param classLoader A Java ClassLoader.
	 * @return true or false
	 */
    public boolean exists(String className) {
        try {
            getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
	 * testing
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String fileName = "./config/Collisions/CompositeStructureHierarchy.xml";
        javax.xml.parsers.DocumentBuilderFactory docBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = docBuilder.parse(new java.io.File(fileName));
        IXmlReader xmlReader = XmlReaderFactory.getXmlReader(document);
        int eventType = xmlReader.getEventType();
        String blanks = "";
        while (eventType != IXmlReader.END_DOCUMENT) {
            switch(eventType) {
                case IXmlReader.START_TAG:
                    blanks += " ";
                    System.out.println(blanks + xmlReader.getName());
                    for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                        System.out.println(blanks + "--> " + xmlReader.getAttributeName(i) + ":" + xmlReader.getAttributeValue(i));
                    }
                    break;
                case IXmlReader.END_TAG:
                    blanks = blanks.substring(1);
                    break;
                case IXmlReader.TEXT:
                    System.out.println(blanks + "==> " + xmlReader.getText());
                    break;
                default:
                    break;
            }
            eventType = xmlReader.next();
        }
    }
}
