package com.daffodilwoods.daffodildb.server.sql99.utils;

import com.daffodilwoods.daffodildb.client.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility._Record;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.utils.comparator.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;
import com.daffodilwoods.database.utility.P;

public abstract class BaseJoinIterator implements _Iterator {

    protected _Iterator leftIterator;

    protected _Iterator rightIterator;

    public static final int ONLYFIRSTHAVEDATA = -2;

    public static final int ONLYSECONDHAVEDATA = 2;

    public static final int BOTHHAVESAMEDATA = 0;

    public static final int FIRSTISCURRENT = -1;

    public static final int SECONDISCURRENT = 1;

    protected ColumnDetails[] hasRecordReferences;

    protected _OrderCount orderCount;

    protected int leftOrderCount = -1;

    protected int state = INVALIDSTATE;

    private SuperComparator superComparator;

    protected _Reference[] underlyingRef;

    public BaseJoinIterator(_Iterator leftIterator0, _Iterator rightIterator0, ColumnDetails[] hasRecordReferences0) {
        leftIterator = leftIterator0;
        rightIterator = rightIterator0;
        hasRecordReferences = hasRecordReferences0;
    }

    public BaseJoinIterator(_Iterator leftIterator0, _Iterator rightIterator0) {
        leftIterator = leftIterator0;
        rightIterator = rightIterator0;
    }

    public _OrderCount getOrderCounts() throws com.daffodilwoods.database.resource.DException {
        _OrderCount orderCount1 = leftIterator.getOrderCounts();
        _OrderCount orderCount2 = rightIterator.getOrderCounts();
        int leftOrderCount = orderCount1.getOrderCount();
        _Order order1 = orderCount1.getOrders();
        _Order order2 = orderCount2.getOrders();
        _Order order = GeneralPurposeStaticClass.getJoinOrdered(order1, order2);
        orderCount = new OrderCount(leftOrderCount + orderCount2.getOrderCount(), order);
        return orderCount;
    }

    public void setKeyCount(Object[][] tableAndKeyCount) throws com.daffodilwoods.database.resource.DException {
        leftIterator.setKeyCount(tableAndKeyCount);
    }

