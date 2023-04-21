package com.metasolutions.jfcml;

import java.awt.Container;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import com.metasolutions.jfcml.extend.AttributeHandler;
import com.metasolutions.jfcml.extend.ChildHandler;
import com.metasolutions.jfcml.extend.ElementHandler;
import com.metasolutions.jfcml.extend.WindowHandler;
import com.metasolutions.jfcml.helpers.Attribute;
import com.metasolutions.jfcml.helpers.Element;
import com.metasolutions.jfcml.helpers.WindowContext;
import com.metasolutions.util.JFCMLConfiguration;
import com.metasolutions.util.PackageRegistry;
import com.metasolutions.util.SimpleStack;

/**
 * A SAX Event Handler which is also a JFCML DocumentHandler manager.
 *
 * @author Shawn Curry
 * @author Mathias Henze
 * @version 0.9 May 28, 2005
 */
public class TagHandler extends DefaultHandler implements java.io.Serializable {

    private static final boolean SAX_TRACE;

    static {
        SAX_TRACE = ((Boolean) PackageRegistry.getService("SAX_TRACE")).booleanValue();
    }

    private static final int ELEMENT_JROOTPANE = "JRootPane".hashCode();

    private static final int ELEMENT_THIS = "this".hashCode();

    private static final int ELEMENT_HTML = "html".hashCode();

    private static final int ELEMENT_SCRIPT = "Script".hashCode();

    protected ElementHandler elementHandler;

    protected AttributeHandler attributeHandler;

    protected WindowHandler windowHandler;

    protected ChildHandler childHandler;

    protected WindowContext context;

    private StringBuffer buffer;

    private boolean parseText = false;

    private Attribute currentAttribute = new Attribute(null, null);

    /**
	 * Constructs a TagHandler using the configuration stored in the PackageRegistry. 
	 */
    public TagHandler() {
        JFCMLConfiguration config = (JFCMLConfiguration) PackageRegistry.getService("com.metasolutions.util.JFCMLConfiguration");
        if (config == null) throw new RuntimeException("No default configuration found in PackageRegistry");
        elementHandler = config.getElementHandler();
        attributeHandler = config.getAttributeHandler();
        windowHandler = config.getWindowHandler();
        childHandler = config.getChildHandler();
        context = elementHandler.getWindowContext();
    }

    /**
	 * Constructs a TagHandler which uses the specified configuration.
	 * 
	 * @param ctxt the WindowContext to use
	 * @param elem the ElementHandler to use
	 * @param attr the AttributeHandler to use
	 * @param window the WindowHandler to use
	 * @param child the ChildHandler to use
	 */
    public TagHandler(WindowContext ctxt, ElementHandler elem, AttributeHandler attr, WindowHandler window, ChildHandler child) {
        context = ctxt;
        elementHandler = elem;
        attributeHandler = attr;
        windowHandler = window;
        childHandler = child;
    }

    /**
     * @return Returns the WindowContext.
     */
    public WindowContext getWindowContext() {
        return context;
    }

    /**
     * Sets the WindowContext for this 'Handler tree.
     * @param context The WindowContext to set.
     */
    public void setWindowContext(WindowContext windowContext) {
        context = windowContext;
        elementHandler.setWindowContext(context);
        attributeHandler.setWindowContext(context);
        windowHandler.setWindowContext(context);
        childHandler.setWindowContext(context);
    }

    public Object handleElement(Element element, SimpleStack children) {
        return elementHandler.handleElement(element, children);
    }

    /**
	 * @param elementHandler The elementHandler to set.
	 */
    public void addElementHandler(ElementHandler handler) {
        handler.setNext(elementHandler);
        elementHandler = handler;
    }

    public void handleAttribute(Element target, Attribute attrib, SimpleStack children) {
        attributeHandler.handleAttribute(target, attrib, children);
    }

    /**
	 * @param handler The AttributeHandler to add to the chain.
	 */
    public void addAttributeHandler(AttributeHandler handler) {
        handler.setNext(attributeHandler);
        attributeHandler = handler;
    }

    /**
	 * @param elem
	 * @param children
	 */
    public void handleChildren(Element elem, SimpleStack children) {
        childHandler.handleChildren(elem, children);
    }

