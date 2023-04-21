package com.limegroup.gnutella.stubs;

import java.net.InetAddress;
import com.google.inject.Singleton;
import com.limegroup.gnutella.FileDesc;
import com.limegroup.gnutella.InsufficientDataException;
import com.limegroup.gnutella.UploadManager;

/**
 * stub for easier testing.  Feel free to override more methods/getters
 */
@Singleton
public class UploadManagerStub implements UploadManager {

    private int _numQueuedUploads;

    private int _uploadsInProgress;

    public void setNumQueuedUploads(int what) {
        _numQueuedUploads = what;
    }

    public void setUploadsInProgress(int what) {
        _uploadsInProgress = what;
    }

    public synchronized int getNumQueuedUploads() {
        return _numQueuedUploads;
    }

    public synchronized int uploadsInProgress() {
        return _uploadsInProgress;
    }

    public float getLastMeasuredBandwidth() {
        return 0;
    }

    public boolean hadSuccesfulUpload() {
        return false;
    }

    public boolean isConnectedTo(InetAddress addr) {
        return false;
    }

    public boolean isServiceable() {
        return false;
    }

    public boolean killUploadsForFileDesc(FileDesc fd) {
        return false;
    }

    public boolean mayBeServiceable() {
        return false;
    }

    public int measuredUploadSpeed() {
        return 0;
    }

    public void start() {
    }

    public void stop() {
    }

    public float getAverageBandwidth() {
        return 0;
    }

    public float getMeasuredBandwidth() throws InsufficientDataException {
        return 0;
    }

    public void measureBandwidth() {
    }
}
