package com.daffodilwoods.daffodildb.server.sql99.ddl.schemamanipulation;

import com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors.*;
import com.daffodilwoods.database.resource.*;

public interface altertableaction extends com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public void setTableDescriptor(_Descriptor tableDescriptor) throws DException;
}
