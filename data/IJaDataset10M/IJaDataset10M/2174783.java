package com.daffodilwoods.daffodildb.server.serversystem.chainedcolumn;

import java.util.*;
import com.daffodilwoods.daffodildb.client.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility._Record;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;

public class ForeignKeyIterator implements _Iterator {

    ChainedTableInfo startingChainedTableInfo;

    HashMap tablesMapping;

    HashMap iteratorMapping;

    HashMap columnsMapping;

    _Iterator startingTableIterator;

    _ServerSession serverSession;

    ArrayList tableDetails;

    public ForeignKeyIterator(_ServerSession serverSession0, _Iterator startingTableIterator0, HashMap tablesMapping0, ChainedTableInfo cti0, HashMap columnsMapping0, ArrayList tableDetailsList0) throws DException {
        this.serverSession = serverSession0;
        this.startingTableIterator = startingTableIterator0;
        this.tablesMapping = tablesMapping0;
        this.startingChainedTableInfo = cti0;
        this.columnsMapping = columnsMapping0;
        this.tableDetails = tableDetailsList0;
        iteratorMapping = new HashMap();
        iteratorMapping.put(startingChainedTableInfo.getChainedColumnInfo(), startingTableIterator);
        makeIterator(startingChainedTableInfo);
    }

