package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Return the factors of an integer number
 * 
 * FactorInteger[-32536] ==> {{-1,1},{2,3},{7,2},{83,1}}
 */
public class FactorInteger extends AbstractTrigArg1 {

    public FactorInteger() {
    }

    @Override
    public IExpr numericEvalD1(final Num arg1) {
        return null;
    }

    @Override
    public IExpr numericEvalDC1(final ComplexNum arg1) {
        return null;
    }

    @Override
    public IExpr evaluateArg1(final IExpr arg1) {
        if (arg1.isInteger()) {
            return ((IntegerSym) arg1).factorInteger();
        }
        return null;
    }

    @Override
    public void setUp(final ISymbol symbol) throws SyntaxError {
        symbol.setAttributes(ISymbol.LISTABLE);
        super.setUp(symbol);
    }
}
