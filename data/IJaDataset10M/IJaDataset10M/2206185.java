package hoplugins.transfers.dao;

import hoplugins.Commons;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB Access Manager for Plugin Settings
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public final class TransferSettingDAO {

    /** Name of the table in the HO database */
    private static final String TABLE_NAME = "TRANSFERS_SETTINGS";

    static {
        checkTable();
    }

    /**
     * Private default constuctor to prevent class instantiation.
     */
    private TransferSettingDAO() {
    }

    /**
     * Set the toggle to enable/disable transfer upload
     *
     * @param auto Boolean value
     */
    public static void setAutomatic(boolean auto) {
        setValue("AUTOMATIC", auto);
    }

    /**
     * Gets the settings for automatic upload
     *
     * @return true if enabled
     */
    public static boolean isAutomatic() {
        return getValue("AUTOMATIC", false);
    }

    /**
     * Set the Plugin Status to started
     */
    public static void setCalendarFix() {
        setValue("TRANSFERS-CALENDARFIX", true);
    }

    /**
     * Gets the plugin status
     *
     * @return true if already started
     */
    public static boolean isCalendarFix() {
        return getValue("TRANSFERS-CALENDARFIX", false);
    }

    /**
     * Set the Plugin Status to started
     */
    public static void setFixed() {
        setValue("FIXED", true);
    }

    /**
     * Gets the plugin status
     *
     * @return true if already started
     */
    public static boolean isFixed() {
        return getValue("FIXED", false);
    }

    /**
     * Set the Plugin Status to started
     */
    public static void setStarted() {
        setValue("STARTED", true);
    }

    /**
     * Gets the plugin status
     *
     * @return true if already started
     */
    public static boolean isStarted() {
        return getValue("STARTED", false);
    }

    /**
     * Set the value in the databse
     *
     * @param key The key
     * @param value the value
     */
    private static void setValue(String key, boolean value) {
        String val = (value) ? "1" : "0";
        String query = "update " + TABLE_NAME + " set VALUE = " + val + " where NAME = '" + key + "'";
        int count = Commons.getModel().getAdapter().executeUpdate(query);
        if (count == 0) {
            Commons.getModel().getAdapter().executeUpdate("insert into " + TABLE_NAME + " (NAME, VALUE) values ('" + key + "', " + val + ")");
        }
    }

    /**
     * Returns a value
     *
     * @param key the key to be returned
     * @param defaultValue to be used if not existing
     *
     * @return the value
     */
    private static boolean getValue(String key, boolean defaultValue) {
        String query = "select VALUE from " + TABLE_NAME + " where NAME='" + key + "'";
        ResultSet rs = Commons.getModel().getAdapter().executeQuery(query);
        try {
            rs.next();
            return rs.getBoolean("VALUE");
        } catch (SQLException e) {
            return defaultValue;
        }
    }

    /**
     * Check if the table exists, if not create it  with default values
     */
    private static void checkTable() {
        try {
            final ResultSet rs = Commons.getModel().getAdapter().executeQuery("select * from " + TABLE_NAME);
            rs.next();
        } catch (Exception e) {
            Commons.getModel().getAdapter().executeUpdate("CREATE TABLE " + TABLE_NAME + " (NAME VARCHAR(25),VALUE BOOLEAN)");
            Commons.getModel().getAdapter().executeUpdate("CREATE INDEX plstatus_id ON " + TABLE_NAME + " (NAME)");
        }
        try {
            final ResultSet rs = Commons.getModel().getAdapter().executeQuery("select NAME from " + TABLE_NAME);
            rs.next();
        } catch (Exception e) {
            Commons.getModel().getAdapter().executeUpdate("ALTER TABLE " + TABLE_NAME + " ADD COLUMN NAME varchar(20)");
            Commons.getModel().getAdapter().executeUpdate("UPDATE " + TABLE_NAME + " SET NAME=KEY");
            Commons.getModel().getAdapter().executeUpdate("DROP INDEX plstatus_id ON " + TABLE_NAME);
            Commons.getModel().getAdapter().executeUpdate("ALTER TABLE " + TABLE_NAME + " DROP COLUMN KEY");
            Commons.getModel().getAdapter().executeUpdate("CREATE INDEX plstatus_id ON " + TABLE_NAME + " (NAME)");
            Commons.getModel().getAdapter().executeUpdate("ALTER TABLE " + TABLE_NAME + " ADD COLUMN VALUE BOOLEAN");
            Commons.getModel().getAdapter().executeUpdate("UPDATE " + TABLE_NAME + " SET VALUE=STATUS");
            Commons.getModel().getAdapter().executeUpdate("ALTER TABLE " + TABLE_NAME + " DROP COLUMN STATUS");
        }
    }
}
