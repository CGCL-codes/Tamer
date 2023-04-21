package org.tranche.annotation.export.export_map;

import java.sql.ResultSet;
import org.tranche.annotation.database.DBUtil;
import org.tranche.annotation.standard.Standard;
import org.tranche.mysql.DBObject;
import org.tranche.mysql.MySQLColumn;
import org.tranche.mysql.MySQLTable;

/**
 * <p>A container for Export_map. Store the mapping of Export and domain class objects.</p>
 * @author Shelly Chang - hlchang.shelly@gmail.com
 */
public class Export_map extends DBObject {

    /**
     * <p>SQL table that corresponds to this object.</p>
     */
    public static final MySQLTable TABLE = new MySQLTable(DBUtil.getAnnotations(), "export_map");

    /**
     * <p>SQL table column name that corresponds to the primary key for this object.</p>
     */
    public static final MySQLColumn COLUMN_PRIMARY_KEY = new MySQLColumn(TABLE, "export_mapID", true, MySQLColumn.DATA_TYPE_INT, true);

    /**
     * <p>Name of the column in the SQL table that corresponds to the source standard id.</p>
     */
    public static final MySQLColumn COLUMN_FROM_STANDARDID = new MySQLColumn(TABLE, "from_standardID", MySQLColumn.DATA_TYPE_INT, false, Standard.COLUMN_PRIMARY_KEY);

    /**
     * <p>Name of the column in the SQL table that corresponds to the destination standard ID.</p>
     */
    public static final MySQLColumn COLUMN_TO_STANDARDID = new MySQLColumn(TABLE, "to_standardID", MySQLColumn.DATA_TYPE_INT, false, Standard.COLUMN_PRIMARY_KEY);

    /**
     * <p>Name of the column in the SQL table that corresponds to the flag noting whether this export map is ready for use.</p>
     */
    public static final MySQLColumn COLUMN_EXPORT_MAP_FINALIZED = new MySQLColumn(TABLE, "finalized", MySQLColumn.DATA_TYPE_TINY_INT, false, "0");

    private int from_standardID, to_standardID;

    private boolean finalized;

    /**
     * <p>Create with a SQL ResultSet that is pointing to the row that represents the category.</p>
     * @param rs SQL ResultSet that is pointing to the row that represents this DBObject
     * @throws java.lang.Exception
     */
    public Export_map(ResultSet rs) throws Exception {
        super(rs.getInt(COLUMN_PRIMARY_KEY.getName()));
        from_standardID = rs.getInt(COLUMN_FROM_STANDARDID.getName());
        to_standardID = rs.getInt(COLUMN_TO_STANDARDID.getName());
        finalized = rs.getBoolean(COLUMN_EXPORT_MAP_FINALIZED.getName());
    }

    /**
     * @return the from_standardID
     */
    public int getFrom_standardID() {
        return from_standardID;
    }

    /**
     * @param from_standardID the from_standardID to set
     */
    public void setFrom_standardID(int from_standardID) {
        this.from_standardID = from_standardID;
    }

    /**
     * @return the to_standardID
     */
    public int getTo_standardID() {
        return to_standardID;
    }

    /**
     * @param to_standardID the to_standardID to set
     */
    public void setTo_standardID(int to_standardID) {
        this.to_standardID = to_standardID;
    }

    /**
     * @return the finalized
     */
    public boolean isFinalized() {
        return finalized;
    }

    /**
     * @param finalized the finalized to set
     */
    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
