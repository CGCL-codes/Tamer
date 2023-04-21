package com.daffodilwoods.daffodildb.server.sql99.dml;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.database.resource.*;

public class pareninsertcolumnlist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public insertcolumnlist _insertcolumnlist0;

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        return _insertcolumnlist0.run(object);
    }

    public ColumnDetails[] getColumnDetails() throws DException {
        return _insertcolumnlist0.getColumnDetails();
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        return _insertcolumnlist0.getParameterInfo();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append("(");
        sb.append(" ");
        sb.append(_insertcolumnlist0);
        sb.append(" ");
        sb.append(")");
        return sb.toString();
    }
}
