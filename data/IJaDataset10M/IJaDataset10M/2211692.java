package unbbayes.prs.mebn.ssbn;

import java.util.List;
import unbbayes.prs.mebn.ContextNode;
import unbbayes.prs.mebn.OrdinaryVariable;
import unbbayes.prs.mebn.kb.KnowledgeBase;
import unbbayes.prs.mebn.ssbn.exception.InvalidContextNodeFormulaException;
import unbbayes.prs.mebn.ssbn.exception.OVInstanceFaultException;

/**
 * Class that contains methods for evaluate the context nodes of a MFrag. 
 * 
 * @author Laecio Lima dos Santos (laecio@gmail.com)
 */
public class ContextNodeAvaliator {

    private KnowledgeBase kb;

    public ContextNodeAvaliator(KnowledgeBase kb) {
        this.kb = kb;
    }

    /**
	 * Evaluate a context node. 
	 * 
	 * @param node
	 * @param ovInstances
	 * @return
	 * @throws OVInstanceFaultException
	 */
    public boolean evaluateContextNode(ContextNode node, List<OVInstance> ovInstances) throws OVInstanceFaultException {
        List<OrdinaryVariable> ovFaultList = node.getOVFaultForOVInstanceSet(ovInstances);
        if (!ovFaultList.isEmpty()) {
            throw new OVInstanceFaultException(ovFaultList);
        } else {
            return kb.evaluateContextNodeFormula(node, ovInstances);
        }
    }

    /**
	 * Evaluate a search context node. A search context node is a node that return
	 * instances of the Knowledge Base that satisfies a restriction. 
	 * 
	 * Ex.: z = StarshipZone(st). 
	 * -> return all the z's. 
	 * 
	 * @param context
	 * @param ovInstances
	 * @return
	 * @throws InvalidContextNodeFormulaException
	 * @throws OVInstanceFaultException 
	 */
    public List<String> evalutateSearchContextNode(ContextNode context, List<OVInstance> ovInstances) throws InvalidContextNodeFormulaException, OVInstanceFaultException {
        List<String> entitiesResult = kb.evaluateSearchContextNodeFormula(context, ovInstances);
        return entitiesResult;
    }
}
