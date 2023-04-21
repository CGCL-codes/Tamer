package com.cbsgmbh.xi.af.audit.helpers;

import com.cbsgmbh.xi.af.trace.helpers.BaseTracer;

public class AuditSysOutImpl implements Audit {

    private BaseTracer baseTracer = null;

    public void logSuccess(String message) {
        output("AUDIT SUCCESS: " + message);
    }

    public void logError(String message) {
        output("AUDIT ERROR: " + message);
    }

    public void logCatched(Throwable t) {
        output("AUDIT CATCHED: " + t.toString());
    }

    public void output(String s) {
        System.out.println(s);
    }

    public void setMessageInformation(String messageId, int direction) {
    }

    public void logEntering(String versionId) {
        logSuccess("Entering: " + versionId);
    }

    public void logExiting(String versionId) {
        logSuccess("Exiting: " + versionId);
    }

    public void setBaseTracer(BaseTracer baseTracer) {
        this.baseTracer = baseTracer;
    }

    public BaseTracer getBaseTracer() {
        return this.baseTracer;
    }
}
