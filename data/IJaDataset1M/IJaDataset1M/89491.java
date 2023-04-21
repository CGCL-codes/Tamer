package com.daffodilwoods.daffodildb.server.datadictionarysystem.information;

import java.util.*;
import com.daffodilwoods.daffodildb.server.datadictionarysystem.*;
import com.daffodilwoods.daffodildb.server.serversystem.dmlvalidation.constraintsystem.*;
import com.daffodilwoods.daffodildb.server.sessionsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;

public class TableInformation implements _TableInformation {

    private String type;

    private String remarks;

    _DataDictionary dataDictionary;

    QualifiedIdentifier tableName;

    private _ColumnsInformation columnInformation;

    private _IndexInformation indexInformation;

    private _FullTextIndexInformation fullTextIndexInformation;

    private _ConstraintInformation constraintInformation;

    private _TriggerInformation triggerInformation;

    private boolean searchedTriggerInformation;

    private boolean searchedIndexInformation;

    private boolean searchedFullTextIndexInformation;

    private boolean searchedConstraintInformation;

    public TableInformation(_DataDictionary dataDictionary, QualifiedIdentifier tableName) {
        this.dataDictionary = dataDictionary;
        this.tableName = tableName;
    }

    public String getQualifiedName() {
        return tableName.getIdentifier();
    }

    public ArrayList getAllColumns() {
        if (columnInformation == null) getColumnInformation();
        String[] columnNames = columnInformation.getColumnNames();
        ArrayList columnsList = new ArrayList();
        for (int i = 0; i < columnNames.length; i++) columnsList.add(columnNames[i]);
        return columnsList;
    }

    public ArrayList getAllIndexes() {
        if (!searchedIndexInformation) getIndexInformation();
        return indexInformation == null ? null : ((IndexInformation) indexInformation).getAllIndexes();
    }

    public ArrayList getAllFullTextIndexes() {
        if (!searchedFullTextIndexInformation) getFullTextIndexInformation();
        return fullTextIndexInformation == null ? null : ((FullTextIndexInformation) fullTextIndexInformation).getAllIndexes();
    }

    public ArrayList getAllTriggers() {
        if (!searchedTriggerInformation) getTriggerInformation();
        return triggerInformation == null ? null : ((TriggerInformation) triggerInformation).getAllTriggers();
    }

    public _ColumnsInformation getColumnInformation() {
        if (columnInformation != null) return columnInformation;
        columnInformation = new ColumnsInformation(dataDictionary, tableName);
        return columnInformation;
    }

    public _ConstraintInformation getConstraintInformation() {
        if (searchedConstraintInformation) return constraintInformation;
        ArrayList constrintInfoList = new ArrayList();
        searchedConstraintInformation = true;
        addConstriantsInfo(constrintInfoList, getCheckConstraints());
        addConstriantsInfo(constrintInfoList, getPrimaryAndUniqueConstraints());
        addConstriantsInfo(constrintInfoList, getReferencedConstraints());
        addConstriantsInfo(constrintInfoList, getReferencingConstraints());
        if (constrintInfoList.size() == 0) return null;
        constraintInformation = new ConstraintInformation((_ConstraintInfo[]) constrintInfoList.toArray(new _ConstraintInfo[0]));
        return constraintInformation;
    }

    private void addConstriantsInfo(ArrayList constraintInfoList, _ConstraintInfo[] constraintInfo) {
        if (constraintInfo == null) return;
        int len = constraintInfo.length;
        for (int i = 0; i < len; i++) constraintInfoList.add(constraintInfo[i]);
    }

    public ArrayList getAllConstraints() {
        if (!searchedConstraintInformation) getConstraintInformation();
        return constraintInformation == null ? null : ((ConstraintInformation) constraintInformation).getAllConstraints();
    }

