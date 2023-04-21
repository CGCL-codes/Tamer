package com.rise.rois.ui.panels;

public class SessionWatcherThread extends Thread {

    private SessionPanel sessionPanel;

    private boolean cancelled = false;

    public SessionWatcherThread(SessionPanel sessionPanel) {
        this.sessionPanel = sessionPanel;
    }

    @Override
    public void run() {
        int pause = 1000;
        while (!isInterrupted() && !cancelled) {
            try {
                sleep(pause);
                sessionPanel.displayData();
                pause = 300000;
            } catch (InterruptedException e) {
                cancelled = true;
            }
        }
    }

    public void setSessionPanel(SessionPanel sessionPanel) {
        this.sessionPanel = sessionPanel;
    }
}
