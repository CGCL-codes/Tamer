package com.daffodilwoods.daffodildb.server.sql99.ddl.schemamanipulation;

import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors._Descriptor;

public interface alterdomainaction extends com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public void setDomainDescriptor(_Descriptor domainDescriptor) throws DException;
}
