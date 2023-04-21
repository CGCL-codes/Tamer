package com.taobao.top.request;

import java.util.Map;
import com.taobao.top.util.TopHashMap;

/**
 * TOP API: taobao.increment.xiaoer.subscribe
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class IncrementXiaoerSubscribeRequest implements TopRequest {

    private Integer duration;

    private String isvAppKey;

    private String status;

    private String topics;

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setIsvAppKey(String isvAppKey) {
        this.isvAppKey = isvAppKey;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getApiName() {
        return "taobao.increment.xiaoer.subscribe";
    }

    public Map<String, String> getTextParams() {
        TopHashMap params = new TopHashMap();
        params.put("duration", this.duration);
        params.put("isv_app_key", this.isvAppKey);
        params.put("status", this.status);
        params.put("topics", this.topics);
        return params;
    }
}
