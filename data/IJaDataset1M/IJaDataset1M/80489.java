package net.aditsu.depeche;

import java.io.Serializable;

public class FilterCondition extends Condition {

    private static final long serialVersionUID = 1L;

    private final String field;

    private final Operator op;

    private final Serializable value;

    public FilterCondition(final String field, final String op, final Serializable value) {
        this.field = field;
        this.op = Operators.get(op);
        this.value = value;
        if (this.op == null) {
            throw new IllegalArgumentException("Operator not supported: " + op);
        }
    }

    @Override
    public Boolean test(final Record r) {
        return op.test(r.get(field), value);
    }

    @Override
    public SQLCondition getSQLCondition(final Provider p) {
        final String o = op.toString();
        return "=".equals(o) ? p.filter(field, value) : p.filter(field, o, value);
    }
}
