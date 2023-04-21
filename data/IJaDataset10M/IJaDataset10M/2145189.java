package sicret.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.rules.RuleAnySubstructure;

/**
 * 
 * Acid anhydrides.
 * @author Martin Martinov
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Dec 17, 2006
 */
public class RuleAcidAnhydrides extends RuleAnySubstructure {

    public static final transient String MSG_18H = "Acyclic group";

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 0;

    /**
	 * Constructor
	 * 
	 */
    public RuleAcidAnhydrides() {
        super();
        QueryAtomContainer q = verhaar.query.FunctionalGroups.createAutoQueryContainer("[*]C(=O)OC(=O)[*]");
        addSubstructure(q);
        id = "45";
        title = "AcidAnhydrides";
        examples[0] = "CN";
        examples[1] = "O=COC=O";
        editable = false;
    }

    /**
	 * {@link toxTree.core.IDecisionRule#verifyRule(IAtomContainer)}
	 */
    public boolean verifyRule(IAtomContainer mol) throws DecisionMethodException {
        logger.info(toString());
        return super.verifyRule(mol);
    }

    public boolean isImplemented() {
        return true;
    }
}
