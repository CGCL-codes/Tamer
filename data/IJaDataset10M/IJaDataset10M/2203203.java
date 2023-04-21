package com.objectcode.time4u.server.api.data;

import java.io.Serializable;

public class StatisticEntry implements Serializable {

    private static final long serialVersionUID = -7252427079151032149L;

    private long m_projectId;

    private int m_duration;

    private int m_count;

    public int getDuration() {
        return m_duration;
    }

    public void setDuration(int duration) {
        m_duration = duration;
    }

    public int getCount() {
        return m_count;
    }

    public void setCount(int count) {
        m_count = count;
    }

    public long getProjectId() {
        return m_projectId;
    }

    public void setProjectId(long projectId) {
        m_projectId = projectId;
    }

    public void aggregate(int minutes) {
        m_duration += minutes;
        m_count++;
    }
}
