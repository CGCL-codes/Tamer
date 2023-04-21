package com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.fromclause;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.database.resource.*;

/**
 * This class reprsents optional rule used with tableorqueryname under tableprimary.
 * this is not supported till now.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class OptSRESERVEDWORD1206543922correlationnameOptparenderivedcolumnlist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public parenderivedcolumnlist _Optparenderivedcolumnlist0;

    public correlationname _correlationname1;

    public SRESERVEDWORD1206543922 _OptSRESERVEDWORD12065439222;

    public Object run(Object object) throws DException {
        Object[] array = new Object[2];
        array[0] = _correlationname1.run(object);
        if (_Optparenderivedcolumnlist0 != null) {
            array[1] = _Optparenderivedcolumnlist0.run(null);
        }
        return array;
    }

    public com.daffodilwoods.daffodildb.server.sql99.utils._Reference[] checkSemantic(com.daffodilwoods.daffodildb.server.serversystem._ServerSession parent, ColumnDetails[] queryColumns, boolean checkUserRight0) throws DException {
        if (_Optparenderivedcolumnlist0 != null) throw new UnsupportedOperationException("Rule Not Supported"); else return null;
    }

    public TableDetails[] getTableDetails() throws DException {
        throw new RuntimeException("OptSRESERVEDWORD1206543922correlationnameOptSleftparen653880241derivedcolumnlistSrightparen_1874859514 not supported");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        if (_OptSRESERVEDWORD12065439222 != null) {
            sb.append(_OptSRESERVEDWORD12065439222);
        }
        sb.append(" ");
        sb.append(_correlationname1);
        sb.append(" ");
        if (_Optparenderivedcolumnlist0 != null) {
            sb.append(_Optparenderivedcolumnlist0);
        }
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        OptSRESERVEDWORD1206543922correlationnameOptparenderivedcolumnlist tempClass = new OptSRESERVEDWORD1206543922correlationnameOptparenderivedcolumnlist();
        if (_Optparenderivedcolumnlist0 != null) {
            tempClass._Optparenderivedcolumnlist0 = (parenderivedcolumnlist) _Optparenderivedcolumnlist0.clone();
        }
        tempClass._correlationname1 = (correlationname) _correlationname1.clone();
        if (_OptSRESERVEDWORD12065439222 != null) {
            tempClass._OptSRESERVEDWORD12065439222 = (SRESERVEDWORD1206543922) _OptSRESERVEDWORD12065439222.clone();
        }
        return tempClass;
    }
}
