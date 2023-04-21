package com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression;

import java.util.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.expressionprimary.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;

public class expressionprimary implements booleanprimary {

    public nonparenthesizedvalueexpressionprimary _nonparenthesizedvalueexpressionprimary0;

    public Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Method clone not supported");
    }

    public void setDefaultValues(_VariableValueOperations parm1) throws com.daffodilwoods.database.resource.DException {
    }

    public boolean checkForSubQuery() throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.checkForSubQuery();
    }

    public _Reference[] getReferences(TableDetails[] parm1) throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.getReferences(parm1);
    }

    public ColumnDetails[] getColumnDetails() throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.getColumnDetails();
    }

    public _Reference[] checkSemantic(_ServerSession parm1) throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.checkSemantic(parm1);
    }

    public int getCardinality() throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.getCardinality();
    }

    public ParameterInfo[] getParameterInfo() throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.getParameterInfo();
    }

    public void getColumnsIncluded(ArrayList parm1) throws com.daffodilwoods.database.resource.DException {
        _nonparenthesizedvalueexpressionprimary0.getColumnsIncluded(parm1);
    }

    public void getTablesIncluded(ArrayList parm1) throws com.daffodilwoods.database.resource.DException {
        _nonparenthesizedvalueexpressionprimary0.getTablesIncluded(parm1);
    }

    public Object run(Object parm1) throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.run(parm1);
    }

    public Object[] getParameters(Object parm1) throws com.daffodilwoods.database.resource.DException {
        return _nonparenthesizedvalueexpressionprimary0.getParameters(parm1);
    }

    public ByteComparison getByteComparison(Object object) throws DException {
        return _nonparenthesizedvalueexpressionprimary0.getByteComparison(object);
    }

    public void releaseResource() throws DException {
        _nonparenthesizedvalueexpressionprimary0.releaseResource();
    }
}
