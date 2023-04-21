package net.sourceforge.ottm8;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 */
public class XmlTaskSource extends TaskSource {

    /**
   */
    static final String CLASS_NAME = XmlTaskSource.class.getName();

    /**
   * XML namespace for ottm8 elements.
   */
    public static final String NAMESPACE = "http://ottm8.sourceforge.net/xml/2004/05/25";

    /**
   */
    Config config;

    /**
   * Parsed XML configuration data.
   */
    Document d;

    /**
   * Children of the ottm8 element.
   */
    NodeList ottm8Children;

    /**
   * Location of last processed node in ottm8Children.
   */
    int lastNode;

    /**
   * Base URL used to resolve any relative URLs encountered
   */
    URL baseURL;

    /**
   * While processing an include element, the XmlTaskSource for the
   * included XML.
   */
    XmlTaskSource includedXmlTaskSource;

    /**
   */
    public XmlTaskSource(String fileName, URL baseURL, Config config) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
        this(new java.io.FileInputStream(fileName), baseURL, config);
    }

    /**
   */
    public XmlTaskSource(URL url, URL baseURL, Config config) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
        this(url.openStream(), baseURL, config);
    }

    /**
   */
    public XmlTaskSource(InputStream is, URL baseURL, Config config) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
        super(config);
        config.getLogger().entering(CLASS_NAME, "<init>", baseURL);
        this.baseURL = baseURL;
        this.config = config;
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        d = db.parse(is, baseURL.toString());
        ottm8Children = d.getDocumentElement().getChildNodes();
        lastNode = -1;
        config.getLogger().exiting(CLASS_NAME, "<init>");
    }

    /**
   */
    Map parseAttributes(Element e) {
        config.getLogger().entering(CLASS_NAME, "parseAttributes", e);
        Map attrs = new java.util.HashMap();
        if (e.getAttribute("id") != null) attrs.put("displayName", e.getAttribute("id")); else if (e.getAttribute("class") != null) attrs.put("displayName", e.getAttribute("class")); else attrs.put("displayName", "(unknown)");
        NodeList xmlAttrElements = null;
        xmlAttrElements = e.getElementsByTagNameNS(NAMESPACE, "attribute");
        for (int i = 0; i < xmlAttrElements.getLength(); ++i) {
            Element xmlAttrElement = (Element) xmlAttrElements.item(i);
            String name = xmlAttrElement.getAttribute("name");
            config.getLogger().finer("parsing attribute element: " + name);
            String appendValue = xmlAttrElement.getAttribute("append");
            if (appendValue == null || appendValue.length() == 0 || !Boolean.parseBoolean(appendValue)) {
                Object o = attrs.remove(name);
                config.getLogger().finer("removed existing attribute value: " + o);
            }
            Object oldValue = attrs.get(name);
            String newValue = xmlAttrElement.getAttribute("value");
            if (oldValue == null) attrs.put(name, newValue); else if (oldValue instanceof String) {
                List l = new java.util.ArrayList(5);
                l.add(oldValue);
                l.add(newValue);
                attrs.put(name, l);
            } else if (oldValue instanceof List) ((List) oldValue).add(newValue); else throw new IllegalArgumentException("cannot append value \"" + newValue + "\" to " + oldValue);
        }
        config.getLogger().exiting(CLASS_NAME, "parseAttributes", attrs);
        return attrs;
    }

    /**
   */
    public Task nextTask() throws java.io.IOException {
        config.getLogger().entering(CLASS_NAME, "nextTask");
        Task t = null;
        if (includedXmlTaskSource != null) {
            t = includedXmlTaskSource.nextTask();
            if (t == null) includedXmlTaskSource = null;
        }
        while (t == null && lastNode < ottm8Children.getLength() - 1) {
            Node n = ottm8Children.item(++lastNode);
            config.getLogger().finest("examining child number " + lastNode + ": " + n.getNodeName());
            if (n.getNodeType() == Node.ELEMENT_NODE && NAMESPACE.equals(n.getNamespaceURI()) && "task".equals(n.getLocalName())) {
                Element e = (Element) n;
                config.getLogger().finest("parsing <task> node " + e);
                Map attrs = parseAttributes(e);
                try {
                    if (e.getAttribute("class") != null && e.getAttribute("class").length() > 0) t = createTaskFromClass(e.getAttribute("class"), attrs); else if (e.getAttribute("id") != null && e.getAttribute("id").length() > 0) t = createTaskFromId(e.getAttribute("id"), attrs); else throw new IllegalArgumentException("task has no id or class");
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                    break;
                } catch (java.beans.IntrospectionException ie) {
                    ie.printStackTrace();
                    break;
                }
            } else if (n.getNodeType() == Node.ELEMENT_NODE && NAMESPACE.equals(n.getNamespaceURI()) && "variable".equals(n.getLocalName())) {
                String varName = ((Element) n).getAttribute("name");
                String varValue = ((Element) n).getAttribute("value");
                if (varName != null && varName.length() > 0 && varValue != null && varValue.length() > 0) config.setVariableValue(varName, varValue);
            } else if (n.getNodeType() == Node.ELEMENT_NODE && NAMESPACE.equals(n.getNamespaceURI()) && "taskdef".equals(n.getLocalName())) {
                Element e = (Element) n;
                String id = e.getAttribute("id");
                String className = e.getAttribute("class");
                Map attrs = parseAttributes(e);
                if (id != null && id.length() > 0 && className != null && className.length() > 0) {
                    config.getLogger().fine("defining task " + id + ": " + className + ": " + attrs);
                    config.createTaskDef(id, className, attrs);
                }
            } else if (n.getNodeType() == Node.ELEMENT_NODE && NAMESPACE.equals(n.getNamespaceURI()) && "include".equals(n.getLocalName())) {
                Element e = (Element) n;
                String urlS = e.getAttribute("url");
                if (urlS != null && urlS.length() > 0) {
                    try {
                        URL url = new URL(baseURL, urlS);
                        config.getLogger().fine("including " + url);
                        includedXmlTaskSource = new XmlTaskSource(url, url, config);
                        t = includedXmlTaskSource.nextTask();
                        if (t == null) includedXmlTaskSource = null;
                    } catch (javax.xml.parsers.ParserConfigurationException pce) {
                        config.getLogger().throwing(CLASS_NAME, "nextTask", pce);
                    } catch (org.xml.sax.SAXException se) {
                        config.getLogger().throwing(CLASS_NAME, "nextTask", se);
                    }
                }
            }
        }
        config.getLogger().exiting(CLASS_NAME, "nextTask", t);
        if (t == null) config.getLogger().finer("completed parsing of " + baseURL);
        return t;
    }
}
