package JaCoP.search;

import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.core.Var;

/**
 * Defines functionality of limited discrepancy search. Plugin in this object to
 * search to change your depth first search into limited discrepancy search.
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 * @version 3.0
 * @param <T> type of variable being used in the search.
 */
public class LDS<T extends Var> implements ExitChildListener<T> {

    boolean timeOut = false;

    int noDiscrepancies;

    int maxNoDiscrepancies;

    boolean recentExitingLeftChildGoingForDiscrepancy = false;

    boolean recentExitingRightChild = false;

    ExitChildListener<T>[] exitChildListeners;

    /**
	 * The search will not be allowed to deviate more than maxDiscrepancies
	 * times from the heuristic (e.g. variable and value ordering) in the
	 * search.
	 * @param maxDiscrepancies maximal number of discrepancies allowed.
	 */
    public LDS(int maxDiscrepancies) {
        assert (maxDiscrepancies >= 0);
        this.maxNoDiscrepancies = maxDiscrepancies;
    }

    /**
	 * It is executed after exiting the left child. The parameters specify the
	 * variable and value used in the choice point. The parameter status
	 * specifies the return code from the child. The return parameter of this
	 * function specifies if the search should continue undisturbed or exit the
	 * current search node with value false.
	 */
    public boolean leftChild(T var, int value, boolean status) {
        if (!status) {
            noDiscrepancies++;
            if (noDiscrepancies >= maxNoDiscrepancies) {
                if (exitChildListeners != null) {
                    for (int i = 0; i < exitChildListeners.length; i++) exitChildListeners[i].leftChild(var, value, status);
                }
                noDiscrepancies--;
                return false;
            } else {
                if (exitChildListeners != null) {
                    boolean code = false;
                    for (int i = 0; i < exitChildListeners.length; i++) code |= exitChildListeners[i].leftChild(var, value, status);
                    if (!code) noDiscrepancies--;
                    return code;
                }
                return true;
            }
        }
        return status;
    }

    /**
	 * It is executed after exiting the left child. The parameters specify the
	 * choice point. The parameter status specifies the return code from the
	 * child. The return parameter of this function specifies if the search
	 * should continue undisturbed or exit the current search node with false.
	 * If the continuing to the right child will exceed the number of allowed 
	 * discrepancies then this function will return false so the right child 
	 * will not be explored.
	 */
    public boolean leftChild(PrimitiveConstraint choice, boolean status) {
        if (!status) {
            noDiscrepancies++;
            if (noDiscrepancies >= maxNoDiscrepancies) {
                if (exitChildListeners != null) {
                    for (int i = 0; i < exitChildListeners.length; i++) exitChildListeners[i].leftChild(choice, status);
                }
                noDiscrepancies--;
                return false;
            } else {
                if (exitChildListeners != null) {
                    boolean code = false;
                    for (int i = 0; i < exitChildListeners.length; i++) code |= exitChildListeners[i].leftChild(choice, status);
                    if (!code) noDiscrepancies--;
                    return code;
                }
                return true;
            }
        }
        return status;
    }

    /**
	 * Exiting the right children requires reduction of the current
	 * number of discrepancies being used.
	 */
    public void rightChild(T var, int value, boolean status) {
        noDiscrepancies--;
    }

    public void rightChild(PrimitiveConstraint choice, boolean status) {
        noDiscrepancies--;
    }

    public void setChildrenListeners(ExitChildListener<T>[] children) {
        exitChildListeners = children;
    }

    public void setChildrenListeners(ExitChildListener<T> child) {
        exitChildListeners = new ExitChildListener[1];
        exitChildListeners[0] = child;
    }
}
