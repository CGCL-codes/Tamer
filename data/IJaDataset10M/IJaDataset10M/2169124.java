package eye.rules;

import toxTree.tree.rules.smarts.RuleSMARTSubstructureCDK;
import ambit2.smarts.query.SMARTSException;

/**
 * 
 * Chlorinated aliphatic alcohols
 * @author Nina Jeliazkova nina@acad.bg
 */
public class Rule18 extends RuleSMARTSubstructureCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1030146366743047422L;

    private static final String MSG18 = "Chlorinated aliphatic alcohols";

    public Rule18() {
        super();
        try {
            addSubstructure("CC(Cl)([#1,C])C([#1,C])([#1,C])[CH2][OH]");
            setID("18");
            setTitle(MSG18);
            editable = false;
            examples[0] = "C(C(C(CC)C)(Cl)C=C)CC";
            examples[1] = "C(C(C(CO)C)(Cl)C=C)CC";
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
