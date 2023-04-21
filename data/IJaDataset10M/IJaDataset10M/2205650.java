package mie;

import java.util.ArrayList;
import java.util.HashMap;
import dtm.*;
import opt.*;
import sao.DML.*;
import utils.*;
import gui.tree.*;
import gui.objects.*;

public class ModelIntegrationEngine extends MieThread {

    private ArrayList<DMLComplete> mie_dmlList;

    private ArrayList<DMLTable> mie_UnconList;

    private HashMap<String, ArrayList<DMLConstraint>> mie_pkList;

    private TreeObject treeNode;

    public ModelIntegrationEngine(Options opts, JLogTextArea logWindow) {
        super(opts, logWindow);
        initialize();
    }

    private void initialize() {
        mie_dmlList = new ArrayList<DMLComplete>();
    }

    public void setTreeNode(TreeObject node) {
        treeNode = node;
    }

    public boolean addDMLComplete(DMLComplete dmlCompleteObject) {
        if (mie_dmlList == null) return (false);
        if (mie_dmlList.contains(dmlCompleteObject)) return (false);
        mie_dmlList.add(dmlCompleteObject);
        return (true);
    }

    public void setThresholds(int strengthThreshold, int confidenceThreshold) {
        opts.setMergeConfidence(confidenceThreshold);
        opts.setMergeStrength(strengthThreshold);
    }

    protected void runLoop() throws InterruptedException {
        if (mie_dmlList == null) return;
        if (mie_dmlList.size() == 0) return;
        for (DMLComplete currentDML : mie_dmlList) {
            processSingleDML(currentDML);
            Thread.sleep(0);
        }
    }

    private void processSingleDML(DMLComplete currentDML) throws InterruptedException {
        if (currentDML == null) return;
        for (String schemaName : currentDML.getSchemaNames()) {
            processSingleSchema(currentDML.getSchema(schemaName));
            Thread.sleep(0);
        }
        currentDML.validateAllConstraints();
        SchemaTreeManager aMgr = new SchemaTreeManager(currentDML, opts, this.opts.globalTreeList, treeNode, getLogWindow());
        aMgr.setAddAffinityOnly(true);
        aMgr.run();
    }

    private void processSingleSchema(DMLSchema currentSchema) throws InterruptedException {
        Thread.sleep(0);
        consolidateDisparateTables(currentSchema, opts.getMergeStrength(), opts.getMergeConfidence());
    }

    private void consolidateDisparateTables(DMLSchema curSchema, int strengthThreshold, int confidenceThreshold) {
        printtextln("Consolidating Unconnected Tables for: " + curSchema.name());
        printtextln("  Merge Confidence Threshold is: " + confidenceThreshold + "%");
        printtextln("  Merge Strength Threshold is  : " + strengthThreshold + "%");
        this.mie_UnconList = curSchema.getUnconnectedTables();
        this.mie_pkList = curSchema.getAllPrimaryKeys();
        rulePKtoPKMatches(curSchema, mie_UnconList, mie_pkList);
        rulePKfieldsToPKMatches(curSchema, mie_UnconList, mie_pkList);
        ruleFieldsToPKParent(curSchema, mie_UnconList, mie_pkList);
        HashMap<Integer, String> masterTrees = new HashMap<Integer, String>();
        masterTrees = createTrees(curSchema);
        connectForests(masterTrees, curSchema);
        printtextln("Done Consolidating");
        printtextln("-------------------------");
        printtextln("Applying Affinities as Foreign Keys");
        for (String anObj : curSchema.getTableNames()) {
            DMLTable curTable = curSchema.getTable(anObj);
            ArrayList<DMLConstraint> constList = curTable.getAffinityList();
            for (DMLConstraint curConst : constList) {
                if (!curConst.getAffinityScore().getTypeClass().equals(DMLAffinityScore.CLASS_TABLE)) continue;
                if (curConst.getConfidence() >= confidenceThreshold && curConst.getStrength() >= strengthThreshold) {
                    if (checkCircularAffinity(curSchema, curConst) == false) {
                        DMLConstraint newConst = curTable.addConstraint(DMLConstraint.FK_CONST + "_" + curConst.getConstraintName(), true, curConst.getColumnNames(true), DMLConstraint.FK_CONST, curConst.getRefTableName(), curConst.getRefColumnNames(true), false);
                        newConst.setAffinityScore(curConst.getAffinityScore());
                        newConst.setWasAffinity(true);
                        curConst.setIsValid(true);
                    }
                }
            }
        }
        printtextln("-------------------------");
    }

