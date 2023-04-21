package org.renjin.primitives.special;

import org.renjin.eval.Context;
import org.renjin.sexp.Environment;
import org.renjin.sexp.FunctionCall;
import org.renjin.sexp.Null;
import org.renjin.sexp.PairList;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.SpecialFunction;

public class WhileFunction extends SpecialFunction {

    public WhileFunction() {
        super("while");
    }

    @Override
    public SEXP apply(Context context, Environment rho, FunctionCall call, PairList args) {
        SEXP condition = args.getElementAsSEXP(0);
        SEXP statement = args.getElementAsSEXP(1);
        while (asLogicalNoNA(context, call, context.evaluate(condition, rho))) {
            try {
                context.evaluate(statement, rho);
            } catch (BreakException e) {
                break;
            } catch (NextException e) {
            }
        }
        context.setInvisibleFlag();
        return Null.INSTANCE;
    }
}
