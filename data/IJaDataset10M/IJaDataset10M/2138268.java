package hermesserver.dictionaryHandler;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.HashMap;
import java.util.Enumeration;

/**
 *
 * @author Simon Thompson MEng CEng MIET
 */
public class hermesDictionaryHandler {

    HashMap hash;

    String filename;

    boolean verbose = false;

    int dictionarySize = 0;

    Document dom;

    NodeList nl;

    Vector keys;

    Vector defn;

    public hermesDictionaryHandler(String filename) {
        this.filename = filename;
        hash = new HashMap();
    }

    public void parseXML() {
        try {
            File xmlFile = new File(filename);
            dom = openFileForParsing(xmlFile);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Element docEle = dom.getDocumentElement();
        nl = docEle.getElementsByTagName("dictionary-item");
        for (int i = 0; i < nl.getLength(); i++) {
            Element el = (Element) nl.item(i);
            if (el != null) {
                try {
                    String key = getTextValue(el, "term");
                    String keyLowerCase = key.toLowerCase();
                    String def = getTextValue(el, "definition");
                    System.out.println("DICTIONARY ITEM - KEY: " + keyLowerCase + " DEFN: " + def);
                    hash.put(keyLowerCase, def);
                    dictionarySize++;
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }

    /**
         * Opens a file ready for parsing, creating a DocumentBuilderFactory
         * @param file Config XML filename
         * @return DocumentBuilderFactory
         * @throws SAXException
         * @throws IOException
         * @throws ParserConfigurationException
         */
    public Document openFileForParsing(File file) throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    /**
         * Returns the text value within a tag
         * @param ele element
         * @param tagName tag to be found within element
         * @return
         */
    public String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nltv = ele.getElementsByTagName(tagName);
        if (nltv != null && nltv.getLength() > 0) {
            Element el = (Element) nltv.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }

    public Vector getVectorofTextValues(Element ele, String tagName) {
        Vector textValues = new Vector();
        NodeList nltv = ele.getElementsByTagName(tagName);
        if (nltv != null && nltv.getLength() > 0) {
            for (int i = 0; i < nltv.getLength(); i++) {
                Element el = (Element) nltv.item(i);
                String textVal = el.getFirstChild().getNodeValue();
                System.out.println(textVal);
                textValues.addElement(textVal);
            }
        }
        return textValues;
    }

    /**
	 * Calls getTextValue and returns a int value
         * @param ele element
         * @param tagName tag to be found within element
	 */
    private int getIntValue(Element ele, String tagName) {
        System.out.println(Integer.parseInt(getTextValue(ele, tagName)));
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    public boolean hasDefinition(String key) {
        String keyLowerCase = key.toLowerCase();
        return hash.containsKey(keyLowerCase);
    }

    public String getDefinition(String key) {
        String keyLowerCase = key.toLowerCase();
        return (String) hash.get(keyLowerCase);
    }
}
