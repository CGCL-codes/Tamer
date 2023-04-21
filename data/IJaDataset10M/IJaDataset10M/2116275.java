package com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression;

import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.plan.condition.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.rowvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.stringvalueexpression.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;

public class domainname extends AbstractRowValueExpression implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter, datatypedomainname, datatype {

    public schemaqualifiedname _domainname0;

    public void setColumnPredicates(_AllColumnPredicates allColumnPredicates) throws DException {
        throw new DException("DSE565", new Object[] { "setColumnPredicates()" });
    }

    public _Iterator execute(_IndexTable indexTable, _Order order, String[] queryColumns, TableDetails tableDetails) throws DException {
        throw new DException("DSE565", new Object[] { "execute()" });
    }

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        if (dataTypeDescriptor != null) {
            dataTypeDescriptor.object_type = com.daffodilwoods.daffodildb.server.sql99.common.SqlKeywords.DOMAIN;
        }
        return _domainname0.run(null);
    }

    public String getDomainName() throws com.daffodilwoods.database.resource.DException {
        return _domainname0.getIdentifierName();
    }

    public String getCatalogName() throws com.daffodilwoods.database.resource.DException {
        return _domainname0.getCatalogName();
    }

    public String getSchemaName() throws com.daffodilwoods.database.resource.DException {
        return _domainname0.getSchemaName();
    }

    DataTypeDescriptor dataTypeDescriptor;

    public void setDescriptor(_Descriptor dataTypeDescriptor) throws DException {
        this.dataTypeDescriptor = (DataTypeDescriptor) dataTypeDescriptor;
    }

    public AbstractRowValueExpression[] getChilds() {
        AbstractRowValueExpression[] childs = new AbstractRowValueExpression[] {};
        return childs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_domainname0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        domainname tempClass = new domainname();
        tempClass._domainname0 = (schemaqualifiedname) _domainname0.clone();
        return tempClass;
    }
}
