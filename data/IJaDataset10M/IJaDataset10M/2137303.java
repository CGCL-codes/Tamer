package com.daffodilwoods.daffodildb.server.sql99.dcl.sqlcontrolstatement;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.common.ColumnDetails;

public class casestatementelseclause {

    public SQLstatementlist _SQLstatementlist0;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439221;

    public Object run(Object object) throws DException {
        _SQLstatementlist0.run(object);
        return null;
    }

    public void setOuterControlStatement(SQLcontrolstatement st) {
        _SQLstatementlist0.setOuterControlStatement(st);
    }

    public SQLstatementlist getSQLStatementList() {
        return _SQLstatementlist0;
    }

    public Object[] getParameters(Object object) throws DException {
        return _SQLstatementlist0.getParameters(object);
    }

    public _Reference[] checkSemantic(_ServerSession object) throws DException {
        return _SQLstatementlist0.checkSemantic(object);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439221);
        sb.append(" ");
        sb.append(_SQLstatementlist0);
        return sb.toString().trim();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public ColumnDetails[] getColumnDetails() throws DException {
        return _SQLstatementlist0.getColumnDetails();
    }
}
