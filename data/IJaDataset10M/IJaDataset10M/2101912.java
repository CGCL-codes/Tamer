package com.daffodilwoods.daffodildb.server.sql99.utils;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;

public abstract class AbstractVariableValues implements _VariableValues {

    public AbstractVariableValues() {
    }

    public Object getColumnValues(_Reference[] parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public Object getColumnValues(_Reference parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public void setIterator(_Iterator parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method setIterator() not yet implemented.");
    }

    public void setConditionVariableValue(_Reference[] parm1, Object[] parm2, int parm3) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method setConditionVariableValue() not yet implemented.");
    }

    public void addReferences(_Reference[] parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method addReferences() not yet implemented.");
    }

    public Object[][] getReferenceAndValuePair() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getReferenceAndValuePair() not yet implemented.");
    }

    public void releaseResource() throws DException {
        throw new java.lang.UnsupportedOperationException("Method releaseResource() not yet implemented.");
    }

    public FieldBase field(_Reference parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public _ServerSession getServerSession() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getServerSession() not yet implemented.");
    }
}
