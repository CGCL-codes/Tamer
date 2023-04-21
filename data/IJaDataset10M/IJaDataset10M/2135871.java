package dice.expression;

import dice.evaluation.*;
import dice.value.*;
import dice.list.Element;

/**
   JDice: <code>+=</code>.
 */
public final class Concat extends ListExpression {

    private final int precedence = 50;

    Expression target;

    Expression source;

    /**
       Creates a new concatenation expression.
       @param target initial list.
       @param source list to append.
     */
    public Concat(Expression target, Expression source) {
        this.target = target;
        this.source = source;
    }

    public ValueList evalList(Namespace ns) throws IllegalValue {
        ValueList result = target.evalList(ns);
        for (Element<Object> elt : source.evalList(ns)) result.set(elt);
        return result;
    }

    void buildString(StringBuilder sb) {
        target.buildString(sb, precedence);
        sb.append(" << ");
        source.buildString(sb, precedence);
    }

    public boolean invariant() {
        return source.invariant() && target.invariant();
    }
}
