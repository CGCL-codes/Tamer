package cz.startnet.sysstatcharts.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 * Helps with data processing
 *
 * @author fordfrog
 * @version $CVSHeader$
 */
public class DataHelper {

    /**
     * Creates a new instance of DataHelper.
     */
    private DataHelper() {
    }

    /**
     * Returns calendar object for string date given in format
     * 'YYYYMMDD'.
     *
     * @param date date string to be converted to calendar object
     *
     * @return calendar object created from date string
     */
    public static Calendar getCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6)));
        return calendar;
    }

    /**
     * Returns host id for the given host name. If the host doesn't
     * exist in database then it is created.
     *
     * @param host name of the host
     * @param con current database connection
     *
     * @return id of the host from database
     *
     * @throws RuntimeException Thrown if problem occured while reading id of
     *         the host from database.
     */
    public static int getHostId(String host, Connection con) {
        Statement stm = DatabaseHelper.createStatement(con);
        ResultSet rs = DatabaseHelper.executeQuery(stm, "SELECT id FROM host WHERE name = '" + host + "')");
        Integer hostId = null;
        try {
            if (rs.next()) {
                hostId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Problem occured while reading id of host: " + host);
        } finally {
            DatabaseHelper.closeResultSet(rs);
        }
        if (hostId == null) {
            hostId = DatabaseHelper.getNewId("host", con).intValue();
            DatabaseHelper.execute(stm, "INSERT INTO Host(Id, Name) VALUES(" + hostId + ", '" + host + "')");
        }
        return hostId.intValue();
    }

    /**
     * Returns the highest period in given table for given host.
     *
     * @param con current database connection
     * @param tableName name of the table where the max period should be
     *        searched
     * @param hostId id of the host that we search the max period for
     *
     * @return found highest period or null if there is no record for the host
     *
     * @throws RuntimeException Thrown if problem occured while reading max
     *         timestamp.
     */
    public static java.sql.Timestamp getHostMaxPeriod(Connection con, String tableName, int hostId) {
        Statement stm = DatabaseHelper.createStatement(con);
        ResultSet rs = DatabaseHelper.executeQuery(stm, "SELECT max(period) FROM " + tableName + " WHERE hostid = " + hostId);
        java.sql.Timestamp timestamp = null;
        try {
            if (rs.next()) {
                timestamp = rs.getTimestamp(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Problem occured while reading max timestamp");
        }
        DatabaseHelper.closeStatement(stm);
        DatabaseHelper.closeResultSet(rs);
        return timestamp;
    }

    /**
     * Parses double and fires runtime exception if value cannot be
     * parsed.
     *
     * @param string string representing the double
     *
     * @return double value parsed from the string
     *
     * @throws RuntimeException Thrown if cannot parse the number.
     */
    public static double parseDouble(String string) {
        Double value = null;
        try {
            value = Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot parse number: " + string);
        }
        return value.doubleValue();
    }

    /**
     * Parses long and fires runtime exception if value cannot be
     * parsed.
     *
     * @param string string representing the long
     *
     * @return long value parsed from the string
     */
    public static Long parseLong(String string) {
        Long value = null;
        value = Long.parseLong(string);
        return value;
    }

    /**
     * Updates date calendar with time value contained in time
     * variable.
     *
     * @param period calendar object that should be updated
     * @param time time string that should be set to the calendar object in
     *        format 'HH:MM:SS'
     *
     * @return calendar object with time set to time from 'time' variable
     */
    public static Calendar updatePeriod(Calendar period, String time) {
        String[] timeParts = time.split(":");
        period.set(Calendar.HOUR_OF_DAY, parseLong(timeParts[0].trim()).intValue());
        period.set(Calendar.MINUTE, parseLong(timeParts[1].trim()).intValue());
        period.set(Calendar.SECOND, parseLong(timeParts[2].trim()).intValue());
        return period;
    }
}
