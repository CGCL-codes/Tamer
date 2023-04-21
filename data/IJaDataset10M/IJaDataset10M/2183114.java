package database;

import java.sql.SQLException;
import java.util.Vector;
import monitor.Attribute;

/** Process Attribute records in the database.
 * Insert, retrieve, and remove records in the Attribute table.
 * Each Attribute exists for one and only one Device Interface.
 * Therefore, an Interface must exist prior to adding a new Attribute.
 * It is the Attributes that are individually polled by the system.
 * 
 * @author  gswalters
 * @version 0.1
 */
public class DbAttribute extends DbAccess {

    /** Create a DbAttribute object that knows about the database connection.
	 * 
	 * @param conn existing DbConnection object providing the database connection
	 */
    public DbAttribute(DbConnection conn) {
        super(conn);
    }

    /** Add a new Attribute record to the database.
	 * 
	 * @param attrib Attribute objcet containing all the Attribute information
	 * @return Boolean indicating if the record add succeeded
	 */
    public boolean addAttribute(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("INSERT INTO " + A_TBL_NAME + " (deviceID,ifIndex,oid,minThreshold,maxThreshold,maxPollLimit,pollFrequency,removed,active)" + " VALUES('" + attrib.getDeviceID() + "','" + attrib.getIfIndex() + "'" + ",'" + attrib.getOid() + "','" + attrib.getMinThreshold() + "'" + ",'" + attrib.getMaxThreshold() + "','" + attrib.getMaxPollLimit() + "'" + ",'" + attrib.getPollFrequency() + "'" + ",'" + attrib.isRemoved() + "','" + attrib.isActive() + "');");
        System.out.println("\nDbAttribute.addAttribute:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Retrieve a specific Device Interface Attribute from the database.
	 * 
	 * @param devID long value of the ManagedDevice record
	 * @param ifIndex int value of the DeviceInterface Index
	 * @param oid int value of the second to last digit of the Attribute's OID
	 * @return Attribute - a null value indicates not found or an error occured
	 */
    public Attribute getAttribute(long devID, int ifIndex, int oid) {
        String sqlString = null;
        sqlString = new String("SELECT * FROM " + A_TBL_NAME + " WHERE deviceID=" + devID + " AND ifIndex=" + ifIndex + " AND oid =" + oid);
        System.out.println("\nDbAttribute.getAttribute:\n  sqlString = " + sqlString);
        return getRecord(sqlString);
    }

    /** Retrieve all Attributes from the database for a specific Device Interface.
	 * 
	 * @param devID long value of the ManagedDevice record
	 * @param ifIndex int value of the DeviceInterface Index
	 * @return Attribute array - a null value indicates not found or an error occured
	 */
    public Vector getAllAttributes(long devID, int ifIndex) {
        String sqlString = null;
        sqlString = new String("SELECT * FROM " + A_TBL_NAME + " WHERE deviceID=" + devID + " AND ifIndex=" + ifIndex);
        System.out.println("\nDbAttribute.getAllAttributes:\n  sqlString = " + sqlString);
        return getRecords(sqlString);
    }

    /** Retrieve all removed Attributes from the database for a specific Device Interface.
	 * 
	 * @param devID long value of the ManagedDevice record
	 * @param ifIndex int value of the DeviceInterface Index
	 * @return Attribute array - a null value indicates not found or an error occured
	 */
    public Vector getRemovedAttributes(long devID, int ifIndex) {
        String sqlString = null;
        sqlString = new String("SELECT * FROM " + A_TBL_NAME + " WHERE deviceID=" + devID + " AND ifIndex=" + ifIndex + " AND removed='true'");
        System.out.println("\nDbAttribute.getRemovedAttributes:\n  sqlString = " + sqlString);
        return getRecords(sqlString);
    }

    /** Retrieve all active Attributes from the database for a specific Device Interface.
	 * 
	 * @param devID long value of the ManagedDevice record
	 * @param ifIndex int value of the DeviceInterface Index
	 * @return Attribute array - a null value indicates not found or an error occured
	 */
    public Vector getActiveAttributes(long devID, int ifIndex) {
        String sqlString = null;
        sqlString = new String("SELECT * FROM " + A_TBL_NAME + " WHERE deviceID=" + devID + " AND ifIndex=" + ifIndex + " AND active='true'");
        System.out.println("\nDbAttribute.getActiveAttributes:\n  sqlString = " + sqlString);
        return getRecords(sqlString);
    }

    /** Retrieve all inactive Attributes from the database for a specific Device Interface.
	 * 
	 * @param devID long value of the ManagedDevice record
	 * @param ifIndex int value of the DeviceInterface Index
	 * @return Attribute array - a null value indicates not found or an error occured
	 */
    public Vector getInactiveAttributes(long devID, int ifIndex) {
        String sqlString = null;
        sqlString = new String("SELECT * FROM " + A_TBL_NAME + " WHERE deviceID=" + devID + " AND ifIndex=" + ifIndex + " AND active='false'");
        System.out.println("\nDbAttribute.getInactiveAttributes:\n  sqlString = " + sqlString);
        return getRecords(sqlString);
    }

    /** Update detail fields in an Attribute record in the database.
	 * This method does not allow the update of the deviceID, ifIndex, or OID.
	 * These fields are the primary key of the Attribute and cannot be changed.
	 * 
	 * @param attrib Attribute object containing the record to update in the database
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean updateAttribute(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET minThreshold='" + attrib.getMinThreshold() + "'" + ",maxThreshold='" + attrib.getMaxThreshold() + "'" + ",maxPollLimit='" + attrib.getMaxPollLimit() + "'" + ",pollFrequency='" + attrib.getPollFrequency() + "'" + ",removed='" + attrib.isRemoved() + "'" + ",active='" + attrib.isActive() + "'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.updateAttribute:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Update threshold fields in an Attribute record in the database.
	 * 
	 * @param attrib Attribute object containing the new thresholds
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setThresholds(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET minThreshold='" + attrib.getMinThreshold() + "'" + ",maxThreshold='" + attrib.getMaxThreshold() + "'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setThresholds:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Update poll fields in an Attribute record in the database.
	 * 
	 * @param attrib Attribute object containing the new thresholds
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setPollInfo(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET maxPollLimit='" + attrib.getMaxPollLimit() + "'" + ",pollFrequency='" + attrib.getPollFrequency() + "'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setPollInfo:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Set a Device Interface's specific Attribute removed.
	 * <P><em>A removed Attribute is also inactive.</em>
	 * 
	 * @param attrib Attribute object containing the deviceID, ifIndex, oid
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setRemoved(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET removed='true',active='false'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setRemoved:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Set a Device Interface's specific Attribute unremoved.
	 * 
	 * @param attrib Attribute object containing the deviceID, ifIndex, oid
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setUnremoved(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET removed='false'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setUnremoved:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Set a Device Interface's specific Attribute active.
	 * 
	 * @param attrib Attribute object containing the deviceID, ifIndex, oid
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setActive(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET active='true'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setActive:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Set a Device Interface's specific Attribute inactive.
	 * 
	 * @param attrib Attribute object containing the deviceID, ifIndex, oid
	 * @return Boolean indicating if the record update succeeded
	 */
    public boolean setInactive(Attribute attrib) {
        String sqlString = null;
        sqlString = new String("UPDATE " + A_TBL_NAME + " SET active='false'" + " WHERE deviceID=" + attrib.getDeviceID() + " AND ifIndex=" + attrib.getIfIndex() + " AND oid=" + attrib.getOid());
        System.out.println("\nDbAttribute.setInactive:\n  sqlString = " + sqlString);
        return executeUpdate(sqlString);
    }

    /** Retrieve a single Attribute record from the database
	 *
	 * @param query String containing the SQL SELECT statement
	 * @return Attribute - a null value indicates not found or an error occured
	 */
    private Attribute getRecord(String query) {
        Attribute attrib = null;
        if (!cleanedQuery) {
            queryCleanUp();
        }
        try {
            sqlStmt = dbConn.createStatement();
            sqlResults = sqlStmt.executeQuery(query);
            if (sqlResults.next()) {
                attrib = new Attribute(sqlResults.getLong(1), sqlResults.getInt(2), sqlResults.getInt(3), sqlResults.getInt(4), sqlResults.getInt(5), sqlResults.getInt(6), sqlResults.getInt(7), sqlResults.getBoolean(8), sqlResults.getBoolean(9));
            }
            sqlResults.close();
            sqlStmt.close();
        } catch (SQLException ex) {
            System.err.println("DbAttribute.getRecord:");
            System.err.println("  Failed to retrieve " + A_TBL_NAME + " record from the database.");
            System.err.println("\tSQL Statement: " + query);
            System.err.println("\tSQLException: " + ex.getMessage());
            System.err.println("\tSQLState: " + ex.getSQLState());
            System.err.println("\tVendorError: " + ex.getErrorCode());
            queryCleanUp();
        }
        return attrib;
    }

    /** Retrieve a multiple Attribute records from the database
	 *
	 * @param query String containing the SQL SELECT statement
	 * @return Attribute - a null value indicates not found or an error occured
	 */
    private Vector getRecords(String query) {
        Vector attribs = null;
        if (!cleanedQuery) {
            queryCleanUp();
        }
        try {
            sqlStmt = dbConn.createStatement();
            sqlResults = sqlStmt.executeQuery(query);
            if (sqlResults.next()) {
                sqlResults.last();
                attribs = new Vector(sqlResults.getRow());
                sqlResults.first();
                int i = 0;
                do {
                    attribs.add(i, new Attribute(sqlResults.getLong(1), sqlResults.getInt(2), sqlResults.getInt(3), sqlResults.getInt(4), sqlResults.getInt(5), sqlResults.getInt(6), sqlResults.getInt(7), sqlResults.getBoolean(8), sqlResults.getBoolean(9)));
                    ++i;
                } while (sqlResults.next());
            }
            sqlResults.close();
            sqlStmt.close();
        } catch (SQLException ex) {
            System.err.println("DbAttribute.getRecords:");
            System.err.println("  Failed to retrieve " + A_TBL_NAME + " records from the database.");
            System.err.println("\tSQL Statement: " + query);
            System.err.println("\tSQLException: " + ex.getMessage());
            System.err.println("\tSQLState: " + ex.getSQLState());
            System.err.println("\tVendorError: " + ex.getErrorCode());
            queryCleanUp();
        }
        return attribs;
    }
}
