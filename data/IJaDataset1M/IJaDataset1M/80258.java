package org.jefb.util.service.impl;

import java.io.Serializable;
import org.jefb.util.ConfigHolder;
import org.springframework.stereotype.Service;

@Service
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long maxTokenLength;

    public void init() {
    }

    public String getAgentId() {
        return "AgentA";
    }

    public String getRelease() {
        return ConfigHolder.getProperty("jefb.release");
    }

    public void setMaxTokenLength(Long maxTokenLength) {
        this.maxTokenLength = maxTokenLength;
    }

    public Long getMaxTokenLength() {
        return maxTokenLength;
    }

    public String getInboxDir() {
        return "/inbox";
    }
}
