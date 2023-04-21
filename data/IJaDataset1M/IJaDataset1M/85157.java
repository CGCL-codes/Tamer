package uk.ac.ed.rapid.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Reads and validates the Rapid XML File
 * 
 * @author Jos Koetsier
 */
public class RapidXML {

    public static URL getXMLSymbolFile() {
        ClassLoader l = RapidXML.class.getClassLoader();
        Object o = l.getResource(RapidXMLConstants.XMLFILE_SYMBOL);
        return (URL) o;
    }

    public static URL getXMLFile() {
        ClassLoader l = RapidXML.class.getClassLoader();
        Object o = l.getResource(RapidXMLConstants.XMLFILE);
        return (URL) o;
    }

    public static String getClassDirectory() {
        URL path = getXMLSymbolFile();
        String result = path.toString();
        return result.substring(0, result.length() - RapidXMLConstants.XMLFILE_SYMBOL.length());
    }

    public static URL getXSLFile() {
        ClassLoader l = RapidXML.class.getClassLoader();
        Object o = l.getResource(RapidXMLConstants.XSLFILE);
        return (URL) o;
    }

    public static URL getCustomXMLFile(String file) {
        ClassLoader l = RapidXML.class.getClassLoader();
        Object o = l.getResource(file);
        return (URL) o;
    }
}
