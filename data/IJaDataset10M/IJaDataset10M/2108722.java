package ftm;

import dtm.*;
import sao.DML.*;
import opt.*;

public class FromToTableMatch {

    private FromToObject topFTO;

    private FromToFieldMatch ftField;

    private DataTypeManager dtc;

    private String sourceClass;

    private String targetClass;

    private Options opts;

    /**
	 * 
	 */
    public FromToTableMatch(FromToObject topFromTo, DataTypeManager DBTypeContainer) {
        initialize(topFromTo, DBTypeContainer);
    }

    private void initialize(FromToObject topFromTo, DataTypeManager DBContainer) {
        topFTO = topFromTo;
        ftField = null;
        dtc = DBContainer;
        setModelClasses("", "");
        setOptions(null);
    }

    public void setModelClasses(String srcClass, String tgtClass) {
        sourceClass = srcClass;
        targetClass = tgtClass;
    }

    public void setOptions(Options globalOpts) {
        opts = globalOpts;
    }

    /**
	 * Execute the full table match, but only if the class routines
	 * allow matching to happen
	 * @param srcTable
	 * @param tgtTable
	 * @return
	 */
    public FromToObject executeTableMatch(DMLTable srcTable, DMLTable tgtTable) {
        if (!matchTables(srcTable, tgtTable)) return (null);
        FromToObject tableFTO = new FromToObject("FROM-TO-TABLE", srcTable, tgtTable);
        if (ftField == null) ftField = new FromToFieldMatch(dtc);
        for (DMLField srcField : srcTable.getFieldObjects()) for (DMLField tgtField : tgtTable.getFieldObjects()) {
            FromToObject fieldFTO = ftField.executeFieldMatch(srcField, tgtField);
            if (fieldFTO != null) tableFTO.addChild(fieldFTO);
        }
        if (tableFTO.hasChildren() == false) {
            tableFTO.dispose();
            tableFTO = null;
        } else {
            topFTO.addChild(tableFTO);
        }
        return (tableFTO);
    }

    private boolean matchTables(DMLTable srcTable, DMLTable tgtTable) {
        boolean result = false;
        if (sourceClass.equals(DMLComplete.NRM_CLASS) && targetClass.equals(DMLComplete.DV_CLASS)) return (matchSourceToVault(srcTable, tgtTable));
        return (result);
    }

    /**
	 * Handle naming conventions from source to vault,
	 * add prefixes/suffixes in accordance with naming conventions
	 * so we can SEE if this table really SHOULD map to the target.
	 * @param srcTable
	 * @param tgtTable
	 * @return
	 */
    private boolean matchSourceToVault(DMLTable srcTable, DMLTable tgtTable) {
        boolean result = false;
        String srcName = srcTable.getTableName();
        String tgtName = tgtTable.getTableName();
        String tgtAbbr = "";
        boolean isPrefixed = opts.getDVIsPrefixed();
        if (tgtTable.getTableType().equals(DMLTable.HUB_CLASS)) return (true); else if (tgtTable.getTableType().equals(DMLTable.SAT_CLASS)) tgtAbbr = opts.getDVSatName(); else if (tgtTable.getTableType().equals(DMLTable.LNK_CLASS)) tgtAbbr = opts.getDVLnkName(); else if (tgtTable.getTableType().equals(DMLTable.HLNK_CLASS)) tgtAbbr = opts.getDVLnkHierName();
        tgtName = removePrefixSuffix(tgtName, tgtAbbr, isPrefixed);
        if (tgtName.equals(srcName)) result = true;
        return (result);
    }

    /**
	 * purpose is to remove the prefix or suffix from the table name, and
	 * return the "cleaned" name
	 * 
	 * @param tgtName
	 * @param tgtAbbr
	 * @param isPrefixed
	 * @return
	 */
    private String removePrefixSuffix(String tgtName, String tgtAbbr, boolean isPrefixed) {
        if (isPrefixed) if (tgtName.startsWith(tgtAbbr)) tgtName = tgtName.replaceFirst(tgtAbbr + "_", ""); else if (tgtName.endsWith(tgtAbbr)) tgtName = tgtName.replaceFirst("_" + tgtAbbr, "");
        return (tgtName);
    }
}
