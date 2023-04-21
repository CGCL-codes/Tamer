package org.gudy.azureus2.ui.common;

/**
 *
 * @author  tobi
 */
public abstract class UITemplate implements IUserInterface {

    private boolean started = false;

    /** Creates a new instance of UITemplate */
    public UITemplate() {
    }

    public void init(boolean first, boolean others) {
    }

    public abstract void openTorrent(String fileName);

    public abstract String[] processArgs(String[] args);

    public void startUI() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }
}
