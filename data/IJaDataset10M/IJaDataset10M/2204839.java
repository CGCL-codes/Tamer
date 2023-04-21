package com.aelitis.azureus.core.download;

public interface StreamManagerDownloadListener {

    public void updateActivity(String str);

    public void updateStats(int secs_until_playable, int buffer_secs, long buffer_bytes, int target_buffer_secs);

    public void ready();

    public void failed(Throwable error);
}
