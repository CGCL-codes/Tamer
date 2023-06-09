package org.nfunk.jep.function;

import java.lang.Math;
import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * The exp function.
 * Defines a method exp(Object param)
 * which calculates 
 * @author Rich Morris
 * Created on 20-Jun-2003
 */
public class Exp extends PostfixMathCommand {

    public Exp() {
        numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        inStack.push(exp(param));
        return;
    }

    public Object exp(Object param) throws ParseException {
        if (param instanceof Complex) {
            Complex z = (Complex) param;
            double x = z.re();
            double y = z.im();
            double mod = Math.exp(x);
            return new Complex(mod * Math.cos(y), mod * Math.sin(y));
        } else if (param instanceof Number) {
            return new Double(Math.exp(((Number) param).doubleValue()));
        }
        throw new ParseException("Invalid parameter type");
    }
}
