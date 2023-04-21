package JaCoP.search;

import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.constraints.XeqC;
import JaCoP.constraints.XgtC;
import JaCoP.constraints.XltC;
import JaCoP.constraints.XlteqC;
import JaCoP.core.IntVar;

/**
 * It is simple and customizable selector of decisions (constraints) which will
 * be enforced by search. However, it does not use X=c as a search decision 
 * but rather X <= c (potentially splitting the domain), unless c is equal to 
 * the maximal value in the domain of X then the constraint X < c is used.
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 * @version 3.0
 * @param <T> type of variable being used in the search.
 */
public class SplitSelect<T extends IntVar> extends SimpleSelect<T> {

    /**
	 * It specifies if the left branch (values smaller or equal to the value selected) 
	 * are first considered.
	 */
    public boolean leftFirst = true;

    /**
	 * The constructor to create a simple choice select mechanism.
	 * @param variables variables upon which the choice points are created.
	 * @param varSelect the variable comparator to choose the variable.
	 * @param indomain the value heuristic to choose a value for a given variable.
	 */
    public SplitSelect(T[] variables, ComparatorVariable<T> varSelect, Indomain<T> indomain) {
        super(variables, varSelect, indomain);
    }

    /**
	 * It constructs a simple selection mechanism for choice points.
	 * @param variables variables used as basis of the choice point.
	 * @param varSelect the main variable comparator.
	 * @param tieBreakerVarSelect secondary variable comparator employed if the first one gives the same metric.
	 * @param indomain the heuristic to choose value assigned to a chosen variable.
	 */
    public SplitSelect(T[] variables, ComparatorVariable<T> varSelect, ComparatorVariable<T> tieBreakerVarSelect, Indomain<T> indomain) {
        super(variables, varSelect, tieBreakerVarSelect, indomain);
    }

    @Override
    public T getChoiceVariable(int index) {
        return null;
    }

    @Override
    public PrimitiveConstraint getChoiceConstraint(int index) {
        T var = super.getChoiceVariable(index);
        if (var == null) return null;
        int value = super.getChoiceValue();
        if (leftFirst) if (var.max() != value) return new XlteqC(var, value); else return new XltC(var, value); else if (var.max() != value) return new XgtC(var, value); else return new XeqC(var, value);
    }
}
