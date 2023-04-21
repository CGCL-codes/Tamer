package com.jes.classfinder.gui.panel.refreshdatabase;

import com.jes.classfinder.listeners.progress.ProgressListener;

/**
 * @author Giulio Giraldi
 */
public interface RefreshDatabaseUpdatingPanel {

    public ProgressListener getProgressListener();

    public void updateStatus(String message);
}
