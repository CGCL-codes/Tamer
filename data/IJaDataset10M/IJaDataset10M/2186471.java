package com.daffodilwoods.daffodildb.server.sql99.dml;

import java.text.*;
import java.util.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.serversystem.datatriggersystem.*;
import com.daffodilwoods.daffodildb.server.serversystem.dmlvalidation.statementtriggersystem.*;
import com.daffodilwoods.daffodildb.server.sessionsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.utility.P;

public class InsertExecuterSimple implements _Executer {

    _StatementExecutionContext sec;

    fromconstructor fromConstructor;

    _DataTriggerTable dataTriggerTable;

    _StatementTriggerTable statementTriggerTable;

    _UserSession childUserSession;

    _Reference[] knownReferences;

    int[] columnsType;

    int[] size;

    int[] columnIndexes;

    int[] scale;

    Collator collator;

    _ServerSession serverSession;

    TableDetails tableDetails;

    _Reference[] questionMarks;

    private Object[][] referenceIteratorMapping;

    _DropTableInfo[] dropTableInfo;

    public InsertExecuterSimple(TableDetails tableDetails, ColumnDetails[] columnDetails, _StatementExecutionContext ssec, fromconstructor fromConstructr, _Reference[] questions) throws DException {
        this.tableDetails = tableDetails;
        sec = ssec;
        serverSession = sec.getServerSession();
        fromConstructor = fromConstructr;
        performInitialisations(columnDetails);
        questionMarks = questions;
        referenceIteratorMapping = (Object[][]) fromConstructor.getReferenceIteratorMapping(tableDetails, sec.getServerSession());
        ArrayList listOfTables = new ArrayList();
        fromConstructor.getTablesIncluded(listOfTables);
        ArrayList dropTablesList = new ArrayList();
        QualifiedIdentifier table;
        for (int i = 0; i < listOfTables.size(); i++) {
            String[] tableName = (String[]) listOfTables.get(i);
            if (tableName.length < 3) {
                if (tableName.length == 1) table = new QualifiedIdentifier(serverSession.getCurrentCatalog(), serverSession.getCurrentSchema(), tableName[0]); else table = new QualifiedIdentifier(serverSession.getCurrentCatalog(), tableName[0], tableName[1]);
            } else table = new QualifiedIdentifier(tableName[0], tableName[1], tableName[2]);
            if (serverSession.getColumnCharacteristics(table).getTableType() == TypeConstants.TABLE) dropTablesList.add(((_MergeTable) serverSession.getIndexTable(table)).getDropTableInfo());
        }
        table = tableDetails.getQualifiedIdentifier();
        dropTablesList.add(((_MergeTable) serverSession.getIndexTable(table)).getDropTableInfo());
        dropTableInfo = (_DropTableInfo[]) dropTablesList.toArray(new _DropTableInfo[0]);
    }

    public Object execute(_VariableValues vv) throws com.daffodilwoods.database.resource.DException {
        checkForDroppedTable();
        childUserSession = sec.getServerSession().getUserSession();
        childUserSession.startSavePoint();
        sec.setUserRight(false);
        _VariableValues initialisedVV = initialiseVariableValues(vv);
        Object[] values = (Object[]) fromConstructor.run(initialisedVV);
        int count = 0;
        sec.setUserSession(childUserSession);
        sec.setTriggerExecutionContext(new TriggerExecutionContext());
        Object insertEvent = null;
        try {
            statementTriggerTable.fireBeforeInsertStatementLevelTriggers(sec);
            Object[] tmpValues = null;
            for (int k = 0; k < values.length; k++) {
                if (values[k] instanceof Object[]) {
                    tmpValues = (Object[]) values[k];
                } else {
                    tmpValues = new FieldBase[] { (FieldBase) values[k] };
                }
                FieldBase[] valuesToInsert = FieldUtility.convertToAppropriateType(FieldUtility.setFieldLiteralBufferRangeWithArray(tmpValues), columnsType, size, scale, collator);
                try {
                    insertEvent = dataTriggerTable.insertWithTriggersOnly(columnIndexes, valuesToInsert, sec);
                } catch (NullPointerException ex) {
                    if (columnIndexes.length != valuesToInsert.length) {
                        throw new DException("DSE214", null);
                    }
                    dataTriggerTable = sec.getDataTriggerTable(tableDetails.getQualifiedIdentifier());
                    insertEvent = dataTriggerTable.insertWithTriggersOnly(columnIndexes, valuesToInsert, sec);
                }
                count++;
            }
            statementTriggerTable.fireAfterInsertStatementLevelTriggers(sec);
        } catch (DException e) {
            childUserSession.rollbackSavePoint(sec);
            if (e.getDseCode().equalsIgnoreCase("DSE5582")) throw new DException("DSE5582", null);
            if (e.getDseCode().equalsIgnoreCase("DSE5583")) throw new DException("DSE5583", null);
            throw new DmlStatementException("DSE1275", new Object[] { tableDetails.getQualifiedTableName() }, e);
        } catch (Exception e) {
            childUserSession.rollbackSavePoint(sec);
            throw new DException("DSE0", new Object[] { e.getMessage() });
        }
        childUserSession.commitSavePoint(sec);
        if (sec.getAutoCommit() && serverSession.getAutoCommit() && !childUserSession.getSession().hasAnyChild()) {
            serverSession.commit();
        }
        if (sec.getAutoGeneratedType()) {
            DMLResult dmlResult = sec.getDMLResult();
            dmlResult.setrowsEffected(count);
            return dmlResult;
        }
        return new Integer(count);
    }

