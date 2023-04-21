package com.daffodilwoods.daffodildb.server.serversystem.dmlvalidation.constraintsystem;

import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;

public interface _ConstraintTable {

    void checkDeleteConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext) throws ConstraintException, DException;

    void checkUpdateConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext, int[] columns) throws ConstraintException, DException;

    void checkInsertConstraints(_ServerSession globalSession, _StatementExecutionContext statementExecutionContext, int[] columns) throws ConstraintException, DException;

    boolean hasDefferred() throws DException;
}
