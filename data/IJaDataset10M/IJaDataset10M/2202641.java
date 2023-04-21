package structurefeatures.rules;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;

public class RuleHalide extends RuleSMARTSSubstructureAmbit {

    private static final long serialVersionUID = 0;

    public RuleHalide() {
        super();
        try {
            super.initSingleSMARTS(super.smartsPatterns, "1", "[CX4,CH,CH2,CH3][Br,Cl,I]");
            super.initSingleSMARTS(super.smartsPatterns, "2", "[P,S][F,Cl,Br,I]");
            super.initSingleSMARTS(super.smartsPatterns, "3", "a-[F,Cl,Br,I]");
            id = "10";
            title = "Halide";
            examples[0] = "";
            examples[1] = "";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
