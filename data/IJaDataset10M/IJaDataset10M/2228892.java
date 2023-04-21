package com.daffodilwoods.daffodildb.server.sql99.expression;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.expressionprimary.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

public class columnname implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public identifierchain _columnname0;

    public void setDefaultValues(_VariableValueOperations variableValueOperation) throws DException {
    }

    public boolean checkForSubQuery() throws DException {
        return false;
    }

    public _Reference[] getReferences(TableDetails[] tableDetails) throws DException {
        throw new DException("DSE565", new Object[] { "getReferences()" });
    }

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        String[] name = (String[]) _columnname0.run(object);
        return name[name.length - 1];
    }

    public Object getColumnName(Object object) throws com.daffodilwoods.database.resource.DException {
        String[] name = (String[]) _columnname0.run(object);
        return name;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_columnname0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        columnname tempClass = new columnname();
        tempClass._columnname0 = (identifierchain) _columnname0.clone();
        return tempClass;
    }
}
