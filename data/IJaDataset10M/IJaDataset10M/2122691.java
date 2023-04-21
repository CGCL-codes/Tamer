package net.sf.jga.fn.adaptor;

import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.util.ArrayUtils;

/**
 * Produces an array containing the results of  passing an input argument to a given set of unary
 * functors.
 * <p>
 * Copyright &copy; 2004-2005  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public class ApplyUnary<T> extends UnaryFunctor<T, Object[]> {

    static final long serialVersionUID = -7934367561074978884L;

    private UnaryFunctor<T, ?>[] _functors;

    public ApplyUnary(UnaryFunctor<T, ?>... functors) {
        _functors = functors;
    }

    public UnaryFunctor<T, ?>[] getFunctors() {
        return _functors;
    }

    public Object[] fn(T arg) {
        Object[] result = new Object[_functors.length];
        for (int i = 0; i < _functors.length; ++i) {
            result[i] = _functors[i].fn(arg);
        }
        return result;
    }

    /**
     * Calls the Visitor's <code>visit(ApplyUnary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof ApplyUnary.Visitor) ((ApplyUnary.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return ArrayUtils.toString(_functors);
    }

    /**
     * Interface for classes that may interpret an <b>ApplyUnary</b> predicate.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(ApplyUnary<?> host);
    }
}
