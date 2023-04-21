package toxtree.plugins.verhaar2.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.ProgressStatus;
import toxTree.tree.TreeResult;

public class Verhaar2TreeResult extends TreeResult {

    /**
     * 
     */
    private static final long serialVersionUID = 2222797084633419611L;

    public Verhaar2TreeResult() {
        super();
    }

    @Override
    public void assignResult(IAtomContainer mol) throws DecisionResultException {
        if (mol == null) return;
        if (getCategory() != null) mol.setProperty(getResultPropertyName(), getCategory().toString());
        mol.setProperty(getClass().getName(), explain(false).toString());
        firePropertyChangeEvent(ProgressStatus._pRuleResult, null, status);
    }

    public String[] getResultPropertyName() {
        if (getDecisionMethod() == null) return new String[] { "Verhaar scheme (Modified)" }; else return new String[] { getDecisionMethod().getClass().getName() };
    }
}
