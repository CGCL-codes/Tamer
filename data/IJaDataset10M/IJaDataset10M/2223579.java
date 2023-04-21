package JaCoP.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.IntervalDomain;
import JaCoP.core.Store;
import JaCoP.core.TimeStamp;
import JaCoP.core.Var;

/**
 * Among constraint in its simplest form. It establishes the following
 * relation. The given number N of X`s take values from the supplied set
 * of values Kset. 
 * 
 * This constraint implements a simple and polynomial algorithm to establish 
 * GAC as presented in different research papers. There are number of 
 * improvements (iterative execution, optimization of computational load upon 
 * backtracking) to improve the constraint further. 
 * 
 * @author Polina Makeeva and Radoslaw Szymanek
 * @version 3.0
 */
public class Among extends Constraint {

    static final boolean debugAll = false;

    static int counter = 1;

    /**
	 * It specifies the list of variables whose values are checked.
	 */
    public IntVar[] list;

    /**
	 * It specifies a set of values which if assigned to a variable from a list makes variable counted.
	 */
    public IntervalDomain kSet;

    /**
	 * It is a counter variable.
	 */
    public IntVar n;

    private TimeStamp<Integer> lowerBorder;

    private TimeStamp<Integer> upperBorder;

    LinkedHashSet<IntVar> variableQueue = new LinkedHashSet<IntVar>();

    private HashMap<IntVar, Integer> position;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "list", "kSet", "n" };

    /**
	 * It constructs an Among constraint.
	 * @param list variables which are compared to Kset
	 * @param kSet set of integer values against which we check if variables are equal to.
	 * @param n number of possible variables equal to a value from Kset.
	 */
    public Among(IntVar[] list, IntervalDomain kSet, IntVar n) {
        assert (list != null) : "List of variables is null";
        assert (kSet != null) : "Set of values is null";
        assert (n != null) : "N variable is null";
        this.queueIndex = 1;
        numberId = counter++;
        this.list = new IntVar[list.length];
        for (int i = 0; i < list.length; i++) {
            assert (list[i] != null) : i + "-th element in list is null";
            this.list[i] = list[i];
        }
        this.numberArgs = (short) (1 + list.length);
        this.kSet = kSet.clone();
        this.n = n;
    }

    /**
	 * It constructs an Among constraint.
	 * @param list variables which are compared to Kset
	 * @param kSet set of integer values against which we check if variables are equal to.
	 * @param n number of possible variables equal to a value from Kset.
	 */
    public Among(ArrayList<IntVar> list, IntervalDomain kSet, IntVar n) {
        this(list.toArray(new IntVar[list.size()]), kSet, n);
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(this.numberArgs - 1);
        variables.add(this.n);
        for (Var v : this.list) variables.add(v);
        return variables;
    }

    @Override
    public void removeLevel(int level) {
        variableQueue.clear();
    }

    @Override
    public void consistency(Store store) {
        if (debugAll) {
            System.out.println("LEVEL : " + store.level);
            System.out.println(this);
        }
        int currentLB = lowerBorder.value();
        int currentUB = upperBorder.value();
        for (IntVar var : variableQueue) {
            if (kSet.contains(var.domain)) {
                int posVar = position.get(var);
                if (posVar != currentLB) {
                    list[posVar] = list[currentLB];
                    list[currentLB] = var;
                    position.put(var, currentLB);
                    position.put(list[posVar], posVar);
                }
                currentLB++;
                var.removeConstraint(this);
            }
            if (!kSet.isIntersecting(var.domain)) {
                int posVar = position.get(var);
                if (posVar != currentUB) {
                    list[posVar] = list[currentUB - 1];
                    list[currentUB - 1] = var;
                    position.put(var, currentUB - 1);
                    position.put(list[posVar], posVar);
                }
                currentUB--;
                var.removeConstraint(this);
            }
        }
        variableQueue.clear();
        if (debugAll) {
            System.out.println("lbS = " + currentLB);
            System.out.println("ubS = " + currentUB);
            System.out.println(" domain of N " + n.domain + " is in [ " + Math.max(n.min(), currentLB) + ", " + Math.min(n.max(), currentUB) + " ]");
        }
        if (Math.max(n.min(), currentLB) > Math.min(n.max(), currentUB)) throw Store.failException;
        n.domain.in(store.level, n, Math.max(n.min(), currentLB), Math.min(n.max(), currentUB));
        upperBorder.update(currentUB);
        lowerBorder.update(currentLB);
        if (currentLB == n.min() && n.domain.singleton()) {
            for (int i = currentLB; i < currentUB; i++) {
                IntVar var = list[i];
                if (!kSet.contains(var.domain)) {
                    if (debugAll) {
                        System.out.println("lb >> The value before in of " + var.id + ": " + var.domain);
                        System.out.println("lb >> subtrack " + kSet);
                        System.out.println("lb >> equals " + var.domain.subtract(kSet));
                    }
                    var.domain.in(store.level, var, var.domain.subtract(kSet));
                    var.removeConstraint(this);
                    if (debugAll) System.out.println("lb >> The value after in of " + var.id + ": " + var.domain);
                }
            }
            upperBorder.update(currentLB);
            if (debugAll) System.out.println("Simple Among is satisfied");
        }
        if (currentUB == n.min() && n.domain.singleton()) {
            for (int i = currentLB; i < currentUB; i++) {
                IntVar var = list[i];
                var.domain.in(store.level, var, kSet);
                var.removeConstraint(this);
            }
            lowerBorder.update(currentUB);
            if (debugAll) System.out.println("Simple Among is satisfied");
        }
        if (debugAll) System.out.println(this);
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return IntDomain.ANY;
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        store.registerRemoveLevelListener(this);
        this.lowerBorder = new TimeStamp<Integer>(store, 0);
        this.upperBorder = new TimeStamp<Integer>(store, list.length);
        int level = store.level;
        int pos = 0;
        position = new HashMap<IntVar, Integer>();
        for (IntVar var : list) {
            position.put(var, pos);
            var.putConstraint(this);
            queueVariable(level, var);
            pos++;
        }
        n.putConstraint(this);
        store.addChanged(this);
        store.countConstraint();
    }

    @Override
    public void queueVariable(int level, Var var) {
        if (debugAll) System.out.println("Var " + var + ((IntVar) var).recentDomainPruning());
        if (var != n) variableQueue.add((IntVar) var);
    }

    @Override
    public void removeConstraint() {
        for (Var var : list) var.removeConstraint(this);
        n.removeConstraint(this);
    }

    @Override
    public boolean satisfied() {
        return (lowerBorder.value() == upperBorder.value() && n.min() == lowerBorder.value() && n.singleton());
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(id());
        result.append(" Among(");
        for (IntVar var : this.list) result.append("variable").append(var.id).append(" : ").append(var.domain).append(" ");
        result.append(")\n Kset : ").append(this.kSet).append("\n");
        result.append("variable ").append(n.id).append(" : ").append(n.domain).append(")\n");
        return result.toString();
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            n.weight++;
            for (Var v : list) v.weight++;
        }
    }
}
