package org.retro.lisp;

import java.util.Vector;

public class Bsetq extends Function {

    public Object evaluate(Interpreter interp, Vector sexp) throws RunTimeException {
        Object rhs = null;
        for (int i = 0; i < sexp.size(); i += 2) {
            try {
                String lhs = ((Name) sexp.elementAt(i)).toString();
                Object orhs = interp.env.get(lhs);
                rhs = interp.evaluateSExp(sexp.elementAt(i + 1));
                if ((orhs != null) && (orhs.getClass() != rhs.getClass())) {
                    throw new RunTimeException("Type mismatch error.", sexp);
                }
                interp.env.put(lhs, rhs);
            } catch (ClassCastException cce) {
                throw new RunTimeException(cce.toString(), sexp);
            }
        }
        return rhs;
    }

    public void verifyArguments(Vector sexp) throws RunTimeException {
        if (sexp.size() < 2) throw new RunTimeException("Too few arguments to setq.", sexp);
        if (sexp.size() % 2 != 0) throw new RunTimeException("setq requires variable " + "value pairs.", sexp);
    }
}
