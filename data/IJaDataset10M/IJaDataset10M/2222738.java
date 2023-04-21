package com.rubecula.jexpression;

/**
 * This is primarily as
 * 
 * @author Robin Hillyard
 * 
 */
public final class CalculatorRPN {

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        if (args.length > 0) {
            try {
                evaluateAndShow(args);
            } catch (JexpressionException e) {
                e.printStackTrace();
            }
        } else System.err.println("Syntax: Calculator ... (reverse polish notation)");
    }

    /**
	 * XXX parameterize the reference to System.out.
	 * 
	 * @param className
	 * @param tokens
	 * @throws JexpressionException
	 */
    static void evaluateAndShow(final String className, final CharSequence... tokens) throws JexpressionException {
        final Evaluator evaluator = EvaluatorFactory.createEvaluator(className, tokens);
        final Number value = evaluator.getValue();
        final CharSequence expression = tokens.length == 1 ? tokens[0] : evaluator.getExpression();
        System.out.println("The value of \"" + expression + "\" is " + value + " (using the class " + className + ")");
    }

    /**
	 * @param args
	 * @throws JexpressionException
	 */
    static void evaluateAndShow(final String[] args) throws JexpressionException {
        evaluateAndShow("com.rubecula.jexpression.rpn.Evaluator_RPN", args);
    }
}
