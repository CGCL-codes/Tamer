package net.wgen.op.db.xform;

import org.apache.log4j.Logger;
import net.wgen.op.logging.TraceKey;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: OutputTransformCallback.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public interface OutputTransformCallback {

    /**
     * When processing a ResultSet, transform a key in a record.
     *
     * @param key the column name as read by the database
     * @param functionName the name of the function that was executed
     * @param paramIdx
     * @param traceKey
     * @param logger
     * @return the transformed key
     */
    Object transformResultSetKey(Object key, String functionName, int paramIdx, TraceKey traceKey, Logger logger);

    /**
     * When processing a ResultSet, transform a value in a record.
     *
     * @param val
     * @param columnName
     * @param functionName
     * @param paramIdx
     * @param traceKey
     * @param logger
     * @return the transformed record value
     */
    Object transformResultSetValue(Object val, String columnName, String functionName, int paramIdx, TraceKey traceKey, Logger logger);

    /**
     * When processing an output value, transform it.
     *
     * @param val
     * @param functionName
     * @param paramIdx
     * @param traceKey
     * @param logger
     * @return the transformed value
     */
    Object transformOutputValue(Object val, String functionName, int paramIdx, TraceKey traceKey, Logger logger);
}
