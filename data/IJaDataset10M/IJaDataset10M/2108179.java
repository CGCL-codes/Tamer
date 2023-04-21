package com.mchange.v2.sql;

import java.sql.*;
import com.mchange.v2.log.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.mchange.lang.ThrowableUtils;
import com.mchange.v2.lang.VersionUtils;

public final class SqlUtils {

    static final MLogger logger = MLog.getLogger(SqlUtils.class);

    static final DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");

    public static final String DRIVER_MANAGER_USER_PROPERTY = "user";

    public static final String DRIVER_MANAGER_PASSWORD_PROPERTY = "password";

    public static String escapeBadSqlPatternChars(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0, len = sb.length(); i < len; ++i) if (sb.charAt(i) == '\'') {
            sb.insert(i, '\'');
            ++len;
            i += 2;
        }
        return sb.toString();
    }

    public static synchronized String escapeAsTimestamp(Date date) {
        return "{ts '" + tsdf.format(date) + "'}";
    }

    public static SQLException toSQLException(Throwable t) {
        return toSQLException(null, t);
    }

    public static SQLException toSQLException(String msg, Throwable t) {
        return toSQLException(msg, null, t);
    }

    public static SQLException toSQLException(String msg, String sqlState, Throwable t) {
        if (t instanceof SQLException) {
            if (Debug.DEBUG && Debug.TRACE == Debug.TRACE_MAX && logger.isLoggable(MLevel.FINER)) {
                SQLException s = (SQLException) t;
                StringBuffer tmp = new StringBuffer(255);
                tmp.append("Attempted to convert SQLException to SQLException. Leaving it alone.");
                tmp.append(" [SQLState: ");
                tmp.append(s.getSQLState());
                tmp.append("; errorCode: ");
                tmp.append(s.getErrorCode());
                tmp.append(']');
                if (msg != null) tmp.append(" Ignoring suggested message: '" + msg + "'.");
                logger.log(MLevel.FINER, tmp.toString(), t);
                SQLException s2 = s;
                while ((s2 = s2.getNextException()) != null) logger.log(MLevel.FINER, "Nested SQLException or SQLWarning: ", s2);
            }
            return (SQLException) t;
        } else {
            if (Debug.DEBUG) {
                if (logger.isLoggable(MLevel.FINE)) logger.log(MLevel.FINE, "Converting Throwable to SQLException...", t);
            }
            if (msg == null) msg = "An SQLException was provoked by the following failure: " + t.toString();
            if (VersionUtils.isAtLeastJavaVersion14()) {
                SQLException out = new SQLException(msg);
                out.initCause(t);
                return out;
            } else return new SQLException(msg + System.getProperty("line.separator") + "[Cause: " + ThrowableUtils.extractStackTrace(t) + ']', sqlState);
        }
    }

    private SqlUtils() {
    }
}
