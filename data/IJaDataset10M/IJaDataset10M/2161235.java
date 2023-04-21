package com.mangobop.impl.query.util;

import java.util.HashMap;
import java.util.List;
import com.mangobop.impl.query.SimpleQueryContext;
import com.mangobop.query.Environment;
import com.mangobop.query.Queriable;
import com.mangobop.query.QueryContext;
import com.mangobop.query.QueryException;
import com.mangobop.query.QueryOperand;
import com.mangobop.query.qom.TwoOperandExpression;
import com.mangobop.types.Operand;
import com.mangobop.types.Type;
import com.mangobop.types.TypeManagerFactory;

/**
 * @author Stefan Meyer
 *
 * Represents an expression that is made up of two operands
 */
public abstract class SimpleTwoOperandsExpression implements Operand, Queriable, TwoOperandExpression {

    private QueryOperand left;

    private QueryOperand right;

    public java.lang.Object getValue(Object from) {
        try {
            QueryContext context = new SimpleQueryContext(null, null, from, null, null);
            return processQuery(null, context).iterator().next();
        } catch (QueryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(Object val, Object to) {
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isConstant() {
        return false;
    }

    public Type getType() {
        return TypeManagerFactory.getInstance().getType("mangobop.types.lang.Object");
    }

    public String getName() {
        return null;
    }

    public int compare(Object o1, Object o2) {
        return 0;
    }

    /**
	 * @return
	 */
    public QueryOperand getLeft() {
        return left;
    }

    /**
	 * @return
	 */
    public QueryOperand getRight() {
        return right;
    }

    /**
	 * @param operand
	 */
    public void setLeft(QueryOperand operand) {
        left = operand;
    }

    /**
	 * @param operand
	 */
    public void setRight(QueryOperand operand) {
        right = operand;
    }

    private HashMap info = new HashMap();

    public Operand prepareQuery(Environment q, QueryContext context) throws QueryException {
        if (context == null) {
            int x = 0;
        }
        info.put(context.getResult(), context);
        info.put(context.getResultType(), context);
        if (left instanceof QueryOperand) {
            ((QueryOperand) left).prepareQuery(context);
        }
        if (left instanceof Queriable) {
            ((Queriable) left).prepareQuery(q, context);
        }
        if (right instanceof Queriable) {
            ((Queriable) right).prepareQuery(q, context);
        }
        if (right instanceof QueryOperand) {
            ((QueryOperand) right).prepareQuery(context);
        }
        return this;
    }

    public java.util.Collection processQuery(Environment q, QueryContext context) throws QueryException {
        java.lang.Object ol;
        java.lang.Object or;
        QueryContext new_context;
        Object base_result = context.getResult();
        Type t = context.getResultType();
        new_context = (QueryContext) info.get(base_result);
        if (new_context == null) {
            new_context = (QueryContext) info.get(t);
        }
        if (new_context == null) {
            new_context = context;
        } else {
            new_context = new_context.createChildForResult(base_result, t);
        }
        ol = left.processQuery(context);
        or = right.processQuery(context);
        return calculate(ol, or);
    }

    public abstract List calculate(java.lang.Object left, java.lang.Object right);

    public String toString() {
        return getName();
    }
}
