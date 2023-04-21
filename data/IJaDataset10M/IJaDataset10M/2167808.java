package net.lukemurphey.nsia.scan;

import java.sql.*;
import java.util.*;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.HashtableSerialization;
import net.lukemurphey.nsia.InputValidationException;
import net.lukemurphey.nsia.NoDatabaseConnectionException;
import net.lukemurphey.nsia.NotFoundException;
import net.lukemurphey.nsia.ScanCallback;
import net.lukemurphey.nsia.SiteGroupManagement;
import net.lukemurphey.nsia.Application.DatabaseAccessType;
import net.lukemurphey.nsia.SiteGroupManagement.SiteGroupDescriptor;

public abstract class ScanRule implements HashtableSerialization {

    protected Application appRes = null;

    protected long scanRuleId = -1;

    protected boolean scanDataObsolete;

    protected int scanFrequency;

    public static final String RULE_TYPE = "";

    protected ScanCallback callback = null;

    protected Timestamp created;

    protected Timestamp modified;

    protected long objectId;

    public static final int RULE_STATE_ACTIVE = 1;

    public static final int RULE_STATE_INACTIVE = 2;

    public static class ScanRuleLoadFailureException extends Exception {

        private static final long serialVersionUID = 1L;

        private Exception innerException = null;

        public ScanRuleLoadFailureException(String message, Exception innerException) {
            super(message);
            this.innerException = innerException;
        }

        public Exception getInnerException() {
            return innerException;
        }
    }

    public static class ScanResultLoadFailureException extends Exception {

        private static final long serialVersionUID = 1L;

        private Exception innerException = null;

        public ScanResultLoadFailureException(String message, Exception innerException) {
            super(message);
            this.innerException = innerException;
        }

        public ScanResultLoadFailureException(String message) {
            super(message);
        }

        public Exception getInnerException() {
            return innerException;
        }
    }

    public static class ScanFailureException extends Exception {

        private static final long serialVersionUID = 1L;

        private Exception innerException = null;

        public ScanFailureException(String message, Exception innerException) {
            super(message);
            this.innerException = innerException;
        }

        public Exception getInnerException() {
            return innerException;
        }
    }

    ScanRule(Application applicationResources) {
        appRes = applicationResources;
    }

    /**
	 * Causes the class to perform a scan per the given parameters.
	 * @return
	 * @throws Exception
	 */
    public abstract ScanResult doScan() throws ScanException;

    /**
	 * Set the callback mechanism for reporting the results of a scan.
	 * @param callback
	 */
    public void setCallback(ScanCallback callback) {
        this.callback = callback;
    }

    /**
	 * Get the scan callback object.
	 * @return
	 */
    protected ScanCallback getScanCallback() {
        return callback;
    }

