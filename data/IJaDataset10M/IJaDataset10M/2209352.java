package com.mangobop.impl.query;

import java.util.Collection;
import com.mangobop.query.QueryContext;
import com.mangobop.query.QueryException;
import com.mangobop.query.QueryOperand;
import com.mangobop.types.Operand;

/**
 * @author Stefan Meyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VariableQueryOperand implements QueryOperand {

    public String toString() {
        return prefix + ":" + name;
    }

    private String name;

    private String prefix;

    public VariableQueryOperand(String prefix, String name) {
        this.name = name;
        this.prefix = prefix;
    }

    public QueryOperand next() {
        return null;
    }

    public Collection processQuery(QueryContext context) throws QueryException {
        Object o = context.getVariables().get(prefix + ":" + name);
        if (o == null) return null;
        java.util.List list = new java.util.ArrayList(1);
        list.add(o);
        return list;
    }

    public Operand prepareQuery(QueryContext context) throws QueryException {
        return null;
    }
}
