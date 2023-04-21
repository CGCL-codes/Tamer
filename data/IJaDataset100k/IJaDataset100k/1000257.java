package net.sf.jga.fn.arithmetic;

import java.text.MessageFormat;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.fn.EvaluationException;

/**
 * Unary Functor that returns the bitwise not of its argument
 * <p>
 * Copyright &copy; 2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 **/
public class BitwiseNot<T extends Number> extends UnaryFunctor<T, T> {

    static final long serialVersionUID = 6788747054561165216L;

    private transient IntegerArithmetic<T> _math;

    private Class<T> _type;

    /**
     * Builds BitwiseNot functor for the given class.  The class argument must
     * be the same as the generic class argument (when generics are in use)
     * or else a ClassCastException will be thrown when the functor is used.
     *
     * @throws IllegalArgumentException if the given class has no Arithmetic
     *      implementation registered with the ArithmeticFactory
     */
    public BitwiseNot(Class<T> c) {
        _type = c;
        getMath();
    }

    /**
     * Returns the type of operands this instance supports
     */
    public Class<T> getType() {
        return _type;
    }

    /**
     * Given argument <b>x</b>, return ~x
     * @return ~x
     */
    public T fn(T x) {
        try {
            return getMath().not(x);
        } catch (ClassCastException ex) {
            String msg = "ClassCastException: Cannot compute ~{0}[{1}]";
            String err = MessageFormat.format(msg, new Object[] { x.getClass(), x });
            throw new EvaluationException(err, ex);
        }
    }

    /**
     * Calls the Visitor's <code>visit(BitwiseNot)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof BitwiseNot.Visitor) ((BitwiseNot.Visitor) v).visit(this); else v.visit(this);
    }

    /**
     */
    private IntegerArithmetic<T> getMath() {
        if (_math == null) {
            _math = ArithmeticFactory.getIntegralArithmetic(_type);
            if (_math == null) {
                String msg = "No implementation of IntegerArithmetic registered for {0}";
                throw new IllegalArgumentException(MessageFormat.format(msg, new Object[] { _type }));
            }
        }
        return _math;
    }

    public String toString() {
        return "BitwiseNot";
    }

    /**
     * Interface for classes that may interpret a <b>BitwiseNot</b>
     * functor.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(BitwiseNot<? extends Number> host);
    }
}
