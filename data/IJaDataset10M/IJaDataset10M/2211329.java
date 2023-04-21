package org.apache.exi.stringTables;

import java.util.*;
import org.apache.exi.core.EXIConstants;
import org.apache.exi.core.headerOptions.HeaderSizeLimitRules;

/**
 * This is a instance of a namespace's string tables...
 *      local and values and Global for this namespace * 
 *
 * <P>
 * TABLES OPERATE OFF OF LOCAL NAMES WITHOUT PREFIX...EVEN IF IT HAS A PREFIX
 * PREFIXES ARE ADDED AT DECODE FROM THE PREFIX TABLE...
 * OR A ARBRITARY PREFIX IS ADDED IF PREFIXES NOT RETAINED
 * <P>
 *
 * This class then delegates the managment of the individual tables to the
 * class StringTable.java
 * 
 * Represents the string data of the EXI standard. This consists of
 * several sub-tables, such as the string table for URIs, the 
 * local string table, the global string table, etc.
 * @author DMcG
 */
public class Tables {

    /**
     * The local name string table, holds local names of elements, attributes and
     * type declarations when schema is used 7.3.1 specification
     */
    private StringTable localNameStringTable = new StringTable();

    /**
     * Mapping of what level in the document this local name belongs
     */
    private HashMap<Integer, StringTable> localNameDefinedLevel = new HashMap<Integer, StringTable>();

    /**
     * Local value tables, one per qname (prefix + localname), so every element
     * and attribute has its own StringTable.
     */
    private HashMap<String, StringTable> localValueTables = new HashMap<String, StringTable>();

    /**
     * The global string table, holds attributes and xml content. All the data
     * strings should be entered in this table. (ONLY VALUES...NOT QNAMES)
     */
    private StringTable globalStringTable = new StringTable();

    /**
     * Returns the compact identifier for a global string. Returns
     * -1 if the string is not found.
     *
     * @param globalEntryString
     * @return
     */
    public int getGlobalCompactIdentifierForString(String globalEntryString) {
        return globalStringTable.getIdentifierForString(globalEntryString);
    }

    /**
     * Gets the global string at this index
     *
     * @param id the index
     * @return the string
     * @throws java.lang.Exception
     */
    public String getGlobalValueForID(int id) throws Exception {
        return globalStringTable.getStringForIdentifier(id);
    }

    private void addStringToGlobalStringTable(String stringAddGlobal) {
        globalStringTable.addString(stringAddGlobal);
    }

    /**
     * How many entries are in the global string table
     * 
     * @return count
     */
    public int getGlobalCount() {
        return globalStringTable.getSize();
    }

    /**
     * how many localnames are in this namespace (attributes, elements)
     *
     * @return count
     */
    public int getLocalNameCount() {
        return localNameStringTable.getSize();
    }

    public void prettyPrint() {
        System.out.println("QNAMES (for this URI)");
        localNameStringTable.prettyPrint();
        System.out.println("\nVALUES (for each QNAME of this URI)");
        int sizeQname = localNameStringTable.getSize();
        for (int i = 0; i < sizeQname; i++) {
            try {
                String qname = localNameStringTable.getStringForIdentifier(i);
                StringTable aTable = localValueTables.get(qname);
                System.out.println("   " + qname);
                aTable.prettyPrint();
            } catch (Exception e) {
                System.out.println("Error in pretty print values \n" + e);
            }
        }
        System.out.println("\nGLOBALS");
        globalStringTable.prettyPrint();
    }

    /** Given a qname and a string value, return the compact identifer for
     * that value. this operates on the local value tables for that qname.
     * @param qname qualified name
     * @param value the value we want the compact idientifer for
     * @return the compact identifier
     */
    public int getIdentifierForLocalValue(String localname, String value) {
        int rtnValue = EXIConstants.VALUE_NOT_YET_SET;
        try {
            rtnValue = localValueTables.get(localname).getIdentifierForString(value);
        } catch (Exception e) {
        }
        return rtnValue;
    }

