package JaCoP.set.constraints;

import java.util.ArrayList;
import JaCoP.constraints.Constraint;
import JaCoP.core.IntDomain;
import JaCoP.core.IntervalDomain;
import JaCoP.core.Store;
import JaCoP.core.TimeStamp;
import JaCoP.core.ValueEnumeration;
import JaCoP.core.Var;
import JaCoP.set.core.SetDomain;
import JaCoP.set.core.SetVar;

/**
 * It creates a lex constraint on a list of set variables. Each consecutive pair of
 * set variables is being constrained to be lexicographically ordered.
 * 
 * For example, 
 * {} <lex {1}
 * {1, 2} <lex {1, 2, 3}
 * {1, 3} <lex {2}
 * {1} < {2}
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 * @version 3.0
 */
public class Lex extends Constraint {

    static int idNumber = 1;

    /**
	 * It specifies a list on which element a lex relationship holds for every 
	 * two consecutive variables.
	 */
    public SetVar a;

    /**
	 * It specifies a list on which element a lex relationship holds for every 
	 * two consecutive variables.
	 */
    public SetVar b;

    /**
	 * It specifies if the relation is strict or not.
	 */
    public boolean strict = true;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "a", "b" };

    protected int inSupport;

    protected int inclusionLevel = -1;

    protected TimeStamp<IntDomain> inDifference;

    protected int smallerElSupport;

    protected int smallerElLevel = -1;

    protected TimeStamp<IntDomain> smallerDifference;

    /**
	 * It constructs an Lexical ordering constraint to restrict the domain of the variables a and b.
	 * It is strict by default.
	 * 
	 * @param a variable that is restricted to be less than b with lexical order.
	 * @param b variable that is restricted to be greater than a with lexical order.
	 */
    public Lex(SetVar a, SetVar b) {
        assert (a != null) : "Variable a is null";
        assert (b != null) : "Variable b is null";
        numberId = idNumber++;
        numberArgs = 2;
        this.a = a;
        this.b = b;
    }

    /**
	 * It constructs an Lexical ordering constraint to restrict the domain of the variables a and b.
	 * 
	 * @param a variable that is restricted to be less than b with lexical order.
	 * @param b variable that is restricted to be greater than a with lexical order.
	 * @param strict specifies if the lex relation is strict. 
	 */
    public Lex(SetVar a, SetVar b, boolean strict) {
        this(a, b);
        this.strict = strict;
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(2);
        variables.add(a);
        variables.add(b);
        return variables;
    }

    @Override
    public void consistency(Store store) {
        if (strict) {
            if (b.domain.lub().isEmpty()) throw Store.failException;
        }
        if (a.domain.lub().isEmpty()) return;
        if (a.domain.card().min() > 0) b.domain.inLUB(store.level, b, new IntervalDomain(a.domain.lub().min(), Integer.MAX_VALUE));
        if (strict && a.domain.card().singleton(1) && b.domain.card().singleton(1)) b.domain.inLUB(store.level, b, new IntervalDomain(a.domain.lub().min() + 1, Integer.MAX_VALUE));
        if (a.domain.glb().isEmpty()) {
            if (b.domain.glb().isEmpty()) {
                int minA = a.domain.lub().min();
                int maxB = b.domain.lub().max();
                if (strict && minA >= maxB || minA > maxB) a.domain.inLUB(store.level, a, IntDomain.emptyIntDomain);
                return;
            } else {
                ValueEnumeration enumerGLBofB = b.domain.glb().valueEnumeration();
                ValueEnumeration enumerLUBofA = a.domain.lub().valueEnumeration();
                int nextElinGLBofB = enumerGLBofB.nextElement();
                int nextElinLUBofA = enumerLUBofA.nextElement();
                int lastElinLUBofA = Integer.MIN_VALUE;
                while (true) {
                    if (nextElinLUBofA < nextElinGLBofB) {
                        return;
                    }
                    if (nextElinLUBofA == nextElinGLBofB) {
                        lastElinLUBofA = nextElinLUBofA;
                        if (!enumerGLBofB.hasMoreElements()) if (enumerLUBofA.hasMoreElements()) {
                            nextElinLUBofA = enumerLUBofA.nextElement();
                            if ((strict && nextElinLUBofA < b.domain.lub().max()) || (!strict && nextElinLUBofA <= b.domain.lub().max())) {
                                return;
                            } else {
                                a.domain.inLUB(store.level, a, new IntervalDomain(Integer.MIN_VALUE, lastElinLUBofA));
                                return;
                            }
                        }
                        if (!enumerLUBofA.hasMoreElements()) return;
                        nextElinGLBofB = enumerGLBofB.nextElement();
                        nextElinLUBofA = enumerLUBofA.nextElement();
                        continue;
                    }
                    if (nextElinGLBofB < nextElinLUBofA) {
                        if (nextElinLUBofA == a.domain.lub().min()) {
                            a.domain.inLUB(store.level, a, IntDomain.emptyIntDomain);
                            return;
                        } else {
                            a.domain.inLUB(store.level, a, new IntervalDomain(Integer.MIN_VALUE, lastElinLUBofA));
                            return;
                        }
                    }
                }
            }
        } else {
            if (b.domain.glb().isEmpty()) {
                if (strict && a.domain.glb().max() == a.domain.lub().max() && a.domain.card().min() == 1 && a.domain.card().max() == 2 && b.domain.lub().max() == a.domain.glb().max() && b.domain.card().max() == 2) {
                    if (b.domain.lub().min() == a.domain.lub().min()) b.domain.inLUBComplement(store.level, b, b.domain.lub().min());
                    a.domain.inGLB(store.level, a, a.domain.lub().min());
                }
                if (b.domain.card().min() == 0) b.domain.inCardinality(store.level, b, 1, Integer.MAX_VALUE);
            } else {
                int noSmaller = 0;
                ValueEnumeration enumerGLBofA = a.domain.glb().valueEnumeration();
                ValueEnumeration enumerGLBofB = b.domain.glb().valueEnumeration();
                ValueEnumeration enumerLUBofA = a.domain.lub().valueEnumeration();
                int nextElinGLBofA = enumerGLBofA.nextElement();
                int nextElinGLBofB = enumerGLBofB.nextElement();
                int nextElinLUBofA = enumerLUBofA.nextElement();
                int previousElinLUBofA = Integer.MIN_VALUE;
                while (true) {
                    if (nextElinGLBofA < nextElinGLBofB) {
                        if (strict && b.domain.card().min() + 1 == b.domain.card().max() && a.domain.card().max() == 2 && a.domain.card().min() == b.domain.card().max() && a.domain.glb().eq(b.domain.lub())) b.domain.inLUBComplement(store.level, b, nextElinGLBofA);
                        return;
                    }
                    if (nextElinLUBofA < nextElinGLBofA && nextElinLUBofA < nextElinGLBofB) {
                        previousElinLUBofA = nextElinLUBofA;
                        noSmaller++;
                        if (noSmaller == 2) return;
                        nextElinLUBofA = enumerLUBofA.nextElement();
                        continue;
                    }
                    if (nextElinGLBofA == nextElinGLBofB) {
                        if (!enumerGLBofA.hasMoreElements()) {
                            if (noSmaller == 1) {
                                if (strict && b.domain.lub().max() == nextElinGLBofA) {
                                    a.domain.inGLB(store.level, a, previousElinLUBofA);
                                }
                                return;
                            } else {
                                if (strict && nextElinGLBofB == b.domain.lub().previousValue(b.domain.lub().max())) b.domain.inGLB(store.level, b, b.domain.lub().max());
                                if (strict && nextElinGLBofA == a.domain.lub().previousValue(a.domain.lub().max()) && a.domain.lub().max() >= b.domain.lub().max()) a.domain.inLUBComplement(store.level, a, a.domain.lub().max());
                                if (strict && nextElinGLBofA == a.domain.lub().max() && nextElinGLBofB == b.domain.lub().max()) throw Store.failException;
                                return;
                            }
                        }
                        if (!enumerGLBofB.hasMoreElements()) {
                            if (noSmaller == 0) {
                                nextElinLUBofA = enumerLUBofA.nextElement();
                                if (nextElinGLBofA + 1 <= nextElinLUBofA - 1) b.domain.inLUB(store.level, b, new IntervalDomain(nextElinGLBofA + 1, nextElinLUBofA - 1).complement());
                                if ((strict && nextElinLUBofA >= b.domain.lub().max()) || (nextElinLUBofA > b.domain.lub().max())) {
                                    throw Store.failException;
                                }
                                if (strict) {
                                    int previous = b.domain.lub().previousValue(b.domain.lub().max());
                                    if (previous == nextElinGLBofB) b.domain.inGLB(store.level, b, b.domain.lub().max());
                                }
                                return;
                            } else {
                                nextElinLUBofA = enumerLUBofA.nextElement();
                                if ((b.domain.lub().max() <= nextElinLUBofA && strict) || (b.domain.lub().max() < nextElinLUBofA && !strict)) {
                                    a.domain.inGLB(store.level, a, previousElinLUBofA);
                                }
                                return;
                            }
                        }
                        nextElinGLBofA = enumerGLBofA.nextElement();
                        nextElinGLBofB = enumerGLBofB.nextElement();
                        nextElinLUBofA = enumerLUBofA.nextElement();
                        continue;
                    }
                    if (nextElinGLBofB < nextElinGLBofA) {
                        if (noSmaller == 0) {
                            if (nextElinLUBofA == nextElinGLBofB) {
                                a.domain.inGLB(store.level, a, nextElinGLBofB);
                                enumerGLBofA.domainHasChanged();
                                if (!enumerGLBofB.hasMoreElements()) {
                                    nextElinLUBofA = enumerLUBofA.nextElement();
                                    if ((strict && nextElinLUBofA >= b.domain.lub().max()) || nextElinLUBofA > b.domain.lub().max()) throw Store.failException;
                                    nextElinGLBofB = b.domain.lub().max();
                                } else {
                                    nextElinGLBofB = enumerGLBofB.nextElement();
                                    nextElinLUBofA = enumerLUBofA.nextElement();
                                }
                                continue;
                            } else throw Store.failException;
                        }
                        if (noSmaller == 1) {
                            if (nextElinLUBofA == nextElinGLBofB) {
                                if (!enumerGLBofB.hasMoreElements()) {
                                    return;
                                }
                                if (!enumerLUBofA.hasMoreElements()) {
                                }
                                nextElinGLBofB = enumerGLBofB.nextElement();
                                nextElinLUBofA = enumerLUBofA.nextElement();
                                while (true) {
                                    if (nextElinLUBofA < nextElinGLBofB) {
                                        return;
                                    } else if (nextElinGLBofB < nextElinLUBofA) {
                                        a.domain.inGLB(store.level, a, previousElinLUBofA);
                                        return;
                                    } else {
                                        if (!enumerGLBofB.hasMoreElements()) {
                                            return;
                                        }
                                        if (!enumerLUBofA.hasMoreElements()) {
                                        }
                                        nextElinGLBofB = enumerGLBofB.nextElement();
                                        nextElinLUBofA = enumerLUBofA.nextElement();
                                    }
                                }
                            } else {
                                a.domain.inGLB(store.level, a, previousElinLUBofA);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return SetDomain.ANY;
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        store.registerRemoveLevelListener(this);
        smallerDifference = new TimeStamp<IntDomain>(store, a.domain.lub().subtract(b.domain.glb()));
        inDifference = new TimeStamp<IntDomain>(store, b.domain.lub().subtract(a.domain.glb()));
        a.putModelConstraint(this, getConsistencyPruningEvent(a));
        b.putModelConstraint(this, getConsistencyPruningEvent(b));
        assert (!a.domain.lub().contains(Integer.MIN_VALUE)) : "Lex constraint does not allow Integer.MIN_VALUE in the domain";
        assert (!b.domain.lub().contains(Integer.MIN_VALUE)) : "Lex constraint does not allow Integer.MIN_VALUE in the domain";
        inSupport = Integer.MIN_VALUE;
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void removeLevel(int level) {
        if (inclusionLevel == level) inclusionLevel = -1;
        if (smallerElLevel == level) smallerElLevel = -1;
    }

    @Override
    public void removeConstraint() {
        a.removeConstraint(this);
        b.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        return false;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Lex(");
        result.append(a).append(", ").append(b);
        result.append(")");
        return result.toString();
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            a.weight++;
            b.weight++;
        }
    }
}
