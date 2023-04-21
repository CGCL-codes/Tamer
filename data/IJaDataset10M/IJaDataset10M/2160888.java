package com.daffodilwoods.daffodildb.server.sql99.token;

public class Scomma94843605 implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter, SQLspecialcharacter, SQLspecialcharacter2 {

    public String _Scomma948436050;

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        return _Scomma948436050;
    }

    public int getType() {
        return OTHERS;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_Scomma948436050);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}
