package org.expasy.jpl.insilico.formula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.expasy.jpl.insilico.bio.JPLMassAccuracy;

/**
 * Simple arithmetic (+ and -) {@code JPLFormula} expression over corresponding
 * formula masses.
 * <p>
 * Formula expression format : ([+-]{@code JPLFormula})+
 * 
 */
public class JPLFormulaExpression {

    /** The string representation of formula expression. */
    private String expression;

    /** The average mass of the expression formulas. */
    private double averageDeltaMass = 0;

    /** The monoisotopic mass of the expression formulas. */
    private double monoisotopicDeltaMass = 0;

    /** The expression formula motif. */
    private final Pattern formulaPattern = Pattern.compile("([A-Za-z0-9]+)");

    /** The operator formula motif. */
    private final Pattern operatorPattern = Pattern.compile("([+-])");

    /**
	 * Build a new instance, parse expression and compute delta masses
	 * (both average and monoisotopic) if expression formula format is ok.
	 * 
	 * @param expression the formula expression.
	 * @throws JPLFormulaExpressionParseException if expression formula
	 *                                        has bad format. 
	 */
    public JPLFormulaExpression(String expression) throws JPLFormulaExpressionParseException {
        String operator;
        Matcher operatorMatcher = operatorPattern.matcher(expression);
        Matcher formulaMatcher = formulaPattern.matcher(expression);
        int nextPosition = 0;
        while (nextPosition < expression.length()) {
            if (operatorMatcher.find()) {
                operator = operatorMatcher.group(1);
                nextPosition = operatorMatcher.end();
            } else {
                throw new JPLFormulaExpressionParseException(expression, "missing operator in formula expression", nextPosition);
            }
            if (formulaMatcher.find()) {
                String formula = formulaMatcher.group(1);
                nextPosition = formulaMatcher.end();
                try {
                    if (operator.equals("+")) {
                        monoisotopicDeltaMass += JPLFormula.getMw(formula);
                        averageDeltaMass += JPLFormula.getMw(formula, JPLMassAccuracy.AVERAGE);
                    } else {
                        monoisotopicDeltaMass -= JPLFormula.getMw(formula);
                        averageDeltaMass -= JPLFormula.getMw(formula, JPLMassAccuracy.AVERAGE);
                    }
                } catch (JPLFormulaParseException e) {
                    monoisotopicDeltaMass = averageDeltaMass = 0;
                    throw new JPLFormulaExpressionParseException(expression, e.getMessage(), formulaMatcher.start(), e);
                }
            } else {
                throw new JPLFormulaExpressionParseException(expression, "invalid or missing formula format", nextPosition);
            }
        }
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public double getAvgDeltaMass() {
        return averageDeltaMass;
    }

    public double getMonoIsotopicDeltaMass() {
        return monoisotopicDeltaMass;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(expression);
        buffer.append(" (mono=");
        buffer.append(monoisotopicDeltaMass);
        buffer.append(", avg=");
        buffer.append(averageDeltaMass);
        buffer.append(")");
        return buffer.toString();
    }
}