    public _IndexInformation getIndexInformation() {
        if (searchedIndexInformation) return indexInformation;
        try {
            _IndexCharacteristics indexCharacteristics = dataDictionary.getIndexCharacteristics(tableName);
            indexCharacteristics.refresh();
            com.daffodilwoods.daffodildb.server.datasystem.indexsystem._IndexInformation[] dsIndexInformation = indexCharacteristics.getIndexInformations();
            searchedIndexInformation = true;
            if (dsIndexInformation == null) return null;
            int len = dsIndexInformation.length;
            ArrayList indexInfoList = new ArrayList();
            boolean onlyRowId = ((GlobalSession) dataDictionary.getServerSession()).getSessionVersionHandler().hasOnlyRowidAsColumnIndex();
            int indexCount = onlyRowId ? 1 : 3;
            for (int i = 0; i < len; i++) {
                if (!dsIndexInformation[i].isSystemGenerated()) {
                    IndexInfo tempIndexInfo = new IndexInfo();
                    tempIndexInfo.name = dsIndexInformation[i].getQualifiedIdentifier().getName();
                    if (dsIndexInformation[i].isUpdated()) {
                        String[] tempcolumns = dsIndexInformation[i].getColumns();
                        int numberOfColumns = tempcolumns.length;
                        String[] columns = new String[numberOfColumns - indexCount];
                        boolean[] order = new boolean[numberOfColumns - indexCount];
                        System.arraycopy(tempcolumns, 0, columns, 0, numberOfColumns - indexCount);
                        System.arraycopy(dsIndexInformation[i].getOrderOfColumns(), 0, order, 0, numberOfColumns - indexCount);
                        tempIndexInfo.columns = columns;
                        tempIndexInfo.orderOfColumns = order;
                    } else {
                        tempIndexInfo.columns = dsIndexInformation[i].getColumns();
                        tempIndexInfo.orderOfColumns = dsIndexInformation[i].getOrderOfColumns();
                    }
                    indexInfoList.add(tempIndexInfo);
                }
            }
            if (indexInfoList.size() == 0) return null;
            IndexInfo[] indexInfo = (IndexInfo[]) indexInfoList.toArray(new IndexInfo[0]);
            indexInformation = new IndexInformation(indexInfo);
            return indexInformation;
        } catch (Exception ex) {
            return null;
        }
    }

    public _FullTextIndexInformation getFullTextIndexInformation() {
        if (searchedFullTextIndexInformation) return fullTextIndexInformation;
        try {
            _FullTextIndexCharacteristics indexCharacteristics = dataDictionary.getFullTextIndexCharacteristics(tableName);
            indexCharacteristics.refresh();
            com.daffodilwoods.daffodildb.server.datasystem.indexsystem._FullTextIndexInformation[] dsIndexInformation = indexCharacteristics.getFullTextIndexInformations();
            searchedFullTextIndexInformation = true;
            if (dsIndexInformation == null) return null;
            int len = dsIndexInformation.length;
            ArrayList fullTextIndexInfoList = new ArrayList();
            boolean onlyRowId = ((GlobalSession) dataDictionary.getServerSession()).getSessionVersionHandler().hasOnlyRowidAsColumnIndex();
            int indexCount = onlyRowId ? 1 : 3;
            for (int i = 0; i < len; i++) {
                FullTextIndexInfo tempIndexInfo = new FullTextIndexInfo();
                tempIndexInfo.name = dsIndexInformation[i].getQualifiedIdentifier().getName();
                if (dsIndexInformation[i].isUpdated()) {
                    String[] tempcolumns = dsIndexInformation[i].getColumns();
                    int numberOfColumns = tempcolumns.length;
                    String[] columns = new String[numberOfColumns - indexCount];
                    boolean[] order = new boolean[numberOfColumns - indexCount];
                    System.arraycopy(tempcolumns, 0, columns, 0, numberOfColumns - indexCount);
                    tempIndexInfo.columns = columns;
                } else {
                    tempIndexInfo.columns = dsIndexInformation[i].getColumns();
                }
                fullTextIndexInfoList.add(tempIndexInfo);
            }
            if (fullTextIndexInfoList.size() == 0) return null;
            FullTextIndexInfo[] indexInfo = (FullTextIndexInfo[]) fullTextIndexInfoList.toArray(new FullTextIndexInfo[0]);
            fullTextIndexInformation = new FullTextIndexInformation(indexInfo);
            return fullTextIndexInformation;
        } catch (Exception ex) {
            return null;
        }
    }

