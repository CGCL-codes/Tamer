package simjistwrapper.xml;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import simjistwrapper.exceptions.ParsingException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserSimFile {

    public ParserSimFile() {
    }

    public Document parse(File file) throws ParsingException {
        Document fileParser = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void error(SAXParseException e) throws SAXParseException {
                    throw e;
                }

                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            });
            fileParser = builder.parse(file);
        } catch (SAXParseException spe) {
            String msg = "\n** Parsing error" + ", line " + spe.getLineNumber() + ", uri " + spe.getSystemId() + "\n   " + spe.getMessage();
            throw new ParsingException(msg, spe);
        } catch (SAXException sxe) {
            throw new ParsingException("Error generated during parsing", sxe);
        } catch (ParserConfigurationException pce) {
            throw new ParsingException("ParserOld with specified options can't be built", pce);
        } catch (IOException ioe) {
            throw new ParsingException("I/O Error during parsing", ioe);
        }
        return fileParser;
    }
}
