package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;
import org.multijava.util.compiler.UnpositionedError;

/**
 * This class represents the addition binary expression.
 */
public class JmlAddExpression extends JAddExpression {

    /**
     * Construct a node in the parsing tree.
     * This method is directly called by the parser.
     * @param	where		the line of this node in the source code
     * @param	left		the left operand
     * @param	right		the right operand
     */
    public JmlAddExpression(TokenReference where, JExpression left, JExpression right) {
        super(where, left, right);
    }

    /**
     * Typechecks the expression and mutates the context to record
     * information gathered during typechecking.
     *
     * @param context the context in which this expression appears
     * @return  a desugared Java expression (see <code>JExpression.typecheck()</code>)
     * @exception PositionedError if the check fails */
    public JExpression typecheck(CExpressionContextType context) throws PositionedError {
        JExpression[] lr = JmlBinaryArithmeticExpressionHelper.typecheck(left, right, getTokenReference(), context);
        left = lr[0];
        right = lr[1];
        return super.typecheck(context);
    }

    /**
     * Verify the operation. Overridden in JML to issue warnings.
     */
    public JExpression verifyOperation(JExpression expr, CExpressionContextType context) {
        if (expr.getType().isNumeric()) {
            return JmlBinaryArithmeticExpressionHelper.verifyOperation(expr, context, OPE_PLUS);
        } else {
            return super.verifyOperation(expr, context);
        }
    }

    public JExpression constantFolding(CExpressionContextType context) throws UnpositionedError {
        JExpression result = this;
        if (type != JmlStdType.Bigint && type != JmlStdType.Real) {
            try {
                result = super.constantFolding(context);
            } catch (UnpositionedError e) {
                if (context.arithmeticMode().equals(AMID_JAVA_MATH)) {
                    fail();
                }
                if (context.arithmeticMode().equals(AMID_SAFE_MATH)) {
                    throw e;
                }
                result = this;
                type = (left.getType().isFloatingPoint() || right.getType().isFloatingPoint()) ? JmlStdType.Real : JmlStdType.Bigint;
            }
        }
        return result;
    }

    /**
     * Override of super class method to send the warning if the unsafe operation
     * result in an overflow when folding constants
     */
    public int compute(int left, int right, CExpressionContextType context) {
        JmlBinaryArithmeticExpressionHelper.compute(this, left, right, context, OPE_PLUS);
        return super.compute(left, right, context);
    }

    /**
     * Override of super class method to send the warning if the unsafe operation
     * result in an overflow when folding constants
     */
    public long compute(long left, long right, CExpressionContextType context) {
        JmlBinaryArithmeticExpressionHelper.compute(this, left, right, context, OPE_PLUS);
        return super.compute(left, right, context);
    }
}
