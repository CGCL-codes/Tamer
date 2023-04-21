package org.mobicents.servlet.sip.proxy;

import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.mobicents.javax.servlet.sip.ResponseType;

public class ProxyBranchTimerTask extends TimerTask {

    private static final Logger logger = Logger.getLogger(ProxyBranchTimerTask.class);

    private ProxyBranchImpl proxyBranch;

    private ResponseType responseType;

    public ProxyBranchTimerTask(ProxyBranchImpl proxyBranch, ResponseType responseType) {
        this.proxyBranch = proxyBranch;
        this.responseType = responseType;
    }

    @Override
    public void run() {
        try {
            if (proxyBranch != null) {
                proxyBranch.onTimeout(this.responseType);
            }
        } catch (Exception e) {
            logger.error("Problem in timeout task", e);
        } finally {
            this.proxyBranch = null;
        }
    }

    @Override
    public boolean cancel() {
        proxyBranch = null;
        responseType = null;
        return super.cancel();
    }
}
