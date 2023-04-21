package com.dynamide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import com.dynamide.DynamideObject;

public class XDB extends DynamideObject {

    public XDB() {
        this(DEFAULT_SAX_DRIVER_CLASS);
    }

    public XDB(String saxDriverClassname) {
        super(null);
        this.m_saxDriverClassname = saxDriverClassname;
        m_builder = new SAXBuilder(saxDriverClassname);
        m_builder.setValidation(false);
    }

    /** Default SAX Driver to use */
    private static final String DEFAULT_SAX_DRIVER_CLASS = "org.apache.xerces.parsers.SAXParser";

    private String m_saxDriverClassname;

    private SAXBuilder m_builder;

    private Hashtable m_pointers = new Hashtable();

    private StringBuffer errorLog = new StringBuffer();

    protected void addError(String error) {
        errorLog.append(error);
        errorLog.append("\r\n");
        logError(error);
    }

    public void clearErrors() {
        errorLog.setLength(0);
    }

    public String getErrors() {
        return errorLog.toString();
    }

    private Hashtable m_xmlStores = new Hashtable();

    public String printStoreNames() {
        return Tools.collectionToString(getStoreNames());
    }

    public Set getStoreNames() {
        return m_xmlStores.keySet();
    }

    private String m_defaultStoreName = "default";

    /** Methods that require an xmlRepositoryName have overloads that don't, if the DefaultStoreName is set. */
    public String getDefaultStoreName() {
        return m_defaultStoreName;
    }

    /** Methods that require an xmlRepositoryName have overloads that don't,
     *  if the DefaultStoreName is set by this setter;
     *  Calling openXML when DefaultStoreName is not set initializes DefaultStoreName.*/
    public void setDefaultStoreName(String new_value) {
        m_defaultStoreName = new_value;
    }

    public boolean cloneXML(String xmlRepositoryName, String clonedRepositoryName) {
        if (m_xmlStores.containsKey(clonedRepositoryName)) {
            addError("Can't clone [" + xmlRepositoryName + "] because destination name [" + clonedRepositoryName + "] exists in the repository cache.");
            return false;
        }
        Element el = (Element) m_xmlStores.get(xmlRepositoryName);
        if (el != null) {
            Element clone = (Element) el.clone();
            clone.detach();
            m_xmlStores.put(clonedRepositoryName, clone);
            return true;
        }
        addError("Can't clone [" + xmlRepositoryName + "] because it wasn't found in the repository cache.");
        return false;
    }

    public Document createDocument(String rootName) {
        return new Document(new Element(rootName));
    }

    public Element createElement(String name) {
        return new Element(name);
    }

    public Element createXML(String xmlRepositoryName, String rootName) {
        Document doc = new Document(new Element(rootName));
        Element root = doc.getRootElement();
        m_xmlStores.put(xmlRepositoryName, root);
        return root;
    }

    public static String dm_nbsp(String source) {
        return StringTools.dm_nbsp(source);
    }

    public String dumpPointerList() {
        return m_pointers.toString();
    }

    public static String getChildText(Element el, String childName) {
        String result = el.getChildText(childName);
        return result != null ? result : "";
    }

    public Element getXML(String xmlRepositoryName) {
        return (Element) m_xmlStores.get(xmlRepositoryName);
    }

    public String getXMLString(String xmlRepositoryName) {
        Element el = (Element) m_xmlStores.get(xmlRepositoryName);
        return outputElement(el);
    }

    public String getXMLStringAsHTML(String xmlRepositoryName) {
        Element el = (Element) m_xmlStores.get(xmlRepositoryName);
        return outputElementAsHTML(el);
    }

    public static String outputElement(Element el) {
        return outputElement_INNER(el, false);
    }

    public static String outputElementContent(Element el) {
        return outputElement_INNER(el, true);
    }