    public _TriggerInformation getTriggerInformation() {
        if (searchedTriggerInformation) return triggerInformation;
        ArrayList triggerInfoList = new ArrayList();
        try {
            _TriggerCharacteristics triggerCharacteristics = dataDictionary.getDDSTriggerOperation().getTriggerCharacteristics(tableName, true);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterDeleteTriggers(), SqlKeywords.AFTER, SqlKeywords.ROW, SqlKeywords.DELETE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterInsertTriggers(), SqlKeywords.AFTER, SqlKeywords.ROW, SqlKeywords.INSERT);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterUpdateTriggers(null), SqlKeywords.AFTER, SqlKeywords.ROW, SqlKeywords.UPDATE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeDeleteTriggers(), SqlKeywords.BEFORE, SqlKeywords.ROW, SqlKeywords.DELETE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeInsertTriggers(), SqlKeywords.BEFORE, SqlKeywords.ROW, SqlKeywords.INSERT);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeUpdateTriggers(null), SqlKeywords.BEFORE, SqlKeywords.ROW, SqlKeywords.UPDATE);
            triggerCharacteristics = dataDictionary.getDDSTriggerOperation().getTriggerCharacteristics(tableName, false);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterDeleteTriggers(), SqlKeywords.AFTER, SqlKeywords.STATEMENT, SqlKeywords.DELETE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterInsertTriggers(), SqlKeywords.AFTER, SqlKeywords.STATEMENT, SqlKeywords.INSERT);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getAfterUpdateTriggers(null), SqlKeywords.AFTER, SqlKeywords.STATEMENT, SqlKeywords.UPDATE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeDeleteTriggers(), SqlKeywords.BEFORE, SqlKeywords.STATEMENT, SqlKeywords.DELETE);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeInsertTriggers(), SqlKeywords.BEFORE, SqlKeywords.STATEMENT, SqlKeywords.INSERT);
            addTriggerInfo(triggerInfoList, triggerCharacteristics.getBeforeUpdateTriggers(null), SqlKeywords.BEFORE, SqlKeywords.STATEMENT, SqlKeywords.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchedTriggerInformation = true;
        if (triggerInfoList.size() == 0) return null;
        triggerInformation = new TriggerInformation((_TriggerInfo[]) triggerInfoList.toArray(new _TriggerInfo[0]));
        return triggerInformation;
    }

    private void addTriggerInfo(ArrayList triggerInfoList, _Trigger[] triggers, String actionTime, String orentiation, String event) {
        if (triggers == null) return;
        int len = triggers.length;
        try {
            for (int i = 0; i < len; i++) {
                TriggerInfo triggerInfo = new TriggerInfo();
                triggerInfo.catalog = triggers[i].getQualifiedIdentifier().catalog;
                triggerInfo.schema = triggers[i].getQualifiedIdentifier().schema;
                triggerInfo.name = triggers[i].getQualifiedIdentifier().getName();
                triggerInfo.actionTime = actionTime;
                triggerInfo.triggerEvent = event;
                triggerInfo.actionOrentiation = orentiation;
                com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.booleanvalueexpression bve = triggers[i].getSearchCondition();
                triggerInfo.whenCondition = bve == null ? "" : bve.toString();
                com.daffodilwoods.daffodildb.server.sql99.SQLexecutablestatement[] statements = triggers[i].getStatement();
                String statementClause = "";
                if (statements != null) {
                    for (int j = 0; j < statements.length; j++) statementClause += statements[j].toString() + " ";
                }
                triggerInfo.triggerStatements = statementClause;
                triggerInfo.oldAlias = triggers[i].getOldAliasName();
                triggerInfo.newAlias = triggers[i].getNewAliasName();
                triggerInfoList.add(triggerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private _CheckConstraintInfo[] getCheckConstraints() {
        try {
            _CheckConstraintCharacteristics checkConstraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getCheckConstraintCharacteristics(tableName, false);
            _CheckConstraint[] constraints = checkConstraintCharacteristics.getCheckConstraintsForDelete();
            CheckConstraintInfo[] nonDeferableConstraintInfo = getCheckConstraintInfo(constraints, "NO");
            checkConstraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getCheckConstraintCharacteristics(tableName, true);
            constraints = checkConstraintCharacteristics.getCheckConstraintsForDelete();
            CheckConstraintInfo[] deferableConstraintInfo = getCheckConstraintInfo(constraints, "YES");
            if (deferableConstraintInfo == null && nonDeferableConstraintInfo == null) return null;
            int noOfdefereableConstraint = deferableConstraintInfo == null ? 0 : deferableConstraintInfo.length;
            int noOfnondefereableConstraint = nonDeferableConstraintInfo == null ? 0 : nonDeferableConstraintInfo.length;
            CheckConstraintInfo[] constraintInfo = new CheckConstraintInfo[noOfdefereableConstraint + noOfnondefereableConstraint];
            if (noOfdefereableConstraint != 0) System.arraycopy(deferableConstraintInfo, 0, constraintInfo, 0, noOfdefereableConstraint);
            if (noOfnondefereableConstraint != 0) System.arraycopy(nonDeferableConstraintInfo, 0, constraintInfo, noOfdefereableConstraint, noOfnondefereableConstraint);
            return (_CheckConstraintInfo[]) constraintInfo;
        } catch (DException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private CheckConstraintInfo[] getCheckConstraintInfo(_CheckConstraint[] constraints, String deferrable) throws DException {
        try {
            if (constraints == null) return null;
            int len = constraints.length;
            CheckConstraintInfo[] checkInfo = new CheckConstraintInfo[len];
            for (int i = 0; i < len; i++) {
                checkInfo[i] = new CheckConstraintInfo();
                checkInfo[i].name = constraints[i].getQualifiedIdentifier().getName();
                checkInfo[i].deferrable = deferrable;
                checkInfo[i].checkCondition = constraints[i].getCondition().toString();
            }
            return checkInfo;
        } catch (DException ex) {
            throw new DException(ex.getDseCode(), ex.getParameters());
        }
    }

    private _UniqueConstraintInfo[] getPrimaryAndUniqueConstraints() {
        try {
            _PrimaryAndUniqueConstraintCharacteristics constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getPrimaryAndUniqueConstraintCharacteristics(tableName, true);
            _UniqueConstraint[] constraints = constraintCharacteristics.getConstraints();
            _UniqueConstraintInfo[] deferableConstraintInfo = getUniqueConstraintInfo(constraints, "YES");
            constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getPrimaryAndUniqueConstraintCharacteristics(tableName, false);
            constraints = constraintCharacteristics.getConstraints();
            _UniqueConstraintInfo[] nonDeferableConstraintInfo = getUniqueConstraintInfo(constraints, "NO");
            if (deferableConstraintInfo == null && nonDeferableConstraintInfo == null) return null;
            int noOfdefereableConstraint = deferableConstraintInfo == null ? 0 : deferableConstraintInfo.length;
            int noOfnondefereableConstraint = nonDeferableConstraintInfo == null ? 0 : nonDeferableConstraintInfo.length;
            _UniqueConstraintInfo[] constraintInfo = new _UniqueConstraintInfo[noOfdefereableConstraint + noOfnondefereableConstraint];
            if (noOfdefereableConstraint != 0) System.arraycopy(deferableConstraintInfo, 0, constraintInfo, 0, noOfdefereableConstraint);
            if (noOfnondefereableConstraint != 0) System.arraycopy(nonDeferableConstraintInfo, 0, constraintInfo, noOfdefereableConstraint, noOfnondefereableConstraint);
            return constraintInfo;
        } catch (Exception ex) {
            return null;
        }
    }

    private _UniqueConstraintInfo[] getUniqueConstraintInfo(_UniqueConstraint[] constraints, String deferrable) throws com.daffodilwoods.database.resource.DException {
        if (constraints == null) return null;
        int len = constraints.length;
        UniqueConstraintInfo[] uniqueInfo = new UniqueConstraintInfo[len];
        for (int i = 0; i < len; i++) {
            uniqueInfo[i] = new UniqueConstraintInfo();
            uniqueInfo[i].name = constraints[i].getQualifiedIdentifier().getName();
            uniqueInfo[i].columns = constraints[i].getColumnNames();
            uniqueInfo[i].type = constraints[i].getType();
            uniqueInfo[i].deferrable = deferrable;
        }
        return (_UniqueConstraintInfo[]) uniqueInfo;
    }

    private _ReferentialConstraintInfo[] getReferencingConstraints() {
        try {
            _ReferencingConstraintCharacteristics constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getReferencingConstraintCharacteristics(tableName, true);
            _ReferentialConstraintInfo[] deferableConstraintInfo = getReferentialInfo(constraintCharacteristics.getReferencingConstraints(), "YES", "Foreign/Referencing");
            constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getReferencingConstraintCharacteristics(tableName, false);
            _ReferentialConstraintInfo[] nonDeferableConstraintInfo = getReferentialInfo(constraintCharacteristics.getReferencingConstraints(), "NO", "Foreign/Referencing");
            return mergerReferentialInfo(deferableConstraintInfo, nonDeferableConstraintInfo);
        } catch (DException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private _ReferentialConstraintInfo[] mergerReferentialInfo(_ReferentialConstraintInfo[] deferableConstraintInfo, _ReferentialConstraintInfo[] nonDeferableConstraintInfo) {
        if (deferableConstraintInfo == null && nonDeferableConstraintInfo == null) return null;
        int noOfdefereableConstraint = deferableConstraintInfo == null ? 0 : deferableConstraintInfo.length;
        int noOfnondefereableConstraint = nonDeferableConstraintInfo == null ? 0 : nonDeferableConstraintInfo.length;
        _ReferentialConstraintInfo[] constraintInfo = new _ReferentialConstraintInfo[noOfdefereableConstraint + noOfnondefereableConstraint];
        if (noOfdefereableConstraint != 0) System.arraycopy(deferableConstraintInfo, 0, constraintInfo, 0, noOfdefereableConstraint);
        if (noOfnondefereableConstraint != 0) System.arraycopy(nonDeferableConstraintInfo, 0, constraintInfo, noOfdefereableConstraint, noOfnondefereableConstraint);
        return (_ReferentialConstraintInfo[]) constraintInfo;
    }

    public _ReferentialConstraintInfo[] getReferencedConstraints() {
        try {
            _ReferencedConstraintCharacteristics constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getReferencedConstraintCharacteristics(tableName, true);
            _ReferentialConstraintInfo[] deferableConstraintInfo = getReferentialInfo(constraintCharacteristics.getReferencedConstraintsForDelete(), "YES", "Foreign/Referenced");
            constraintCharacteristics = dataDictionary.getDDSConstraintsOperation().getReferencedConstraintCharacteristics(tableName, false);
            _ReferentialConstraintInfo[] nonDeferableConstraintInfo = getReferentialInfo(constraintCharacteristics.getReferencedConstraintsForDelete(), "NO", "Foreign/Referenced");
            return mergerReferentialInfo(deferableConstraintInfo, nonDeferableConstraintInfo);
        } catch (DException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private _ReferentialConstraintInfo[] getReferentialInfo(_ReferentialConstraint[] constraints, String deferrable, String type) throws DException {
        try {
            if (constraints == null) return null;
            int len = constraints.length;
            ReferentialConstraintInfo[] referentialInfo = new ReferentialConstraintInfo[len];
            for (int i = 0; i < len; i++) {
                referentialInfo[i] = new ReferentialConstraintInfo();
                referentialInfo[i].name = constraints[i].getQualifiedIdentifier().getName();
                referentialInfo[i].referencedTable = constraints[i].getReferencedTable().getIdentifier();
                referentialInfo[i].referencedColumns = constraints[i].getReferencedColumnNames();
                referentialInfo[i].matchOption = getMatchOption(constraints[i].getMatch_Option());
                referentialInfo[i].updateRule = getRuleName(constraints[i].getUpdate_Rule());
                referentialInfo[i].deleteRule = getRuleName(constraints[i].getDelete_Rule());
                referentialInfo[i].deferrable = deferrable;
                referentialInfo[i].type = type;
                referentialInfo[i].referencingColumns = (String[]) ((ReferentialConstraintDescriptor) constraints[i]).tableConstraintDescriptor.getReferencingColumns().toArray(new String[0]);
            }
            return referentialInfo;
        } catch (DException ex) {
            throw new DException(ex.getDseCode(), ex.getParameters());
        }
    }

    private String getMatchOption(int match_option) {
        if (match_option == TypeConstant.Match_Simple) return "Simple"; else if (match_option == TypeConstant.Match_Partial) return "Partial"; else if (match_option == TypeConstant.Match_Full) return "Full";
        return "";
    }

    private String getRuleName(int rule) {
        if (rule == TypeConstant.CASCADE) return "Cascade"; else if (rule == TypeConstant.SETNULL) return "Set Null"; else if (rule == TypeConstant.SETDEFAULT) return "Set Default"; else if (rule == TypeConstant.RESTRICT) return "Restrict";
        return "";
    }

    public String getName() {
        return tableName.name;
    }

    public String getCatalog() {
        return tableName.catalog;
    }

    public String getSchema() {
        return tableName.schema;
    }

    public String getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }
}
