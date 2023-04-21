package toxTree.tree.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.MolFlags;
import toxTree.tree.AbstractRule;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.base.interfaces.IProcessor;

/**
 * Verifies if the moleucle is open chain
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-5-2
 */
public class RuleOpenChain extends AbstractRule {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -6401742853134220208L;

    /**
	 * Constructor
	 * 
	 */
    public RuleOpenChain() {
        super();
        id = "[Open chain]";
        title = "Open chain";
        explanation.append("<html>Is the substance <i>open chain</i>?<p>");
        examples[0] = "OC(=O)CCCCCCCCCCCC1CCC=C1";
        examples[1] = "CC(C)=O";
    }

    /**
	 * {@link toxTree.core.IDecisionRule#verifyRule(IAtomContainer)}
	 */
    public boolean verifyRule(IAtomContainer mol) throws DecisionMethodException {
        MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
        if (mf == null) throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
        return mf.isOpenChain();
    }

    @Override
    public boolean isImplemented() {
        return true;
    }

    @Override
    public IProcessor<IAtomContainer, IChemObjectSelection> getSelector() {
        RuleSMARTSSubstructureAmbit rule = new RuleSMARTSSubstructureAmbit();
        try {
            rule.addSubstructure("[R0;!#1]!@[R0;!#1]");
        } catch (Exception x) {
            x.printStackTrace();
        }
        ;
        return rule.getSelector();
    }
}
