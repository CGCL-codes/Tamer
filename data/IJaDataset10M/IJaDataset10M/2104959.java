package com.daffodilwoods.daffodildb.server.sql99.dml.declarecursor;

import com.daffodilwoods.daffodildb.server.sql99.expression.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

public class SRESERVEDWORD1206543922columnnamelist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public String toString() {
        StringBuffer clause = new StringBuffer();
        clause.append("  ");
        clause.append(_SRESERVEDWORD12065439221.toString());
        clause.append("  ");
        clause.append(_columnnamelist0.toString());
        return clause.toString().trim();
    }

    public columnnamelist _columnnamelist0;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439221;

    public Object run(Object object) throws DException {
        return _columnnamelist0.run(object);
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
