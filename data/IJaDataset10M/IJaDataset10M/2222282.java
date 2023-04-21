package com.daffodilwoods.daffodildb.server.sql99.dml;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.*;
import com.daffodilwoods.database.resource.*;

public class insertcolumnlist implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public columnnamelist _insertcolumnlist0;

    /**
    * This method returns the column Names explicity specified by the user in the insert/update/delete
    * statement by calling the run method of ColumnName List
    * @param object
    * @return Object
    * @throws com.daffodilwoods.database.resource.DException
    */
    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        return _insertcolumnlist0.run(object);
    }

    /**
    * This method returns the Colum Details from the insert column list.
    * @return
    * @throws DException
    */
    public ParameterInfo[] getParameterInfo() throws DException {
        return _insertcolumnlist0.getParameterInfo();
    }

    /**
    * This method returns the column details from the insert Column List
    * @return
    * @throws DException
    */
    public ColumnDetails[] getColumnDetails() throws DException {
        return _insertcolumnlist0.getColumnDetailsFull();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_insertcolumnlist0);
        return sb.toString();
    }
}
