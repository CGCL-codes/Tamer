package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import org.argouml.cognitive.Designer;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */
public class CrTooManyTransitions extends CrUML {

    public static String THRESHOLD = "Threshold";

    public CrTooManyTransitions() {
        setHeadline("Reduce Transitions on <ocl>self</ocl>");
        addSupportedDecision(CrUML.decSTATE_MACHINES);
        setArg(THRESHOLD, new Integer(10));
        addTrigger("incoming");
        addTrigger("outgoing");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(dm instanceof MStateVertex)) return NO_PROBLEM;
        MStateVertex sv = (MStateVertex) dm;
        int threshold = ((Integer) getArg(THRESHOLD)).intValue();
        Collection in = sv.getIncomings();
        Collection out = sv.getOutgoings();
        int inSize = (in == null) ? 0 : in.size();
        int outSize = (out == null) ? 0 : out.size();
        if (inSize + outSize <= threshold) return NO_PROBLEM;
        return PROBLEM_FOUND;
    }
}