    private HashMap<Integer, String> createTrees(DMLSchema curSchema) {
        HashMap<String, String> masterMap = new HashMap<String, String>();
        for (DMLTable curTable : curSchema.getTables().values()) masterMap.put(curTable.getTableName(), curTable.getRefToNames() + "," + curTable.getRefFromNames());
        for (String key : masterMap.keySet()) {
            String holdV = masterMap.get(key);
            if (holdV.equals(",")) holdV = "";
            if (holdV.length() > 0 && holdV.equals("!") == false) {
                String[] depOn = holdV.split(",");
                int count = 0;
                int maxSize = depOn.length;
                while (count < maxSize) {
                    boolean chg = false;
                    String subDep = depOn[count];
                    if (subDep.length() > 0) {
                        String newDep = masterMap.get(subDep);
                        if (newDep.equals("!") == false) {
                            chg = true;
                            for (String newSub : newDep.split(",")) if (checkContains(holdV, newSub) == false && newSub.equals(key) == false) {
                                holdV += "," + newSub;
                            }
                        }
                    }
                    if (chg) {
                        holdV = holdV.replace(",,", ",");
                        depOn = holdV.split(",");
                        masterMap.put(subDep, "!");
                        maxSize = depOn.length;
                    }
                    count++;
                }
                holdV = holdV.replace(",,", ",");
                masterMap.put(key, holdV);
            }
        }
        for (DMLTable curTable : curSchema.getTables().values()) if (masterMap.get(curTable.getTableName()).equals("!")) masterMap.remove(curTable.getTableName());
        HashMap<Integer, String> resultMap = new HashMap<Integer, String>(masterMap.size());
        int count = 0;
        for (String key : masterMap.keySet()) {
            String result = key + "," + masterMap.get(key);
            result = result.replace(",,", ",");
            resultMap.put(count, result);
            count++;
        }
        masterMap.clear();
        masterMap = null;
        return (resultMap);
    }

    private boolean checkContains(String major, String find) {
        if (major.length() == 0 || find.length() == 0) return (true);
        major = "," + major + ",";
        return (major.contains("," + find + ","));
    }

    private boolean checkCircularAffinity(DMLSchema curSchema, DMLConstraint curConst) {
        boolean result = false;
        DMLTable refTable = curSchema.getTable(curConst.getRefTableName());
        ArrayList<DMLConstraint> constList = refTable.getConstraintList(DMLConstraint.FK_CONST);
        for (DMLConstraint aConst : constList) if (aConst.getRefTableName().equals(curConst.getTableName())) result = true;
        return (result);
    }

    private String lookupMetaNames(DMLTable curTable, String actualNames) {
        String[] nameSet = actualNames.toUpperCase().split(",");
        String metaNames = "";
        for (String subName : nameSet) {
            DMLField curField = curTable.getFieldByName(subName);
            if (curField != null) {
                String metaField = curField.getMetaName().toUpperCase();
                if (metaField.length() > 0) metaNames = StringUtils.addToString(metaNames, metaField, ",");
            }
        }
        return (metaNames);
    }

    private String lookupFullNames(DMLTable curTable, String actualNames) {
        String[] nameSet = actualNames.toUpperCase().split(",");
        String metaNames = "";
        for (String subName : nameSet) {
            DMLField curField = curTable.getFieldByName(subName);
            if (curField != null) {
                String metaField = curField.getBusinessName().toUpperCase();
                if (metaField.length() > 0) metaNames = StringUtils.addToString(metaNames, metaField, "\\t");
            }
        }
        return (metaNames);
    }

