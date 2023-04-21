package com.monkygames.sc2bob.io;

/**
 * Used for receiving progress updates from SC2PodDownloader.
 * @version 1.0
 */
public interface SC2PodDownloaderInterface {

    /**
     * Updates the progress of the downloader.
     * @param download the number of files downloaded.
     * @param message any message associated with the current downloading files.
     */
    public void progressUpdated(int downloaded, String message);

    /**
     * Sets the number of files to be downloaded.
     * @param total the total number of files to be downloaded.
     **/
    public void totalFilesToBeDownloaded(int total);
}
