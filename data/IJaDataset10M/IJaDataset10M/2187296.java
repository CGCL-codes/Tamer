package org.opennms.netmgt.snmp;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.log4j.Category;
import org.opennms.core.concurrent.BarrierSignaler;
import org.opennms.core.utils.ThreadCategory;

public abstract class SnmpWalker {

    protected abstract static class WalkerPduBuilder extends PduBuilder {

        protected WalkerPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        public abstract void reset();
    }

    private String m_name;

    private CollectionTracker m_tracker;

    private BarrierSignaler m_signal;

    private InetAddress m_address;

    private WalkerPduBuilder m_pduBuilder;

    private ResponseProcessor m_responseProcessor;

    private int m_maxVarsPerPdu;

    private boolean m_error = false;

    private String m_errorMessage;

    private Throwable m_errorThrowable;

    protected SnmpWalker(InetAddress address, String name, int maxVarsPerPdu, int maxRepititions, CollectionTracker tracker) {
        m_address = address;
        m_signal = new BarrierSignaler(1);
        m_name = name;
        m_tracker = tracker;
        m_tracker.setMaxRepetitions(maxRepititions);
        m_maxVarsPerPdu = maxVarsPerPdu;
    }

    protected abstract WalkerPduBuilder createPduBuilder(int maxVarsPerPdu);

    public void start() {
        m_pduBuilder = createPduBuilder(m_maxVarsPerPdu);
        try {
            buildAndSendNextPdu();
        } catch (Throwable e) {
            handleFatalError(e);
        }
    }

    public int getMaxVarsPerPdu() {
        return (m_pduBuilder == null ? m_maxVarsPerPdu : m_pduBuilder.getMaxVarsPerPdu());
    }

    protected void buildAndSendNextPdu() throws IOException {
        if (m_tracker.isFinished()) {
            handleDone();
        } else {
            m_pduBuilder.reset();
            m_responseProcessor = m_tracker.buildNextPdu(m_pduBuilder);
            sendNextPdu(m_pduBuilder);
        }
    }

    protected abstract void sendNextPdu(WalkerPduBuilder pduBuilder) throws IOException;

    protected void handleDone() {
        finish();
    }

    /**
     * <P>
     * Returns the success or failure code for collection of the data.
     * </P>
     */
    public boolean failed() {
        return m_error;
    }

    public boolean timedOut() {
        return m_tracker.timedOut();
    }

    protected void handleAuthError(String msg) {
        m_tracker.setFailed(true);
        processError("Authentication error processing", msg, null);
    }

    protected void handleError(String msg) {
        m_tracker.setTimedOut(false);
        processError("Error retrieving", msg, null);
    }

    protected void handleFatalError(Throwable e) {
        m_tracker.setFailed(true);
        processError("Unexpected error occurred processing", e.toString(), e);
    }

    protected void handleTimeout(String msg) {
        m_tracker.setTimedOut(true);
        processError("Timeout retrieving", msg, null);
    }

    private void processError(String reason, String cause, Throwable t) {
        String logMessage = reason + " " + getName() + " for " + m_address + ": " + cause;
        m_error = true;
        m_errorMessage = logMessage;
        m_errorThrowable = t;
        finish();
    }

    private void finish() {
        signal();
        try {
            close();
        } catch (IOException e) {
            log().error(getName() + ": Unexpected Error occured closing snmp session for: " + m_address, e);
        }
    }

    protected abstract void close() throws IOException;

    public String getName() {
        return m_name;
    }

    private void signal() {
        synchronized (this) {
            notifyAll();
        }
        if (m_signal != null) {
            m_signal.signalAll();
        }
    }

    private final Category log() {
        return ThreadCategory.getInstance(SnmpWalker.class);
    }

    public void waitFor() throws InterruptedException {
        m_signal.waitFor();
    }

    public void waitFor(long timeout) throws InterruptedException {
        m_signal.waitFor(timeout);
    }

    protected boolean processErrors(int errorStatus, int errorIndex) {
        return m_responseProcessor.processErrors(errorStatus, errorIndex);
    }

    protected void processResponse(SnmpObjId receivedOid, SnmpValue val) {
        m_responseProcessor.processResponse(receivedOid, val);
    }

    protected void setAddress(InetAddress address) {
        m_address = address;
    }

    protected InetAddress getAddress() {
        return m_address;
    }

    public String getErrorMessage() {
        return m_errorMessage;
    }

    public Throwable getErrorThrowable() {
        return m_errorThrowable;
    }
}
