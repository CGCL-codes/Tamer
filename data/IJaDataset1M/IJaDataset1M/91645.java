package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Predicate function
 * 
 * Returns <code>1</code> if the 1st argument evaluates to <code>True</code>;
 * returns <code>0</code> if the 1st argument evaluates to <code>False</code>;
 * and <code>null</code> otherwise.
 */
public class Boole extends AbstractFunctionEvaluator {

    public Boole() {
    }

    @Override
    public IExpr evaluate(final IAST functionList) {
        if (functionList.size() != 2) {
            throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
        }
        if (functionList.get(1).isSymbol()) {
            if (functionList.get(1).equals(F.True)) {
                return F.C1;
            }
            if (functionList.get(1).equals(F.False)) {
                return F.C0;
            }
        }
        return null;
    }

    @Override
    public void setUp(final ISymbol symbol) {
        symbol.setAttributes(ISymbol.LISTABLE);
    }
}
