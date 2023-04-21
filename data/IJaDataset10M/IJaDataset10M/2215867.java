package net.sourceforge.gateway.sstp.rules.ser;

import net.sourceforge.gateway.sstp.efile.x2010V01.SimplifiedReturnDocumentType;
import net.sourceforge.gateway.sstp.rules.BaseRule;

/**
 * StateID is not valid
 */
public class Rule000026 extends BaseRule implements SimplifiedReturnDocumentRule {

    public boolean validate(SimplifiedReturnDocumentType doc) {
        if (!doc.getSSTPFilingHeader().isSetStateID()) {
            return true;
        }
        String stateId = doc.getSSTPFilingHeader().getStateID();
        if (!stateId.equals("")) {
            return true;
        } else {
            this.setErrorCode("000026");
            this.setXPath("/SSTSimplifiedReturnTransmission/SimplifiedReturnDocument/SSTPFilingHeader/StateID");
            this.setErrorMessage("State identification number provided by filer is not a valid id number for the state.");
            this.setDataValue(doc.getSSTPFilingHeader().getStateID());
            return false;
        }
    }
}
