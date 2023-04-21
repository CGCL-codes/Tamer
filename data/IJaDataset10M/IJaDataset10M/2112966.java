package de.miethxml.hawron.xml;

import java.io.IOException;
import java.util.ArrayList;
import de.miethxml.toolkit.conf.ConfigManager;
import de.miethxml.toolkit.plugins.PluginConfig;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public class SAXPluginBuilder implements ContentHandler {

    StringBuffer buf;

    int DESCRIPTION_MODE = 0;

    int INSTANCECLASS_MODE = 1;

    int NAME_MODE = 2;

    int INTERFACE_MODE = 3;

    ArrayList classpath;

    ArrayList resources;

    PluginConfig plugin;

    /**
     *
     *
     *
     */
    public SAXPluginBuilder() {
        super();
        buf = new StringBuffer();
        classpath = new ArrayList();
        resources = new ArrayList();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        buf.append(ch, start, length);
    }

    public void endDocument() throws SAXException {
        if (classpath.size() > 0) {
            String[] cpath = new String[classpath.size()];
            for (int i = 0; i < classpath.size(); i++) {
                cpath[i] = (String) classpath.get(i);
            }
            plugin.setClasspath(cpath);
        }
        if (resources.size() > 0) {
            String[] res = new String[resources.size()];
            for (int i = 0; i < resources.size(); i++) {
                res[i] = (String) resources.get(i);
            }
            plugin.setResources(res);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals("interface")) {
            plugin.setInterfaceName(buf.toString());
        } else if (localName.equals("name")) {
            plugin.setName(buf.toString());
        } else if (localName.equals("description")) {
            plugin.setDescription(buf.toString());
        } else if (localName.equals("instanceclass")) {
            plugin.setInstanceClass(buf.toString());
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    public void startDocument() throws SAXException {
        plugin = new PluginConfig();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        buf.delete(0, buf.length());
        if (localName.equals("classpath")) {
            String src = atts.getValue(atts.getIndex("src"));
            classpath.add(src);
        } else if (localName.equals("resource")) {
            String src = atts.getValue(atts.getIndex("src"));
            resources.add(src);
        } else if (localName.equals("resources")) {
            classpath.clear();
            resources.clear();
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public PluginConfig parseURI(String uri) {
        try {
            XMLReader saxparser = XMLReaderFactory.createXMLReader(ConfigManager.getInstance().getProperty("SAXParser"));
            saxparser.setContentHandler(this);
            saxparser.parse(uri);
        } catch (SAXException e) {
            System.err.println(e.getMessage());
        } catch (IOException ioe) {
        }
        return plugin;
    }
}