    protected void logScanComplete(ScanResultCode resultCode, int deviations, String ruleType, String specimen, boolean sendToEventLog, boolean increaseScanCounts) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, null, this.scanRuleId, sendToEventLog, increaseScanCounts, true);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen, boolean sendToEventLog, boolean increaseScanCounts) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, null, this.scanRuleId, sendToEventLog, increaseScanCounts);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen, boolean sendToEventLog) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, null, this.scanRuleId, sendToEventLog);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen, String message, boolean sendToEventLog) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, message, this.scanRuleId, sendToEventLog);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen, String message, boolean sendToEventLog, boolean noteScanCompleted) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, message, this.scanRuleId, sendToEventLog, noteScanCompleted);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, null, this.scanRuleId);
        }
    }

    protected void logScanResult(ScanResultCode resultCode, int deviations, String ruleType, String specimen, String message) {
        if (callback != null) {
            callback.logScanResult(resultCode, deviations, ruleType, specimen, message, this.scanRuleId);
        }
    }

    /**
	 * This method causes the class to load the scan parameters from database. The class
	 * will load the scan parameters for the rule corresponding to the rule identifier.
	 * @param scanRuleId
	 * @return
	 */
    public abstract boolean loadFromDatabase(long scanRuleId) throws NotFoundException, NoDatabaseConnectionException, SQLException, ScanRuleLoadFailureException;

    /**
	 * The method below loads a scan result from the database corresponding to the scan result originally saved to disk.
	 * @precondition A database connection must be available and the data in the database must be valid (or an exception will be throw). Note that the result will be null if the result for the given ID could not be found.
	 * @postcondition A scan result will be returned or null if none could be found for the given identifier. 
	 * @param connection
	 * @param scanResultId
	 * @return
	 * @throws SQLException 
	 */
    public abstract ScanResult loadScanResult(long scanResultId) throws NotFoundException, NoDatabaseConnectionException, SQLException, ScanResultLoadFailureException;

    /**
	 * Get the rule ID associated with this rule. Returns -1 if no identifier has been set.
	 * @precondition None
	 * @postcondition The rule ID will be returned or -1 if no rule has been set
	 * @return
	 */
    public long getRuleId() {
        return scanRuleId;
    }

    /**
	 * Set the frequency (in seconds) that the rule should be scanned.
	 * @precondition The scan frequency must be greater than 15 seconds
	 * @postcondition The scan frequncy will be set to the value specified
	 * @return
	 */
    public void setScanFrequency(int scanFrequency) {
        if (scanFrequency < 15) throw new IllegalArgumentException("The scan frequency cannot be less than 15 seconds");
        this.scanFrequency = scanFrequency;
    }

    /**
	 * Retrieve the frequency (seconds) that the rule should be scanned.
	 * @precondition None
	 * @postcondition The scan frequncy will be returned
	 * @return
	 */
    public int getScanFrequency() {
        return scanFrequency;
    }

    /**
	 * Retrieves a hashtable description of the class.
	 * @return
	 */
    public Hashtable<String, Object> toHashtable() {
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put("RuleID", Long.valueOf(scanRuleId));
        hashtable.put("RuleType", RULE_TYPE);
        hashtable.put("RuleType", getRuleType());
        return hashtable;
    }

    /**
	 * Determine if the rule was updated but not scanned yet.
	 * @return
	 */
    public boolean isScanDataObsolete() {
        return scanDataObsolete;
    }

    /**
	 * Get the object ID associated with this rule. Returns -1 if no identifier has been set.
	 * @precondition None
	 * @postcondition The rule ID will be returned or -1 if no rule has been set
	 * @return
	 */
    public long getObjectId() {
        return objectId;
    }

    /**
	 * Determine if a rule ID is associated with this rule. Returns -1 if no identifier has been set.
	 * @precondition None
	 * @postcondition Returns a boolean if an identifier has been set
	 * @return
	 */
    public boolean isRuleIdSet() {
        if (scanRuleId == -1) return false; else return true;
    }

    /**
	 * Get the site group that is associated with the given rule.
	 * @param ruleID
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 */
    public static int getSiteGroupForRule(long ruleID) throws SQLException, NoDatabaseConnectionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Application.getApplication().getDatabaseConnection(DatabaseAccessType.SCANNER);
            statement = connection.prepareStatement("Select SiteGroupID from ScanRule where ScanRuleID = ?");
            statement.setLong(1, ruleID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("SiteGroupID");
            } else {
                return -1;
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
	 * Delete the rule associated with the given identifier.
	 * @precondition The scan rule identifier must be valid
	 * @postcondition The rule will be deleted
	 * @param scanRuleId
	 * @return
	 * @throws SQLException 
	 * @throws NoDatabaseConnectionException 
	 */
    public static boolean deleteRule(long scanRuleId) throws SQLException, NoDatabaseConnectionException {
        Application appRes = Application.getApplication();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = appRes.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            statement = connection.prepareStatement("Delete from ScanRule where ScanRuleID = ?");
            statement.setLong(1, scanRuleId);
            if (statement.executeUpdate() < 1) return false; else return true;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    /**
	 * Set flag in the the given rule to indicate that the scan data associated rule to obsolete (i.e. the current scan results are no valid and the rule msut be rescanned).  
	 * @param ruleId
	 * @return
	 * @throws NoDatabaseConnectionException
	 * @throws SQLException
	 */
    public static boolean setScanDataObsolete(long ruleId) throws NoDatabaseConnectionException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        SQLException initCause = null;
        try {
            connection = Application.getApplication().getDatabaseConnection(DatabaseAccessType.SCANNER);
            statement = connection.prepareStatement("Update ScanRule set ScanDataObsolete = 1 where ScanRuleID = ?");
            statement.setLong(1, ruleId);
            if (statement.executeUpdate() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            initCause = e;
            throw e;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                if (e.getCause() == null) {
                    e.initCause(initCause);
                    throw e;
                } else if (initCause != null) {
                    throw initCause;
                } else {
                    throw e;
                }
            }
        }
    }

    /**
	 * Get the the site group that owns the given rule.
	 * @param ruleId
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException
	 * @throws NotFoundException
	 * @throws InputValidationException 
	 */
    public static SiteGroupDescriptor getAssociatedSiteGroup(long ruleId) throws SQLException, NoDatabaseConnectionException, NotFoundException, InputValidationException {
        int siteGroupID = getAssociatedSiteGroupID(ruleId);
        SiteGroupManagement siteGroupMgmt = new SiteGroupManagement(Application.getApplication());
        return siteGroupMgmt.getGroupDescriptor(siteGroupID);
    }

    /**
	 * Get the identifier for the site group identifier that owns the given rule.
	 * @param ruleId
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException 
	 * @throws NotFoundException 
	 */
    public static int getAssociatedSiteGroupID(long ruleId) throws SQLException, NoDatabaseConnectionException, NotFoundException {
        Application appRes = Application.getApplication();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = appRes.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            statement = connection.prepareStatement("Select * from ScanRule where ScanRuleID = ?");
            statement.setLong(1, ruleId);
            result = statement.executeQuery();
            if (result.next()) return result.getInt("SiteGroupID"); else throw new NotFoundException("No rule could be found with the given rule identifier");
        } finally {
            if (statement != null) statement.close();
            if (result != null) result.close();
            if (connection != null) connection.close();
        }
    }

    public abstract String getRuleType();

    public abstract String getSpecimenDescription();

    public abstract void delete() throws SQLException, NoDatabaseConnectionException;

    /**
	 * Create a new scan rule and allocate a new rule ID. The new rule ID will be returned. 
	 * @param siteGroupId
	 * @param scanFrequency
	 * @param ruletype
	 * @return
	 * @throws SQLException
	 * @throws NoDatabaseConnectionException 
	 */
    protected synchronized long createRule(long siteGroupId, long scanFrequency, String ruletype, int ruleState) throws SQLException, NoDatabaseConnectionException {
        Application appRes = Application.getApplication();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = appRes.getDatabaseConnection(Application.DatabaseAccessType.SCANNER);
            statement = connection.prepareStatement("Insert into ScanRule (SiteGroupID, ScanFrequency, RuleType, State, Created, Modified) values (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, siteGroupId);
            statement.setLong(2, scanFrequency);
            statement.setString(3, ruletype);
            statement.setInt(4, ruleState);
            created = new Timestamp(new java.util.Date().getTime());
            statement.setTimestamp(5, created);
            statement.setTimestamp(6, created);
            if (statement.executeUpdate() < 1) return -1;
            results = statement.getGeneratedKeys();
            if (results.next()) return results.getLong(1); else return -1;
        } finally {
            if (statement != null) statement.close();
            if (results != null) results.close();
            if (connection != null) connection.close();
        }
    }
}
