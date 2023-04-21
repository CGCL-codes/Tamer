package de.uni_leipzig.lots.common.xml;

import de.uni_leipzig.lots.common.objects.task.TaskCollection;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlTaskImport {

    protected static final Logger logger = Logger.getLogger(XmlTaskImport.class.getName());

    private SAXParserFactory factory;

    private EntityResolver resolver;

    private FileFilter acceptAllFilter;

    public XmlTaskImport() {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        acceptAllFilter = new FileFilter() {

            public boolean accept(File pathname) {
                return true;
            }
        };
    }

    /**
     * Sets the resolver for resolving external entities.
     *
     * @param resolver
     */
    public void setResolver(EntityResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Läd alle Dateien in einem Verzeichnis.
     *
     * @param file
     * @return eine <tt>Collection</tt> von <tt>TaskCollection</tt>'s
     * @throws IOException
     * @throws SAXException
     */
    public Collection<TaskCollection> loadDir(File file) throws IOException, SAXException {
        return loadDir(file, acceptAllFilter);
    }

    /**
     * Läd alle Dateien in einem Verzeichnis.
     *
     * @param file
     * @param fileFilter
     * @return eine <tt>Collection</tt> von <tt>TaskCollection</tt>'s
     * @throws IOException
     * @throws SAXException
     */
    public Collection<TaskCollection> loadDir(File file, FileFilter fileFilter) throws IOException, SAXException {
        if (!file.exists()) {
            throw new FileNotFoundException("The file: " + file.getAbsolutePath() + " was not found.");
        }
        if (!file.canRead()) {
            throw new IOException("Can't read the file: " + file.getAbsolutePath() + ".");
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles(fileFilter);
            if (files.length > 0) {
                logger.fine("use taskCollections recursive from directory: " + file.getName());
                ArrayList taskCollections = new ArrayList();
                for (int i = 0; i < files.length; i++) {
                    taskCollections.addAll(loadDir(files[i], fileFilter));
                }
                taskCollections.trimToSize();
                return taskCollections;
            } else {
                return new ArrayList(0);
            }
        }
        if (!fileFilter.accept(file)) {
            logger.fine("Don't use the file: " + file.getName() + " it is filtered by: " + fileFilter);
            return new ArrayList(0);
        }
        logger.fine("use tasks from file: " + file.getName());
        InputSource inputSource = new InputSource();
        inputSource.setSystemId(file.getCanonicalPath());
        inputSource.setCharacterStream(new FileReader(file));
        List tmp = new ArrayList(1);
        tmp.add(load(inputSource));
        return tmp;
    }

    public TaskCollection load(Reader input) throws IOException, SAXException {
        return load(new InputSource(input));
    }

    public TaskCollection load(InputStream input) throws IOException, SAXException {
        return load(new InputSource(input));
    }

    /**
     * Parses the input into a taskcollection.
     *
     * @param input input to parse
     * @return a generated taskcollection
     * @throws IOException  io error
     * @throws SAXException parsing error
     */
    public synchronized TaskCollection load(InputSource input) throws IOException, SAXException {
        logger.fine(appendInputSource(new StringBuffer("parse inputsource: "), input).toString());
        XMLReader reader = getXMLReader();
        TaskCollectionHandler taskCollectionHandler = new TaskCollectionHandler();
        reader.setContentHandler(taskCollectionHandler);
        reader.setErrorHandler(taskCollectionHandler);
        reader.parse(input);
        if (input.getByteStream() != null) input.getByteStream().close();
        if (input.getCharacterStream() != null) input.getCharacterStream().close();
        return taskCollectionHandler.getTaskCollection();
    }

    private XMLReader getXMLReader() throws SAXException {
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            if (resolver != null) {
                saxParser.getXMLReader().setEntityResolver(resolver);
            } else {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.logp(Level.WARNING, getClass().getName(), "getXMLReader", "No resolver is set. Use default resolving of external entities.");
                }
            }
        } catch (ParserConfigurationException e) {
            logger.logp(Level.SEVERE, "XmlTaskImport", "getXMLReader", "ParserConfigurationException while creating a new SAX parser.", e);
            throw new SAXException("ParserConfigurationException while creating a new parser.", e);
        } catch (SAXException e) {
            logger.logp(Level.SEVERE, "XmlTaskImport", "getXMLReader", "SAXException while creating a new SAX parser.", e);
            throw e;
        }
        if (logger.isLoggable(Level.CONFIG)) {
            logger.logp(Level.CONFIG, "XmlTaskImport", "getXMLReader", "parser name = " + saxParser.getClass().getName());
            logger.logp(Level.CONFIG, "XmlTaskImport", "getXMLReader", "parser is validating = " + saxParser.isValidating());
            logger.logp(Level.CONFIG, "XmlTaskImport", "getXMLReader", "parser is namespace aware = " + saxParser.isNamespaceAware());
        }
        return saxParser.getXMLReader();
    }

    private static StringBuffer appendInputSource(StringBuffer sb, InputSource input) {
        return sb.append("publicId = ").append(input.getPublicId()).append(", systemId = ").append(input.getSystemId());
    }
}
