package org.gdbms.engine.instruction;

import org.gdbms.engine.values.NullValue;
import org.gdbms.engine.values.Value;
import org.gdbms.engine.values.ValueFactory;

/**
 * Adaptador
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class IsClauseAdapter extends AbstractExpression implements Expression {

    /**
     * @see org.gdbms.engine.instruction.Expression#getFieldName()
     */
    public String getFieldName() {
        return null;
    }

    /**
     * @see org.gdbms.engine.instruction.Expression#simplify()
     */
    public void simplify() {
    }

    /**
     * @see org.gdbms.engine.instruction.Expression#evaluate(long)
     */
    public Value evaluate(long row) throws EvaluationException {
        Value value = ((Expression) getChilds()[0]).evaluate(row);
        boolean b = value instanceof NullValue;
        if (getEntity().first_token.next.next.image.toLowerCase().equals("not")) b = !b;
        return ValueFactory.createValue(b);
    }

    /**
     * @see org.gdbms.engine.instruction.Expression#isLiteral()
     */
    public boolean isLiteral() {
        return false;
    }
}
