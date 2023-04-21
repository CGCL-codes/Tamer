package org.sodeja.runtime.scheme3;

import java.util.List;
import org.sodeja.runtime.Evaluator;
import org.sodeja.runtime.Frame;

public class CompiledSchemeUtils {

    public static Object evalSingle(Evaluator<CompiledSchemeExpression> evaluator, Frame<CompiledSchemeExpression> frame, List<CompiledSchemeExpression> valueExpressions) {
        Object value = null;
        for (CompiledSchemeExpression expr : valueExpressions) {
            value = evaluator.eval(frame, expr);
        }
        return value;
    }
}
