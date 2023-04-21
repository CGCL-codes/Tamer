package net.sf.reqbook.services.validator;

import net.sf.reqbook.services.pipe.DefaultPipeHandler;
import net.sf.reqbook.services.pipe.SAXErrorsProducer;
import org.apache.log4j.Logger;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierHandler;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * $Id: RNGValidatorPipeHandler.java,v 1.7 2006/01/08 10:13:52 pavel_sher Exp $
 *
 * @author Pavel Sher
 */
public class RNGValidatorPipeHandler extends DefaultPipeHandler implements SAXErrorsProducer {

    private Schema schema;

    private VerifierHandler verifierHandler;

    private static final Logger logger = Logger.getLogger(RNGValidatorPipeHandler.class);

    public RNGValidatorPipeHandler(Schema schema) {
        this.schema = schema;
    }

    private VerifierHandler getVerifierHandler() throws SAXException {
        if (verifierHandler == null) {
            try {
                Verifier verifier = schema.newVerifier();
                verifier.setErrorHandler(this);
                verifierHandler = verifier.getVerifierHandler();
            } catch (VerifierConfigurationException e) {
                throw new SAXException(e);
            }
        }
        return verifierHandler;
    }

    public void setDocumentLocator(Locator locator) {
        verifierHandler = null;
        try {
            getVerifierHandler().setDocumentLocator(locator);
        } catch (SAXException e) {
        }
        super.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
        logger.info("Starting validation...");
        getVerifierHandler().startDocument();
        super.startDocument();
    }

    public void endDocument() throws SAXException {
        getVerifierHandler().endDocument();
        logger.info("Validation finished");
        super.endDocument();
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        getVerifierHandler().startPrefixMapping(prefix, uri);
        super.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        getVerifierHandler().endPrefixMapping(prefix);
        super.endPrefixMapping(prefix);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        getVerifierHandler().startElement(uri, localName, qName, atts);
        super.startElement(uri, localName, qName, atts);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        getVerifierHandler().endElement(uri, localName, qName);
        super.endElement(uri, localName, qName);
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        getVerifierHandler().characters(ch, start, length);
        super.characters(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        getVerifierHandler().ignorableWhitespace(ch, start, length);
        super.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        getVerifierHandler().processingInstruction(target, data);
        super.processingInstruction(target, data);
    }

    public void skippedEntity(String name) throws SAXException {
        getVerifierHandler().skippedEntity(name);
        super.skippedEntity(name);
    }
}
