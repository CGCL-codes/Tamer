package org.mov.parser.expression;

import org.mov.util.*;
import org.mov.parser.*;
import org.mov.quote.*;

/**
 * A representation of the concept of <code>day close</code>.
 */
public class DayCloseExpression extends TerminalExpression {

    public float evaluate(QuoteBundle quoteBundle, String symbol, int day) {
        return 0.0F;
    }

    public String toString() {
        return "day_close";
    }

    public int checkType() throws TypeMismatchException {
        return QUOTE_TYPE;
    }
}
