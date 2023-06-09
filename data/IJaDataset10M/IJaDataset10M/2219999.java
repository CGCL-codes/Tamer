package org.go.database.delegate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.go.Calendar;
import org.go.Trigger;
import org.go.Work;
import org.go.database.TriggerPersistenceDelegate;
import org.go.spi.ClassLoadHelper;
import org.slf4j.Logger;

/**
 * <p>
 * This is a driver delegate for the Oracle 10 and 11 database.
 * </p>
 * 
 * @see org.quartz.impl.jdbcjobstore.WebLogicDelegate
 * @see org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate
 * @author James House
 * @author Patrick Lightbody
 * @author Eric Mueller
 */
public class OracleDelegate extends StdJDBCDelegate {

    /**
	 * <p>
	 * Create new OrcaleDelegate instance.
	 * </p>
	 * 
	 * @param logger
	 *          the logger to use during execution
	 * @param tablePrefix
	 *          the prefix of all table names
	 */
    public OracleDelegate(Logger logger, String tablePrefix, String schedName, String instanceId, ClassLoadHelper classLoadHelper) {
        super(logger, tablePrefix, schedName, instanceId, classLoadHelper);
    }

    /**
	 * <p>
	 * Create new OrcaleDelegate instance.
	 * </p>
	 * 
	 * @param logger
	 *          the logger to use during execution
	 * @param tablePrefix
	 *          the prefix of all table names
	 * @param useProperties
	 *          use java.util.Properties for storage
	 */
    public OracleDelegate(Logger logger, String tablePrefix, String schedName, String instanceId, ClassLoadHelper classLoadHelper, Boolean useProperties) {
        super(logger, tablePrefix, schedName, instanceId, classLoadHelper, useProperties);
    }

    public static final String INSERT_ORACLE_JOB_DETAIL = "INSERT INTO " + TABLE_PREFIX_SUBST + TABLE_JOB_DETAILS + " (" + COL_SCHEDULER_NAME + ", " + COL_JOB_NAME + ", " + COL_JOB_GROUP + ", " + COL_DESCRIPTION + ", " + COL_JOB_CLASS + ", " + COL_IS_DURABLE + ", " + COL_IS_NONCONCURRENT + ", " + COL_IS_UPDATE_DATA + ", " + COL_REQUESTS_RECOVERY + ", " + COL_JOB_DATAMAP + ") " + " VALUES(" + SCHED_NAME_SUBST + ", ?, ?, ?, ?, ?, ?, ?, ?, EMPTY_BLOB())";

    public static final String UPDATE_ORACLE_JOB_DETAIL = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_JOB_DETAILS + " SET " + COL_DESCRIPTION + " = ?, " + COL_JOB_CLASS + " = ?, " + COL_IS_DURABLE + " = ?, " + COL_IS_NONCONCURRENT + " = ?, " + COL_IS_UPDATE_DATA + " = ?, " + COL_REQUESTS_RECOVERY + " = ?, " + COL_JOB_DATAMAP + " = EMPTY_BLOB() " + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_JOB_NAME + " = ? AND " + COL_JOB_GROUP + " = ?";

    public static final String UPDATE_ORACLE_JOB_DETAIL_BLOB = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_JOB_DETAILS + " SET " + COL_JOB_DATAMAP + " = ? " + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_JOB_NAME + " = ? AND " + COL_JOB_GROUP + " = ?";

    public static final String SELECT_ORACLE_JOB_DETAIL_BLOB = "SELECT " + COL_JOB_DATAMAP + " FROM " + TABLE_PREFIX_SUBST + TABLE_JOB_DETAILS + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_JOB_NAME + " = ? AND " + COL_JOB_GROUP + " = ? FOR UPDATE";

    public static final String UPDATE_ORACLE_TRIGGER = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " SET " + COL_JOB_NAME + " = ?, " + COL_JOB_GROUP + " = ?, " + COL_DESCRIPTION + " = ?, " + COL_NEXT_FIRE_TIME + " = ?, " + COL_PREV_FIRE_TIME + " = ?, " + COL_TRIGGER_STATE + " = ?, " + COL_TRIGGER_TYPE + " = ?, " + COL_START_TIME + " = ?, " + COL_END_TIME + " = ?, " + COL_CALENDAR_NAME + " = ?, " + COL_MISFIRE_INSTRUCTION + " = ?, " + COL_PRIORITY + " = ? WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_TRIGGER_NAME + " = ? AND " + COL_TRIGGER_GROUP + " = ?";

    public static final String SELECT_ORACLE_TRIGGER_JOB_DETAIL_BLOB = "SELECT " + COL_JOB_DATAMAP + " FROM " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_TRIGGER_NAME + " = ? AND " + COL_TRIGGER_GROUP + " = ? FOR UPDATE";

