package com.sts.webmeet.content.client.audio;

import com.sts.webmeet.client.*;

public interface Speaker {

    public void startPlaying(int iChannels, float fSampleRate, int iBitsPerSample, AtomicDataSource ads) throws Exception;

    public int stopPlaying();
}
