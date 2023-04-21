package org.jaffa.transaction.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jaffa.modules.messaging.domain.BusinessEventLogMeta;
import org.jaffa.transaction.domain.Transaction;
import org.jaffa.transaction.domain.TransactionField;
import org.jaffa.transaction.services.configdomain.Param;
import org.jaffa.transaction.services.configdomain.TransactionInfo;

/** A helper class to set/unset the Log4J context based on the configuration of a Transaction.
 */
public class LoggingService {

    private static final Logger log = Logger.getLogger(LoggingService.class);

    /** The default CORRELATION_TYPE to be added to the MDC. */
    public static final String DEFAULT_CORRELATION_TYPE = "TRANSACTION";

    /** A thread variable to hold a stack of loggingContexts. Each loggingContext is a Map of key/value pairs. */
    private static ThreadLocal<Stack<Map<String, Object>>> t_loggingContext = new ThreadLocal<Stack<Map<String, Object>>>();

    /**
     * Adds the appropriate elements to the Message Driven Context (MDC) of Log4J, as specified in the input transaction config.
     * @param payload Any serializable object.
     * @param transactionInfo the corresponding TransactionInfo object, as specified in the configuration file.
     */
    public static void setLoggingContext(Object payload, TransactionInfo transactionInfo, Transaction transaction) {
        Map<String, Object> currentLoggingContext = MDC.getContext() != null ? new HashMap<String, Object>(MDC.getContext()) : new HashMap<String, Object>();
        Stack<Map<String, Object>> stack = t_loggingContext.get();
        if (stack == null) {
            stack = new Stack<Map<String, Object>>();
            t_loggingContext.set(stack);
        }
        stack.push(currentLoggingContext);
        if (transactionInfo == null && payload != null) transactionInfo = ConfigurationService.getInstance().getTransactionInfo(payload);
        if (transactionInfo != null && transactionInfo.getHeader() != null && transactionInfo.getHeader().getParam() != null) {
            for (Param param : transactionInfo.getHeader().getParam()) {
                if (param.getLoggingName() != null) {
                    String key = param.getLoggingName().value();
                    try {
                        Object value = TransactionEngine.obtainParamValue(param, payload);
                        if (value != null) MDC.put(key, value); else MDC.remove(key);
                    } catch (Exception e) {
                        if (log.isDebugEnabled()) log.debug("Error in obtaining value for the LoggingName " + key, e);
                    }
                }
            }
        }
        if (MDC.get(BusinessEventLogMeta.CORRELATION_TYPE) == null && transaction != null && transaction.getId() != null) {
            MDC.put(BusinessEventLogMeta.CORRELATION_TYPE, DEFAULT_CORRELATION_TYPE);
            MDC.put(BusinessEventLogMeta.CORRELATION_KEY1, transaction.getId());
            if (transaction.getType() != null) MDC.put(BusinessEventLogMeta.CORRELATION_KEY2, transaction.getType());
            if (transaction.getSubType() != null) MDC.put(BusinessEventLogMeta.CORRELATION_KEY3, transaction.getSubType());
            if (transaction.getCreatedBy() != null) MDC.put(BusinessEventLogMeta.LOGGED_BY, transaction.getCreatedBy());
        }
        if (transaction != null && transaction.getId() != null) MDC.put(BusinessEventLogMeta.MESSAGE_ID, transaction.getId());
        try {
            if (transaction.getTransactionFieldArray() != null) {
                for (TransactionField tField : transaction.getTransactionFieldArray()) {
                    if ("JaffaTransactionInvokerScheduledTaskId".equals(tField.getFieldName())) MDC.put(BusinessEventLogMeta.SCHEDULED_TASK_ID, tField.getValue());
                }
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) log.debug("Unable to fetch transaction field array from transaction", e);
        }
    }

    /**
     * Remove the elements from the Message Driven Context (MDC) of Log4J, that may have been added by the call to setLoggingContext().
     */
    public static void unsetLoggingContext() {
        Stack<Map<String, Object>> stack = t_loggingContext.get();
        if (stack == null || stack.size() == 0) throw new UnsupportedOperationException("The unsetLoggingContext() method can only be called after a setLoggingContext()");
        if (MDC.getContext() != null) {
            Set<String> keys = new HashSet<String>(MDC.getContext().keySet());
            for (String key : keys) MDC.remove(key);
        }
        Map<String, Object> previousLoggingContext = stack.pop();
        for (Map.Entry<String, Object> me : previousLoggingContext.entrySet()) MDC.put(me.getKey(), me.getValue());
    }
}
