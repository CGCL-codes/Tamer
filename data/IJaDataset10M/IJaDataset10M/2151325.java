package dice.expression;

import dice.evaluation.*;

/**
   Expressions that always evaluate into a string.
*/
public abstract class StringExpression extends NonFunctionExpression {

    /**
       Equivalent to <code>evalString(ns).</code>
     */
    public final Object evaluate(Namespace ns) {
        return evalString(ns);
    }

    /**
       Overload this method.
     */
    public abstract String evalString(Namespace ns);
}
