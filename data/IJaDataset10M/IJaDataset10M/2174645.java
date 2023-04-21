package goalc;

import java.util.ArrayList;
import ail.syntax.Action;
import ail.syntax.Event;
import ail.syntax.GBelief;
import ail.syntax.Guard;
import ail.syntax.Literal;
import ail.syntax.Plan;
import ail.syntax.Goal;
import ail.syntax.Deed;
import ail.syntax.Atom;
import ail.syntax.Pred;
import ail.syntax.Structure;
import ail.syntax.Term;
import ail.syntax.VarTerm;

/**
 * Class for GOAL Conditional Actions.  These are just plans but we
 * provide more GOAL natural constructors.
 * @author louiseadennis
 *
 */
public class ActionRule extends Plan {

    /**
	 * Construct a conditional action from a mental state and a goal.  This
	 * is a reactive conditinal action.
	 * @param ms
	 * @param g2
	 */
    public ActionRule(MentalState ms, ArrayList<Deed> ds) {
        setTrigger(new Event(Event.AILAddition, new Goal(new VarTerm("Any"), Goal.achieveGoal)));
        setContextSingle(ms, ds.size());
        ArrayList<Deed> prf = new ArrayList<Deed>();
        setPrefix(prf);
        ArrayList<Deed> body = ds;
        setBody(body);
        setSource(new Atom("self"));
    }

    /**
     * Constructs a plan from a Literal.  This needs to be expanded.
     * @param l
     */
    public ActionRule(Literal l) {
        if (l.getFunctor().equals("plan")) {
            setTrigger(new Event(Event.AILAddition, new Goal(new VarTerm("Any"), Goal.achieveGoal)));
            Term guard = l.getTerm(0);
            ArrayList<Guard> guards = new ArrayList<Guard>();
            guards.add(new Guard(new GBelief(new Goal(new Literal(true, new Pred((Structure) guard)), Goal.achieveGoal))));
            setContext(guards);
            setPrefix(new ArrayList<Deed>());
            ArrayList<Deed> deeds = new ArrayList<Deed>();
            Term body = l.getTerm(2);
            Deed deed = new Deed(Deed.AILAddition, new Goal(new Literal(true, new Pred((Structure) body)), Goal.performGoal));
            deeds.add(deed);
            setBody(deeds);
        }
    }
}
