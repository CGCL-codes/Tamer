package com.daffodilwoods.daffodildb.server.sql99.dml;

import java.util.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.expressionprimary.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

public class fetchtargetlist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        int i = 0;
        for (; i < _OptRepScomma94843605targetspecification0.length - 1; i++) {
            sb.append(_OptRepScomma94843605targetspecification0[i]);
            sb.append(",");
        }
        sb.append(_OptRepScomma94843605targetspecification0[i]);
        return sb.toString().trim();
    }

    public targetspecification[] _OptRepScomma94843605targetspecification0;

    public Object run(Object object) throws DException {
        int len = _OptRepScomma94843605targetspecification0.length;
        Object[] objToReturn = new Object[len];
        for (int i = 0; i < len; i++) {
            objToReturn[i] = _OptRepScomma94843605targetspecification0[i].run(object);
        }
        return objToReturn;
    }

    public _Reference[] getReferences(_ServerSession serverSession) throws DException {
        ArrayList list = new ArrayList();
        int len = _OptRepScomma94843605targetspecification0.length;
        for (int i = 0; i < len; i++) {
            list.addAll(Arrays.asList(_OptRepScomma94843605targetspecification0[i].getReferences(new TableDetails[0])));
        }
        return (_Reference[]) list.toArray(new _Reference[0]);
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
