package dice.expression;

import dice.evaluation.*;
import dice.value.*;

/**
   JDice: <code>length</code>.
 */
public final class Length extends IntExpression {

    private final int precedence = 50;

    Expression lst;

    /**
       Creates a new <code>length</code> expression.
       @param lst list to count elements in.
     */
    public Length(Expression lst) {
        this.lst = lst;
    }

    public int evalInt(Namespace ns) throws IllegalValue {
        return lst.evalList(ns).size();
    }

    void buildString(StringBuilder sb) {
        sb.append("length ");
        lst.buildString(sb, precedence);
    }

    public boolean invariant() {
        return lst.invariant();
    }
}
