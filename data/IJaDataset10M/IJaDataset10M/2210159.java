package com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition;

import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

public class SNONRESERVEDWORD136444255SRESERVEDWORD1206543922suinteger implements sequenceincrementer {

    public suinteger _suinteger0;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439221;

    public SNONRESERVEDWORD136444255 _SNONRESERVEDWORD1364442552;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SNONRESERVEDWORD1364442552);
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439221);
        sb.append(" ");
        sb.append(_suinteger0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public Object run(Object object) throws DException {
        return _suinteger0.run(null);
    }
}