    private boolean matchConstraint(DMLSchema curSchema, DMLConstraint curConst, DMLConstraint pkConst) {
        boolean result = curConst.exactMatch(pkConst);
        if (result) return (result);
        DMLTable srcTable = curSchema.getTable(curConst.getTableName());
        DMLTable tgtTable = curSchema.getTable(pkConst.getTableName());
        if (srcTable == null || tgtTable == null) return (result);
        if (opts.getUseMetaNames()) {
            String lhsFields = lookupMetaNames(srcTable, curConst.getColumnNames(true));
            String rhsFields = lookupMetaNames(tgtTable, pkConst.getColumnNames(true));
            if (lhsFields.equals(rhsFields) && lhsFields.length() > 0) return (true);
        }
        if (opts.getUseFullNames()) {
            String lhsFields = lookupFullNames(srcTable, curConst.getColumnNames(true));
            String rhsFields = lookupFullNames(tgtTable, pkConst.getColumnNames(true));
            if (lhsFields.equals(rhsFields) && lhsFields.length() > 0) return (true);
            if (StringUtils.compareSetsFullMatch(lhsFields.split("\\t"), rhsFields.split("\\t"), true, false)) return (true);
        }
        return (result);
    }

    private void rulePKtoPKMatches(DMLSchema curSchema, ArrayList<DMLTable> unconList, HashMap<String, ArrayList<DMLConstraint>> pkMap) {
        for (DMLTable curTable : unconList) {
            DMLConstraint pkConst = curTable.getPKConstraint();
            ArrayList<DMLConstraint> bestMatch = new ArrayList<DMLConstraint>();
            if (pkConst != null) {
                String[] fields = pkConst.getColumnNames(true).split(",");
                ArrayList<DMLConstraint> constList = pkMap.get(fields[0]);
                for (DMLConstraint curConst : constList) {
                    sleepy(0);
                    if (curConst != pkConst) {
                        if (matchConstraint(curSchema, curConst, pkConst)) bestMatch.add(curConst); else if (curConst.subsetMatch(pkConst)) bestMatch.add(curConst);
                    }
                }
            }
            if (bestMatch != null) {
                for (DMLConstraint matchConst : bestMatch) {
                    String cName = "";
                    String tName = curTable.getTableName();
                    sleepy(0);
                    if (curTable.getRefFromNames().contains(matchConst.getTableName()) == false) {
                        int maxlen = 20;
                        if (tName.length() <= 20) maxlen = tName.length() - 1;
                        cName = tName.substring(0, maxlen) + "_mchv_" + (curTable.getConstraintCount() + 1);
                        DMLTable refTable = (DMLTable) matchConst.getParentTablePtr();
                        printtextln("  Consolidating Match " + curTable.getTableName() + " referencing " + refTable.getTableName() + " **" + cName);
                        DMLConstraint fkConst = curTable.addConstraint(cName, false, matchConst.getColumnNames(true), DMLConstraint.FK_CONST, matchConst.getTableName(), matchConst.getColumnNames(true), false);
                        setupFKConstraint(fkConst, pkConst, matchConst, curTable, refTable);
                    }
                }
            }
        }
    }

    private void rulePKfieldsToPKMatches(DMLSchema curSchema, ArrayList<DMLTable> unconList, HashMap<String, ArrayList<DMLConstraint>> pkMap) {
        ArrayList<DMLTable> removeList = new ArrayList<DMLTable>();
        for (DMLTable curTable : unconList) {
            sleepy(0);
            DMLConstraint pkConst = curTable.getPKConstraint();
            ArrayList<DMLConstraint> bestMatch = new ArrayList<DMLConstraint>();
            if (pkConst != null) {
                String[] fields = pkConst.getColumnNames(true).split(",");
                for (String fname : fields) {
                    if (pkMap.containsKey(fname)) {
                        sleepy(0);
                        ArrayList<DMLConstraint> constList = pkMap.get(fname);
                        for (DMLConstraint curConst : constList) if (curConst != pkConst) if (pkConst.subsetMatchRandomOrder(curConst)) bestMatch.add(curConst);
                    }
                }
            }
            for (DMLConstraint matchConst : bestMatch) {
                String cName = "";
                String tName = curTable.getTableName();
                sleepy(0);
                if (curTable.getRefFromNames().contains(matchConst.getTableName()) == false) {
                    int maxlen = 20;
                    if (tName.length() <= 20) maxlen = tName.length() - 1;
                    cName = tName.substring(0, maxlen) + "_mchv_" + (curTable.getConstraintCount() + 1);
                    DMLTable refTable = (DMLTable) matchConst.getParentTablePtr();
                    printtextln("  Consolidating Match " + curTable.getTableName() + " referencing " + refTable.getTableName() + " " + cName);
                    DMLConstraint fkConst = curTable.addConstraint(cName, false, matchConst.getColumnNames(true), DMLConstraint.FK_CONST, matchConst.getTableName(), matchConst.getColumnNames(true), false);
                    setupFKConstraint(fkConst, matchConst, matchConst, curTable, refTable);
                    if (removeList.contains(curTable) == false) removeList.add(curTable);
                }
            }
        }
        for (DMLTable curTable : removeList) unconList.remove(curTable);
        removeList.clear();
    }

