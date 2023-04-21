package com.butterfill.opb.webdemo.data;

import com.butterfill.opb.*;
import com.butterfill.opb.data.*;
import com.butterfill.opb.groups.*;
import com.butterfill.opb.timing.*;
import com.butterfill.opb.util.*;
import com.butterfill.opb.plsql.util.*;
import java.util.logging.*;

/**
 * File created from the PL/SQL package specification
 * logger_flag.
 */
public class LoggerFlagImpl implements LoggerFlag {

    /**
     * The name of this class.
     */
    public static final String CLASS_NAME = LoggerFlagImpl.class.getName();

    /**
     * The logger of this class.
     */
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Creates a new instance of LoggerFlagImpl.
     */
    public LoggerFlagImpl() {
        logger.entering(CLASS_NAME, "LoggerFlagImpl()");
    }

    /**
     * Returns a String representation of this instance and it's values.
     * @return A String representation of this LoggerFlagImpl.
     */
    @Override
    public String toString() {
        return com.butterfill.opb.util.OpbToStringHelper.toString(this);
    }

    /**
     * The group mananger map to be used by this LoggerFlagImpl.
     */
    private OpbGroupManagerMap opbGroupManagerMap;

    /**
     * Sets the group manager map to be used by this LoggerFlagImpl.
     * @param map The group manager map to use.
     */
    public void setGroupManagerMap(final OpbGroupManagerMap map) {
        this.opbGroupManagerMap = map;
    }

    /**
     * Returns the group manager map used by this LoggerFlagImpl.
     * @return The group manager map used by this instance.
     */
    public OpbGroupManagerMap getGroupManagerMap() {
        return opbGroupManagerMap;
    }

    /**
     * The event timer provider to be used by this LoggerFlagImpl.
     */
    private OpbEventTimerProvider opbEventTimerProvider;

    /**
     * Sets the event timer to be used by this LoggerFlagImpl.
     * @param provider The event timer to use.
     */
    public void setOpbEventTimerProvider(final OpbEventTimerProvider provider) {
        this.opbEventTimerProvider = provider;
    }

    /**
     * The data object source to be used by this LoggerFlagImpl.
     */
    private OpbDataObjectSource opbDataObjectSource;

    /**
     * Sets the data object source to be used by this LoggerFlagImpl.
     * @param source The data object source to use.
     */
    public void setOpbDataObjectSource(final OpbDataObjectSource source) {
        this.opbDataObjectSource = source;
    }

    /**
     * The connection provider to be used by this LoggerFlagImpl.
     */
    private OpbConnectionProvider opbConnectionProvider;

    /**
     * Sets the connection provider to be used by this LoggerFlagImpl.
     * @param provider The connection provider to use.
     */
    public void setOpbConnectionProvider(final OpbConnectionProvider provider) {
        this.opbConnectionProvider = provider;
    }

    /**
     * Resets all field values to their initial values.
     */
    public void opbClearState() {
        final String methodName = "opbClearState()";
        logger.entering(CLASS_NAME, methodName);
        opbId = null;
        rowId = null;
        logLevel = null;
        logLevelDataSourceValue = null;
        logUser = null;
        logUserDataSourceValue = null;
    }

    /**
     * The id of this LoggerFlagImpl. 
     * Set by opbLoad(ResultSet).
     */
    private OpbId opbId;

    /**
     * Returns the id of this LoggerFlagImpl.
     * This ID is created using the field(s):
     * <ul>
     * <li>rowId</li>
     * </ul>
     * This method will return null if opbLoad(ResultSet) has not been called.
     * 
     * @return The ID of this instance.
     */
    public OpbId getOpbId() {
        return opbId;
    }

    /**
     * Resets all field values to their initial values by calling 
     * opbClearState() and then sets all field values using values taken from 
     * the current row in resultSet.
     * <br/>
     * This method will look for the following fields in resultSet;
     * <ul>
     * <li>row_id is <em>mandatory</em></li>
     * <li>log_level is <em>mandatory</em></li>
     * <li>log_user is <em>mandatory</em></li>
     * </ul>
     * 
     * @param resultSet The result set from which this instance should be loaded.
     * @throws OpbDataAccessException If we fail to load this instance.
     */
    public void opbLoad(final java.sql.ResultSet resultSet) throws OpbDataAccessException {
        final String methodName = "opbLoad(ResultSet)";
        logger.entering(CLASS_NAME, methodName);
        opbClearState();
        OpbAssert.notNull(logger, CLASS_NAME, methodName, "resultSet", resultSet);
        try {
            rowId = OpbSqlHelper.getValue(rowId, resultSet, "row_id", true);
            logLevel = OpbSqlHelper.getValue(logLevel, resultSet, "log_level", true);
            logLevelDataSourceValue = logLevel;
            logUser = OpbSqlHelper.getValue(logUser, resultSet, "log_user", true);
            logUserDataSourceValue = logUser;
            opbId = new OpbId(rowId);
        } catch (Exception ex) {
            OpbExceptionHelper.throwException(new OpbDataAccessException("failed to load", ex), logger, CLASS_NAME, methodName);
        } finally {
            logger.exiting(CLASS_NAME, methodName);
        }
    }

    /**
     * Derived from an opb-package field.
     */
    private String rowId = null;

    /**
     * Returns the value of rowId.
     * @return The value of rowId.
     */
    public String getRowId() {
        return rowId;
    }

    /**
     * Sets the value of rowId.
     * @param a The new value for rowId.
     */
    private void setRowId(final String a) {
        this.rowId = a;
    }

    /**
     * Derived from an opb-package field.
     */
    private java.math.BigDecimal logLevel = null;

