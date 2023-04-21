package com.daffodilwoods.daffodildb.server.sql99.expression;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.parser.*;
import com.daffodilwoods.database.resource.*;
import java.util.*;

public interface RowValueExpressionSuper extends StatementExecuter, Parameters {

    public void setDefaultValues(_VariableValueOperations variableValueOperations) throws DException;

    public boolean checkForSubQuery() throws DException;

    public _Reference[] getReferences(TableDetails[] tableDetails) throws DException;

    public ColumnDetails[] getColumnDetails() throws DException;

    public _Reference[] checkSemantic(_ServerSession parent) throws DException;

    public int getCardinality() throws DException;

    public ParameterInfo[] getParameterInfo() throws DException;

    public Object[] getParameters(Object object) throws DException;

    public void getColumnsIncluded(ArrayList aList) throws DException;

    public void getTablesIncluded(ArrayList aList) throws DException;

    public ByteComparison getByteComparison(Object object) throws DException;

    public void releaseResource() throws DException;
}
