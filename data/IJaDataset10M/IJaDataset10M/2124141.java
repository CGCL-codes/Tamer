package org.openfrag.OpenCDS.core.download;

import java.util.*;

/**
 * The listener for the DownloadManager class. Listens for state changes,
 *  and other actions.
 *
 * @author  Lars 'Levia' Wesselius
*/
public interface DownloadManagerListener {

    /**
     * This function is fired when a state change has appeared.
     *
     * @param   download    The download that this state change affects to.
     * @param   oldState    The old state of the download.
     * @param   newState    The new state of the download.
    */
    public void downloadStateChanged(Download download, int oldState, int newState);

    /**
     * This function is fired when a download has completed downloading.
     *
     * @param   download    The download which has completed downloading.
    */
    public void downloadDone(Download download);

    /**
     * This function is fired when a download has been removed from the system.
     *
     * @param   download    The download which has been, or will be removed.
    */
    public void downloadRemoved(Download download);

    /**
     * This function is fired when a download has been added to the system.
     *
     * @param   download    The download which has been added.
    */
    public void downloadAdded(Download download);
}
