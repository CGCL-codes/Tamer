package JaCoP.search;

import JaCoP.search.ComparatorVariable;
import JaCoP.core.Var;

/**
 * 
 * Defines a WeightedDegree comparator for Variables. Every time a constraint
 * failure is encountered all variables within the scope of that constraints
 * have increased weight. The comparator will choose the variable with the 
 * highest weight divided by its size.
 * 
 * 
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 * 
 * @version 3.0
 * @param <T> type of variable being compared.
 * 
 */
public class WeightedDegree<T extends Var> implements ComparatorVariable<T> {

    public int compare(float left, T var) {
        float right = ((float) var.weight) / ((float) var.getSize());
        if (left > right) return 1;
        if (left < right) return -1;
        return 0;
    }

    public int compare(T leftVar, T rightVar) {
        float left = ((float) leftVar.weight) / ((float) leftVar.getSize());
        float right = ((float) rightVar.weight) / ((float) rightVar.getSize());
        if (left > right) return 1;
        if (left < right) return -1;
        return 0;
    }

    public float metric(T var) {
        return ((float) var.weight) / ((float) var.getSize());
    }
}