    private void ruleFieldsToPKParent(DMLSchema curSchema, ArrayList<DMLTable> unconList, HashMap<String, ArrayList<DMLConstraint>> pkMap) {
        for (DMLTable curTable : unconList) {
            sleepy(0);
            ArrayList<DMLConstraint> bestMatch = new ArrayList<DMLConstraint>();
            ArrayList<String> bestMatchNames = new ArrayList<String>();
            for (String pkKey : pkMap.keySet()) {
                sleepy(0);
                if (curTable.containsField(pkKey)) {
                    ArrayList<DMLConstraint> constList = pkMap.get(pkKey);
                    for (DMLConstraint matchConst : constList) {
                        String theMatch = "";
                        if (curTable != matchConst.getParentTablePtr()) theMatch = constInTable(curTable, matchConst);
                        if (theMatch.length() > 0) {
                            bestMatch.add(matchConst);
                            bestMatchNames.add(theMatch);
                        }
                    }
                }
            }
            if (bestMatch.size() > 0) {
                int count = 0;
                for (DMLConstraint matchConst : bestMatch) {
                    String cName = "";
                    String tName = curTable.getTableName();
                    sleepy(0);
                    if (curTable.getRefToNames().contains(matchConst.getTableName()) == false) {
                        int maxlen = 20;
                        if (tName.length() <= 20) maxlen = tName.length() - 1;
                        cName = tName.substring(0, maxlen) + "_mchv_" + (curTable.getConstraintCount() + 1);
                        DMLTable refTable = (DMLTable) matchConst.getParentTablePtr();
                        printtextln("  Consolidating Match " + curTable.getTableName() + " referencing " + refTable.getTableName() + " **" + cName);
                        String colNames = bestMatchNames.get(count);
                        DMLConstraint fkConst = curTable.addConstraint(cName, false, colNames, DMLConstraint.FK_CONST, matchConst.getTableName(), colNames, false);
                        setupFKConstraint(fkConst, null, matchConst, curTable, refTable);
                    }
                    count++;
                }
            }
        }
    }

    private void setupFKConstraint(DMLConstraint fkConst, DMLConstraint pkConst, DMLConstraint matchConst, DMLTable curTable, DMLTable refTable) {
        curTable.addRefTo(matchConst.getTableName());
        refTable.addRefFrom(curTable.getTableName());
        if (pkConst != null) {
            fkConst.setColumnDatatypes(pkConst.getColumnDatatypes());
            fkConst.setColumnPrecisions(pkConst.getColumnPrecisions());
            fkConst.setColumnSequences(pkConst.getColumnSequences());
            fkConst.setNumColumns(pkConst.getNumColumns());
            fkConst.setConstraintScale(pkConst.getConstraintScale());
        } else computeColumnSetup(curTable, fkConst);
        fkConst.setRefTablePtr(matchConst.getParentTablePtr());
        fkConst.setNumNullColumns(0);
        fkConst.setRefTableSeq(refTable.getSequence());
        fkConst.setRefColSequences(matchConst.getColumnSequences());
        fkConst.setIsActive(true);
        fkConst.setIsValid(true);
        fkConst.setIsEnabled();
        fkConst.setIsResolved(true);
    }

    private void computeColumnSetup(DMLTable curTable, DMLConstraint fkConst) {
        String colDtypes = "";
        String colPrec = "";
        String colSeq = "";
        int numCols = 0;
        int fullScale = 0;
        String[] fNames = fkConst.getColumnNames(true).split(",");
        for (String fName : fNames) {
            DMLField curField = curTable.getFieldByName(fName);
            colDtypes += "," + curField.getDatatype();
            colPrec += "," + curField.getPrecision();
            colSeq += "," + curField.getSequence();
            fullScale += curField.getPrecision();
        }
        colDtypes = colDtypes.replaceFirst(",", "");
        colPrec = colPrec.replaceFirst(",", "");
        colSeq = colSeq.replaceFirst(",", "");
        numCols = fNames.length;
        fkConst.setColumnDatatypes(colDtypes);
        fkConst.setColumnPrecisions(colPrec);
        fkConst.setColumnSequences(colSeq);
        fkConst.setNumColumns(numCols);
        fkConst.setConstraintScale(fullScale);
    }

