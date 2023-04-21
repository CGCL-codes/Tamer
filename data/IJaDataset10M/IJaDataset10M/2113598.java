package net.sf.jga.fn.adaptor;

import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Binary Functor that passes the results of a Binary Functor as the argument
 * to a Unary Functor.  This allows for the construction of compound
 * functors from the primitives found in the arithmetic, logical, property, and
 * comparison packages.
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 **/
public class ChainBinary<T1, T2, F, R> extends BinaryFunctor<T1, T2, R> {

    static final long serialVersionUID = -8161448545088932320L;

    private UnaryFunctor<F, R> _f;

    private BinaryFunctor<T1, T2, F> _g;

    /**
     * Builds a ChainBinary functor, given outer functor <b>f</b> and inner
     * functor <b>g</b>.
     * @throws IllegalArgumentException if any of the functors is missing
     */
    public ChainBinary(UnaryFunctor<F, R> f, BinaryFunctor<T1, T2, F> g) {
        if (f == null || g == null) {
            String msg = "Two functors are required";
            throw new IllegalArgumentException(msg);
        }
        _f = f;
        _g = g;
        ;
    }

    /**
     * Returns the outer functor
     * @return the outer functor
     */
    public UnaryFunctor<F, R> getOuterFunctor() {
        return _f;
    }

    /**
     * Returns the inner functor
     * @return the inner functor
     */
    public BinaryFunctor<T1, T2, F> getInnerFunctor() {
        return _g;
    }

    /**
     * Passes arguments <b>x</b> and <b>y</b> to the inner functor, and passes
     * the result to the outer functor.
     * 
     * @return f(g(x,y))
     */
    public R fn(T1 x, T2 y) {
        return _f.fn(_g.fn(x, y));
    }

    /**
     * Calls the Visitor's <code>visit(ChainBinary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof ChainBinary.Visitor) ((ChainBinary.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return _f + ".compose(" + _g + ")";
    }

    /**
     * Interface for classes that may interpret a <b>ChainBinary</b> functor.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(ChainBinary<?, ?, ?, ?> host);
    }
}
