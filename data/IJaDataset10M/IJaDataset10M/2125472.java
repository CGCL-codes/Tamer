package org.columba.core.xml;

import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.columba.core.logging.Logging;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlIO extends DefaultHandler {

    private static final Logger LOG = Logger.getLogger("org.columba.core.xml");

    private static final String ROOT_XML_ELEMENT_NAME = "__COLUMBA_XML_TREE_TOP__";

    private XmlElement rootElement;

    private XmlElement currentElement;

    private int writeIndent = 2;

    private int maxOneLineData = 20;

    private CharArrayWriter contents = new CharArrayWriter();

    private URL url = null;

    public XmlIO(URL url) {
        super();
        this.url = url;
    }

    public XmlIO() {
        currentElement = null;
    }

    /**
	 * Creates a XmlIO object with the specified element at the top.
	 * 
	 * @param element
	 *            the element at the top.
	 */
    public XmlIO(XmlElement element) {
        rootElement = new XmlElement(ROOT_XML_ELEMENT_NAME);
        rootElement.addElement(element);
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public boolean load() {
        return load(url);
    }

    /**
	 * Loads from the InputStream into the root Xml Element.
	 * 
	 * @param input
	 *            the input stream to load from.
	 */
    public boolean load(InputStream input) {
        rootElement = new XmlElement(ROOT_XML_ELEMENT_NAME);
        currentElement = rootElement;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(input, this);
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            LOG.severe("XML config error while attempting to read from the input stream \n'" + input + "'");
            LOG.severe(ex.toString());
            ex.printStackTrace();
            return (false);
        } catch (SAXException ex) {
            LOG.severe("XML parse error while attempting to read from the input stream \n'" + input + "'");
            LOG.severe(ex.toString());
            ex.printStackTrace();
            return (false);
        } catch (IOException ex) {
            LOG.severe("I/O error while attempting to read from the input stream \n'" + input + "'");
            LOG.severe(ex.toString());
            ex.printStackTrace();
            return (false);
        }
        return (true);
    }

    /**
	 * Load a file. This is what starts things off.
	 * 
	 * @param inputURL
	 *            the URL to load XML from.
	 */
    public boolean load(URL inputURL) {
        rootElement = new XmlElement(ROOT_XML_ELEMENT_NAME);
        currentElement = rootElement;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputURL.toString(), this);
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            LOG.severe("XML config error while attempting to read XML file \n'" + inputURL + "'");
            LOG.severe(ex.toString());
            if (Logging.DEBUG) ex.printStackTrace();
            return (false);
        } catch (SAXException ex) {
            LOG.severe("XML parse error while attempting to read XML file \n'" + inputURL + "'");
            LOG.severe(ex.toString());
            if (Logging.DEBUG) ex.printStackTrace();
            return (false);
        } catch (IOException ex) {
            LOG.severe("I/O error while attempting to read XML file \n'" + inputURL + "'");
            LOG.severe(ex.toString());
            if (Logging.DEBUG) ex.printStackTrace();
            return (false);
        }
        return (true);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
        try {
            contents.reset();
            String name = localName;
            if (name.equals("")) {
                name = qName;
            }
            XmlElement p = currentElement;
            currentElement = currentElement.addSubElement(name);
            currentElement.setParent(p);
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i);
                    if (aName.equals("")) {
                        aName = attrs.getQName(i);
                    }
                    currentElement.addAttribute(aName, attrs.getValue(i));
                }
            }
        } catch (java.lang.NullPointerException ex) {
            LOG.severe("Null!!!");
            LOG.severe(ex.toString());
            ex.printStackTrace();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        currentElement.setData(contents.toString().trim());
        contents.reset();
        currentElement = currentElement.getParent();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        contents.write(ch, start, length);
    }

    /**
	 * Returns the root for the XmlElement hiearchy. Note that this Xml Element
	 * will always have the name <code>__COLUMBA_XML_TREE_TOP__</code>.
	 * <p>
	 * Methods that want to retrieve elements from this root should use the
	 * {@link XmlElement#getElement(String)} in order to get the wanted element.
	 * 
	 * @return a XmlElement if it has been loaded or initialized with it; null
	 *         otherwise.
	 */
    public XmlElement getRoot() {
        return (rootElement);
    }

    public void errorDialog(String Msg) {
        JOptionPane.showMessageDialog(null, "Error: " + Msg);
    }

    public void warningDialog(String Msg) {
        JOptionPane.showMessageDialog(null, "Warning: " + Msg);
    }

    public void infoDialog(String Msg) {
        JOptionPane.showMessageDialog(null, "Info: " + Msg);
    }

    public void save() throws Exception {
        write(new FileOutputStream(url.getPath()));
    }

    public void write(OutputStream out) throws IOException {
        BufferedWriter PW = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        PW.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        if (rootElement.subElements.size() > 0) {
            for (int i = 0; i < rootElement.subElements.size(); i++) {
                _writeSubNode(PW, (XmlElement) rootElement.subElements.get(i), 0);
            }
        }
        PW.flush();
    }

    private void _writeSubNode(Writer out, XmlElement element, int indent) throws IOException {
        _writeSpace(out, indent);
        out.write("<");
        out.write(element.getName());
        for (Enumeration e = element.getAttributeNames(); e.hasMoreElements(); ) {
            String K = (String) e.nextElement();
            out.write(" " + K + "=\"" + TextUtils.escapeText(element.getAttribute(K)) + "\"");
        }
        out.write(">");
        String data = element.getData();
        if ((data != null) && !data.equals("")) {
            if (data.length() > maxOneLineData) {
                out.write("\n");
                _writeSpace(out, indent + writeIndent);
            }
            out.write(TextUtils.escapeText(data));
        }
        List subElements = element.getElements();
        if (subElements.size() > 0) {
            out.write("\n");
            for (Iterator it = subElements.iterator(); it.hasNext(); ) {
                _writeSubNode(out, (XmlElement) it.next(), indent + writeIndent);
            }
            _writeSpace(out, indent);
        }
        if (data.length() > maxOneLineData) {
            out.write("\n");
            _writeSpace(out, indent);
        }
        out.write("</" + TextUtils.escapeText(element.getName()) + ">\n");
    }

    private void _writeSpace(Writer out, int numSpaces) throws IOException {
        for (int i = 0; i < numSpaces; i++) {
            out.write(" ");
        }
    }
}
