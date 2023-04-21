package com.leclercb.taskunifier.gui.threads.checkversion;

public class CheckVersionThread extends Thread {

    public CheckVersionThread(boolean silent) {
        super(new CheckVersionRunnable(silent));
    }
}
