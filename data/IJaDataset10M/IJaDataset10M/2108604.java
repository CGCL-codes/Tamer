package com.limegroup.gnutella.downloader;

import java.io.IOException;

public class QueuedException extends IOException {

    private int minPollTime = 45000;

    private int maxPollTime = 120000;

    private int queuePos = -1;

    public QueuedException(int minPoll, int maxPoll, int pos) {
        this.minPollTime = minPoll;
        this.maxPollTime = maxPoll;
        this.queuePos = pos;
    }

    public int getMinPollTime() {
        return minPollTime;
    }

    public int getMaxPollTime() {
        return maxPollTime;
    }

    public int getQueuePosition() {
        return queuePos;
    }
}