    public static String outputElement_INNER(Element el, boolean contentOnly) {
        try {
            if (el == null) {
                Log.error(XDB.class, "NullElement passed to outputElement");
                return "";
            }
            XMLOutputter xmloutputter = com.dynamide.JDOMFile.createJDomXMLOutputter(false);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(bos);
            if (contentOnly) {
                xmloutputter.outputElementContent(el, out);
            } else {
                xmloutputter.output(el, out);
            }
            out.close();
            String result = bos.toString();
            try {
                if (bos != null) bos.reset(); else System.out.println("bos was null, not closing");
            } catch (Exception e) {
                System.out.println("ERROR: couldn't reset() bos in XDB " + e);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: [82] [outputElement_INNER] " + e;
        }
    }

    public static String outputElementAsHTML(Element el) {
        String xmlstring = outputElement(el);
        xmlstring = StringTools.searchAndReplaceAll(xmlstring, "<", "&lt;");
        return "<pre>" + xmlstring + "</pre>";
    }

    public Element openXML(String xmlFilename) {
        return openXML(m_defaultStoreName, xmlFilename);
    }

    /** TODO: write a public void invalidate xmlRepository method to remove names from the store.
      * If the DefaultStoreName is not set, this method will set it as a side-effect.
      */
    public Element openXML(String xmlRepositoryName, String xmlFilename) {
        try {
            Element element = (Element) m_xmlStores.get(xmlRepositoryName);
            if (element == null) {
                Document document = read(xmlFilename);
                element = document.getRootElement();
                m_xmlStores.put(xmlRepositoryName, element);
            }
            if (m_defaultStoreName == "") {
                m_defaultStoreName = xmlRepositoryName;
            }
            return element;
        } catch (Exception e) {
            addError("ERROR: [84] [openXML] " + e);
            return null;
        }
    }

    /** Removes the named xmlRepository from the store. Also resets the DefaultStoreName to ""
     *  if it was the current default.
     */
    public void closeXML(String xmlRepositoryName) {
        if (m_defaultStoreName.equals(xmlRepositoryName)) {
            m_defaultStoreName = "";
        }
        m_xmlStores.remove(xmlRepositoryName);
    }

    /** This is a dom4j convenience method, which knows about XHTML.
     *  If you just want pretty printed xml from your jdom
     *  tree, use this class' getXMLString, which doesn't respect the rules of XHTML, such as PRE sections.
     */
    public static String outputNode(org.dom4j.Node node) {
        try {
            if (node == null) {
                System.out.println("Node was null in call to outputNode");
                return "";
            }
            java.io.StringWriter sw = new java.io.StringWriter();
            org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
            format.setNewlines(true);
            format.setTrimText(true);
            format.setXHTML(true);
            format.setExpandEmptyElements(false);
            format.setLineSeparator(System.getProperty("line.separator"));
            org.dom4j.io.HTMLWriter writer = new org.dom4j.io.HTMLWriter(sw, format);
            writer.write(node);
            writer.flush();
            return sw.toString();
        } catch (Exception e) {
            String err = "ERROR: [outputNode] " + e;
            System.out.println(err);
            return err;
        }
    }

    public XPath prepare(String xpathExpression) throws org.jaxen.JaxenException {
        return (XPath) (new JDOMXPath(xpathExpression));
    }

    public Document read(String filename) throws JDOMException, IOException {
        try {
            return m_builder.build(new File(filename));
        } catch (JDOMException e2) {
            System.out.println("JDOMFile exception: " + e2 + "\r\n::filename was: " + filename);
            throw e2;
        }
    }

    public Document readFromString(String source) throws IOException, JDOMException {
        Document document = readFromString(source, getDefaultStoreName());
        return document;
    }

    public Document readFromString(String source, String xmlRepositoryName) throws IOException, JDOMException {
        try {
            m_builder.setExpandEntities(false);
            m_builder.setValidation(false);
            Document document = m_builder.build(new StringReader(source));
            Element element = document.getRootElement();
            m_xmlStores.put(xmlRepositoryName, element);
            return document;
        } catch (JDOMException e2) {
            addError("ERROR: [85] " + e2);
            throw e2;
        }
    }

    /** clear the repository name from the space */
    public synchronized void removeXML(String xmlRepositoryName) {
        m_xmlStores.remove(xmlRepositoryName);
    }

    /** @see #storePointer
     */
    public Object retrievePointer(String name) {
        return m_pointers.get(name);
    }

    public synchronized boolean saveXML(String filename) {
        return saveXML(m_defaultStoreName, "", filename);
    }

    public synchronized boolean saveXML(String xmlRepositoryName, String filename) {
        return saveXML(xmlRepositoryName, "", filename);
    }

    public synchronized boolean saveXML(String xmlRepositoryName, String directory, String filename) {
        String content = "<?xml version='1.0' encoding='UTF-8'?>" + System.getProperty("line.separator") + getXMLString(xmlRepositoryName);
        return (null != com.dynamide.util.FileTools.saveFile("", filename, content));
    }

    /**  Select from the DefaultStoreName, if set. */
    public List select(String xpathExpression) {
        if (m_defaultStoreName == "") {
            logError("default store name not set");
            return new Vector();
        }
        return select(m_defaultStoreName, xpathExpression);
    }

    public List select(String storeName, String xpathExpression) {
        Element element = getXML(storeName);
        if (element == null) {
            addError("error: [select(String,String)]. Element was not found. storeName: " + storeName + " xpath: " + xpathExpression);
        }
        return select(element, xpathExpression);
    }

    public List select(Element element, String xpathExpression) {
        try {
            if (element != null && xpathExpression != null) {
                XPath xpath = (XPath) (new JDOMXPath(xpathExpression));
                return select(element, xpath);
            }
        } catch (Exception e) {
            addError("error: " + e + " while calling select for expression: " + xpathExpression);
        }
        return new Vector();
    }

    /** @see #prepare
     */
    public List select(Element element, XPath xpath) {
        try {
            if (element == null) {
                if (xpath != null) {
                    addError("error: select(Element,XPath)] Element is null, but xpath was: " + xpath);
                } else {
                    addError("error: select(Element,XPath)] Element is null");
                }
            } else {
                if (xpath != null) {
                    return xpath.selectNodes(element);
                }
            }
        } catch (Exception e) {
            addError("error: [select(Element,XPath)]  " + e);
        }
        return new Vector();
    }

    /**  Select from the DefaultStoreName, if set. */
    public Element selectFirst(String xpathExpression) {
        if (m_defaultStoreName == "") {
            logError("default store name not set");
            return null;
        }
        return selectFirst(m_defaultStoreName, xpathExpression);
    }

    public Element selectFirst(String storeName, String xpathExpression) {
        Element element = getXML(storeName);
        return selectFirst(element, xpathExpression);
    }

    public Element selectFirst(Element element, String xpathExpression) {
        try {
            XPath xpath = (XPath) (new JDOMXPath(xpathExpression));
            return selectFirst(element, xpath);
        } catch (Exception e) {
            addError("error: " + e + " while calling selectFirst using expression: " + xpathExpression);
        }
        return null;
    }

    /** @see #prepare
     */
    public Element selectFirst(Element element, XPath xpath) {
        try {
            return (Element) xpath.selectSingleNode(element);
        } catch (Exception e) {
            addError("error: [selectFirst(Element,XPath)]" + e);
        }
        return null;
    }

    /** @see #retrievePointer
     */
    public void storePointer(String name, Object pointer) {
        if (name != null && pointer != null) {
            m_pointers.put(name, pointer);
        } else {
            addError("ERROR: [86] null value in storePointer: name: " + name + " pointer: " + pointer);
        }
    }

    public static String unescape(String source) {
        return StringTools.unescape(source);
    }

    /**  Uses the DefaultStoreName, if set. */
    public String valueOf(String xpathExpression) {
        if (m_defaultStoreName == "") {
            logError("default store name not set");
            return null;
        }
        return valueOf(m_defaultStoreName, xpathExpression);
    }

    public String valueOf(String storeName, String xpathExpression) {
        Element store = getXML(storeName);
        if (store != null) {
            return valueOf(store, xpathExpression);
        }
        return "";
    }

    public String valueOf(Element element, String xpathExpression) {
        try {
            XPath xpath = (XPath) (new JDOMXPath(xpathExpression));
            return xpath.valueOf(element);
        } catch (Exception e) {
            addError("error: " + e + " while getting valueOf for expression: " + xpathExpression);
        }
        return "";
    }

    Element m_quickElement = null;

    public void setQuickElement(Element element) {
        m_quickElement = element;
    }

    public String v(String xpathExpression) {
        return valueOf(m_quickElement, xpathExpression);
    }

    public Object get(String xpathExpression) {
        if (m_quickElement != null) {
            return v(xpathExpression);
        }
        return valueOf(xpathExpression);
    }

    /**  Uses the DefaultStoreName, if set. */
    public String valueOfAll(String xpathExpression) {
        if (m_defaultStoreName == "") {
            logError("default store name not set");
            return null;
        }
        return valueOfAll(m_defaultStoreName, xpathExpression);
    }

    public String valueOfAll(String storeName, String xpathExpression) {
        Element store = getXML(storeName);
        if (store != null) {
            return valueOfAll(store, xpathExpression);
        }
        return "";
    }

    public String valueOfAll(Element element, String xpathExpression) {
        StringBuffer buf = new StringBuffer();
        List list = select(element, xpathExpression);
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            String result;
            Object obj = it.next();
            if (obj instanceof org.jdom.Element) {
                result = outputElement((org.jdom.Element) obj);
            } else if (obj instanceof org.jdom.Attribute) {
                result = ((org.jdom.Attribute) obj).getValue();
            } else if (obj instanceof org.jdom.Document) {
                result = outputElement(((org.jdom.Document) obj).getRootElement());
            } else {
                result = "ERROR: unhandled class type: " + obj.getClass().getName();
            }
            buf.append(result);
        }
        return buf.toString();
    }

    public XDBElement xdbElement(Element el) {
        return new XDBElement(this, el);
    }

    public XDBElement first(Element el) {
        return new XDBElement(this, el);
    }

    public XDBElement first(String xpath) {
        return first(selectFirst(xpath));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("");
        XDB xdb = new XDB();
        String storeName = args[0];
        String file = args[1];
        String xpath = args[2];
        xdb.openXML(storeName, file);
        List list = xdb.select(storeName, xpath);
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            String result;
            Object obj = it.next();
            if (obj instanceof org.jdom.Element) {
                result = outputElement((org.jdom.Element) obj);
            } else if (obj instanceof org.jdom.Attribute) {
                result = ((org.jdom.Attribute) obj).getValue();
            } else {
                result = "ERROR: unhandled class type: " + obj.getClass().getName();
            }
            System.out.println(result);
        }
    }
}
