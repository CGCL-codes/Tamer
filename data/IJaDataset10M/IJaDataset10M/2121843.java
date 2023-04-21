package org.rakiura.cpn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Set of utilities for XML Document processing.
 * This is a class supporting the use of Sun Java XML API (jaxp).
 * <br>
 * <br>
 * Created: Mon Jun 28 14:51:25 1999<br>
 *
 *@author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 *@version @version@ $Revision: 1.10 $
 */
public final class XmlUtil {

    /**
	 * Normalizes the given string. */
    public static String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch(ch) {
                case '<':
                    {
                        str.append("&lt;");
                        break;
                    }
                case '>':
                    {
                        str.append("&gt;");
                        break;
                    }
                case '&':
                    {
                        str.append("&amp;");
                        break;
                    }
                case '"':
                    {
                        str.append("&quot;");
                        break;
                    }
                default:
                    {
                        str.append(ch);
                    }
            }
        }
        return str.toString();
    }

    public static Document parseFile(final File aFile) throws SAXException, ParserConfigurationException {
        Properties props;
        props = new Properties();
        return parseFile(aFile, props);
    }

    public static Document parseFile(final File aFile, final String propfilename) throws SAXException, ParserConfigurationException {
        Properties props;
        props = new Properties();
        try {
            InputStream propsStream = new FileInputStream(propfilename);
            props.load(propsStream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return parseFile(aFile, props);
    }

    public static Document parseFile(final File aFile, final Properties props) throws SAXException, ParserConfigurationException {
        InputStream input = null;
        try {
            input = new FileInputStream(aFile);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        return parse(input, props);
    }

    public static Document parse(InputStream input, Properties props) throws SAXException, ParserConfigurationException {
        return parse(new InputSource(input), props);
    }

    public static Document parse(String input) throws SAXException, ParserConfigurationException {
        return parse(new StringReader(input), null);
    }

    public static Document parse(Reader input) throws SAXException, ParserConfigurationException {
        return parse(new InputSource(input), null);
    }

    public static Document parse(Reader input, Properties props) throws SAXException, ParserConfigurationException {
        return parse(new InputSource(input), props);
    }

    public static Document parse(InputSource input, @SuppressWarnings("unused") Properties props) throws SAXException, ParserConfigurationException {
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(input);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return doc;
    }

    /**
	 * Creates ElementNode for given tag with particular text as content.
	 */
    public static Element createTextNode(String tag, String content) throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element node = doc.createElement(tag);
        Text t = doc.createTextNode(content);
        node.appendChild(t);
        return node;
    }

    /**
	 * Saves a given XML document to the given output stream.
	 */
    public static void writeXML(Document document, OutputStream os) throws IOException {
        DOMSource src = new DOMSource(document);
        StreamResult res = new StreamResult(os);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer t = tf.newTransformer();
            t.transform(src, res);
        } catch (TransformerException e) {
            throw new IOException(e.getMessage());
        }
    }
}
