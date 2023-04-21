package goalc;

import java.util.ArrayList;
import ail.semantics.operationalrules.*;
import ail.semantics.AILAgent;
import ail.semantics.Intention;
import ail.semantics.ApplicablePlan;
import ail.syntax.DefaultAILStructure;
import ail.syntax.Event;
import ail.syntax.GBelief;
import ail.syntax.Guard;
import ail.syntax.Literal;
import ail.syntax.Action;
import gov.nasa.jpf.annotation.FilterField;
import gov.nasa.jpf.jvm.Verify;

/**
 * This rule plans the goal using conditional actions.
 * 
 * @author lad
 *
 */
public class PlanWithActionRule implements OSRule {

    @FilterField
    private static final String name = "Plan with Cond Actions";

    @FilterField
    private ArrayList<ApplicablePlan> aps = new ArrayList<ApplicablePlan>();

    public String getName() {
        return name;
    }

    public boolean checkPreconditions(AILAgent a) {
        aps = a.filterPlans(a.appPlans(new Intention()));
        if (!aps.isEmpty()) return true;
        return false;
    }

    public void apply(AILAgent a) {
        Intention i = a.getIntention();
        if (aps.isEmpty()) {
        } else {
            int plannum = a.choosePlan(aps, i);
            ApplicablePlan p = aps.get(plannum);
            if (p.getN() == 0 && (!(p.getGuard().isEmpty()) && (!(p.getGuard().get(p.getGuard().size() - 1).isTrivial())))) {
                Literal gl = new Literal("state");
                gl.addTerm(p.getGuard().get(p.getGuard().size() - 1).toTerm());
                Event state = new Event(Event.AILAddition, DefaultAILStructure.AILBel, gl);
                p.getGuard().set(p.getGuard().size() - 1, new Guard(new GBelief(GBelief.GTrue)));
                a.setIntention(new Intention(state, p.getPrefix(), p.getGuard(), p.getUnifier().clone(), AILAgent.refertoself()));
                if (!i.empty()) {
                    a.getIntentions().add(i);
                }
            } else if (p.getN() == 0 && (!(p.getGuard().isEmpty()) && ((p.getGuard().get(p.getGuard().size() - 1).isTrivial())))) {
                Literal gl = new Literal("alwaystrue");
                Event state = new Event(Event.AILAddition, DefaultAILStructure.AILBel, gl);
                a.setIntention(new Intention(state, p.getPrefix(), p.getGuard(), p.getUnifier().clone(), AILAgent.refertoself()));
                a.getIntentions().add(i);
            } else {
                System.err.println("Encountering a triggered Action Rule:  This should not happen!");
            }
        }
        aps = new ArrayList<ApplicablePlan>();
    }
}
