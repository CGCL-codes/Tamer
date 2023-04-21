package mutant.rules;

import toxTree.tree.rules.StructureAlertCDK;
import ambit2.smarts.query.SMARTSException;

public class SA53_nogen extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8723199576199214305L;

    public SA53_nogen() {
        super();
        try {
            addSubstructure("c1cc(N)ccc1S(=O)(=O)N");
            addSubstructure("c1cc(S)ccc1S(=O)(=O)N");
            addSubstructure("c1ccccc1S(=O)(=O)[N;-1]");
            addSubstructure("c1cc(C)ccc1S(=O)(=O)O");
            setID("SA53_nogen");
            setTitle("Benzensulfonic ethers");
            setExplanation("Nongenotoxic mechanism");
            examples[0] = "c1ccccc1S(=O)(=O)O";
            examples[1] = "c1cc(C)ccc1S(=O)(=O)O";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
