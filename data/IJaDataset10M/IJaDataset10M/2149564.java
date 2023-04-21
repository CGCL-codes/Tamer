package com.limegroup.gnutella.archive;

public interface UploadListener {

    public void fileStarted();

    public void fileProgressed();

    public void fileCompleted();

    public void checkinStarted();

    public void checkinCompleted();

    public void connected();
}
