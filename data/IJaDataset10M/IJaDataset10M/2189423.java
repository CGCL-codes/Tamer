package com.atosorigin.nl.agreement.utility.events;

import com.atosorigin.nl.agreement.dispatcher.AccountingEvent;

/**
 * @author Jeroen Benckhuijsen
 */
public class ServiceCallEvent extends AccountingEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5768593807506168470L;

    public static final String TYPE = "ServiceCall";

    private Double duration;

    public ServiceCallEvent() {
        setType(TYPE);
    }

    /**
	 * @return the duration
	 */
    public Double getDuration() {
        return duration;
    }

    /**
	 * @param duration the duration to set
	 */
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Service call").append(super.toString()).append(" with duration ").append(this.duration);
        return buf.toString();
    }
}
