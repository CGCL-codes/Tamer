package com.google.template.soy.jssrc.restricted;

import com.google.template.soy.exprtree.Operator;
import java.util.List;

/**
 * Common utilities for dealing with JS expressions.
 *
 * <p> Important: This class may only be used in implementing plugins (e.g. functions, directives).
 *
 */
public class JsExprUtils {

    private JsExprUtils() {
    }

    /**
   * Builds one JS expression that computes the concatenation of the given JS expressions. The '+'
   * operator is used for concatenation. Operands will be protected with an extra pair of
   * parentheses if and only if needed.
   *
   * @param jsExprs The JS expressions to concatentate.
   * @return One JS expression that computes the concatenation of the given JS expressions.
   */
    public static JsExpr concatJsExprs(List<JsExpr> jsExprs) {
        if (jsExprs.size() == 0) {
            return new JsExpr("''", Integer.MAX_VALUE);
        }
        if (jsExprs.size() == 1) {
            return jsExprs.get(0);
        }
        int plusOpPrec = Operator.PLUS.getPrecedence();
        StringBuilder resultSb = new StringBuilder();
        boolean isFirst = true;
        for (JsExpr jsExpr : jsExprs) {
            boolean needsProtection = isFirst ? jsExpr.getPrecedence() < plusOpPrec : jsExpr.getPrecedence() <= plusOpPrec;
            if (isFirst) {
                isFirst = false;
            } else {
                resultSb.append(" + ");
            }
            if (needsProtection) {
                resultSb.append("(").append(jsExpr.getText()).append(")");
            } else {
                resultSb.append(jsExpr.getText());
            }
        }
        return new JsExpr(resultSb.toString(), plusOpPrec);
    }
}
