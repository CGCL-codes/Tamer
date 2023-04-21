package net.sf.jga.fn.adaptor;

import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Unary Functor that passes the results of two Unary Functors as the arguments
 * to a Binary Functor.  This allows for the construction of compound functors
 * from the primitives found in the arithmetic, logical, property, and
 * comparison packages.
 * <p>
 * For example: LogicalAnd is of limited utility since it takes only Boolean
 * arguments.  To use LogicalAnd for something a little more interesting
 * (eg, is the given integer between 1 and 10), combine LogicalAnd with
 * GreaterEqual and LessEqual using Binary&nbsp;Compose and Bind1st as follows:
 * <pre>
 * new ComposeUnary&lt;Integer,Boolean,Boolean,Boolean&gt; (
 *     new Bind1st&lt;Integer,Integer,Boolean&gt; (1, new LessEqual&lt;Integer&gt;()),
 *     new Bind1st&lt;Integer,Integer,Boolean&gt; (10, new GreaterEqual&lt;Integer&gt;()),
 *     new LogicalAnd());
 * </pre>
 * While it may not be the most readable construction in the world, it does
 * become easier over time.
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 **/
public class ComposeUnary<T, F1, F2, R> extends UnaryFunctor<T, R> {

    static final long serialVersionUID = -836030733262754108L;

    private UnaryFunctor<T, F1> _f;

    private UnaryFunctor<T, F2> _g;

    private BinaryFunctor<F1, F2, R> _h;

    /**
     * Builds a ComposeUnary functor, given two inner functors <b>f</b> and
     * <b>g</b>, and outer functor <b>h</b>.
     * @throws IllegalArgumentException if any of the functors is missing
     */
    public ComposeUnary(UnaryFunctor<T, F1> f, UnaryFunctor<T, F2> g, BinaryFunctor<F1, F2, R> h) {
        if (f == null || g == null || h == null) {
            throw new IllegalArgumentException("Three functors are required");
        }
        _f = f;
        _g = g;
        _h = h;
    }

    /**
     * Returns the first of two inner functors
     * @return the first of two inner functors
     */
    public UnaryFunctor<T, F1> getFirstInnerFunctor() {
        return _f;
    }

    /**
     * Returns the second of two inner functors
     * @return the second of two inner functors
     */
    public UnaryFunctor<T, F2> getSecondInnerFunctor() {
        return _g;
    }

    /**
     * Returns the outer functor
     * @return the outer functor
     */
    public BinaryFunctor<F1, F2, R> getOuterFunctor() {
        return _h;
    }

    /**
     * Given argument <b>x</b>, passes x to both inner functors, and passes the
     * results of those functors to the outer functor.
     * 
     * @return h(f(x), g(x))
     */
    public R fn(T x) {
        return _h.fn(_f.fn(x), _g.fn(x));
    }

    /**
     * Calls the Visitor's <code>visit(ComposeUnary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof ComposeUnary.Visitor) ((ComposeUnary.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return _h + ".compose(" + _f + "," + _g + ")";
    }

    /**
     * Interface for classes that may interpret a <b>ComposeUnary</b> functor.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(ComposeUnary<?, ?, ?, ?> host);
    }
}
