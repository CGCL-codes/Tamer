package org.activebpel.rt.bpel.impl.activity;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.impl.IAeBpelObject;
import org.activebpel.rt.bpel.impl.activity.support.AeOnAlarm;
import org.activebpel.rt.bpel.impl.activity.support.AeOnMessage;

/**
 * Interface for <code>pick</code>s and <code>eventHandlers</code>. Provides methods
 * for adding messages and alarms into the parent.
 */
public interface IAeEventParent extends IAeBpelObject {

    /**
    * Adds the alarm to the <code>pick</code> or <code>eventHandlers</code>
    * @param aAlarm
    */
    public void addAlarm(AeOnAlarm aAlarm);

    /**
    * Adds the message to the <code>pick</code> or <code>eventHandlers</code>
    * @param aMessage
    */
    public void addMessage(AeOnMessage aMessage);

    /**
    * Callback from a child when it becomes active.
    * @param aChild the child becoming active.
    */
    public void childActive(IAeBpelObject aChild) throws AeBusinessProcessException;
}