    private String constInTable(DMLTable curTable, DMLConstraint matchConst) {
        String result = "";
        int matchCount = 0;
        int count = 0;
        String[] matchNames = matchConst.getColumnNames(true).split(",");
        String[] matchDtypes = matchConst.getColumnDatatypes().split("\t");
        DMLField curField = null;
        for (String curMatchName : matchNames) {
            sleepy(0);
            curField = curTable.getFieldByName(curMatchName);
            if (curField != null) if (curField.getDatatype().equals(matchDtypes[count])) matchCount++;
            count++;
            if (matchCount == count) result += "," + curMatchName;
        }
        result = result.replaceFirst(",", "");
        return (result);
    }

    @SuppressWarnings("unused")
    private void ruleFindPKMatches(DMLSchema curSchema, ArrayList<DMLTable> unconList, HashMap<String, ArrayList<DMLConstraint>> pkMap) {
        int count = 0;
        ArrayList<DMLAffinityScore> gradedMatchList = new ArrayList<DMLAffinityScore>();
        for (DMLTable curTable : unconList) {
            sleepy(0);
            gradedMatchList.clear();
            printtext("   Examining: " + curTable.getTableName());
            count = 0;
            for (Object anObj : curTable.getFieldList()) {
                sleepy(0);
                DMLField curField = (DMLField) curTable.getFieldByName((String) anObj);
                if (pkMap.containsKey(curField.getFieldName())) count = gradeConstraintMatches(curTable, pkMap.get(curField.getFieldName()), gradedMatchList);
            }
            if (count > 0) opts.appendtextln("   Affinities: " + count); else opts.appendtextln("");
        }
    }

    @SuppressWarnings("unused")
    private void rulePKNoAttach(DMLSchema curSchema) {
        ArrayList<DMLTable> unconTablesList = new ArrayList<DMLTable>();
        for (String anObj : curSchema.getTableNames()) {
            sleepy(0);
            DMLTable curTable = curSchema.getTable(anObj);
            if (curTable.getRefFromNames().length() == 0 && curTable.getRefToNames().length() == 0 && curTable.getPKConstraint() != null) unconTablesList.add(curTable);
        }
        for (DMLTable parentTable : unconTablesList) {
            sleepy(0);
            int affCount = 0;
            printtext("  Examining :" + parentTable.getTableName());
            DMLConstraint pkConst = parentTable.getPKConstraint();
            String pkFieldList = pkConst.getColumnNames(true);
            String pkFields[] = pkFieldList.split(",");
            for (String tblKey : curSchema.getTableNames()) {
                sleepy(0);
                int avgConfidence = 0;
                int avgStrength = 0;
                DMLTable childTable = curSchema.getTable(tblKey);
                for (String pkName : pkFields) {
                    if (tblKey.toString().equals(parentTable.getTableName()) == false && childTable.containsField(pkName)) {
                        DMLField sourceField = childTable.getFieldByName(pkName);
                        DMLField targetField = parentTable.getFieldByName(pkName);
                        DMLAffinityScore afScore = weighFields(sourceField, targetField);
                        avgConfidence += afScore.getConfidence();
                        avgStrength += afScore.getStrength();
                    }
                }
                if (avgStrength > 0 || avgConfidence > 0) {
                    affCount++;
                    avgConfidence = avgConfidence / pkFields.length;
                    avgStrength = avgStrength / pkFields.length;
                    DMLAffinityScore avgScore = new DMLAffinityScore();
                    avgScore.setConfidence(avgConfidence);
                    avgScore.setStrength(avgStrength);
                    avgScore.setTypeClass(DMLAffinityScore.CLASS_FIELD);
                    String childPKFields = childTable.getPKFields();
                    if (childPKFields.equals(pkFieldList)) this.addAffinityConstraint(parentTable, childTable, pkFieldList, pkFieldList, avgScore); else this.addAffinityConstraint(childTable, parentTable, pkFieldList, pkFieldList, avgScore);
                }
            }
            if (affCount > 0) printtext(" Affinities: " + affCount);
            printtext("\n");
        }
    }

