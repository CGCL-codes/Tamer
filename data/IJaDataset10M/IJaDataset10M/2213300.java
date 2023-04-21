package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.ExprRingFactory;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.interfaces.BiPredicate;

/**
 * Returns <code>True</code>, if the given expression is a polynoomial object
 * 
 */
public class PolynomialQ extends AbstractFunctionEvaluator implements BiPredicate<IExpr> {

    public PolynomialQ() {
    }

    /**
	 * Returns <code>True</code> if the given expression is a polynoomial object;
	 * <code>False</code> otherwise
	 */
    @Override
    public IExpr evaluate(final IAST ast) {
        if (ast.size() != 3) {
            throw new WrongNumberOfArguments(ast, 2, ast.size() - 1);
        }
        IAST list;
        if (ast.get(2).isList()) {
            list = (IAST) ast.get(2);
        } else {
            list = List(ast.get(2));
        }
        return F.bool(polynomialQ(ast.get(1), list));
    }

    public static boolean polynomialQ(final IExpr polnomialExpr, final IAST variables) {
        try {
            IExpr expr = F.evalExpandAll(polnomialExpr);
            ASTRange r = new ASTRange(variables, 1);
            JASConvert<IExpr> jas = new JASConvert<IExpr>(r.toList(), new ExprRingFactory());
            return jas.expr2IExprJAS(expr) != null;
        } catch (JASConversionException e) {
        }
        return false;
    }

    @Override
    public void setUp(final ISymbol symbol) {
    }

    public boolean apply(final IExpr firstArg, final IExpr secondArg) {
        IAST list;
        if (secondArg.isList()) {
            list = (IAST) secondArg;
        } else {
            list = List(secondArg);
        }
        return polynomialQ(firstArg, list);
    }
}
