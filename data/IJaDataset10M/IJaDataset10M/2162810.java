package org.dbe.composer.wfengine.bpel.server.deploy.validate;

import java.text.MessageFormat;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.def.validation.ISdlBaseErrorReporter;
import org.dbe.composer.wfengine.bpel.deploy.SdlDeploymentSchemas;
import org.dbe.composer.wfengine.bpel.server.deploy.bpr.IBprFile;
import org.dbe.composer.wfengine.xml.XMLParserBase;
import org.w3c.dom.Document;

/**
 *  Schema validation of pdd files.
 */
public class PddSchemaValidator extends AbstractPddIterator {

    private static final Logger logger = Logger.getLogger(PddSchemaValidator.class.getName());

    /** Error message template for invalid PDD file. */
    private static final String PDD_SCHEMA = "wrong PDD schema";

    /** Console error message. */
    private static final String CONSOLE_ERROR = "console error";

    protected void validateImpl(SdlPddInfo aPddInfo, IBprFile aBprFile, ISdlBaseErrorReporter aReporter) throws SdlException {
        logger.debug("validateImpl() " + aPddInfo.getName());
        XMLParserBase parser = new XMLParserBase();
        parser.setNamespaceAware(true);
        parser.setValidating(true);
        if (!isValidPdd(aPddInfo, parser)) {
            logger.error("wrong PDD schema ");
            String[] args = { aPddInfo.getName() };
            aReporter.addError(PDD_SCHEMA, args, null);
        }
    }

    /**
     * Validates the pdd dom against its schema.
     * @param aPddInfo
     * @param aParser
     */
    private boolean isValidPdd(SdlPddInfo aPddInfo, XMLParserBase aParser) {
        boolean isValid = false;
        logger.debug("isValidPdd() " + aPddInfo.getName() + ", loading pdd Schemas ");
        try {
            Iterator schemaIt = SdlDeploymentSchemas.getPddSchemas();
            Document doc = aPddInfo.getDoc();
            aParser.validateWsdlDocument(doc, schemaIt);
            isValid = !aParser.hasParseWarnings();
        } catch (SdlException e) {
            logger.error("Error: " + e);
            SdlException.logError(e, MessageFormat.format(CONSOLE_ERROR, new String[] { aPddInfo.getName() }));
        } finally {
            aParser.resetParseWarnings();
        }
        logger.info("isValidPdd() " + isValid + " for " + aPddInfo.getName());
        return isValid;
    }
}
