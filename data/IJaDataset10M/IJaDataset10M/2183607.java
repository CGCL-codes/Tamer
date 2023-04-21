package de.xlii.bgo;

interface DownloadProgressListener {

    void publishProgress(int progress);

    void publishTarget(int target);
}
