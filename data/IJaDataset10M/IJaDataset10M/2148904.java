package JaCoP.constraints;

import java.util.ArrayList;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;
import JaCoP.core.IntervalDomain;

/**
 * Constraint X * C #= Z
 * 
 * Boundary consistency is used.
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.1
 */
public class XmulCeqZ extends Constraint {

    static int idNumber = 1;

    /**
	 * It specifies variable x in constraint x * c = z. 
	 */
    public IntVar x;

    /**
	 * It specifies constant c in constraint x * c = z. 
	 */
    public int c;

    /**
	 * It specifies variable x in constraint x * c = z. 
	 */
    public IntVar z;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "x", "c", "z" };

    /**
	 * It constructs a constraint X * C = Z.
	 * @param x variable x.
	 * @param c constant c.
	 * @param z variable z.
	 */
    public XmulCeqZ(IntVar x, int c, IntVar z) {
        assert (x != null) : "Variable x is null";
        assert (z != null) : "Variable z is null";
        numberId = idNumber++;
        numberArgs = 2;
        this.x = x;
        this.c = c;
        this.z = z;
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(2);
        variables.add(x);
        variables.add(z);
        return variables;
    }

    @Override
    public void consistency(Store store) {
        if (c != 0) do {
            store.propagationHasOccurred = false;
            IntervalDomain xBounds = IntDomain.divIntBounds(z.min(), z.max(), c, c);
            x.domain.in(store.level, x, xBounds);
            IntervalDomain zBounds = IntDomain.mulBounds(x.min(), x.max(), c, c);
            z.domain.in(store.level, z, zBounds);
        } while (store.propagationHasOccurred); else z.domain.in(store.level, z, 0, 0);
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return IntDomain.BOUND;
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        x.putModelConstraint(this, getConsistencyPruningEvent(x));
        z.putModelConstraint(this, getConsistencyPruningEvent(z));
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void removeConstraint() {
        x.removeConstraint(this);
        z.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        IntDomain Xdom = x.dom(), Zdom = z.dom();
        return Xdom.singleton() && Zdom.singleton() && Xdom.min() * c == Zdom.min();
    }

    @Override
    public String toString() {
        return id() + " : XmulCeqZ(" + x + ", " + c + ", " + z + " )";
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            x.weight++;
            z.weight++;
        }
    }
}
