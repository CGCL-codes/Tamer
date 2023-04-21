package org.apache.maven.bootstrap.util;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Parse an XML file.
 *
 * @version $Id: AbstractReader.java 420409 2006-07-10 03:31:52Z kenney $
 */
public abstract class AbstractReader extends DefaultHandler {

    private SAXParserFactory saxFactory;

    public void parse(File file) throws ParserConfigurationException, SAXException, IOException {
        saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtil.copy(new FileInputStream(file), output);
        String out = output.toString("UTF-8");
        out = StringUtils.replace(out, "&oslash;", "ø");
        out = StringUtils.replace(out, "&copy;", "©");
        out = StringUtils.replace(out, "&ndash;", "–");
        InputSource is = new InputSource(new ByteArrayInputStream(out.getBytes("UTF-8")));
        try {
            parser.parse(is, this);
        } catch (SAXException e) {
            System.err.println("Error reading POM: " + file);
            throw e;
        }
    }

    public void warning(SAXParseException spe) {
        printParseError("Warning", spe);
    }

    public void error(SAXParseException spe) {
        printParseError("Error", spe);
    }

    public void fatalError(SAXParseException spe) {
        printParseError("Fatal Error", spe);
    }

    private final void printParseError(String type, SAXParseException spe) {
        System.err.println(type + " [line " + spe.getLineNumber() + ", row " + spe.getColumnNumber() + "]: " + spe.getMessage());
    }
}
