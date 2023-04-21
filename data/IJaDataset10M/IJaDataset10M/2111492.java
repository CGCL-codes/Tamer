package net.sf.gateway.mef.businessrules.ty2009.Form111;

import net.sf.gateway.mef.businessrules.baserules.Form111Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Form111_0033_010 extends Form111Rule {

    /**
     * Logging.
     */
    private static final Log LOG = LogFactory.getLog(Form111_0033_010.class);

    public Form111_0033_010() {
        this.setRuleNumber("Form 111-0033-010");
        this.setState("Vermont");
        this.setRuleText("Capital Gains Exclusion, Schedule IN153 must be attached.");
        this.setErrorCategory("Incorrect Data");
        this.setSeverity("Reject");
        this.setDataValue("");
        this.setXpath("");
    }

    public boolean validate(Document doc) {
        NodeList capitalGain = doc.getElementsByTagName("CapitalGain");
        if (capitalGain != null && capitalGain.getLength() > 0 && capitalGain.item(0) != null) {
            NodeList FormIN153List = doc.getElementsByTagName("FormIN153");
            if (FormIN153List == null || FormIN153List.getLength() == 0) {
                LOG.error("<" + this.getRuleNumber() + "> Capital Gains Exclusion, Schedule IN153 must be attached.");
                return false;
            }
        }
        LOG.info("<" + this.getRuleNumber() + "> Validation Successful");
        return true;
    }
}
