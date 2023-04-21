package net.sf.gateway.mef.businessrules.ty2011.Header;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.gateway.mef.businessrules.baserules.HeaderRule;
import net.sf.gateway.mef.databases.tables.MefSubmission;
import net.sf.gateway.mef.utilities.DBUtils;
import net.sf.gateway.mef.utilities.SchemaValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class Header_00001_010 extends HeaderRule {

    /**
     * Logging.
     */
    private static final Log LOG = LogFactory.getLog(Header_00001_010.class);

    public Header_00001_010() {
        this.setRuleNumber("Header-00001-010");
        this.setState("Vermont");
        this.setRuleText("Binary attachment count - Must be equal to the number of PDF documents attached to this return");
        this.setErrorCategory("Incorrect Data");
        this.setSeverity("Reject");
        this.setDataValue("");
        this.setXpath("/ReturnState/ReturnHeaderState/@binaryAttachmentCount");
    }

    /**
     * Counts the number of attachments for a given submission.
     * 
     * @param submissionId
     *        to look up.
     * @return count of attachments
     */
    protected int countBinaryAttachments(String submissionId) {
        MefSubmission mefSubmission = DBUtils.getSubmission(submissionId);
        if (mefSubmission == null) {
            return 0;
        }
        return mefSubmission.getPdfs().size();
    }

    public boolean validate(String submissionId, Document doc) {
        NamespaceContext ctx = SchemaValidator.getEFileNamespaceContext();
        String countStr = "";
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(ctx);
            countStr = xpath.evaluate("/efil:ReturnState/efil:ReturnHeaderState/@binaryAttachmentCount", doc);
        } catch (XPathExpressionException e) {
            LOG.error("<" + this.getRuleNumber() + "> XPath Error", e);
            return false;
        }
        if (countStr != null && !"".equals(countStr)) {
            int count = Integer.parseInt(countStr);
            this.setDataValue(countStr);
            int realCount = this.countBinaryAttachments(submissionId);
            if (count == realCount) {
                LOG.info("<" + this.getRuleNumber() + "> Validation Successful");
                return true;
            } else {
                LOG.error("<" + this.getRuleNumber() + "> " + count + " != " + realCount);
                return false;
            }
        } else {
            LOG.info("<" + this.getRuleNumber() + "> Validation Successful");
            return true;
        }
    }
}
