package es.caib.zkib.jxpath.ri.compiler;

import es.caib.zkib.jxpath.ri.EvalContext;
import es.caib.zkib.jxpath.ri.InfoSetUtil;

/**
 * Implementation of {@link Expression} for the operation "mod".
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.1 $ $Date: 2009-04-03 08:13:14 $
 */
public class CoreOperationMod extends CoreOperation {

    /**
     * Create a new CoreOperationMod.
     * @param arg1 dividend
     * @param arg2 divisor
     */
    public CoreOperationMod(Expression arg1, Expression arg2) {
        super(new Expression[] { arg1, arg2 });
    }

    public Object computeValue(EvalContext context) {
        long l = (long) InfoSetUtil.doubleValue(args[0].computeValue(context));
        long r = (long) InfoSetUtil.doubleValue(args[1].computeValue(context));
        return new Double(l % r);
    }

    protected int getPrecedence() {
        return MULTIPLY_PRECEDENCE;
    }

    protected boolean isSymmetric() {
        return false;
    }

    public String getSymbol() {
        return "mod";
    }
}