    public Object execute(Object[] obj) throws com.daffodilwoods.database.resource.DException {
        checkForDroppedTable();
        if (obj == null) {
            if (questionMarks != null) {
                throw new DException("DSE1273", null);
            }
            return execute((_VariableValues) null);
        }
        _VariableValues vv = new VariableValues(questionMarks, sec.getServerSession());
        if (obj.length != questionMarks.length) {
            throw new DmlStatementException("DSE1275", new Object[] { tableDetails.getQualifiedTableName() }, new DException("DSE1252", new Object[] { new Integer(obj.length), new Integer(questionMarks.length) }));
        }
        vv.setConditionVariableValue(questionMarks, FieldUtility.getFields(obj), 2);
        return execute(vv);
    }

    private void performInitialisations(ColumnDetails[] columnDetails) throws DException {
        statementTriggerTable = sec.getStatementTriggerTable(tableDetails.getQualifiedIdentifier());
    }

    private _VariableValues initialiseVariableValues(_VariableValues vv) throws DException {
        _VariableValues vvToReturn = null;
        Object[][] refValuePair = null;
        _Reference[] ref = null;
        if (vv != null) {
            refValuePair = vv.getReferenceAndValuePair();
            ref = getReferences(refValuePair);
        }
        if (questionMarks != null && ref == null) {
            throw new DException("DSE1273", null);
        }
        if (referenceIteratorMapping != null) {
            vvToReturn = new SubQueryVariableValues(ref, (Object[][]) referenceIteratorMapping, sec.getServerSession());
        } else {
            vvToReturn = new VariableValues(ref, sec.getServerSession());
        }
        if (vv != null && ref != null) {
            vvToReturn.setConditionVariableValue(ref, getValues(refValuePair), 1);
        }
        return vvToReturn;
    }

    private _Reference[] getReferences(Object[][] refValue) {
        if (refValue != null) {
            int len = refValue.length;
            _Reference[] refToReturn = new _Reference[len];
            for (int i = 0; i < len; i++) {
                refToReturn[i] = (_Reference) refValue[i][0];
            }
            return refToReturn;
        }
        return null;
    }

    private Object[] getValues(Object[][] refValue) {
        int len = refValue.length;
        Object[] toReturn = new Object[len];
        for (int i = 0; i < len; i++) {
            toReturn[i] = refValue[i][1];
        }
        return toReturn;
    }

    public Object executeForFresh(Object[] parm1) throws com.daffodilwoods.database.resource.DException {
        return execute(parm1);
    }

    public void checkForDroppedTable() throws DException {
        for (int i = 0; i < dropTableInfo.length; i++) {
            if (dropTableInfo[i].isTableDropped()) throw new DException("DSE5584", null);
        }
    }

    public void setInitialParameters(int[] columnsType0, int[] size0, int[] columnIndexes0, int[] scale0, Collator collator0) {
        columnsType = columnsType0;
        size = size0;
        columnIndexes = columnIndexes0;
        scale = scale0;
        collator = collator0;
    }
}
