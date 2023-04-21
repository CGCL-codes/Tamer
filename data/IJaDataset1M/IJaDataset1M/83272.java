package org.mov.parser.expression;

import org.mov.parser.TypeMismatchException;
import org.mov.parser.Variables;
import org.mov.quote.QuoteBundle;
import org.mov.quote.Symbol;
import org.mov.util.TradingDate;

/**
 * A function that returns the current day of year. The first day of
 * the year (Januarary 1st) will be 1.
 */
public class DayOfYearExpression extends TerminalExpression {

    public DayOfYearExpression() {
    }

    public double evaluate(Variables variables, QuoteBundle quoteBundle, Symbol symbol, int day) {
        TradingDate date = quoteBundle.offsetToDate(day);
        return date.getDayOfYear();
    }

    public String toString() {
        return "dayofyear()";
    }

    public int checkType() throws TypeMismatchException {
        return getType();
    }

    /**
     * Get the type of the expression.
     *
     * @return returns {@link #INTEGER_TYPE}.
     */
    public int getType() {
        return INTEGER_TYPE;
    }

    public Object clone() {
        return new DayOfYearExpression();
    }
}
