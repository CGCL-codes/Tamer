package com.amazon.carbonado.rel;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class OpenFilter extends Filter {

    public static final OpenFilter THE = new OpenFilter();

    private OpenFilter() {
    }

    public <R, P> R accept(FilterVisitor<R, P> visitor, P param) {
        return visitor.visit(this, param);
    }

    @Override
    public Filter and(Filter filter) {
        return filter;
    }

    @Override
    public Filter or(Filter filter) {
        return this;
    }

    @Override
    public Filter not() {
        return ClosedFilter.THE;
    }

    @Override
    public boolean isDisjunctiveNormalForm() {
        return true;
    }

    @Override
    public boolean isConjunctiveNormalForm() {
        return true;
    }

    @Override
    int computeHashCode() {
        return System.identityHashCode(this);
    }

    @Override
    Filter buildDisjunctiveNormalForm() {
        return this;
    }

    @Override
    Filter buildConjunctiveNormalForm() {
        return this;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
