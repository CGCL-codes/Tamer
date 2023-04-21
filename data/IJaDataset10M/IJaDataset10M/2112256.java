package mie.Forward.TableTypes;

import java.util.HashMap;
import java.util.ArrayList;
import dtm.DataTypeManager;
import dtm.SingleDatatype;
import ftm.FromToManager;
import opt.Options;
import sao.DML.*;

public class HubTableManager extends BaseTableManager {

    private HashMap<DMLField, DMLField> hubTableMap;

    public HubTableManager(Options options, DMLSchema sourceSchema, DMLSchema targetSchema, FromToManager ftMgr) {
        super(options, sourceSchema, targetSchema, ftMgr);
    }

    public void setupHubList() throws InterruptedException {
        Thread.sleep(0);
        if (getOptions() == null || getSourceSchema() == null || getTargetSchema() == null || getFromToManager() == null) return;
        DMLSchema sourceSchema = getSourceSchema();
        HashMap<String, DMLTable> tblMap = sourceSchema.getTables();
        hubTableMap = new HashMap<DMLField, DMLField>();
        DMLTable curTable;
        for (String tblName : sourceSchema.getTableNames()) {
            curTable = tblMap.get(tblName);
            Thread.sleep(0);
            if (curTable.getPKConstraint() != null) assignHub(curTable);
        }
    }

    public HashMap<DMLField, DMLField> getHubMap() {
        return (hubTableMap);
    }

    private void assignHub(DMLTable curTable) {
        DMLConstraint pkConst = curTable.getPKConstraint();
        if (pkConst == null) return;
        if (pkConst.getIsValid() == false || pkConst.getIsResolved() == false) return;
        String[] pkFields = pkConst.getColumnNames(true).split(",");
        for (String pkFieldName : pkFields) processPKField(curTable, pkFieldName);
    }

    private void processPKField(DMLTable curTable, String pkFieldName) {
        DMLField hubField = null;
        ArrayList<DMLField> hubList = getTopFKFields(curTable, pkFieldName);
        DMLField srcField = curTable.getFieldByName(pkFieldName);
        if (hubList.size() == 1) hubField = hubList.get(0); else if (hubList.size() > 1) {
            int count = 0;
            while (count < hubList.size()) {
                if (hubList.get(count) == srcField) hubList.remove(count);
                count++;
            }
            collapseHubFields(srcField, hubList);
            if (hubList.size() > 1) {
                getOptions().printtextln("***** WARNING: Source Field leads to more than one TOP PARENT ***");
                for (DMLField tgtField : hubList) {
                    DMLTable tgtTable = (DMLTable) tgtField.getParentTable();
                    int level = tgtTable.getTreeLevel();
                    if (srcField.getFieldName().equals(tgtField.getFieldName()) == false) this.getOptions().printtextln(curTable.getTableName() + " . " + srcField.getFieldName() + "  Target: " + tgtTable.getTableName() + " . " + tgtField.getFieldName() + " " + level);
                }
                getOptions().printtextln("-------------------------------");
            }
            hubField = hubList.get(hubList.size() - 1);
        }
        if (hubField != null) hubTableMap.put(srcField, hubField);
    }

    private ArrayList<DMLField> getTopFKFields(DMLTable curTable, String fieldName) {
        ArrayList<DMLField> topFields = new ArrayList<DMLField>();
        DMLField curField = curTable.getFieldByName(fieldName);
        int treeLevel = 200;
        DMLSchema topSchema = (DMLSchema) curTable.getParentSchema();
        DataTypeManager dtm = topSchema.getDatatypeConverter();
        SingleDatatype sdt = dtm.getSingleDatatype(curTable.getDBType(), curField.getDatatype());
        if (sdt.getBaseType().contains("date") || sdt.getBaseType().contains("time")) return (topFields);
        for (DMLConstraint curConst : curTable.getConstraintList(DMLConstraint.FK_CONST)) if (curConst.getIsValid() == true && curConst.getIsEnabled() == true && curConst.getIsRecursive() == false) {
            DMLField topField = getTopField(curTable, curConst, fieldName);
            DMLTable topTable = (DMLTable) topField.getParentTable();
            if (topTable.getTreeLevel() <= treeLevel) {
                treeLevel = topTable.getTreeLevel();
                if (topFields.contains(topField) == false) topFields.add(topField);
            }
        }
        if (topFields.size() == 0) topFields.add(curField);
        return (topFields);
    }

    private DMLField getTopField(DMLTable curTable, DMLConstraint curConst, String fieldName) {
        DMLSchema sourceSchema = getSourceSchema();
        DMLConstraint parentPK = null;
        DMLConstraint childConst = curConst;
        DMLTable parentTable = null;
        Boolean finished = false;
        String refFieldName = fieldName;
        DMLField result = null;
        while (!finished) {
            refFieldName = childConst.getMatchingRefName(refFieldName);
            parentTable = sourceSchema.getTable(childConst.getRefTableName());
            parentPK = parentTable.getPKConstraint();
            String pkCols = "," + parentPK.getColumnNames(true) + ",";
            if (parentTable == curTable) finished = true;
            if (pkCols.contains("," + refFieldName + ",")) {
                childConst = parentTable.getFirstFK(refFieldName);
                if (childConst == null) {
                    result = parentTable.getFieldByName(refFieldName);
                    finished = true;
                }
            } else finished = true;
        }
        if (result == null) result = curTable.getFieldByName(fieldName);
        return (result);
    }

    private void collapseHubFields(DMLField srcField, ArrayList<DMLField> hubList) {
        if (hubList.size() <= 1) return;
        ArrayList<DMLField> newList = new ArrayList<DMLField>(hubList.size());
        int matchCount = 0;
        int count = hubList.size() - 1;
        while (count >= 0) {
            DMLField tgtField = hubList.get(count);
            if (tgtField.getDatatype().equals(srcField.getDatatype()) && tgtField.getPrecision() >= srcField.getPrecision()) {
                matchCount++;
                if (matchCount == 1) newList.add(tgtField);
            }
            count--;
        }
        hubList.clear();
        for (DMLField tgtField : newList) hubList.add(tgtField);
        newList.clear();
    }
}
