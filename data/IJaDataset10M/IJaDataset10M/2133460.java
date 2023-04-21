package com.ibm.tuningfork.core.sharing;

import com.ibm.tuningfork.core.dialog.ErrorHandling;
import com.ibm.tuningfork.infra.Logging;

public abstract class SharingRequestReceiver extends Thread {

    private boolean shuttingDown;

    protected SharingConduit session;

    private Throwable failureCause;

    protected int exceptionLimit() {
        return 0;
    }

    protected SharingRequestReceiver(String s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            if (!startProcessingRequests()) return;
            processRequests();
        } catch (Exception e) {
            failureCause = e;
            log("processRequest: " + e);
            e.printStackTrace();
            shutDown();
        }
    }

    protected boolean startProcessingRequests() throws Exception {
        return true;
    }

    protected final void processRequests() throws Exception {
        int exceptionCount = 0;
        for (; ; ) try {
            processARequestOrComplain();
        } catch (Exception e) {
            if (exceptionCount++ > exceptionLimit()) throw e;
        }
    }

    protected abstract void processARequest() throws Exception;

    private void processARequestOrComplain() throws Exception {
        try {
            processARequest();
        } catch (Exception e) {
            ErrorHandling.displayWarningFromAnyThread("Could not process a sharing request", e.getMessage(), false);
            throw e;
        }
    }

    public void shutDown() {
        if (shuttingDown) return;
        shuttingDown = true;
        try {
            session.close();
        } finally {
            if (failureCause != null) {
                String message = failureCause.getMessage();
                failureCause = null;
                ErrorHandling.displayWarningFromAnyThread("A Sharing Receiver has shut down", message, false);
            }
        }
        shuttingDown = false;
    }

    protected void log(String s) {
        if (Transceiver.Debug) Logging.msgln("log: " + this + ": " + s);
    }
}