    public _OrderCount getOrderCounts() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getOrderCounts() not yet implemented.");
    }

    public void setKeyCount(Object[][] tt) throws DException {
        throw new java.lang.UnsupportedOperationException("Method setKeyCount() not yet implemented.");
    }

    public TableDetails[] getTableDetails() throws DException {
        return (TableDetails[]) tableDetails.toArray(new TableDetails[tableDetails.size()]);
    }

    public _Iterator getBaseIterator(ColumnDetails column) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getBaseIterator() not yet implemented.");
    }

    public void setConditionVariableValue(_Reference[] parm1, Object[] parm2, int parm3) throws DException {
        startingTableIterator.setConditionVariableValue(parm1, parm2, parm3);
    }

    public Object getColumnValues() throws DException {
        Object[] tds = tableDetails.toArray();
        int valuesCount = 0;
        for (int i = 0; i < tds.length; i++) {
            valuesCount += ((TableDetails) tds[i]).cc.getColumnCount();
        }
        Object[] ccis = tablesMapping.keySet().toArray();
        Object[] values = new Object[valuesCount];
        int pos = 0;
        for (int i = 0; i < tds.length; i++) {
            Object[] val = (Object[]) ((_Iterator) iteratorMapping.get(ccis[i])).getColumnValues();
            System.arraycopy(val, 0, values, pos, val.length);
            pos = pos + val.length;
        }
        return values;
    }

    public _KeyColumnInformation[] getKeyColumnInformations() throws DException {
        Object[] ccis = tablesMapping.keySet().toArray();
        ArrayList kcis = new ArrayList();
        for (int i = 0; i < ccis.length; i++) {
            _KeyColumnInformation[] kci = ((_Iterator) iteratorMapping.get(ccis[i])).getKeyColumnInformations();
            for (int j = 0; j < kci.length; j++) {
                kcis.add(kci[j]);
            }
        }
        return (_KeyColumnInformation[]) (kcis.toArray(new _KeyColumnInformation[kcis.size()]));
    }

    public Object[] getUniqueColumnReference() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getUniqueColumnReference() not yet implemented.");
    }

    public boolean seek(Object parm1) throws DException {
        boolean found = startingTableIterator.seek(parm1);
        if (found) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return found;
    }

    public _Order getDefaultOrder() throws DException {
        Object[] ccis = tablesMapping.keySet().toArray();
        ArrayList listOfCDs = new ArrayList();
        boolean[] array = null;
        for (int i = 0; i < ccis.length; i++) {
            _Order order = ((_Iterator) iteratorMapping.get(ccis[i])).getDefaultOrder();
            listOfCDs.addAll(Arrays.asList(order.getKeyColumnDetails()));
            array = array == null ? order.getOrderOfColumns() : concatenateBooleans(array, order.getOrderOfColumns());
        }
        SelectOrder selectOrder = new SelectOrder((ColumnDetails[]) listOfCDs.toArray(new ColumnDetails[listOfCDs.size()]), array);
        return selectOrder;
    }

    private boolean[] concatenateBooleans(boolean[] array, boolean[] orderBooleans) throws DException {
        boolean[] newb = new boolean[array.length + orderBooleans.length];
        System.arraycopy(array, 0, newb, 0, array.length);
        System.arraycopy(orderBooleans, 0, newb, array.length, orderBooleans.length);
        return newb;
    }

    public Object getColumnValues(_Reference[] parm1) throws DException {
        Object[] values = new Object[parm1.length];
        for (int i = 0; i < parm1.length; i++) {
            String[] aliasName = ((ColumnDetails) parm1[i]).getTableAliasArray();
            if (aliasName == null) values[i] = startingTableIterator.getColumnValues(parm1);
            _Iterator iterator = (_Iterator) iteratorMapping.get(columnsMapping.get(aliasName));
            if (iterator != null) values[i] = iterator.getColumnValues(parm1);
        }
        return values;
    }

    public Object getColumnValues(_Reference parm1) throws DException {
        String[] aliasName = ((ColumnDetails) parm1).getTableAliasArray();
        if (aliasName == null) return startingTableIterator.getColumnValues(parm1);
        _Iterator iterator = (_Iterator) iteratorMapping.get(columnsMapping.get(aliasName));
        if (iterator != null) return iterator.getColumnValues(parm1);
        return null;
    }

    public void setIterator(_Iterator parm1) throws DException {
        throw new java.lang.UnsupportedOperationException("Method setIterator() not yet implemented.");
    }

    public void addReferences(_Reference[] parm1) throws DException {
        throw new java.lang.UnsupportedOperationException("Method addReferences() not yet implemented.");
    }

    public Object[][] getReferenceAndValuePair() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getReferenceAndValuePair() not yet implemented.");
    }

    public boolean seekFromTop(_IndexPredicate[] parm1) throws DException {
        boolean found = startingTableIterator.seekFromTop(parm1);
        if (found) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return found;
    }

    public boolean seekFromTopRelative(Object parm1) throws DException {
        boolean found = startingTableIterator.seekFromTopRelative(parm1);
        if (found) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return found;
    }

    public boolean seekFromBottom(_IndexPredicate[] parm1) throws DException {
        boolean found = startingTableIterator.seekFromBottom(parm1);
        if (found) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return found;
    }

    public boolean seekFromBottomRelative(Object parm1) throws DException {
        boolean found = startingTableIterator.seekFromBottomRelative(parm1);
        if (found) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return found;
    }

    public boolean first() throws DException {
        boolean firstFound = startingTableIterator.first();
        if (firstFound) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return firstFound;
    }

    public boolean last() throws DException {
        boolean lastFound = startingTableIterator.last();
        if (lastFound) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return lastFound;
    }

    public boolean next() throws DException {
        boolean nextFound = startingTableIterator.next();
        if (nextFound) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return nextFound;
    }

    public boolean previous() throws DException {
        boolean preFound = startingTableIterator.previous();
        if (preFound) {
            alignChilds(startingChainedTableInfo, startingTableIterator.getRecord());
        }
        return preFound;
    }

    public Object getKey() throws DException {
        return startingTableIterator.getKey();
    }

    public void move(Object parm1) throws DException {
        startingTableIterator.move(parm1);
    }

    public Object getColumnValues(int[] parm1) throws DException {
        return startingTableIterator.getColumnValues(parm1);
    }

    public _Record getRecord() throws DException {
        return startingTableIterator.getRecord();
    }

    private void makeIterator(ChainedTableInfo startTableInfo) throws DException {
        ArrayList childsTableInfos = startTableInfo.getChildsChainedTableInfo();
        if (childsTableInfos == null) return;
        for (int i = 0, size = childsTableInfos.size(); i < size; i++) {
            ChainedTableInfo childTableInfo = (ChainedTableInfo) childsTableInfos.get(i);
            ChainedColumnInfo cci = childTableInfo.getChainedColumnInfo();
            IteratorInfo it = childTableInfo.getReferencingToReferencedTableIteratorInfo();
            _SingleTableExecuter singleTableExecuter = new ConditionSingleTableExecuter(null, it.tableDetails, serverSession, it.bve, null);
            _Iterator childIterator = serverSession.getIterator(childTableInfo.getTableName(), singleTableExecuter);
            iteratorMapping.put(cci, childIterator);
            makeIterator(childTableInfo);
        }
    }

    void alignChilds(ChainedTableInfo parent, _Record record) throws DException {
        ArrayList childInfos = parent.getChildsChainedTableInfo();
        if (childInfos == null) {
            ;
            return;
        }
        for (int i = 0, size = childInfos.size(); i < size; i++) {
            ChainedTableInfo cti = (ChainedTableInfo) childInfos.get(i);
            _Iterator it = (_Iterator) iteratorMapping.get(cti.getChainedColumnInfo());
            IteratorInfo itInfo = cti.getReferencingToReferencedTableIteratorInfo();
            it.setConditionVariableValue(itInfo.ref, new Object[] { record.getObject(itInfo.columnIndex) }, 0);
            boolean first = it.first();
            if (first == false) continue; else alignChilds(cti, it.getRecord());
        }
    }

    public TemporaryForeignKeyIterator getTemporaryIterator(QualifiedIdentifier tableName, _Iterator singleRowIterator) throws DException {
        return new TemporaryForeignKeyIterator(serverSession, tablesMapping, singleRowIterator, tableName, columnsMapping);
    }

    public Object getColumnValues(int column) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public _ExecutionPlan getExecutionPlan() throws DException {
        _ExecutionPlan cplan = startingTableIterator.getExecutionPlan();
        _ExecutionPlan cplans[] = cplan == null ? null : new _ExecutionPlan[] { cplan };
        ExecutionPlan plan = new ExecutionPlan("ForeignKeyIterator", cplans, null, null, null);
        return plan;
    }

    public ExecutionPlanForBrowser getExecutionPlanForBrowser() throws DException {
        return startingTableIterator.getExecutionPlanForBrowser();
    }

    public _Iterator getBaseIteratorHasRecord(ColumnDetails hasRecordColumn) throws DException {
        throw new DException("DSE565", new Object[] { "getBaseIterator" });
    }

    public Object[][] getFunctionalColumnMapping() throws DException {
        throw new UnsupportedOperationException("Method not Supported");
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
    }

    public byte[] getByteKey() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getByteKey() not yet implemented.");
    }

    public void moveByteKey(byte[] key) throws DException {
        throw new java.lang.UnsupportedOperationException("Method mByteKey() not yet implemented.");
    }

    public void setSpecificUnderlyingReferences(_Reference[] specificUnderlyingReferences) throws DException {
        throw new java.lang.UnsupportedOperationException("Method setSpecificUnderlyingReferences() not yet implemented.");
    }
}