    /**
     * Returns the value of logLevel.
     * @return The value of logLevel.
     */
    public java.math.BigDecimal getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the value of logLevel.
     * @param a The new value for logLevel.
     */
    public void setLogLevel(final java.math.BigDecimal a) {
        this.logLevel = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private java.math.BigDecimal logLevelDataSourceValue = null;

    /**
     * Returns the value of logLevelDataSourceValue.
     * This is the last value returned by the data source for logLevel.
     * @return The value of logLevelDataSourceValue.
     */
    public java.math.BigDecimal getLogLevelDataSourceValue() {
        return logLevelDataSourceValue;
    }

    /**
     * Returns true if the value of logLevel 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if logLevel has changed since it was loaded.
     */
    public boolean getLogLevelChanged() {
        return !OpbComparisonHelper.isEqual(logLevel, logLevelDataSourceValue);
    }

    /**
     * Derived from an opb-package field.
     */
    private String logUser = null;

    /**
     * Returns the value of logUser.
     * @return The value of logUser.
     */
    public String getLogUser() {
        return logUser;
    }

    /**
     * Sets the value of logUser.
     * @param a The new value for logUser.
     */
    public void setLogUser(final String a) {
        this.logUser = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String logUserDataSourceValue = null;

    /**
     * Returns the value of logUserDataSourceValue.
     * This is the last value returned by the data source for logUser.
     * @return The value of logUserDataSourceValue.
     */
    public String getLogUserDataSourceValue() {
        return logUserDataSourceValue;
    }

    /**
     * Returns true if the value of logUser 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if logUser has changed since it was loaded.
     */
    public boolean getLogUserChanged() {
        return !OpbComparisonHelper.isEqual(logUser, logUserDataSourceValue);
    }

    /**
     * 
     * Calls the database function del.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String del(final String pRowId, final java.math.BigDecimal pOldLogLevel, final String pOldLogUser) throws OpbDataAccessException {
        final String methodName = "del(String, java.math.BigDecimal, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := logger_flag.del(?, ?, ?); END;", "DbCall:logger_flag#del(VARCHAR2, NUMBER, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(2, java.sql.Types.VARCHAR, pRowId);
        opbCallHelper.setObject(3, java.sql.Types.DECIMAL, pOldLogLevel);
        opbCallHelper.setObject(4, java.sql.Types.VARCHAR, pOldLogUser);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        opbDataObjectSource.clearCached(LoggerFlag.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls del using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pOldLogLevel is mapped to logLevelDataSourceValue</li>
     * <li>pOldLogUser is mapped to logUserDataSourceValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String del() throws OpbDataAccessException {
        final String methodName = "del()";
        logger.entering(CLASS_NAME, methodName);
        String result = del(getRowId(), getLogLevelDataSourceValue(), getLogUserDataSourceValue());
        return result;
    }

    /**
     * 
     * Calls the database function ins.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String ins(final OpbValueWrapper<String> pRowId, final java.math.BigDecimal pLogLevel, final String pLogUser) throws OpbDataAccessException {
        final String methodName = "ins(OpbValueWrapper, java.math.BigDecimal, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbAssert.notNull(logger, CLASS_NAME, methodName, "pRowId", pRowId);
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := logger_flag.ins(?, ?, ?); END;", "DbCall:logger_flag#ins(VARCHAR2, NUMBER, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.registerOutParameter(2, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(3, java.sql.Types.DECIMAL, pLogLevel);
        opbCallHelper.setObject(4, java.sql.Types.VARCHAR, pLogUser);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        pRowId.setValue(opbCallHelper.get(String.class, 2));
        opbDataObjectSource.invalidateCached(LoggerFlag.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls ins using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pLogLevel is mapped to logLevel</li>
     * <li>pLogUser is mapped to logUser</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String ins() throws OpbDataAccessException {
        final String methodName = "ins()";
        logger.entering(CLASS_NAME, methodName);
        OpbValueWrapper<String> pRowIdValueWrapper = new OpbValueWrapperImpl<String>();
        String result = ins(pRowIdValueWrapper, getLogLevel(), getLogUser());
        setRowId(pRowIdValueWrapper.getValue());
        return result;
    }

    /**
     * 
     * Calls the database function upd.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String upd(final String pRowId, final java.math.BigDecimal pOldLogLevel, final java.math.BigDecimal pLogLevel, final String pOldLogUser, final String pLogUser) throws OpbDataAccessException {
        final String methodName = "upd(String, java.math.BigDecimal, java.math.BigDecimal, String, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := logger_flag.upd(?, ?, ?, ?, ?); END;", "DbCall:logger_flag#upd(VARCHAR2, NUMBER, NUMBER, VARCHAR2, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(2, java.sql.Types.VARCHAR, pRowId);
        opbCallHelper.setObject(3, java.sql.Types.DECIMAL, pOldLogLevel);
        opbCallHelper.setObject(4, java.sql.Types.DECIMAL, pLogLevel);
        opbCallHelper.setObject(5, java.sql.Types.VARCHAR, pOldLogUser);
        opbCallHelper.setObject(6, java.sql.Types.VARCHAR, pLogUser);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        opbDataObjectSource.invalidateCached(LoggerFlag.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls upd using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pOldLogLevel is mapped to logLevelDataSourceValue</li>
     * <li>pLogLevel is mapped to logLevel</li>
     * <li>pOldLogUser is mapped to logUserDataSourceValue</li>
     * <li>pLogUser is mapped to logUser</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String upd() throws OpbDataAccessException {
        final String methodName = "upd()";
        logger.entering(CLASS_NAME, methodName);
        String result = upd(getRowId(), getLogLevelDataSourceValue(), getLogLevel(), getLogUserDataSourceValue(), getLogUser());
        return result;
    }
}
