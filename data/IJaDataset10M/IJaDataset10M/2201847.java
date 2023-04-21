package org.openscience.nmrshiftdb.om.map;

import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.db.map.DatabaseMap;
import org.apache.turbine.util.db.map.MapBuilder;
import org.apache.turbine.util.db.map.TableMap;

/**
 */
public class DBSpectrumTypeMapBuilder implements MapBuilder {

    /** the name of this class */
    public static final String CLASS_NAME = "org.openscience.nmrshiftdb.om.map.DBSpectrumTypeMapBuilder";

    /** item */
    public static String getTable() {
        return "SPECTRUM_TYPE";
    }

    /** SPECTRUM_TYPE.SPECTRUM_TYPE_ID */
    public static String getDBSpectrumType_SpectrumTypeId() {
        return getTable() + ".SPECTRUM_TYPE_ID";
    }

    /** SPECTRUM_TYPE.DIMENSIONALITY */
    public static String getDBSpectrumType_Dimensionality() {
        return getTable() + ".DIMENSIONALITY";
    }

    /** SPECTRUM_TYPE.NAME */
    public static String getDBSpectrumType_Name() {
        return getTable() + ".NAME";
    }

    /** SPECTRUM_TYPE.USER_ID */
    public static String getDBSpectrumType_UserId() {
        return getTable() + ".USER_ID";
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
        tMap.addPrimaryKey(getDBSpectrumType_SpectrumTypeId(), new Integer(0));
        tMap.addColumn(getDBSpectrumType_Dimensionality(), new Integer(0));
        tMap.addColumn(getDBSpectrumType_Name(), new String());
        tMap.addForeignKey(getDBSpectrumType_UserId(), new Integer(0), "TURBINE_USER", "USER_ID");
    }
}