    private int gradeConstraintMatches(DMLTable curTable, ArrayList<DMLConstraint> constList, ArrayList<DMLAffinityScore> gradedMatchList) {
        int count = 0;
        DMLSchema curSchema = (DMLSchema) curTable.getParentSchema();
        ArrayList<DMLField> fldMatches = new ArrayList<DMLField>();
        for (DMLConstraint curConst : constList) {
            sleepy(0);
            DMLAffinityScore afScore = new DMLAffinityScore();
            afScore.setConfidence(100);
            afScore.setStrength(10);
            afScore.setTypeClass(DMLAffinityScore.CLASS_CONSTRAINT);
            curConst.setAffinityScore(afScore);
            DMLTable parentTable = (DMLTable) curSchema.getTable(curConst.getTableName());
            if (parentTable != null) {
                sleepy(0);
                fldMatches.clear();
                DMLField[] fieldSet = parentTable.getAllFields(curConst.getColumnNames(true));
                ArrayList<DMLAffinityScore> compositeList = new ArrayList<DMLAffinityScore>();
                if (fieldSet.length > 1) {
                    int avgConf = 0;
                    int avgStrength = 0;
                    int fieldCount = 0;
                    int match = 0;
                    for (DMLField aField : fieldSet) {
                        sleepy(0);
                        fieldCount++;
                        if (curTable.containsField(aField.getFieldName())) {
                            match++;
                            if (match == fieldCount) {
                                DMLAffinityScore curScore = weighFields(curTable.getFieldByName(aField.getFieldName()), aField);
                                compositeList.add(curScore);
                                avgConf += curScore.getConfidence();
                                avgStrength += curScore.getStrength();
                            }
                        }
                    }
                    if (compositeList.size() == fieldSet.length) {
                        DMLAffinityScore avgScore = new DMLAffinityScore();
                        avgScore.setConfidence(avgConf / compositeList.size());
                        avgScore.setStrength(avgStrength / compositeList.size());
                        avgScore.setTypeClass(DMLAffinityScore.CLASS_CONSTRAINT);
                        addAffinityConstraint(curTable, parentTable, curConst.getColumnNames(true), curConst.getColumnNames(true), avgScore);
                        afScore = avgScore;
                    }
                } else {
                    afScore = weighFields(curTable.getFieldByName(fieldSet[0].getFieldName()), fieldSet[0]);
                    addAffinityConstraint(curTable.getFieldByName(fieldSet[0].getFieldName()), fieldSet[0], afScore);
                    count++;
                }
                gradedMatchList.add(afScore);
            }
        }
        return (count);
    }

    private DMLAffinityScore weighFields(DMLField sourceField, DMLField targetField) {
        return (wieghFields(sourceField, targetField, true, true));
    }

