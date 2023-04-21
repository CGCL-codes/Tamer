package com.rcreations.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 */
public class ProgressMonitorUtils {

    /**
    * Prevent instantiation.
    */
    private ProgressMonitorUtils() {
    }

    /**
    * Given a progress monitor instance, it automatically increments the numeric status value, in a loop,
    * with the given period and increment.
    */
    public static void autoIncrementProgressMonitor(final ProgressMonitorInterface progressMonitor, int iPeriodMillis, final int iIncrement) {
        final Timer timer = new Timer(iPeriodMillis, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int iProgress = progressMonitor.getProgressValue() + iIncrement;
                if (iProgress > progressMonitor.getProgressMax()) {
                    iProgress = progressMonitor.getProgressMin();
                }
                progressMonitor.setProgressValue(iProgress);
            }
        });
        progressMonitor.addListener(new ProgressMonitorListener() {

            public void notifyProgress(int iProgress) {
            }

            public void notifyClosed() {
                timer.stop();
                progressMonitor.removeListener(this);
            }
        });
        timer.start();
    }
}