    public static final String UPDATE_ORACLE_TRIGGER_JOB_DETAIL_BLOB = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " SET " + COL_JOB_DATAMAP + " = ? " + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_TRIGGER_NAME + " = ? AND " + COL_TRIGGER_GROUP + " = ?";

    public static final String UPDATE_ORACLE_TRIGGER_JOB_DETAIL_EMPTY_BLOB = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " SET " + COL_JOB_DATAMAP + " = EMPTY_BLOB() " + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_TRIGGER_NAME + " = ? AND " + COL_TRIGGER_GROUP + " = ?";

    public static final String INSERT_ORACLE_CALENDAR = "INSERT INTO " + TABLE_PREFIX_SUBST + TABLE_CALENDARS + " (" + COL_SCHEDULER_NAME + ", " + COL_CALENDAR_NAME + ", " + COL_CALENDAR + ") " + " VALUES(" + SCHED_NAME_SUBST + ", ?, EMPTY_BLOB())";

    public static final String SELECT_ORACLE_CALENDAR_BLOB = "SELECT " + COL_CALENDAR + " FROM " + TABLE_PREFIX_SUBST + TABLE_CALENDARS + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_CALENDAR_NAME + " = ? FOR UPDATE";

    public static final String UPDATE_ORACLE_CALENDAR_BLOB = "UPDATE " + TABLE_PREFIX_SUBST + TABLE_CALENDARS + " SET " + COL_CALENDAR + " = ? " + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST + " AND " + COL_CALENDAR_NAME + " = ?";

    @Override
    protected Object getObjectFromBlob(ResultSet rs, String colName) throws ClassNotFoundException, IOException, SQLException {
        Object obj = null;
        InputStream binaryInput = rs.getBinaryStream(colName);
        if (binaryInput != null) {
            ObjectInputStream in = new ObjectInputStream(binaryInput);
            try {
                obj = in.readObject();
            } finally {
                in.close();
            }
        }
        return obj;
    }

