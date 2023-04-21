package org.opt4j.sat.sat4j;

import org.sat4j.minisat.core.Heap;
import org.sat4j.minisat.core.ILits;
import org.sat4j.minisat.orders.VarOrderHeap;

/**
 * A {@code LiteralOrder} implementation for the SAT4J interface. <br>
 * (not documented, see <a href="http://www.sat4j.org">Sat4J.org</a>)
 * 
 * @author lukasiewycz
 * 
 */
public class LiteralOrder extends VarOrderHeap {

    protected static final double VAR_RESCALE_FACTOR = 1e-100;

    protected static final double VAR_RESCALE_BOUND = 1 / VAR_RESCALE_FACTOR;

    private static final long serialVersionUID = 1L;

    protected double varInc = 0;

    /**
	 * Constructs a {@code LiteralOrder}.
	 */
    public LiteralOrder() {
    }

    @Override
    public void newVar() {
        newVar(1);
    }

    @Override
    public void newVar(int howmany) {
        setLits(lits);
    }

    @Override
    public void setLits(ILits lits) {
        this.lits = lits;
        int nlength = 2 * (lits.nVars() + 1);
        activity = new double[nlength];
        phase = new int[0];
        activity[0] = -1;
        activity[1] = -1;
        heap = new Heap(activity);
        heap.setBounds(nlength);
        for (int p = 2; p < nlength; p++) {
            activity[p] = 0.0;
            if (lits.belongsToPool(p >> 1)) {
                heap.insert(p);
            }
        }
    }

    @Override
    public void init() {
    }

    @Override
    protected void updateActivity(final int p) {
        if ((activity[p] += varInc) > VAR_RESCALE_BOUND) {
            varRescaleActivity();
        }
    }

    /**
	 * Increments the activity of a literals with a specified value.
	 * 
	 * @param p
	 *            the variable
	 * @param value
	 *            the value to increments
	 */
    protected void updateActivity(final int p, final double value) {
        if ((activity[p] += value) > VAR_RESCALE_BOUND) {
            varRescaleActivity();
        }
    }

    @Override
    public void updateVar(int p) {
        updateActivity(p);
        if (heap.inHeap(p)) {
            heap.increase(p);
        }
    }

    /**
	 * Sets the value {@code varInc} that increases the activity of literals to
	 * the specified value.
	 * 
	 * @param value
	 *            the varInc value to set
	 */
    public void setVarInc(double value) {
        varInc = value;
    }

    /**
	 * Rescales the activity of the literals.
	 */
    protected void varRescaleActivity() {
        for (int p = 2; p < activity.length; p++) {
            activity[p] *= VAR_RESCALE_FACTOR;
        }
        varInc *= VAR_RESCALE_FACTOR;
    }

    /**
	 * Sets the activity of the literal to the specified value.
	 * 
	 * @param p
	 *            the literal
	 * @param value
	 *            the activity value
	 */
    public void setVarActivity(int p, double value) {
        updateActivity(p, value);
        if (heap.inHeap(p)) {
            heap.increase(p);
        }
    }

    @Override
    public int select() {
        while (!heap.empty()) {
            int p = heap.getmin();
            if (lits.isUnassigned(p)) {
                return p;
            }
        }
        return ILits.UNDEFINED;
    }

    @Override
    public void undo(int x) {
        if (!heap.inHeap(x << 1)) heap.insert(x << 1);
        if (!heap.inHeap((x << 1) ^ 1)) heap.insert((x << 1) ^ 1);
    }

    @Override
    public String toString() {
        return "LiteralOrder";
    }
}
