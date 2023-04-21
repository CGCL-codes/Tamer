package com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition;

import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

public class sequencestartersuinteger implements initializesequence {

    public suinteger _suinteger0;

    public sequencestarter _sequencestarter1;

    public Object run(Object object) throws DException {
        return _suinteger0.run(null);
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_sequencestarter1);
        sb.append(" ");
        sb.append(_suinteger0);
        return sb.toString();
    }
}
