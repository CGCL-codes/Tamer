package ch.nostromo.lib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Tools XML contains some usefull "xml" functions.
 * 
 * @author Bernhard von Gunten <bvg@users.sourceforge.net>
 */
public class ToolsXML {

    /** The Constant SIMPLE_DATE_FORMAT. */
    public static final String SIMPLE_DATE_FORMAT = "dd.MM.yyyy";

    /**
   * Description of the Method.
   * 
   * @param file Description of the Parameter
   * 
   * @return Return Value
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    public static Document loadXmlFile(File file) throws Exception {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            return domBuilder.parse(file);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Gets the first sub element.
   * 
   * @param parent the parent
   * @param name the name
   * 
   * @return the first sub element
   */
    public static Element getFirstSubElement(Element parent, String name) {
        NodeList list = parent.getElementsByTagName(name);
        return (Element) list.item(0);
    }

    /**
   * Description of the Method.
   * 
   * @param file Description of the Parameter
   * @param doc Description of the Parameter
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    public static void saveXmlFile(File file, Document doc) throws Exception {
        try {
            Source source = new DOMSource(doc);
            FileOutputStream outputStream = new FileOutputStream(file);
            Result result = new StreamResult(new OutputStreamWriter(outputStream, "UTF8"));
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", new Integer(2));
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Description of the Method.
   * 
   * @param xmlString Description of the Parameter
   * 
   * @return Return Value
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    public static Document stringToXml(String xmlString) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            return factory.newDocumentBuilder().parse(inputSource);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Description of the Method.
   * 
   * @param doc Description of the Parameter
   * 
   * @return Return Value
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    public static String xmlToString(Document doc) throws Exception {
        try {
            Source source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            return stringWriter.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Description of the Method.
   * 
   * @return Return Value
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    public static Document createXmlDocument() throws Exception {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Convert simple calendar.
   * 
   * @param day the day
   * 
   * @return the string
   */
    public static String convertSimpleCalendar(Calendar day) {
        SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        return df.format(day.getTime());
    }

    /**
   * Gets the simple calendar attribute.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the simple calendar attribute
   * 
   * @throws Exception the exception
   */
    public static Calendar getSimpleCalendarAttribute(Element parent, String key) throws Exception {
        String value = parent.getAttribute(key);
        SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date date = df.parse(value);
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        return result;
    }

    /**
   * Gets the simple calendar element.
   * 
   * @param parent the parent
   * @param key the key
   * 
   * @return the simple calendar element
   * 
   * @throws Exception the exception
   */
    public static Calendar getSimpleCalendarElement(Element parent, String key) throws Exception {
        String value = getTextElement(parent, key);
        SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date date = df.parse(value);
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        return result;
    }

    /**
   * Gets the int attribute.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the int attribute
   */
    public static int getIntAttribute(Element parent, String key) {
        String value = parent.getAttribute(key);
        return Integer.valueOf(value).intValue();
    }

    /**
   * Gets the int element.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the int element
   */
    public static int getIntElement(Element parent, String key) {
        Element el = getFirstSubElement(parent, key);
        String value = el.getTextContent();
        return Integer.valueOf(value).intValue();
    }

    /**
   * Gets the double attribute.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the double attribute
   */
    public static double getDoubleAttribute(Element parent, String key) {
        String value = parent.getAttribute(key);
        return Double.valueOf(value).doubleValue();
    }

    /**
   * Gets the double element.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the double element
   */
    public static double getDoubleElement(Element parent, String key) {
        Element el = getFirstSubElement(parent, key);
        String value = el.getTextContent();
        return Double.valueOf(value).doubleValue();
    }

    /**
   * Gets the boolean attribute.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the boolean attribute
   */
    public static boolean getBooleanAttribute(Element parent, String key) {
        String value = parent.getAttribute(key);
        return Boolean.valueOf(value).booleanValue();
    }

    /**
   * Gets the boolean element.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the boolean element
   */
    public static boolean getBooleanElement(Element parent, String key) {
        Element el = getFirstSubElement(parent, key);
        String value = el.getTextContent();
        return Boolean.valueOf(value).booleanValue();
    }

    /**
   * Gets the attribute.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the attribute
   */
    public static String getTextAttribute(Element parent, String key) {
        return parent.getAttribute(key);
    }

    /**
   * Gets the text element.
   * 
   * @param key the key
   * @param parent the parent
   * 
   * @return the text element
   */
    public static String getTextElement(Element parent, String key) {
        Element el = getFirstSubElement(parent, key);
        return el.getTextContent();
    }

    /**
   * Adds the element.
   * 
   * @param parent the parent
   * @param key the key
   * @param value the value
   * 
   * @return the element
   */
    public static Element appendElement(Element parent, String key, boolean value) {
        return appendElement(parent, key, String.valueOf(value));
    }

    /**
   * Adds the element.
   * 
   * @param parent the parent
   * @param key the key
   * @param value the value
   * 
   * @return the element
   */
    public static Element appendElement(Element parent, String key, double value) {
        return appendElement(parent, key, String.valueOf(value));
    }

    /**
   * Adds the element.
   * 
   * @param parent the parent
   * @param key the key
   * @param value the value
   * 
   * @return the element
   */
    public static Element appendElement(Element parent, String key, int value) {
        return appendElement(parent, key, String.valueOf(value));
    }

    /**
   * Adds the element.
   * 
   * @param parent the parent
   * @param key the key
   * @param value the value
   * 
   * @return the element
   */
    public static Element appendElement(Element parent, String key, String value) {
        Element element = appendElement(parent, key);
        element.setTextContent(value);
        parent.appendChild(element);
        return element;
    }

    /**
   * Adds the emptyelement.
   * 
   * @param parent the parent
   * @param key the key
   * 
   * @return the element
   */
    public static Element appendElement(Element parent, String key) {
        Element element = parent.getOwnerDocument().createElement(key);
        parent.appendChild(element);
        return element;
    }
}
