package ail.semantics.operationalrules;

import java.util.ArrayList;
import ail.semantics.AILAgent;
import ail.semantics.ApplicablePlan;
import ail.semantics.Intention;
import ail.syntax.GBelief;
import ail.syntax.Literal;
import ail.syntax.Event;
import ail.syntax.Guard;
import ail.syntax.DefaultAILStructure;
import ail.syntax.Deed;
import gov.nasa.jpf.annotation.FilterField;

/**
 * Applying one of the applicable plans to the current intention.
 * 
 * @author lad
 *
 */
public class ApplyApplicablePlans implements OSRule {

    @FilterField
    private static final String name = "ApplyApplicablePlans";

    public String getName() {
        return name;
    }

    public boolean checkPreconditions(AILAgent a) {
        return (!a.getApplicablePlans().isEmpty());
    }

    public void apply(AILAgent a) {
        Intention i = a.getIntention();
        ArrayList<ApplicablePlan> aps = a.getApplicablePlans();
        ApplicablePlan p = aps.get(0);
        p = a.selectPlan(aps, i);
        ArrayList<Guard> guardstack = p.getGuard();
        if (p.getN() == 0 && (!(guardstack.isEmpty()) && (!(guardstack.get(guardstack.size() - 1).isTrivial())))) {
            Literal gl = new Literal("state");
            gl.addTerm(guardstack.get(guardstack.size() - 1).toTerm());
            Event state = new Event(Deed.AILAddition, DefaultAILStructure.AILBel, gl);
            guardstack.set(guardstack.size() - 1, new Guard(new GBelief(GBelief.GTrue)));
            a.setIntention(new Intention(state, p.getPrefix(), guardstack, p.getUnifier().clone(), AILAgent.refertoself()));
        } else {
            i.dropP(p.getN());
            if (!(guardstack.isEmpty()) && (!(guardstack.get(guardstack.size() - 1).isTrivial()))) {
                guardstack.set(guardstack.size() - 1, new Guard(new GBelief(GBelief.GTrue)));
            }
            if (p.getPrefix().size() != 0) {
                i.iConcat(p.getEvent(), p.getPrefix(), guardstack, p.getUnifier().clone());
            }
        }
        a.clearApplicablePlans();
    }
}
