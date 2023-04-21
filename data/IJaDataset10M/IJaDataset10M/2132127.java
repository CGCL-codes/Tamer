package org.mobicents.servlet.sip.core.timers;

import java.io.Serializable;
import org.mobicents.servlet.sip.core.session.MobicentsSipApplicationSessionKey;
import org.mobicents.servlet.sip.core.session.SipApplicationSessionKey;
import org.mobicents.timers.PeriodicScheduleStrategy;
import org.mobicents.timers.TimerTaskData;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public class TimerServiceTaskData extends TimerTaskData implements Serializable {

    private Serializable data;

    private MobicentsSipApplicationSessionKey key;

    private long delay;

    public TimerServiceTaskData(Serializable id, long startTime, long period, PeriodicScheduleStrategy periodicScheduleStrategy) {
        super(id, startTime, period, periodicScheduleStrategy);
    }

    /**
	 * @param data the data to set
	 */
    public void setData(Serializable data) {
        this.data = data;
    }

    /**
	 * @return the data
	 */
    public Serializable getData() {
        return data;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(MobicentsSipApplicationSessionKey key) {
        this.key = key;
    }

    /**
	 * @return the key
	 */
    public MobicentsSipApplicationSessionKey getKey() {
        return key;
    }

    /**
	 * @param delay the delay to set
	 */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
	 * @return the delay
	 */
    public long getDelay() {
        return delay;
    }
}