    private DMLAffinityScore wieghFields(DMLField sourceField, DMLField targetField, boolean isWeighComments, boolean isWeighTitles) {
        DMLTable sourceTable = (DMLTable) sourceField.getParentTable();
        DMLSchema sourceSchema = (DMLSchema) sourceTable.getParentSchema();
        DataTypeManager srcConv = sourceSchema.getDatatypeConverter();
        String sourceName = sourceField.getFieldName();
        String sourceType = sourceField.getDatatype();
        int sourcePrec = sourceField.getPrecision();
        int sourceScale = sourceField.getScale();
        boolean sourceIsNull = sourceField.getIsNull();
        SingleDatatype sdt = srcConv.getSingleDatatype(sourceTable.getDBType(), sourceType);
        DMLTable targetTable = (DMLTable) targetField.getParentTable();
        DMLSchema targetSchema = (DMLSchema) targetTable.getParentSchema();
        DataTypeManager tgtConv = targetSchema.getDatatypeConverter();
        String targetName = targetField.getFieldName();
        String targetType = targetField.getDatatype();
        int targetPrec = targetField.getPrecision();
        int targetScale = targetField.getScale();
        boolean targetIsNull = targetField.getIsNull();
        SingleDatatype tdt = tgtConv.getSingleDatatype(targetTable.getDBType(), targetType);
        DMLAffinityScore afScore = new DMLAffinityScore();
        afScore.setTypeClass(DMLAffinityScore.CLASS_FIELD);
        int confidence = 100;
        int strength = 10;
        if (opts.getMergeCharVarchar() && sourcePrec == targetPrec && sourceType.equals(targetType) == false && (sourceType.equals("char") || targetType.equals("char")) && (sourceType.startsWith("varchar") || targetType.startsWith("varchar"))) {
            DMLField aField;
            DMLTable aTable;
            if (sourceType.equals("char") && targetType.contains("varchar")) {
                sourceField.setDatatype(targetType);
                aTable = sourceTable;
                aField = sourceField;
            } else {
                targetField.setDatatype(sourceType);
                aTable = targetTable;
                aField = targetField;
            }
            printtext("\n WARNING: Datatype changed CHAR to VARCHAR according to opts, Field: " + aTable.getTableName() + "." + aField.getFieldName());
        }
        if (sourceName.equals(targetName)) strength += 40; else confidence += -20;
        if (sourceType.equals(targetType)) strength += 20; else if (sdt.getTypeClass().equals(tdt.getTypeClass())) strength += 10; else if (sdt.getGenericType().equals(tdt.getGenericType())) confidence += -5; else confidence += -15;
        if (sourcePrec == targetPrec) strength += 15; else if (sourcePrec < targetPrec) strength += 10; else if (sourcePrec > targetPrec) {
            confidence += -30;
            strength += -25;
        }
        if (sourceScale == targetScale) strength += 10; else if (sourceScale < targetScale) strength += 7; else confidence += -5;
        if (sourceIsNull == targetIsNull) strength += 3; else if (sourceIsNull == false && targetIsNull == true) strength += 1; else confidence += -5;
        afScore.setConfidence(confidence);
        afScore.setStrength(strength);
        return (afScore);
    }

    private DMLConstraint addAffinityConstraint(DMLField sourceField, DMLField targetField, DMLAffinityScore afScore) {
        DMLTable sourceTable = (DMLTable) sourceField.getParentTable();
        DMLTable targetTable = (DMLTable) targetField.getParentTable();
        DMLConstraint afConst = addAffinityConstraint(sourceTable, targetTable, sourceField.getFieldName(), targetField.getFieldName(), afScore);
        afConst.setColumnDatatypes(sourceField.getDatatype());
        afConst.setColumnPrecisions(Integer.toString(sourceField.getPrecision()));
        return (afConst);
    }

    private DMLConstraint addAffinityConstraint(DMLTable sourceTable, DMLTable targetTable, String sourceFields, String targetFields, DMLAffinityScore afScore) {
        DMLConstraint afConst = sourceTable.addAffinityConstraint("", sourceFields, targetTable.getTableName(), targetFields, afScore);
        afConst.setRefTablePtr(targetTable);
        afConst.setIsValid(false);
        afConst.setIsActive(true);
        afConst.setIsDisabled();
        afConst.setIsResolved(false);
        return (afConst);
    }

    private void connectForests(HashMap<Integer, String> masterTrees, DMLSchema curSchema) {
        if (masterTrees.size() <= 1) return;
        for (String curForest : masterTrees.values()) iterateAlternateForests(masterTrees, curSchema, curForest);
    }

    private void iterateAlternateForests(HashMap<Integer, String> masterTrees, DMLSchema curSchema, String curForest) {
        if (masterTrees == null || curSchema == null || curForest.length() == 0) return;
        sleepy(0);
        ArrayList<String> tablesList = StringUtils.stringSetToArray(curForest.split(","));
        for (String curTableName : tablesList) {
            for (String nextForest : masterTrees.values()) {
                sleepy(0);
                if (nextForest.equals(curForest)) continue;
                ArrayList<String> nextTablesList = StringUtils.stringSetToArray(nextForest.split(","));
                for (String nextTableName : nextTablesList) iterateTwoTables(curSchema, curTableName, nextTableName);
            }
        }
    }

    private void iterateTwoTables(DMLSchema curSchema, String curTableName, String nextTableName) {
        if (curSchema == null || curTableName.length() == 0 || nextTableName.length() == 0) return;
        sleepy(0);
        DMLTable curTable = curSchema.getTable(curTableName);
        DMLTable nextTable = curSchema.getTable(nextTableName);
        if (curTable == null || nextTable == null) return;
        curTable.compareTables(nextTable, true);
    }

    private void sleepy(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}
