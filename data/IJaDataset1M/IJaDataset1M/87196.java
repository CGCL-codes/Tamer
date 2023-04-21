package com.daffodilwoods.daffodildb.server.sql99.expression;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
public class SRESERVEDWORD1206543922correlationname implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public correlationname _correlationname0;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439221;

    public void setDefaultValues(_VariableValueOperations variableValueOperation) throws DException {
    }

    public boolean checkForSubQuery() throws DException {
        return false;
    }

    public _Reference[] getReferences(TableDetails[] tableDetails) throws DException {
        throw new DException("DSE565", new Object[] { "getReferences()" });
    }

    public Object run(Object object) throws DException {
        return _correlationname0.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439221);
        sb.append(" ");
        sb.append(_correlationname0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        SRESERVEDWORD1206543922correlationname tempClass = new SRESERVEDWORD1206543922correlationname();
        tempClass._correlationname0 = (correlationname) _correlationname0.clone();
        tempClass._SRESERVEDWORD12065439221 = (SRESERVEDWORD1206543922) _SRESERVEDWORD12065439221.clone();
        return tempClass;
    }
}
