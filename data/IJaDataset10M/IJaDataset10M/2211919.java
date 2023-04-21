package structurefeatures.rules;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;

public class RuleNChloramine extends RuleSMARTSSubstructureAmbit {

    private static final long serialVersionUID = 0;

    public RuleNChloramine() {
        super();
        try {
            super.initSingleSMARTS(super.smartsPatterns, "1", "S(=O)(=O)NCl");
            super.initSingleSMARTS(super.smartsPatterns, "2", "S(=O)(=O)N(Cl)");
            super.initSingleSMARTS(super.smartsPatterns, "3", "ClNS(=O)(=O)");
            super.initSingleSMARTS(super.smartsPatterns, "4", "N(Cl)S(=O)(=O)");
            id = "31";
            title = "N-chloramine";
            examples[0] = "";
            examples[1] = "";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
