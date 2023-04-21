package com.pbxworkbench.commons.ui;

public interface ProgressListener {

    void onStarted(String status);

    void onStatusChanged(String status);

    void onCompleted(String status);

    void onAborted(String reason);
}
