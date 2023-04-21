package org.jdbcfacade;

/**
 * This is an implementation of DBLogger that does nothing. Use this if you
 * don't want to log anything.
 */
public class DoNothingDBLogger implements DBLogger {

    /**
   * @see DBLogger#logConnectError(Exception) s
   */
    public void logConnectError(Exception exception) {
    }

    /**
   * @see DBLogger#logGetConnection(String, String)
   */
    public void logGetConnection(String message, String correlationId) {
    }

    /**
   * @see DBLogger#logReturnConnection(String, String)
   */
    public void logReturnConnection(String message, String correlationId) {
    }

    /**
   * @see DBLogger#logSQLBegin(String, String, DBParamSet)
   */
    public long logSQLBegin(String statementType, String sql, DBParamSet parameterSet) {
        return 0L;
    }

    /**
   * @see DBLogger#logSQLEnd(String, long, String, int, long, DBParamSet)
   */
    public void logSQLEnd(String statementType, long correlationId, String sql, int numRecords, long timeInMilliseconds, DBParamSet parameterSet) {
    }

    /**
   * @see DBLogger#logSQLError(String, long, String, DBParamSet, Throwable)
   */
    public void logSQLError(String statementType, long correlationId, String sql, DBParamSet parameterSet, Throwable throwable) {
    }

    /**
   * @see DBLogger#logWarning(String)
   */
    public void logWarning(String message) {
    }
}
