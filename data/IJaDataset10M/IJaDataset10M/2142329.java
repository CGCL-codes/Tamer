package sies.kr.term;

import java.util.ArrayList;
import java.util.HashMap;
import sies.kr.reasoning.WorkingMemory;

public class RegEx extends Compound {

    /**
	 * <code>EXPRCANNOTBEAREGEXOPERATORERRMSG</code> error when an
	 * expression is not a negation.
	 * 
	 */
    private static final String EXPRCANNOTBEAREGEXOPERATORERRMSG = "This cannot be a regular expression operator";

    /**
	 * <code>REGEXOPERATOR</code> is the operator's symbol.
	 * 
	 */
    public static final String REGEXOPERATOR = "~";

    /**
	 * <code>isARegEx</code> returns whether true or false the expresssion
	 * represents a negation.
	 * 
	 * @param expression
	 *            a <code>String</code> value
	 * @return a <code>boolean</code> value
	 */
    public static final boolean isARegEx(final String expression) {
        return expression.matches("^\\" + REGEXOPERATOR + "[(].*[)]$");
    }

    /**
	 * Creates a new <code>RegEx</code> instance.
	 * 
	 * @param expression
	 *            a <code>String</code> value
	 */
    public RegEx(String expression) {
        this(expression, new HashMap());
    }

    /**
	 * Creates a new <code>RegEx</code> instance.
	 * 
	 * @param expression
	 *            a <code>String</code> value
	 * @param variables
	 *            a <code>HashMap</code> value
	 */
    public RegEx(final String expression, final HashMap variables) {
        boolean throwError = true;
        if (isARegEx(expression)) {
            this.buildFromExpression(expression, variables);
            if (this.sanityCheckOk()) {
                throwError = false;
            }
        }
        if (throwError) {
            throw new IllegalArgumentException(EXPRCANNOTBEAREGEXOPERATORERRMSG + ": " + expression);
        }
    }

    /**
	 * <code>getArgument</code> returns the single argument of the negation.
	 * 
	 * @return an <code>Object</code> value
	 */
    public Object getArgument() {
        return this.getArguments().get(0);
    }

    /**
	 * <code>isTrue</code> returns whether true or false the negation
	 * is satisfied.
	 * 
	 * @param wm
	 *            a <code>WorkingMemory</code> value
	 * @return a <code>boolean</code> value
	 */
    public final boolean matches() {
        ArrayList args = this.getArguments();
        String arg0 = ((Constant) args.get(0)).getStringValue();
        String arg1 = ((Constant) args.get(1)).getStringValue();
        return arg0.matches(arg1);
    }

    /**
	 * <code>sanityCheckOk</code> returns whether true or false the predicate
	 * is well-formed.
	 *
	 * @return a <code>boolean</code> value
	 */
    private final boolean sanityCheckOk() {
        if (this.getArguments().size() == 2) {
            for (int i = 0; i < 2; i++) {
                Object arg = this.getArguments().get(i);
                if (!((arg instanceof Constant && ((Constant) arg).isAString()) || (arg instanceof Variable && ((Variable) arg).getValue() instanceof Constant && ((Constant) ((Variable) arg).getValue()).isAString()))) {
                    return false;
                }
            }
        }
        return false;
    }
}
