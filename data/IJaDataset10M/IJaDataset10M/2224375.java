package net.woodstock.nettool4j;

import java.util.logging.Logger;

public abstract class AbstractRunnable implements Runnable {

    private static Logger logger = Logger.getLogger("net.woodstock.nettool");

    private boolean runEnabled;

    public AbstractRunnable() {
        super();
        this.runEnabled = true;
    }

    protected Logger getLogger() {
        return AbstractRunnable.logger;
    }

    public boolean isRunEnabled() {
        return this.runEnabled;
    }

    public void setRunEnabled(final boolean runEnabled) {
        this.runEnabled = runEnabled;
    }
}
