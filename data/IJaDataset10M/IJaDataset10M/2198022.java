package com.daffodilwoods.daffodildb.server.sql99.dcl.sqlcontrolstatement;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

public class simplecaseoperand2 {

    public valueexpression _simplecaseoperand20;

    private _Reference[] expRefs;

    private _ServerSession serverSession;

    public Object run(Object object) throws DException {
        return SearchConditionUtility.executeExpression(_simplecaseoperand20, expRefs, (VariableValues) object, serverSession);
    }

    public Object[] getParameters(Object object) throws DException {
        return _simplecaseoperand20.getParameters(object);
    }

    public _Reference[] checkSemantic(_ServerSession object) throws DException {
        serverSession = object;
        _simplecaseoperand20.getColumnDetails();
        expRefs = _simplecaseoperand20.getReferences(SQLcontrolstatement.dummyTableDetail);
        ;
        return expRefs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_simplecaseoperand20);
        return sb.toString().trim();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
