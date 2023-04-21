package net.sf.orcc.ir.transforms;

import java.util.Iterator;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.ActorTransformation;
import net.sf.orcc.ir.StateVariable;
import net.sf.orcc.ir.Variable;
import net.sf.orcc.util.OrderedMap;

/**
 * This class defines a very simple Dead Global Elimination.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class DeadGlobalElimination implements ActorTransformation {

    @Override
    public void transform(Actor actor) {
        OrderedMap<Variable> stateVariables = actor.getStateVars();
        Iterator<Variable> it = stateVariables.iterator();
        while (it.hasNext()) {
            StateVariable variable = (StateVariable) it.next();
            if (!variable.isUsed()) {
                it.remove();
            }
        }
    }
}
