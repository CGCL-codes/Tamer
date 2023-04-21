package org.equanda.tapestry.parser.descriptions;

import org.equanda.tapestry.model.GMDatabase;
import org.equanda.tapestry.model.GMFactory;
import org.equanda.tapestry.model.GMTable;
import org.equanda.util.xml.XMLParser;
import org.equanda.util.xml.tree.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class to parse XML descriptions and provide information needed in the GUI classes
 *
 * @author Florin
 */
public class Parser {

    Document doc;

    public Parser(String xmlFileName) throws SAXException, IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
        if (stream != null) {
            parse(stream);
        } else {
            throw new IOException("description file not found " + xmlFileName);
        }
    }

    public Parser(File file) throws SAXException, IOException {
        if (file != null) {
            parse(new FileInputStream(file));
        } else {
            throw new IOException("file is required for parsing");
        }
    }

    public Parser(InputStream stream) throws SAXException, IOException {
        if (stream != null) {
            parse(stream);
        } else {
            throw new IOException("non null stream is required for parsing");
        }
    }

    protected void parse(InputStream stream) throws SAXException, IOException {
        GMFactory factory = new GMFactory();
        XMLParser parser = new XMLParser(factory, false, null, null);
        parser.setErrorHandler(new ErrorHandler() {

            public void error(SAXParseException x) throws SAXException {
                throw x;
            }

            public void fatalError(SAXParseException x) throws SAXException {
                throw x;
            }

            public void warning(SAXParseException x) throws SAXException {
                throw x;
            }
        });
        if (stream != null) {
            doc = parser.parse(stream);
        } else {
            throw new IOException("file is required for parsing");
        }
    }

    public boolean isDocumentTable() {
        return doc.getDocumentRoot() instanceof GMTable;
    }

    public boolean isDocumentDatabase() {
        return doc.getDocumentRoot() instanceof GMDatabase;
    }

    public GMTable getTable() {
        if (!isDocumentTable()) return null;
        return (GMTable) doc.getDocumentRoot();
    }

    public GMDatabase getDatabase() {
        if (!isDocumentDatabase()) return null;
        return (GMDatabase) doc.getDocumentRoot();
    }
}
