package com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

public class SRESERVEDWORD1206543922routinetypespecificname implements specificroutinedesignator {

    public specificname _specificname0;

    public routinetype _routinetype1;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439222;

    public Object run(Object object) throws DException {
        return null;
    }

    public String getObjectName() throws DException {
        return _specificname0.getIdentifierName();
    }

    public String getSchemaName() throws DException {
        return _specificname0.getSchemaName();
    }

    public String getCatalogName() throws DException {
        return _specificname0.getCatalogName();
    }

    public String getObjectType() {
        return SqlKeywords.SPECIFIC;
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public String getRoutineType() {
        return _routinetype1.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439222);
        sb.append(" ");
        sb.append(_routinetype1);
        sb.append(" ");
        sb.append(_specificname0);
        return sb.toString();
    }
}
