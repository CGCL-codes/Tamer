package org.brandao.jcptbr.ejb.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.brandao.jcptbr.ejb.persistence.xml.parser.PersistenceUnitHandler;
import org.brandao.jcptbr.ejb.persistence.xml.parser.XMLPersistenceError;
import org.brandao.jcptbr.ejb.persistence.xml.parser.XMLPersistenceUnitConstants;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Brandao
 */
public class PersistenceUnitProcessor {

    private static Map<String, PersistenceUnitInfo> units = new HashMap<String, PersistenceUnitInfo>();

    private static boolean initialized = false;

    public static void startProcessor() {
        if (!isInitialized()) {
            initProcessor();
        }
    }

    public static PersistenceUnitInfo getPersistenceUnit(String id) {
        if (units.containsKey(id)) return units.get(id); else throw new ExceptionInInitializerError("persistence unit " + id + " not found!");
    }

    private static void initProcessor() {
        try {
            List<PersistenceUnitInfo> unts = getAllPersistenceUnits();
            for (PersistenceUnitInfo unit : unts) units.put(unit.getPersistenceUnitName(), unit);
            initialized = true;
        } catch (ExceptionInInitializerError exi) {
            throw exi;
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static List<PersistenceUnitInfo> getAllPersistenceUnits() throws IOException, SAXException, ParserConfigurationException {
        Enumeration<URL> resources = getAllResources();
        return processResources(resources);
    }

    public static Enumeration<URL> getAllResources() throws IOException {
        Enumeration<URL> resources = getClassLoader().getResources("META-INF/persistence.xml");
        return resources;
    }

    public static List<PersistenceUnitInfo> processResources(Enumeration<URL> resources) throws IOException, SAXException, ParserConfigurationException {
        List<PersistenceUnitInfo> localUnits = null;
        while (resources.hasMoreElements()) {
            List<PersistenceUnitInfo> unts = processResource(resources.nextElement());
            if (localUnits == null) localUnits = unts; else localUnits.addAll(unts);
        }
        return localUnits == null ? new ArrayList<PersistenceUnitInfo>() : localUnits;
    }

    public static List<PersistenceUnitInfo> processResource(URL resource) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(resource.getFile());
        InputStream in = resource.openStream();
        return processPersistenceXML(in);
    }

    private static List<PersistenceUnitInfo> processPersistenceXML(InputStream input) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        XMLReader xmlReader = null;
        PersistenceUnitHandler handler = new PersistenceUnitHandler();
        XMLPersistenceError errorHandler = new XMLPersistenceError();
        InputSource inSource = new InputSource(input);
        URL schemaURL = getClassLoader().getResource(XMLPersistenceUnitConstants.PERSISTENCE_SCHEMA);
        saxFactory.setValidating(true);
        saxFactory.setNamespaceAware(true);
        parser = saxFactory.newSAXParser();
        parser.setProperty(XMLPersistenceUnitConstants.JAXP_SCHEMA_LANGUAGE, XMLPersistenceUnitConstants.XML_SCHEMA);
        xmlReader = parser.getXMLReader();
        xmlReader.setProperty(XMLPersistenceUnitConstants.JAXP_SCHEMA_SOURCE, schemaURL.toString());
        xmlReader.setContentHandler(handler);
        xmlReader.setErrorHandler(errorHandler);
        xmlReader.parse(inSource);
        if (errorHandler.getException() != null) throw errorHandler.getException();
        return handler.getPersistenceUnits();
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
