package eye.rules;

import toxTree.tree.rules.smarts.RuleSMARTSubstructureCDK;
import ambit2.smarts.query.SMARTSException;

/**
 * @author Nina Jeliazkova nina@acad.bg
 */
public class Rule28 extends RuleSMARTSubstructureCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8607025658661359589L;

    private static final String MSG28 = "Triphenylphosphonium salts";

    public Rule28() {
        super();
        try {
            addSubstructure("c1([cH][cH][cH][cH][cH]1)[P+](c2[cH][cH][cH][cH][cH]2)(c3[cH][cH][cH][cH][cH]3)[CH2]");
            setID("28");
            setTitle(MSG28);
            editable = false;
            examples[0] = "c1(ccccc1)C(c2ccccc2)(c3ccccc3)CC=C";
            examples[1] = "c1(ccccc1)[P+](c2ccccc2)(c3ccccc3)CC=C";
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
