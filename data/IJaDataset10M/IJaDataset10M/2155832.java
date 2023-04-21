package cramer2.rules;

import toxTree.tree.rules.RuleOpenChain;

/**
 * Rule 19 of the Cramer scheme (see {@link cramer2.CramerRulesExtendedExtended})
 * @author Nina Jeliazkova 
 * <b>Modified</b> 2005-9-2
 */
public class RuleIsOpenChain extends RuleOpenChain {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1697237934140549609L;

    /**
	 * 
	 */
    public RuleIsOpenChain() {
        super();
        id = "19";
        title = "Open chain";
        explanation.append("<html>Is the substance <i>open chain</i> (G)?<p>");
        explanation.append("Q19-21 deal with open-chain substances.</html>");
        examples[0] = "OC(=O)CCCCCCCCCCCC1CCC=C1";
        examples[1] = "CC(C)=O";
        editable = false;
    }
}
