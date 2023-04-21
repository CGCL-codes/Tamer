package net.sf.gateway.mef.businessrules.ty2010.R000;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.gateway.mef.businessrules.baserules.R000Rule;
import net.sf.gateway.mef.utilities.SchemaValidator;
import net.sf.gateway.mef.utilities.ZipUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class R000_00000_040 extends R000Rule {

    /**
     * Logging.
     */
    private static final Log LOG = LogFactory.getLog(R000_00000_040.class);

    public R000_00000_040() {
        this.setRuleNumber("R000-00000-040");
        this.setState("Vermont");
        this.setRuleText("The XML data has failed schema validation.");
        this.setErrorCategory("XML Error");
        this.setSeverity("Reject and Stop");
        this.setDataValue("");
        this.setXpath("");
    }

    @Override
    public boolean validate(byte[] zipdata) {
        if (!ZipUtils.isZipFile(zipdata)) {
            LOG.error("<" + this.getRuleNumber() + "> File is not a zip file.");
            return false;
        }
        Map<String, byte[]> files = null;
        try {
            files = ZipUtils.unzipFiles(zipdata);
        } catch (IOException e) {
            LOG.error("<" + this.getRuleNumber() + "> File failed to unzip properly.", e);
            return false;
        }
        byte[] manifestBytes = files.get("/manifest/manifest.xml");
        if (manifestBytes == null) {
            LOG.error("<" + this.getRuleNumber() + "> Zip file doesn't contain a manifest.");
            this.setDataValue("/manifest/manifest.xml");
            return false;
        }
        String manifestString = new String(manifestBytes);
        DocumentBuilder builder;
        Document manifest;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(manifestString));
            manifest = builder.parse(is);
        } catch (Exception e) {
            LOG.error("<" + this.getRuleNumber() + "> Can't parse the manifest", e);
            this.setDataValue("/manifest/manifest.xml");
            return false;
        }
        boolean isManifestValid = SchemaValidator.validate(manifest, SchemaValidator.TY2009_2009v4o1_Common, SchemaValidator.efileAttachments_xsd);
        if (!isManifestValid) {
            LOG.error("<" + this.getRuleNumber() + "> Schema validation of the manifest file failed.");
            this.setDataValue("/manifest/manifest.xml");
            return false;
        }
        byte[] submissionBytes = files.get("/xml/submission.xml");
        if (submissionBytes == null) {
            LOG.error("<" + this.getRuleNumber() + "> Zip file doesn't contain the required submission.xml file");
            this.setDataValue("/xml/submission.xml");
            return false;
        }
        String submissionString = new String(submissionBytes);
        Document submission;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(submissionString));
            submission = builder.parse(is);
        } catch (Exception e) {
            LOG.error("<" + this.getRuleNumber() + "> Can't parse the submission", e);
            this.setDataValue("/xml/submission.xml");
            return false;
        }
        NamespaceContext ctx = SchemaValidator.getEFileNamespaceContext();
        String submissionType = "";
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(ctx);
            submissionType = xpath.evaluate("/efil:StateSubmissionManifest/efil:SubmissionType", manifest);
        } catch (XPathExpressionException e) {
            LOG.error("<" + this.getRuleNumber() + "> XPath Error", e);
            return false;
        }
        if (submissionType == null || "".equals(submissionType)) {
            LOG.error("<" + this.getRuleNumber() + "> couldn't parse SubmissionType");
            return false;
        }
        if ("VTIN111".equals(submissionType)) {
            boolean isSubmissionValid = SchemaValidator.validate(submission, SchemaValidator.TY2009_VTIndividual2009V1o0_VTIndivdual, SchemaValidator.IndividualReturnVT111_xsd);
            if (!isSubmissionValid) {
                LOG.error("<" + this.getRuleNumber() + "> Schema validation of a " + submissionType + " submission file failed: " + SchemaValidator.xmlSchemaValidationErrorMessage);
                this.setDataValue("/xml/submission.xml");
                return false;
            }
        } else {
            boolean isSubmissionValid = SchemaValidator.validate(submission, SchemaValidator.TY2009_VTIndividual2009V1o0_VTIndivdual, SchemaValidator.IndividualReturnVT122_xsd);
            if (!isSubmissionValid) {
                LOG.error("<" + this.getRuleNumber() + "> Schema validation of a " + submissionType + " submission file failed: " + SchemaValidator.xmlSchemaValidationErrorMessage);
                this.setDataValue("/xml/submission.xml");
                return false;
            }
        }
        LOG.info("<" + this.getRuleNumber() + "> Validation Successful");
        return true;
    }
}