    public TableDetails[] getTableDetails() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getTableDetails() not yet implemented.");
    }

    public _Iterator getBaseIterator(ColumnDetails column) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getBaseIterator() not yet implemented.");
    }

    public void setConditionVariableValue(_Reference[] parm1, Object[] parm2, int parm3) throws DException {
        if (underlyingRef != null) {
            parm1 = GeneralPurposeStaticClass.getJointReferences(parm1, underlyingRef);
            parm2 = GeneralPurposeStaticClass.getJointValues(this, parm2, underlyingRef.length);
        }
        leftIterator.setConditionVariableValue(parm1, parm2, parm3);
        rightIterator.setConditionVariableValue(parm1, parm2, parm3);
    }

    public _Order getDefaultOrder() throws com.daffodilwoods.database.resource.DException {
        return GeneralPurposeStaticClass.getJoinOrdered(leftIterator.getDefaultOrder(), rightIterator.getDefaultOrder());
    }

    public _ExecutionPlan getExecutionPlan() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getExecutionPlan() not yet implemented.");
    }

    public ExecutionPlanForBrowser getExecutionPlanForBrowser() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getExecutionPlanForBrowser() not yet implemented.");
    }

    public Object getColumnValues(_Reference[] parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public Object getColumnValues(_Reference parm1) throws com.daffodilwoods.database.resource.DException {
        return ((Object[]) getColumnValues(new _Reference[] { parm1 }))[0];
    }

    public boolean seekFromTop(_IndexPredicate[] parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method seekFromTop() not yet implemented.");
    }

    public boolean seekFromBottom(_IndexPredicate[] parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method seekFromBottom() not yet implemented.");
    }

    public boolean seekFromTopRelative(Object parm1) throws com.daffodilwoods.database.resource.DException {
        while (next()) {
            int cmp = compareValues((Object[]) parm1);
            if (cmp >= 0) {
                return true;
            }
        }
        state = AFTERLAST;
        return false;
    }

    private int compareValues(Object[] second) throws DException {
        ColumnDetails[] cd = null;
        try {
            cd = orderCount.getOrders().getColumnDetails();
            Object[] first = (Object[]) getColumnValues(cd);
            return superComparator.compare(first, second);
        } catch (NullPointerException ex) {
            boolean[] orderType = orderCount.getOrders().getOrderOfColumns();
            superComparator = GeneralPurposeStaticClass.getObjectOrderComparator(orderType);
            return superComparator.compare(getColumnValues(cd), second);
        }
    }

    public boolean seekFromBottomRelative(Object parm1) throws com.daffodilwoods.database.resource.DException {
        while (previous()) {
            int cmp = compareValues((Object[]) parm1);
            if (cmp <= 0) {
                return true;
            }
        }
        state = BEFOREFIRST;
        return false;
    }

    public _KeyColumnInformation[] getKeyColumnInformations() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getKeyColumnInformations() not yet implemented.");
    }

    public Object[] getUniqueColumnReference() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getUniqueColumnReference() not yet implemented.");
    }

    public boolean seek(Object parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method seek() not yet implemented.");
    }

    public boolean first() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method first() not yet implemented.");
    }

    public boolean last() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method last() not yet implemented.");
    }

    public boolean next() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method next() not yet implemented.");
    }

    public boolean previous() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method previous() not yet implemented.");
    }

    public Object getKey() throws com.daffodilwoods.database.resource.DException {
        return leftIterator.getKey();
    }

    public void move(Object keys) throws com.daffodilwoods.database.resource.DException {
        leftIterator.move(keys);
    }

    public byte[] getByteKey() throws DException {
        return leftIterator.getByteKey();
    }

    public void moveByteKey(byte[] key) throws DException {
        leftIterator.moveByteKey(key);
    }

    public _Record getRecord() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getRecord() not yet implemented.");
    }

    public Object getColumnValues() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public Object getColumnValues(int[] parm1) throws com.daffodilwoods.database.resource.DException {
        new Exception("sdfsdjfb").printStackTrace();
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public _ExecutionPlan getExecutionPlan(String name) throws DException {
        _ExecutionPlan cplans[] = new _ExecutionPlan[2];
        cplans[0] = leftIterator.getExecutionPlan();
        cplans[1] = rightIterator.getExecutionPlan();
        return new ExecutionPlan(name, cplans, null, null, null);
    }

    public Object[][] getFunctionalColumnMapping() throws DException {
        return GeneralPurposeStaticClass.getMappingOfTableBVE(leftIterator.getFunctionalColumnMapping(), rightIterator.getFunctionalColumnMapping());
    }

    public _Iterator getBaseIteratorHasRecord(ColumnDetails hasRecordColumn) throws DException {
        if (hasRecordReferences != null) {
            for (int i = 0, length = hasRecordReferences.length; i < length; i++) {
                if (hasRecordReferences[i] == hasRecordColumn) {
                    return this;
                }
            }
        }
        _Iterator iterator = leftIterator.getBaseIteratorHasRecord(hasRecordColumn);
        if (iterator != null) {
            return iterator;
        }
        return rightIterator.getBaseIteratorHasRecord(hasRecordColumn);
    }

    public FieldBase field(_Reference reference) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method not yet implemented.");
    }

    public FieldBase[] fields(_Reference[] references) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method fields() not yet implemented.");
    }

    public FieldBase[] fields(int[] columns) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method fields() not yet implemented.");
    }

    public void releaseResource() throws DException {
        leftIterator.releaseResource();
        rightIterator.releaseResource();
        superComparator = null;
    }

    public void setSpecificUnderlyingReferences(_Reference[] specificUnderlyingReferences) throws DException {
        underlyingRef = specificUnderlyingReferences;
    }
}
