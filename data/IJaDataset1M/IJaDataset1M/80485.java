package net.sf.jga.fn.logical;

import net.sf.jga.fn.UnaryFunctor;

/**
 * Unary Predicate that returns true when Boolean argument <b>x</b> is false.
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 **/
public class LogicalNot extends UnaryFunctor<Boolean, Boolean> {

    static final long serialVersionUID = -3464871237361189509L;

    /**
     * Given Boolean argument <b>x</b>, returns true when x is false, false
     * when x is true
     * 
     * @return !x
     */
    public Boolean fn(Boolean x) {
        return !x;
    }

    /**
     * Calls the Visitor's <code>visit(LogicalNot)</code> method, if it
     * implements the nested Visitor interface.
     */
    public void accept(net.sf.jga.fn.Visitor v) {
        if (v instanceof LogicalNot.Visitor) ((LogicalNot.Visitor) v).visit(this); else v.visit(this);
    }

    public String toString() {
        return "LogicalNot";
    }

    /**
     * Interface for classes that may interpret a <b>LogicalNot</b>
     * predicate.
     */
    public interface Visitor extends net.sf.jga.fn.Visitor {

        public void visit(LogicalNot host);
    }
}
