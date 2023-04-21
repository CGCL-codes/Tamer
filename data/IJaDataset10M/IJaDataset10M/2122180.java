package com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition;

import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

public class SRESERVEDWORD1206543922languagecode implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public languagecode _languagecode0;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439221;

    public Object run(Object object) throws DException {
        return _languagecode0.run(null);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439221);
        sb.append(" ");
        sb.append(_languagecode0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
