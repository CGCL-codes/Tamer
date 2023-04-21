package com.netease.t.live.dto;

import java.util.List;
import com.netease.t.live.entity.ThreadData;

public class ThreadListJSON {

    private String response_code;

    private List<ThreadData> threads;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public List<ThreadData> getThreads() {
        return threads;
    }

    public void setThreads(List<ThreadData> threads) {
        this.threads = threads;
    }
}
