package mutant.rules;

import toxTree.tree.rules.StructureAlertCDK;
import ambit2.smarts.query.SMARTSException;

public class SA41_nogen extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8723199576199214305L;

    public SA41_nogen() {
        super();
        try {
            addSubstructure("[C;!R&$(C([C;!R])([C;!R])[C;!R][C;!R])&!$(CCCCCCCCCCCC)][C;!R&$(C[OX2;!R]),$(C(=O)[OX2;!R])]");
            setID("SA41_nogen");
            setTitle("substituted n-alkylcarboxylic acids");
            setExplanation("Nongenotoxic mechanism");
            examples[0] = "CCCCCCCCCCCCCCCCCCCCCCCCCCCC(C)CO";
            examples[1] = "CCCCCCC(C)CO";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
