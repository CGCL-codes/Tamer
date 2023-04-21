package org.opennms.netmgt.scheduler;

import java.util.Random;
import org.opennms.core.utils.ThreadCategory;

/**
 * Represents a Schedule 
 *
 * @author brozow
 */
public class Schedule {

    public static final Random random = new Random();

    private final ReadyRunnable m_schedulable;

    private final ScheduleInterval m_interval;

    private final ScheduleTimer m_timer;

    private volatile int m_currentExpirationCode;

    private volatile boolean m_scheduled = false;

    class ScheduleEntry implements ReadyRunnable {

        private final int m_expirationCode;

        public ScheduleEntry(int expirationCode) {
            m_expirationCode = expirationCode;
        }

        /**
         * @return
         */
        private boolean isExpired() {
            return m_expirationCode < m_currentExpirationCode;
        }

        public boolean isReady() {
            return isExpired() || m_schedulable.isReady();
        }

        public void run() {
            if (isExpired()) {
                ThreadCategory.getInstance(getClass()).debug("Schedule " + this + " expired.  No need to run.");
                return;
            }
            if (!m_interval.scheduledSuspension()) {
                try {
                    Schedule.this.run();
                } catch (PostponeNecessary e) {
                    m_timer.schedule(random.nextInt(10) * 1000 + 5000, this);
                    return;
                }
            }
            if (isExpired()) {
                ThreadCategory.getInstance(getClass()).debug("Schedule " + this + " expired.  No need to reschedule.");
                return;
            }
            long interval = m_interval.getInterval();
            if (interval >= 0 && m_scheduled) m_timer.schedule(interval, this);
        }

        public String toString() {
            return "ScheduleEntry[expCode=" + m_expirationCode + "] for " + m_schedulable;
        }
    }

    /**
     * @param interval
     * @param timer
     * @param m_schedulable
     * 
     */
    public Schedule(ReadyRunnable schedulable, ScheduleInterval interval, ScheduleTimer timer) {
        m_schedulable = schedulable;
        m_interval = interval;
        m_timer = timer;
        m_currentExpirationCode = 0;
    }

    /**
     * 
     */
    public void schedule() {
        m_scheduled = true;
        schedule(0);
    }

    private void schedule(long interval) {
        if (interval >= 0 && m_scheduled) m_timer.schedule(interval, new ScheduleEntry(++m_currentExpirationCode));
    }

    /**
     * 
     */
    public void run() {
        m_schedulable.run();
    }

    /**
     * 
     */
    public void adjustSchedule() {
        schedule(m_interval.getInterval());
    }

    /**
     * 
     */
    public void unschedule() {
        m_scheduled = false;
        m_currentExpirationCode++;
    }
}
