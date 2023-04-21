package structurefeatures.rules;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;

public class RuleNitrogenMustard extends RuleSMARTSSubstructureAmbit {

    private static final long serialVersionUID = 0;

    public RuleNitrogenMustard() {
        super();
        try {
            super.initSingleSMARTS(super.smartsPatterns, "1", "N(CCCl)CCCl");
            id = "21";
            title = "nitrogen mustard";
            examples[0] = "";
            examples[1] = "";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
