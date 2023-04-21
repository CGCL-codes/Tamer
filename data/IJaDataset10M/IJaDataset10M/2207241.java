package net.sourceforge.jfunctions.functions.basic;

import net.sourceforge.jfunctions.filters.Filter;
import net.sourceforge.jfunctions.functions.Transformation;
import net.sourceforge.jfunctions.structures.GenericPair;
import net.sourceforge.jfunctions.structures.Pair;

public class FilterAttachTransformation<T> extends Transformation<T, Pair<T, Boolean>> {

    private Filter<T> filter;

    public FilterAttachTransformation(Filter<T> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Pair<T, Boolean> apply(T argument) {
        return new GenericPair<T, Boolean>(argument, filter.accept(argument));
    }

    @Override
    public String toString() {
        return "pair(filter(" + filter + "),boolean)";
    }
}
