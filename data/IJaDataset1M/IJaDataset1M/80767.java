package JaCoP.constraints;

import java.util.ArrayList;
import JaCoP.core.Domain;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;

/**
 * Constraints X #\= C
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.0
 */
public class XneqC extends PrimitiveConstraint {

    static int idNumber = 1;

    /**
	 * It specifies variable x in constraint x != c.
	 */
    public IntVar x;

    /**
	 * It specifies constant c in constraint x != c.
	 */
    public int c;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "x", "c" };

    /**
	 * It constructs x != c constraint.
	 * @param x variable x.
	 * @param c constant c.
	 */
    public XneqC(IntVar x, int c) {
        assert (x != null) : "Variable x is null";
        numberId = idNumber++;
        numberArgs = 1;
        this.x = x;
        this.c = c;
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(1);
        variables.add(x);
        return variables;
    }

    @Override
    public void consistency(Store store) {
        x.domain.inComplement(store.level, x, c);
    }

    @Override
    public int getNestedPruningEvent(Var var, boolean mode) {
        if (mode) {
            if (consistencyPruningEvents != null) {
                Integer possibleEvent = consistencyPruningEvents.get(var);
                if (possibleEvent != null) return possibleEvent;
            }
            return IntDomain.ANY;
        } else {
            if (notConsistencyPruningEvents != null) {
                Integer possibleEvent = notConsistencyPruningEvents.get(var);
                if (possibleEvent != null) return possibleEvent;
            }
            return IntDomain.ANY;
        }
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return Domain.NONE;
    }

    @Override
    public int getNotConsistencyPruningEvent(Var var) {
        if (notConsistencyPruningEvents != null) {
            Integer possibleEvent = notConsistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return Domain.NONE;
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        x.putModelConstraint(this, getConsistencyPruningEvent(x));
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void notConsistency(Store store) {
        x.domain.in(store.level, x, c, c);
    }

    @Override
    public boolean notSatisfied() {
        return x.singleton(c);
    }

    @Override
    public void removeConstraint() {
        x.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        return !x.domain.contains(c);
    }

    @Override
    public String toString() {
        return id() + " : XneqC(" + x + ", " + c + " )";
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            x.weight++;
        }
    }
}
