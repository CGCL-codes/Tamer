package JaCoP.constraints;

import java.util.ArrayList;
import JaCoP.core.Store;
import JaCoP.core.Var;
import JaCoP.util.SimpleHashSet;

/**
 * Constraint "not costraint"
 * 
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.0
 */
public class Not extends PrimitiveConstraint {

    static int IdNumber = 1;

    /**
	 * It specifies the constraint which negation is being created.
	 */
    public PrimitiveConstraint c;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "c" };

    /**
	 * It constructs not constraint.
	 * @param c primitive constraint which is being negated.
	 */
    public Not(PrimitiveConstraint c) {
        numberId = IdNumber++;
        this.c = c;
        numberArgs += c.numberArgs();
    }

    @Override
    public ArrayList<Var> arguments() {
        return c.arguments();
    }

    @Override
    public void consistency(Store store) {
        c.notConsistency(store);
    }

    @Override
    public int getNestedPruningEvent(Var var, boolean mode) {
        return getConsistencyPruningEvent(var);
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return c.getNestedPruningEvent(var, false);
    }

    @Override
    public int getNotConsistencyPruningEvent(Var var) {
        if (notConsistencyPruningEvents != null) {
            Integer possibleEvent = notConsistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return c.getNestedPruningEvent(var, true);
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        SimpleHashSet<Var> variables = new SimpleHashSet<Var>();
        for (Var V : c.arguments()) variables.add(V);
        while (!variables.isEmpty()) {
            Var V = variables.removeFirst();
            V.putModelConstraint(this, getConsistencyPruningEvent(V));
        }
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void notConsistency(Store store) {
        c.consistency(store);
    }

    @Override
    public boolean notSatisfied() {
        return c.satisfied();
    }

    @Override
    public void removeConstraint() {
        for (Var V : c.arguments()) V.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        return c.notSatisfied();
    }

    @Override
    public String toString() {
        return id() + " : Not( " + c + ")";
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            c.increaseWeight();
        }
    }
}
