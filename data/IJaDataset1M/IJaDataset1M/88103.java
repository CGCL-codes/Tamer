package net.wgen.op;

import net.wgen.op.logging.LoggingServices;
import net.wgen.op.logging.TraceKey;
import net.wgen.op.db.CallExecutor;
import net.wgen.op.db.MappedConnectionFactory;
import net.wgen.op.db.DatabaseUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Connection;

/**
 * Utils class that runs Ops and provides some tracing functionality.
 *
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: OpUtils.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class OpUtils {

    /**
     * Execute an Op and time it.
     *
     * @param op the DataOp to execute
     * @param callExecutor
     *
     * @throws net.wgen.op.OpException
     */
    public static void runOp(Op op, CallExecutor callExecutor) throws OpException {
        long t1 = System.currentTimeMillis();
        try {
            op._execute(callExecutor);
        } catch (Exception ex) {
            throw new OpException(op, "Error: " + ex.getMessage() + " Op: " + OpUtils.toString(op), ex);
        } finally {
            long t2 = System.currentTimeMillis();
            OpUtils.logOpTiming(t1, t2, op);
            op.getTraceKey().getExecInfo().incrementOpsExecuted();
        }
    }

    /**
     * Create a string representation of a DataOp.
     *
     * @param op the op to be toString'ed
     *
     * @return the string representation of a DataOp
     */
    public static String toString(Op op) {
        return (new OpToStringBuilder(op)).toString();
    }

    /**
     * Logs an OP-TIME message to the application log for the DataOp.
     *
     * @param t1 the start time
     * @param t2 the end time
     * @param op the op that was executed
     */
    public static void logOpTiming(long t1, long t2, Op op) {
        LoggingServices ls = LoggingServices.getInstance();
        Logger logger = Logger.getLogger(op.getClass());
        String amountOfWork;
        try {
            amountOfWork = op.getAmountOfWorkDone();
        } catch (Exception ex) {
            amountOfWork = "ERROR in " + op.getClass().getName() + ".getAmountOfWorkDone(): " + ex.toString();
        }
        String work = "work=" + amountOfWork;
        String info = toString(op);
        ls.logTiming(logger, LoggingServices.LOGTAG_OP_TIMING, t1, t2, op.getTraceKey(), work, info);
    }

    /**
     * Used by the ParamTreePathCallbackTransformer to generate a map of input fields and
     * values.
     *
     * @param op the op to be examined
     *
     * @return a map of field name to value for what look like the Op's inputs
     *
     * @throws IllegalAccessException
     *
     * @see net.wgen.op.db.dbunit.ParamTreePathCallbackTransformer#getOpParams(net.wgen.op.db.DatabaseCall)
     */
    public static Map getLoggableFieldsAndValues(Op op) throws IllegalAccessException {
        Field[] f = op.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(f, true);
        op.getLogger().debug(op.getTraceKey() + " evaluating " + f.length + " fields on " + op.getClass().getName());
        Map nameToValue = new LinkedHashMap();
        for (int i = 0; i < f.length; i++) {
            boolean isLoggable = looksLikeLoggableInput(op, f[i]);
            if (!isLoggable) {
                op.getLogger().debug(op.getTraceKey() + " skipping " + f[i].getName());
                continue;
            }
            try {
                nameToValue.put(f[i].getName(), f[i].get(op));
            } catch (IllegalAccessException ex) {
                op.getLogger().warn(op.getTraceKey() + " illegal access getting field " + f[i].getName() + " " + ex.getMessage());
                throw ex;
            }
        }
        return nameToValue;
    }

    /**
     * Called by the OpToStringBuilder to determine whether or not a field should be included in an
     * Op's string representation.
     *
     * So, in other words, the field will be accepted for inclusion in the String if:
     * <ul>
     * <li> avoid byte arrays and the OpExecutor field
     * <li> include _rootOp
     * <li> include field names that appear to have setters
     * </ul>
     *
     * @param op
     * @param field
     *
     * @return whether or not this field appears to be loggable
     */
    public static boolean looksLikeLoggableInput(Op op, Field field) {
        String name = field.getName();
        Class type = field.getType();
        if (type.getName().indexOf("[B") > -1) return false;
        if (type.getName().indexOf("OpExecutor") > -1) return false;
        if ("_rootOp".equals(name)) return true;
        if (name.startsWith("_")) name = name.substring(1);
        name = StringUtils.capitalize(name);
        String setName = "set" + name;
        Method m = null;
        try {
            m = op.getClass().getMethod(setName, new Class[] { type });
            boolean hasSetter = m != null;
            return hasSetter;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Convenience function to wrap single connections in a MappedConnectionFactory.
     *
     * @param connection
     * @param traceKey
     *
     * @return a connection factory for the single connection
     */
    protected static MappedConnectionFactory wrapSingleConnection(final Connection connection, TraceKey traceKey) {
        return new MappedConnectionFactory(traceKey) {

            public synchronized Connection getConnection(String dsName) {
                return connection;
            }

            public void closeOpenConnectionsQuietly() {
                DatabaseUtils.closeConnectionQuietly(connection);
            }
        };
    }
}
