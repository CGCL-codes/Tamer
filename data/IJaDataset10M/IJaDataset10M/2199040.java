package net.sf.jga.fn.adaptor;

import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.Functor;
import net.sf.jga.util.ArrayUtils;

/**
 * Functor that uses a Generator to produce the Nth argument to a given Functor.
 * The generated result is inserted into the argument list given to this functor
 * with the modified list being passed to the nested functor.
 * <p>
 * Copyright &copy; 2009  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public class ComposeBinaryNth<R> extends Functor<R> {

    private static final long serialVersionUID = 1371141864350370835L;

    private Functor<R> fn;

    private BinaryFunctor<?, ?, ?> bf;

    private int idx;

    public ComposeBinaryNth(Functor<R> fn, int idx, BinaryFunctor<?, ?, ?> bf) {
        if (fn == null) {
            throw new IllegalArgumentException("Must supply a Functor whose argument is to be modified");
        }
        if (idx < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        if (bf == null) {
            throw new IllegalArgumentException("Must supply a BinaryFunctor to replace a pair of runtime arguments");
        }
        this.fn = fn;
        this.bf = bf;
        this.idx = idx;
    }

    /**
     * Returns the Functor that is invoked
     */
    public Functor<R> getFunctor() {
        return fn;
    }

    /**
     * Returns the generator that produces the indicated argument.
     */
    public BinaryFunctor<?, ?, ?> getBinaryFunctor() {
        return bf;
    }

    /**
     * Returns the results of the functor, using the nested generator to produce the Nth argument.
     * If there are fewer than N+1 arguments, no modification will be attempted.
     * @return fn.eval(args[0], ..., args[N-1], bf(args[N], args[N+1]), args[N+2], ...)
     */
    public R eval(Object... args) {
        if (idx >= args.length - 1) {
            return fn.eval(args);
        }
        Object newArg = bf.eval(args[idx], args[idx + 1]);
        Object[] newArgs = ArrayUtils.remove(args, idx);
        newArgs[idx] = newArg;
        return fn.eval(newArgs);
    }

    /**
     * Calls the Visitor's <code>visit(Generate2nd)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof ComposeBinaryNth.Visitor) ((ComposeBinaryNth.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return fn + ".composeNth(" + idx + ":" + bf + ")";
    }

    /**
     * Interface for classes that may interpret a <b>Generate2nd</b>
     * functor.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(ComposeBinaryNth<?> host);
    }
}
