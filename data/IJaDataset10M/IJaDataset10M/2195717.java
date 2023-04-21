package org.tripcom.security.testbed;

import junit.framework.Assert;
import net.jini.core.entry.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.integration.entry.Error;
import org.tripcom.integration.entry.ErrorResultExternal;
import org.tripcom.integration.entry.ManagementOperation;
import org.tripcom.integration.entry.ManagementOperationExternal;
import org.tripcom.integration.entry.OutOperationExternal;
import org.tripcom.integration.entry.RdOperationExternal;
import org.tripcom.integration.entry.SecurityCookieInfo;
import org.tripcom.integration.entry.SecurityInfo;
import org.tripcom.integration.entry.SpaceURI;

/**
 * Monitor and collect data about the execution of a request by the Security
 * Manager.
 * <p>
 * This class is instantiated by {@link EnhancedTestbed#process(Entry)} and can
 * be used to monitor the execution of an operation. Initially only the
 * operation id field is set. Then, other fields are automatically updated as
 * soon as the request is received by the Security Manager and a response (and
 * an optional cookie) is returned by the component.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class Operation {

    /** Log object. */
    private static Log log = LogFactory.getLog(Operation.class);

    /** The operation id. */
    private final long operationId;

    /** Flag stating whether security checks are enabled. */
    private boolean securityEnabled;

    /** An optional validator to check entries after operation completion. */
    private OperationValidator validator;

    /** The request entry, updated as soon as received by the SM. */
    private Entry requestEntry;

    /** The response entry, updated as soon as received by the system bus. */
    private Entry responseEntry;

    /** The cookie entry, updated as soon as received by the system bus. */
    private Entry cookieEntry;

    /** The instant when the request is written to the bus. */
    private long requestSentTimestamp;

    /** The instant when the request is taken by the SM. */
    private long requestReceivedTimestamp;

    /** The instant when the response is written by SM to the bus. */
    private long responseSentTimestamp;

    /** The instant when the response is taken from the bus. */
    private long responseReceivedTimestamp;

    /** The instant when the cookie is written by SM to the bus. */
    private long cookieSentTimestamp;

    /** The instant when the cookie is taken from the bus. */
    private long cookieReceivedTimestamp;

    /**
     * Create a new Operation for the operation id specified.
     * 
     * @param operationId the id of the operation.
     * @param securityEnabled true if security controls are enabled.
     * @param validator an optional validator to check the response and cookie
     *            entries after the completion of the operation (possibly null).
     */
    Operation(long operationId, boolean securityEnabled, OperationValidator validator) {
        this.operationId = operationId;
        this.securityEnabled = securityEnabled;
        this.validator = validator;
        this.requestEntry = null;
        this.responseEntry = null;
        this.cookieEntry = null;
        this.requestReceivedTimestamp = 0L;
        this.requestSentTimestamp = 0L;
        this.responseSentTimestamp = 0L;
        this.responseReceivedTimestamp = 0L;
        this.cookieSentTimestamp = 0L;
        this.cookieReceivedTimestamp = 0L;
    }

    /**
     * Return the identifier of the operation.
     * 
     * @return the operation id.
     */
    public long getOperationId() {
        return operationId;
    }

    /**
     * Checks whether security controls are enabled for this operation.
     * 
     * @return true if security controlas are enabled.
     */
    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    /**
     * Returns the optional validator attached to this operation.
     * 
     * @return the validator for the operation.
     */
    public OperationValidator getValidator() {
        return validator;
    }

    /**
     * Return the request entry, if received by the Security Manager.
     * 
     * @return the request entry.
     */
    public synchronized Entry getRequestEntry() {
        return requestEntry;
    }

    /**
     * Return the response entry, if received from the system bus.
     * 
     * @return the response entry.
     */
    public synchronized Entry getResponseEntry() {
        return responseEntry;
    }

    /**
     * Return the cookie entry, if written by the Security Manager and received
     * from the system bus.
     * 
     * @return the cookie entry.
     */
    public synchronized Entry getCookieEntry() {
        return cookieEntry;
    }

    /**
     * Check whether the operation has been completed. An operation is
     * considered completed if the Security Manager has returned the response
     * entry for it (either an error or an entry targeted at other internal
     * components).
     * 
     * @return true if the operation has been completed.
     */
    public synchronized boolean isCompleted() {
        if (responseEntry == null) {
            return false;
        }
        if (responseEntry instanceof ErrorResultExternal) {
            Error error = ((ErrorResultExternal) responseEntry).error;
            if ((error == Error.SMMalformedRequest) || (error == Error.SMInvalidSpace) || (error == Error.SMAuthenticationRejected) || (error == Error.SMUnsupportedOperation)) {
                return true;
            }
        }
        return (cookieEntry != null) || !isCookieExpected();
    }

    /**
     * Check whether the operation has been completed and authorized by the
     * Security Manager.
     * 
     * @return true if the operation has been completed and a response different
     *         from an error has been returned by the Security Manager.
     */
    public synchronized boolean isClientAuthorized() {
        return !(responseEntry instanceof ErrorResultExternal);
    }

    /**
     * Check whether a cookie is expected from the Security Manager assuming
     * that the request is authorized.
     * 
     * @return true if a cookie should be emitted by the Security Manager in
     *         case the request is authorized; note that this method returns
     *         false until the request is sent.
     */
    public synchronized boolean isCookieExpected() {
        if (!securityEnabled || (requestEntry == null)) {
            return false;
        }
        SecurityInfo securityInfo = null;
        if (requestEntry instanceof RdOperationExternal) {
            RdOperationExternal rd = (RdOperationExternal) requestEntry;
            securityInfo = (rd.space == null) ? null : rd.securityInfo;
        } else if (requestEntry instanceof OutOperationExternal) {
            securityInfo = ((OutOperationExternal) requestEntry).securityInfo;
        } else if (requestEntry instanceof ManagementOperationExternal) {
            ManagementOperationExternal mgmtEntry;
            mgmtEntry = (ManagementOperationExternal) requestEntry;
            if ((mgmtEntry.operation != ManagementOperation.Create) || mgmtEntry.parameters.isEmpty() || !((SpaceURI) mgmtEntry.parameters.get(0)).isRootSpace()) {
                securityInfo = mgmtEntry.securityInfo;
            }
        }
        return (securityInfo != null) && (securityInfo.getCertificate() != null) && !(securityInfo instanceof SecurityCookieInfo);
    }

    /**
     * Check whether a cookie was returned by the Security Manager as part of
     * the processing of the operation.
     * 
     * @return true if a cookie has been set by the Security Manager.
     */
    public synchronized boolean isCookieSet() {
        return cookieEntry != null;
    }

    /**
     * Return the timestamp for the instant when the request was written to the
     * system bus.
     * 
     * @return the timestamp for the request sent event, in nanoseconds.
     */
    public synchronized long getRequestSentTimestamp() {
        return requestSentTimestamp;
    }

    /**
     * Return the timestamp for the instant when the request was received by the
     * Security Manager from the system bus.
     * 
     * @return the timestamp for the request received event, in nanoseconds.
     */
    public synchronized long getRequestReceivedTimestamp() {
        return requestReceivedTimestamp;
    }

    /**
     * Return the timestamp for the instant when the response was written by the
     * Security Manager to the system bus.
     * 
     * @return the timestamp for the response sent event, in nanoseconds.
     */
    public synchronized long getResponseSentTimestamp() {
        return responseSentTimestamp;
    }

    /**
     * Return the timestamp for the instant when the response was received from
     * the system bus.
     * 
     * @return the timestamp for the response received event, in nanoseconds.
     */
    public synchronized long getResponseReceivedTimestamp() {
        return responseReceivedTimestamp;
    }

    /**
     * Return the timestamp for the instant when the cookie entry was written by
     * the Security Manager to the system bus.
     * 
     * @return the timestamp for the cookie sent event, in nanoseconds.
     */
    public synchronized long getCookieSentTimestamp() {
        return cookieSentTimestamp;
    }

    /**
     * Return the timestamp for the instant when the cookie entry was received
     * from the system bus.
     * 
     * @return the timestamp for the cookie received event, in nanoseconds.
     */
    public synchronized long getCookieReceivedTimestamp() {
        return cookieReceivedTimestamp;
    }

    /**
     * Return the total time taken to process the operation, comprised the
     * system bus time.
     * 
     * @return the total processing time, in nanoseconds.
     */
    public synchronized long getTotalProcessingTime() {
        return responseReceivedTimestamp - requestSentTimestamp;
    }

    /**
     * Return the time taken by the Security Manager to process the operation.
     * 
     * @return the processing time of the Security Manager, in nanoseconds.
     */
    public synchronized long getComponentProcessingTime() {
        return responseSentTimestamp - requestReceivedTimestamp;
    }

    /**
     * Return the time spent for communication over the system bus.
     * 
     * @return the cummunication time, in nanoseconds.
     */
    public synchronized long getCommunicationTime() {
        return getTotalProcessingTime() - getComponentProcessingTime();
    }

    /**
     * Block the invoking thread until the operation completes or the specified
     * timeout expires.
     * 
     * @param timeout the maximum time to wait for the completion of the
     *            operation (not negative).
     * @return true if the operation completed, false if the timeout expired.
     */
    public boolean awaitCompletion(long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("invalid timeout: " + timeout);
        }
        if (!isCompleted()) {
            long expireTime = System.currentTimeMillis() + timeout;
            while (true) {
                try {
                    long waitTime = expireTime - System.currentTimeMillis();
                    if (waitTime <= 0) {
                        break;
                    }
                    synchronized (this) {
                        this.wait(waitTime);
                    }
                } catch (InterruptedException ex) {
                }
                if (isCompleted()) {
                    break;
                }
            }
        }
        if (validator != null) {
            if (!isCompleted()) {
                Assert.fail("Operation [" + operationId + "] " + requestEntry.getClass().getSimpleName() + " timed out");
            }
            validator.validate(requestEntry, cookieEntry, responseEntry);
        }
        return isCompleted();
    }

    /**
     * Callback method invoked by
     * {@link EnhancedTestbed#notifyEntrySent(Entry, long)} to signal that an
     * entry related to the operation has been written to the system bus.
     * 
     * @param entry the entry written to the system bus (not null).
     * @param timestamp the timestamp for the event.
     */
    synchronized void notifyEntrySent(Entry entry, long timestamp) {
        assert (entry != null);
        EntryType type = EntryType.classifyEntry(entry);
        switch(type) {
            case SM_REQUEST:
                requestSentTimestamp = timestamp;
                break;
            case SM_RESPONSE:
                responseSentTimestamp = timestamp;
                break;
            case SM_COOKIE:
                cookieSentTimestamp = timestamp;
                break;
            default:
                return;
        }
        log.debug(getEventMessage(entry, type, true, timestamp));
    }

    /**
     * Callback method invoked by
     * {@link EnhancedTestbed#notifyEntryReceived(Entry, long)} to signal that
     * an entry related to the operation has been received from the system bus.
     * 
     * @param entry the entry received from the system bus (not null).
     * @param timestamp the timestamp for the event.
     */
    synchronized void notifyEntryReceived(Entry entry, long timestamp) {
        assert (entry != null);
        EntryType type = EntryType.classifyEntry(entry);
        switch(type) {
            case SM_REQUEST:
                requestEntry = entry;
                requestReceivedTimestamp = timestamp;
                break;
            case SM_RESPONSE:
                responseEntry = entry;
                responseReceivedTimestamp = timestamp;
                this.notifyAll();
                break;
            case SM_COOKIE:
                cookieEntry = entry;
                cookieReceivedTimestamp = timestamp;
                break;
            default:
                return;
        }
        log.debug(getEventMessage(entry, type, false, timestamp));
    }

    /**
     * Builds the log message related to the sending or reception of an entry.
     * 
     * @param entry the entry send of received (not null).
     * @param type the type of entry (not null).
     * @param sent true if the entry has been sent, false if it has been
     *            received.
     * @param timestamp the timestamp of the sending or reception event.
     * @return the log message.
     */
    private String getEventMessage(Object entry, EntryType type, boolean sent, long timestamp) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(operationId).append("] ");
        builder.append(type.toString().toLowerCase());
        builder.append(" ");
        builder.append(entry.getClass().getSimpleName());
        builder.append(" ");
        builder.append(sent ? "sent" : "received");
        if ((type != EntryType.SM_REQUEST) || !sent) {
            builder.append(" @ ");
            builder.append(timestamp - requestSentTimestamp);
            builder.append(" ms from start");
        } else {
            builder.append(" @ ");
            builder.append(timestamp);
            builder.append(" ms ");
        }
        return builder.toString();
    }

    /**
     * {@inheritDoc} The method returns a one-line string containing the
     * operation id, type, authorization outcome, whether a cookie has been set
     * and the processing times.
     */
    @Override
    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(operationId).append("]");
        if (requestEntry != null) {
            builder.append(" ").append(requestEntry.getClass().getSimpleName());
        }
        if (isCompleted()) {
            builder.append(" => ");
            builder.append(isClientAuthorized() ? "permit" : "denied");
            if (isCookieSet()) {
                builder.append(", cookie set");
            } else {
                builder.append(", cookie NOT set");
            }
            builder.append(", sm=").append(getComponentProcessingTime());
            builder.append(", net=").append(getCommunicationTime());
            builder.append(", total=").append(getTotalProcessingTime());
        }
        return builder.toString();
    }
}
