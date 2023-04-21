package org.opennms.netmgt.poller.mock;

import org.opennms.netmgt.scheduler.Timer;

/**
 * Represents a MockTimer 
 *
 * @author brozow
 */
public class MockTimer implements Timer {

    private long m_currentTime;

    /**
     * 
     */
    public MockTimer() {
        m_currentTime = 0L;
    }

    public long getCurrentTime() {
        return m_currentTime;
    }

    public void setCurrentTime(long currentTime) {
        m_currentTime = currentTime;
    }
}
