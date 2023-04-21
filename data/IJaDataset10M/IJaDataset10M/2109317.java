package com.jes.classfinder.gui.panel.refreshdatabase;

import java.util.List;
import com.jes.classfinder.listeners.progress.ProgressListener;
import com.jes.classfinder.model.DirectoryLocation;

/**
 * @author John Dickerson
 */
public interface RefreshDatabasePanel {

    public void showProgressPanel();

    public void showDirectoryLocations(List<DirectoryLocation> directoryLocationList);

    public ProgressListener getProgressListener();

    public void disableRefreshDatabaseButton();

    public void enableRefreshDatabaseButton();
}
