package MScheme.machine;

import MScheme.expressions.SExpr;

public class Machine {

    private ContinuationStack _stack = new ContinuationStack();

    SExpr run(SExpr sexpr) {
        try {
            Continuation cc;
            while ((cc = _stack.getTop()) != null) {
                sexpr = cc.invoke(_stack, sexpr);
            }
        } catch (Exception e) {
        }
        return sexpr;
    }
}