    @Override
    public int insertJobDetail(Connection conn, Work job) throws IOException, SQLException {
        ByteArrayOutputStream baos = null;
        byte[] data = baos.toByteArray();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(INSERT_ORACLE_JOB_DETAIL));
            ps.setString(1, job.getKey().getName());
            ps.setString(2, job.getKey().getGroup());
            ps.setString(3, job.getDescription());
            setBoolean(ps, 6, job.isConcurrentExectionDisallowed());
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement(rtp(SELECT_ORACLE_JOB_DETAIL_BLOB));
            ps.setString(1, job.getKey().getName());
            ps.setString(2, job.getKey().getGroup());
            rs = ps.executeQuery();
            int res = 0;
            Blob dbBlob = null;
            if (rs.next()) {
                dbBlob = writeDataToBlob(rs, 1, data);
            } else {
                return res;
            }
            rs.close();
            ps.close();
            ps = conn.prepareStatement(rtp(UPDATE_ORACLE_JOB_DETAIL_BLOB));
            ps.setBlob(1, dbBlob);
            ps.setString(2, job.getKey().getName());
            ps.setString(3, job.getKey().getGroup());
            res = ps.executeUpdate();
            return res;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
    }

    @Override
    protected Object getJobDataFromBlob(ResultSet rs, String colName) throws ClassNotFoundException, IOException, SQLException {
        if (canUseProperties()) {
            InputStream binaryInput = rs.getBinaryStream(colName);
            return binaryInput;
        }
        return getObjectFromBlob(rs, colName);
    }

    @Override
    public int updateJobDetail(Connection conn, Work job) throws IOException, SQLException {
        ByteArrayOutputStream baos = null;
        byte[] data = baos.toByteArray();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(UPDATE_ORACLE_JOB_DETAIL));
            ps.setString(1, job.getDescription());
            setBoolean(ps, 4, job.isConcurrentExectionDisallowed());
            ps.setString(7, job.getKey().getName());
            ps.setString(8, job.getKey().getGroup());
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement(rtp(SELECT_ORACLE_JOB_DETAIL_BLOB));
            ps.setString(1, job.getKey().getName());
            ps.setString(2, job.getKey().getGroup());
            rs = ps.executeQuery();
            int res = 0;
            if (rs.next()) {
                Blob dbBlob = writeDataToBlob(rs, 1, data);
                ps2 = conn.prepareStatement(rtp(UPDATE_ORACLE_JOB_DETAIL_BLOB));
                ps2.setBlob(1, dbBlob);
                ps2.setString(2, job.getKey().getName());
                ps2.setString(3, job.getKey().getGroup());
                res = ps2.executeUpdate();
            }
            return res;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeStatement(ps2);
        }
    }

    @Override
    public int insertTrigger(Connection conn, Trigger trigger, String state, Work jobDetail) throws SQLException, IOException {
        byte[] data = null;
        if (trigger.getJobDataMap().size() > 0) {
            data = serializeJobData(trigger.getJobDataMap()).toByteArray();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        int insertResult = 0;
        try {
            ps = conn.prepareStatement(rtp(INSERT_TRIGGER));
            ps.setString(1, trigger.getKey().getName());
            ps.setString(2, trigger.getKey().getGroup());
            ps.setString(3, trigger.getJobKey().getName());
            ps.setString(4, trigger.getJobKey().getGroup());
            ps.setString(5, trigger.getDescription());
            ps.setBigDecimal(6, new BigDecimal(String.valueOf(trigger.getNextFireTime().getTime())));
            long prevFireTime = -1;
            if (trigger.getPreviousFireTime() != null) {
                prevFireTime = trigger.getPreviousFireTime().getTime();
            }
            ps.setBigDecimal(7, new BigDecimal(String.valueOf(prevFireTime)));
            ps.setString(8, state);
            TriggerPersistenceDelegate tDel = findTriggerPersistenceDelegate(trigger);
            String type = TTYPE_BLOB;
            if (tDel != null) type = tDel.getHandledTriggerTypeDiscriminator();
            ps.setString(9, type);
            ps.setBigDecimal(10, new BigDecimal(String.valueOf(trigger.getStartTime().getTime())));
            long endTime = 0;
            if (trigger.getEndTime() != null) {
                endTime = trigger.getEndTime().getTime();
            }
            ps.setBigDecimal(11, new BigDecimal(String.valueOf(endTime)));
            ps.setString(12, trigger.getCalendarName());
            ps.setInt(13, trigger.getMisfireInstruction());
            ps.setBinaryStream(14, null, 0);
            ps.setInt(15, trigger.getPriority());
            insertResult = ps.executeUpdate();
            if (data != null) {
                ps.close();
                ps = conn.prepareStatement(rtp(UPDATE_ORACLE_TRIGGER_JOB_DETAIL_EMPTY_BLOB));
                ps.setString(1, trigger.getKey().getName());
                ps.setString(2, trigger.getKey().getGroup());
                ps.executeUpdate();
                ps.close();
                ps = conn.prepareStatement(rtp(SELECT_ORACLE_TRIGGER_JOB_DETAIL_BLOB));
                ps.setString(1, trigger.getKey().getName());
                ps.setString(2, trigger.getKey().getGroup());
                rs = ps.executeQuery();
                Blob dbBlob = null;
                if (rs.next()) {
                    dbBlob = writeDataToBlob(rs, 1, data);
                } else {
                    return 0;
                }
                rs.close();
                ps.close();
                ps = conn.prepareStatement(rtp(UPDATE_ORACLE_TRIGGER_JOB_DETAIL_BLOB));
                ps.setBlob(1, dbBlob);
                ps.setString(2, trigger.getKey().getName());
                ps.setString(3, trigger.getKey().getGroup());
                ps.executeUpdate();
            }
            if (tDel == null) insertBlobTrigger(conn, trigger); else tDel.insertExtendedTriggerProperties(conn, trigger, state, jobDetail);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
        return insertResult;
    }

    @Override
    public int updateTrigger(Connection conn, Trigger trigger, String state, Work jobDetail) throws SQLException, IOException {
        boolean updateJobData = trigger.getJobDataMap().isDirty();
        byte[] data = null;
        if (updateJobData && trigger.getJobDataMap().size() > 0) {
            data = serializeJobData(trigger.getJobDataMap()).toByteArray();
        }
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int insertResult = 0;
        try {
            ps = conn.prepareStatement(rtp(UPDATE_ORACLE_TRIGGER));
            ps.setString(1, trigger.getJobKey().getName());
            ps.setString(2, trigger.getJobKey().getGroup());
            ps.setString(3, trigger.getDescription());
            long nextFireTime = -1;
            if (trigger.getNextFireTime() != null) {
                nextFireTime = trigger.getNextFireTime().getTime();
            }
            ps.setBigDecimal(4, new BigDecimal(String.valueOf(nextFireTime)));
            long prevFireTime = -1;
            if (trigger.getPreviousFireTime() != null) {
                prevFireTime = trigger.getPreviousFireTime().getTime();
            }
            ps.setBigDecimal(5, new BigDecimal(String.valueOf(prevFireTime)));
            ps.setString(6, state);
            TriggerPersistenceDelegate tDel = findTriggerPersistenceDelegate(trigger);
            String type = TTYPE_BLOB;
            if (tDel != null) type = tDel.getHandledTriggerTypeDiscriminator();
            ps.setString(7, type);
            ps.setBigDecimal(8, new BigDecimal(String.valueOf(trigger.getStartTime().getTime())));
            long endTime = 0;
            if (trigger.getEndTime() != null) {
                endTime = trigger.getEndTime().getTime();
            }
            ps.setBigDecimal(9, new BigDecimal(String.valueOf(endTime)));
            ps.setString(10, trigger.getCalendarName());
            ps.setInt(11, trigger.getMisfireInstruction());
            ps.setInt(12, trigger.getPriority());
            ps.setString(13, trigger.getKey().getName());
            ps.setString(14, trigger.getKey().getGroup());
            insertResult = ps.executeUpdate();
            if (updateJobData) {
                ps.close();
                ps = conn.prepareStatement(rtp(UPDATE_ORACLE_TRIGGER_JOB_DETAIL_EMPTY_BLOB));
                ps.setString(1, trigger.getKey().getName());
                ps.setString(2, trigger.getKey().getGroup());
                ps.executeUpdate();
                ps.close();
                ps = conn.prepareStatement(rtp(SELECT_ORACLE_TRIGGER_JOB_DETAIL_BLOB));
                ps.setString(1, trigger.getKey().getName());
                ps.setString(2, trigger.getKey().getGroup());
                rs = ps.executeQuery();
                if (rs.next()) {
                    Blob dbBlob = writeDataToBlob(rs, 1, data);
                    ps2 = conn.prepareStatement(rtp(UPDATE_ORACLE_TRIGGER_JOB_DETAIL_BLOB));
                    ps2.setBlob(1, dbBlob);
                    ps2.setString(2, trigger.getKey().getName());
                    ps2.setString(3, trigger.getKey().getGroup());
                    ps2.executeUpdate();
                }
            }
            if (tDel == null) updateBlobTrigger(conn, trigger); else tDel.updateExtendedTriggerProperties(conn, trigger, state, jobDetail);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeStatement(ps2);
        }
        return insertResult;
    }

    @Override
    public int insertCalendar(Connection conn, String calendarName, Calendar calendar) throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeObject(calendar);
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(INSERT_ORACLE_CALENDAR));
            ps.setString(1, calendarName);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement(rtp(SELECT_ORACLE_CALENDAR_BLOB));
            ps.setString(1, calendarName);
            rs = ps.executeQuery();
            if (rs.next()) {
                Blob dbBlob = writeDataToBlob(rs, 1, baos.toByteArray());
                ps2 = conn.prepareStatement(rtp(UPDATE_ORACLE_CALENDAR_BLOB));
                ps2.setBlob(1, dbBlob);
                ps2.setString(2, calendarName);
                return ps2.executeUpdate();
            }
            return 0;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeStatement(ps2);
        }
    }

    @Override
    public int updateCalendar(Connection conn, String calendarName, Calendar calendar) throws IOException, SQLException {
        ByteArrayOutputStream baos = serializeObject(calendar);
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(SELECT_ORACLE_CALENDAR_BLOB));
            ps.setString(1, calendarName);
            rs = ps.executeQuery();
            if (rs.next()) {
                Blob dbBlob = writeDataToBlob(rs, 1, baos.toByteArray());
                ps2 = conn.prepareStatement(rtp(UPDATE_ORACLE_CALENDAR_BLOB));
                ps2.setBlob(1, dbBlob);
                ps2.setString(2, calendarName);
                return ps2.executeUpdate();
            }
            return 0;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeStatement(ps2);
        }
    }

    @Override
    public int updateJobData(Connection conn, Work job) throws IOException, SQLException {
        ByteArrayOutputStream baos = null;
        byte[] data = baos.toByteArray();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(rtp(SELECT_ORACLE_JOB_DETAIL_BLOB));
            ps.setString(1, job.getKey().getName());
            ps.setString(2, job.getKey().getGroup());
            rs = ps.executeQuery();
            int res = 0;
            if (rs.next()) {
                Blob dbBlob = writeDataToBlob(rs, 1, data);
                ps2 = conn.prepareStatement(rtp(UPDATE_ORACLE_JOB_DETAIL_BLOB));
                ps2.setBlob(1, dbBlob);
                ps2.setString(2, job.getKey().getName());
                ps2.setString(3, job.getKey().getGroup());
                res = ps2.executeUpdate();
            }
            return res;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeStatement(ps2);
        }
    }

    @SuppressWarnings("deprecation")
    protected Blob writeDataToBlob(ResultSet rs, int column, byte[] data) throws SQLException {
        Blob blob = rs.getBlob(column);
        if (blob == null) {
            throw new SQLException("Driver's Blob representation is null!");
        }
        if (blob instanceof oracle.sql.BLOB) {
            ((oracle.sql.BLOB) blob).setBytes(1, data);
            ((oracle.sql.BLOB) blob).truncate(data.length);
            return blob;
        } else {
            throw new SQLException("Driver's Blob representation is of an unsupported type: " + blob.getClass().getName());
        }
    }
}
