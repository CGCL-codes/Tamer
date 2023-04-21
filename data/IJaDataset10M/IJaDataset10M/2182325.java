package com.daffodilwoods.daffodildb.server.sql99.dcl.sqlsessionstatement;

import java.util.*;

public class sessioncharacteristiclist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public sessioncharacteristic[] _OptRepScomma94843605sessioncharacteristic0;

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < _OptRepScomma94843605sessioncharacteristic0.length; i++) {
            Object sessionTransactionMode = _OptRepScomma94843605sessioncharacteristic0[i].run(object);
            list.add(sessionTransactionMode);
        }
        return list;
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_OptRepScomma94843605sessioncharacteristic0[0]);
        for (int i = 1; i < _OptRepScomma94843605sessioncharacteristic0.length; i++) {
            sb.append(",").append(_OptRepScomma94843605sessioncharacteristic0[i]);
        }
        return sb.toString();
    }
}
