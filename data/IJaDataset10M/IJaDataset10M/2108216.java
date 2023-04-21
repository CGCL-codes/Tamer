package JaCoP.constraints;

import java.util.ArrayList;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;
import JaCoP.core.IntervalDomain;

/**
 * Constraint X mod Y = Z
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.1
 */
public class XmodYeqZ extends Constraint {

    static int counter = 1;

    /**
	 * It specifies variable x in constraint x mod y = z. 
	 */
    public IntVar x;

    /**
	 * It specifies variable y in constraint x mod y = z. 
	 */
    public IntVar y;

    /**
	 * It specifies variable z in constraint x mod y = z. 
	 */
    public IntVar z;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "x", "y", "z" };

    /**
	 * It constructs a constraint X mod Y = Z.
	 * @param x variable x.
	 * @param y variable y.
	 * @param z variable z.
	 */
    public XmodYeqZ(IntVar x, IntVar y, IntVar z) {
        assert (x != null) : "Variable x is null";
        assert (y != null) : "Variable y is null";
        assert (z != null) : "Variable z is null";
        numberId = counter++;
        numberArgs = 3;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(3);
        variables.add(z);
        variables.add(y);
        variables.add(x);
        return variables;
    }

    @Override
    public void consistency(Store store) {
        int resultMin = IntDomain.MinInt;
        int resultMax = IntDomain.MaxInt;
        do {
            store.propagationHasOccurred = false;
            y.domain.inComplement(store.level, y, 0);
            int reminderMin, reminderMax;
            if (x.min() >= 0) {
                reminderMin = 0;
                reminderMax = Math.max(Math.abs(y.min()), Math.abs(y.max())) - 1;
            } else if (x.max() < 0) {
                reminderMax = 0;
                reminderMin = -Math.max(Math.abs(y.min()), Math.abs(y.max())) + 1;
            } else {
                reminderMin = Math.min(Math.min(y.min(), -y.min()), Math.min(y.max(), -y.max())) + 1;
                reminderMax = Math.max(Math.max(y.min(), -y.min()), Math.max(y.max(), -y.max())) - 1;
            }
            z.domain.in(store.level, z, reminderMin, reminderMax);
            int oldResultMin = resultMin, oldResultMax = resultMax;
            IntervalDomain result = IntDomain.divBounds(x.min(), x.max(), y.min(), y.max());
            resultMin = result.min();
            resultMax = result.max();
            if (oldResultMin != resultMin || oldResultMax != resultMax) store.propagationHasOccurred = true;
            IntervalDomain yBounds = IntDomain.divBounds(x.min() - reminderMax, x.max() - reminderMin, resultMin, resultMax);
            y.domain.in(store.level, y, yBounds);
            IntervalDomain reminder = IntDomain.mulBounds(resultMin, resultMax, y.min(), y.max());
            int zMin = reminder.min(), zMax = reminder.max();
            reminderMin = x.min() - zMax;
            reminderMax = x.max() - zMin;
            z.domain.in(store.level, z, reminderMin, reminderMax);
            x.domain.in(store.level, x, zMin + z.min(), zMax + z.max());
            assert checkSolution(resultMin, resultMax) == null : checkSolution(resultMin, resultMax);
        } while (store.propagationHasOccurred);
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
        z.putModelConstraint(this, getConsistencyPruningEvent(z));
        y.putModelConstraint(this, getConsistencyPruningEvent(y));
        x.putModelConstraint(this, getConsistencyPruningEvent(x));
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void removeConstraint() {
        z.removeConstraint(this);
        y.removeConstraint(this);
        x.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        IntDomain Xdom = z.dom(), Ydom = y.dom(), Zdom = x.dom();
        return Xdom.singleton() && Ydom.singleton() && Zdom.singleton() && (Ydom.min() * Xdom.min() <= Zdom.min() || Ydom.min() * Xdom.min() < Zdom.min() + Ydom.min());
    }

    @Override
    public String toString() {
        return id() + " : XmodYeqZ(" + x + ", " + y + ", " + z + " )";
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            z.weight++;
            y.weight++;
            x.weight++;
        }
    }

    String checkSolution(int resultMin, int resultMax) {
        String result = null;
        if (z.singleton() && y.singleton() && x.singleton()) {
            result = "Operation mod does not hold " + x + " mod " + y + " = " + z + "(result " + resultMin + ".." + resultMax;
            for (int i = resultMin; i <= resultMax; i++) {
                if (i * y.value() + z.value() == x.value()) result = null;
            }
        } else result = null;
        return result;
    }
}
