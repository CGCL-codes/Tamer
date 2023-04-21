package org.eledge;

import org.apache.tapestry.engine.IMonitor;

public class EledgeMonitor implements IMonitor {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EledgeMonitor.class);

    public void pageCreateBegin(String pageName) {
    }

    public void pageCreateEnd(String pageName) {
    }

    public void pageLoadBegin(String pageName) {
    }

    public void pageLoadEnd(String pageName) {
    }

    public void pageRenderBegin(String pageName) {
    }

    public void pageRenderEnd(String pageName) {
    }

    public void pageRewindBegin(String pageName) {
    }

    public void pageRewindEnd(String pageName) {
    }

    public void serviceBegin(String serviceName, String detailMessage) {
        log.debug("Beginning service: " + serviceName);
    }

    public void serviceEnd(String serviceName) {
        log.debug("Ending service: " + serviceName);
    }

    public void serviceException(Throwable exception) {
        EledgeAdminEmailer.sendExceptionReport(exception);
    }

    public void sessionBegin() {
    }
}