    /**
     * Given a qname, gets its compact identifier
     *     (Elements names, Attributes names and type Declarations in Schema)
     * @param localname
     * @return
     */
    public int getIdentifierForLocalName(String localname) {
        try {
            return localNameStringTable.getIdentifierForString(localname);
        } catch (Exception e) {
        }
        return EXIConstants.VALUE_NOT_YET_SET;
    }

    /**
     * Delete the local values contained at this level in the document
     * 
     * @param level
     */
    public void deleteLocalValues(int level) {
        StringTable levelTable = localNameDefinedLevel.get(level);
        int levelSize = levelTable.getSize();
        for (int i = 0; i < levelSize; i++) {
            try {
                String thisLocalName = levelTable.getStringForIdentifier(i);
                StringTable localNameTable = localValueTables.get(thisLocalName);
                localNameTable.deleteEntries();
            } catch (Exception ex) {
                System.out.println("******ERROR TABLES deleteLocalValues");
            }
        }
    }

    /**
     * Adds a value to the localname table and gloval
     *
     * Get the string table associated with localname then add aString to the table as well as global
     *
     * If already exist,then do nothing
     *
     * @param localname
     * @param value
     */
    public int addStringToLocalValue(String localname, String value) {
        if (HeaderSizeLimitRules.VALUE_MAX_LENG.getSizeLimit() != EXIConstants.UNBOUNDED) {
            if (value.length() > HeaderSizeLimitRules.VALUE_MAX_LENG.getSizeLimit()) {
                return EXIConstants.STRING_VALUE_ENTER_TOO_MANY;
            }
        }
        StringTable aTable = localValueTables.get(localname);
        if (aTable == null) {
            return globalStringTable.addString(value);
        } else if (aTable.stringExists(value)) {
            return aTable.getIdentifierForString(value);
        }
        if (globalStringTable.stringExists(value)) {
            aTable.addString(value);
            return globalStringTable.getIdentifierForString(value);
        }
        if (HeaderSizeLimitRules.VALUE_PARTITION_CAP.getSizeLimit() != EXIConstants.UNBOUNDED) {
            if (aTable.getSize() > HeaderSizeLimitRules.VALUE_PARTITION_CAP.getSizeLimit()) {
                return EXIConstants.STRING_VALUE_ENTER_TOO_MANY;
            }
        }
        aTable.addString(value);
        return globalStringTable.addString(value);
    }

    /**
     * within this localname (attribute, element) how many values are contained
     * @param localname
     * @return how many values are in this Qname table
     */
    public int getValueCount(String localname) {
        StringTable aTable = localValueTables.get(localname);
        if (aTable == null) {
            return 0;
        } else {
            return aTable.getSize();
        }
    }

    /**
     * Gets the local value at this index for this local name
     * @param localname - local name to search
     * @param idx index within this local name
     * @return
     */
    public String getValueForLocal(String localname, int idx) throws Exception {
        StringTable aTable = localValueTables.get(localname);
        return aTable.getStringForIdentifier(idx);
    }

    public String getLocalNameForIndex(int idx) throws IdentifierNotFoundException {
        return localNameStringTable.getStringForIdentifier(idx);
    }

    /**
     * Adds a element or attribute name to the localname table and makes mirror entry
     * for the localValueTable
     *
     * if the qname already exist, then do nothing
     *
     * @param localname
     */
    public void addStringToLocalNameTable(String localname, int level) {
        if (!localNameStringTable.stringExists(localname)) {
            localNameStringTable.addString(localname);
            localValueTables.put(localname, new StringTable());
            StringTable levelTable = localNameDefinedLevel.get(level);
            if (levelTable == null) {
                localNameDefinedLevel.put(level, new StringTable());
                levelTable = localNameDefinedLevel.get(level);
            }
            levelTable.addString(localname);
        }
    }

    public void printTableLevels() {
        int size = localNameDefinedLevel.size();
        for (int i = 0; i < size; i++) {
            try {
                StringTable levelTable = localNameDefinedLevel.get(i);
                int sizeInner = levelTable.getSize();
                for (int j = 0; j < sizeInner; j++) {
                    System.out.println("level = " + i + "  " + levelTable.getStringForIdentifier(j));
                }
            } catch (Exception ex) {
                System.out.println("******ERROR TABLES PrintTableLevels");
            }
        }
    }
}
