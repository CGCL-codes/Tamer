package org.openscience.nmrshiftdb.om.map;

import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.db.map.DatabaseMap;
import org.apache.turbine.util.db.map.MapBuilder;
import org.apache.turbine.util.db.map.TableMap;

/**
 */
public class DBKeywordMapBuilder implements MapBuilder {

    /** the name of this class */
    public static final String CLASS_NAME = "org.openscience.nmrshiftdb.om.map.DBKeywordMapBuilder";

    /** item */
    public static String getTable() {
        return "KEYWORD";
    }

    /** KEYWORD.KEYWORD_ID */
    public static String getDBKeyword_KeywordId() {
        return getTable() + ".KEYWORD_ID";
    }

    /** KEYWORD.KEYWORD */
    public static String getDBKeyword_Keyword() {
        return getTable() + ".KEYWORD";
    }

    /** KEYWORD.KEYWORD_SOUNDEX */
    public static String getDBKeyword_KeywordSoundex() {
        return getTable() + ".KEYWORD_SOUNDEX";
    }

    /**  the database map  */
    private DatabaseMap dbMap = null;

    /**
        tells us if this DatabaseMapBuilder is built so that we don't have
        to re-build it every time
    */
    public boolean isBuilt() {
        if (dbMap != null) return true;
        return false;
    }

    /**  gets the databasemap this map builder built.  */
    public DatabaseMap getDatabaseMap() {
        return this.dbMap;
    }

    /** the doBuild() method builds the DatabaseMap */
    public void doBuild() throws Exception {
        dbMap = TurbineDB.getDatabaseMap("default");
        dbMap.addTable(getTable());
        TableMap tMap = dbMap.getTable(getTable());
        tMap.setPrimaryKeyMethod(TableMap.IDBROKERTABLE);
        tMap.addPrimaryKey(getDBKeyword_KeywordId(), new Integer(0));
        tMap.addColumn(getDBKeyword_Keyword(), new String());
        tMap.addColumn(getDBKeyword_KeywordSoundex(), new String());
    }
}
