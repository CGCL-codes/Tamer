package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * UML 1.5 Well-formedness rule [2] for Pseudostates: <p>
 * History vertices can have at most one outgoing transition.
 *
 * Well-formedness rule [2] for PseudoState. See page 137 of UML 1.4
 * Semantics. OMG document UML 1.4.2 formal/04-07-02.
 *
 * @author pepargouml@yahoo.es
 */
public class CrInvalidHistory extends CrUML {

    /**
     * The constructor.
     */
    public CrInvalidHistory() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.STATE_MACHINES);
        addTrigger("outgoing");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAPseudostate(dm))) return NO_PROBLEM;
        Object k = Model.getFacade().getKind(dm);
        if (!Model.getFacade().equalsPseudostateKind(k, Model.getPseudostateKind().getDeepHistory()) && !Model.getFacade().equalsPseudostateKind(k, Model.getPseudostateKind().getShallowHistory())) return NO_PROBLEM;
        Collection outgoing = Model.getFacade().getOutgoings(dm);
        int nOutgoing = outgoing == null ? 0 : outgoing.size();
        if (nOutgoing > 1) return PROBLEM_FOUND;
        return NO_PROBLEM;
    }

    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getPseudostate());
        return ret;
    }
}
