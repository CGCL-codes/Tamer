package com.daffodilwoods.daffodildb.server.sql99.token;

import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.predicates.*;

public class Snotequalto_108361092 implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter, SQLspecialcharacter2, compop {

    public String _Snotequalto_1083610920;

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        return _Snotequalto_1083610920;
    }

    public int getType() {
        return OTHERS;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_Snotequalto_1083610920);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
