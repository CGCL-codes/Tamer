package mic.rules;

import toxTree.tree.rules.StructureAlertCDK;
import ambit2.smarts.query.SMARTSException;

public class SA24 extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5624687406911731732L;

    public static String SA24_title = "α,β unsaturated alkoxy";

    public SA24() {
        super();
        try {
            setContainsAllSubstructures(false);
            addSubstructure(SA24_title, "[!$([#6](=O)[!O]),#1][C!H0;!R]([!$([#6](=O)[!O]),#1])!@;=[C!H0;!R]O[#6]");
            setID("SA24");
            setTitle(SA24_title);
            setExplanation(SA24_title + "<br>An aromatic substituent on the oxygen is also allowed.<br>If a partial overlap with SA10 is found, this alert should not fire.");
            examples[0] = "COC=CC(C)=O";
            examples[1] = "C=COC=1C=CC(=CC=1)N(=O)O";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