    /**
	 * @param childHandler The ChildHandler to add to the chain.
	 */
    public void addChildHandler(ChildHandler handler) {
        handler.setNext(childHandler);
        childHandler = handler;
    }

    public void startWindow(String fileid, Container window) {
        windowHandler.startWindow(fileid, window);
    }

    public void endWindow() {
        windowHandler.endWindow();
    }

    /**
	 * @param windowHandler The WindowHandler to add to the chain.
	 */
    public void addWindowHandler(WindowHandler handler) {
        handler.setNext(handler);
        windowHandler = handler;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length);
        if (chars.trim().length() == 0) return;
        if (SAX_TRACE) System.out.println("characters:\t" + chars);
        if (!parseText) {
            buffer = new StringBuffer();
            parseText = true;
        }
        buffer.append(chars);
    }

    /**
	 * Part of the <tt>DefaultHandler</tt>, this method is invoked by the <tt>SaxParser</tt>
	 * when it begins an element tag.<p>
	 *
	 * In the default implementation, this method will compute
	 * a <tt>hashCode</tt> of the <tt>qName</tt> parameter, and capture
	 * this and the <tt>attrs</tt> parameter into an <tt>Element</tt> data structure.
	 *
	 * @see com.metasolutions.jfcml.helpers.Element
	 * @see javax.xml.parsers.SAXParser
	 * @see org.xml.sax.helpers.DefaultHandler
	 */
    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        if (SAX_TRACE) System.out.println("begin:\t" + qName + "\t" + lName + "\t" + context.getElements().size());
        int hash = qName.hashCode();
        if (hash == ELEMENT_JROOTPANE) context.pushRootPane();
        Element elem = new Element(qName, hash, new AttributesImpl(attrs), null);
        if (parseText) buffer.append(startElementString(elem)); else {
            context.setCurrentElement(elem);
            context.getElements().push(elem);
        }
    }

    /**
	 * Part of the <tt>DefaultHandler</tt>, this method is invoked by the <tt>SaxParser</tt>
	 * when it finds an end tag.<p>
	 * 
	 */
    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        int hash = qName.hashCode();
        SimpleStack elements = context.getElements();
        SimpleStack childElements = context.getChildElements();
        if (SAX_TRACE) System.out.println("end:\t" + qName + "\t" + sName + "\t" + elements.size());
        if (hash == ELEMENT_THIS) return;
        childElements.clear();
        Element t;
        while (hash != (t = (Element) elements.pop()).hash) childElements.push(t);
        if (!parseText) context.setCurrentElement(t); else {
            if (context.getCurrentElement().hash == hash) {
                childElements.clear();
                childElements.push(new Element("CDATA", "CDATA".hashCode(), null, buffer.toString()));
                parseText = false;
            } else {
                buffer.append(endElementString(t));
                return;
            }
        }
        handleElement(t, childElements);
        if (t.obj != null) {
            for (int i = t.attr.getLength(); i-- > 0; ) {
                currentAttribute.qName = t.attr.getQName(i);
                currentAttribute.value = t.attr.getValue(i);
                handleAttribute(t, currentAttribute, childElements);
            }
            handleChildren(t, childElements);
            if (t.obj != null) elements.push(t);
        }
        t.hash = 0;
    }

    /**
	 * Part of the <tt>DefaultHandler</tt>, this method is invoked by the <tt>SaxParser</tt>
	 * when an error occurs.<p>
	 *
	 * In the default implementation, this method simply rethrows the
	 * <tt>Exception</tt>.
	 *
	 * @see javax.xml.parsers.SAXParser
	 * @see org.xml.sax.helpers.DefaultHandler
	 */
    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }

    /**
     * Gets a String representing the start Element.
     * @param elem the Element
     * @return a String representing the start Element.
     */
    private String startElementString(Element elem) {
        StringBuffer buf = new StringBuffer("<" + elem.qName);
        for (int i = elem.attr.getLength(); i-- > 0; ) buf.append(" " + elem.attr.getQName(i) + "=\"" + elem.attr.getValue(i) + "\"");
        return buf.append('>').toString();
    }

    /**
     * Gets a String representing an end Element.
     * @param t the Element
     * @return a String representing the end Element. 
     */
    private String endElementString(Element t) {
        return "</" + t.qName + ">";
    }
}
