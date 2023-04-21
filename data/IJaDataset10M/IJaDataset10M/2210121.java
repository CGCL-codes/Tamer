package net.sf.jga.fn.adaptor;

import net.sf.jga.fn.BinaryFunctor;

/**
 * Executes two BinaryFunctors, returning the results of the second.
 * <p>
 * Copyright &copy; 2005  David A. Hall
 */
public class CompoundBinary<T1, T2, R> extends BinaryFunctor<T1, T2, R> {

    static final long serialVersionUID = -6716796579409524752L;

    private BinaryFunctor<T1, T2, ?> _fn1;

    private BinaryFunctor<T1, T2, R> _fn2;

    public CompoundBinary(BinaryFunctor<T1, T2, ?> fn1, BinaryFunctor<T1, T2, R> fn2) {
        if (fn1 == null || fn2 == null) throw new IllegalArgumentException("Two functors are required");
        _fn1 = fn1;
        _fn2 = fn2;
    }

    /**
     * @return the first of the two nested functors
     */
    public BinaryFunctor<T1, T2, ?> getFirstFunctor() {
        return _fn1;
    }

    /**
     * @return the second of the two nested functors
     */
    public BinaryFunctor<T1, T2, R> getSecondFunctor() {
        return _fn2;
    }

    /**
     * Executes both functors, returning the results of the second.
     */
    public R fn(T1 arg1, T2 arg2) {
        _fn1.fn(arg1, arg2);
        return _fn2.fn(arg1, arg2);
    }

    /**
     * Calls the Visitor's <code>visit(CompoundBinary)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof CompoundBinary.Visitor) ((CompoundBinary.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return _fn1 + "," + _fn2;
    }

    /**
     * Interface for classes that may interpret a <b>Constant</b> binaryFunctor.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(CompoundBinary<?, ?, ?> host);
    }
}
