package org.simpleframework.util.lease;

/**
 * A <code>Monitor</code> object is used to receive notification
 * when a scheduled contract has expired. This acts as a monitor
 * to determine whether the <code>Cleaner</code> should actually
 * be given notification. In a concurrent setting the messages
 * issued by lease holders and the scheduling system must be 
 * synchronized in such a way that a renewal is atomic. So the
 * monitor is used to determine whether incoming renewals are to
 * be accepted or rejected based on expired contracts.
 *
 * @author Niall Gallagher
 */
interface Monitor<T> {

    /**
    * The method determines if the <code>Cleaner</code> should
    * be given notification. Organization is required so that
    * if a lease renewal was requested at the time of this
    * notification that only one event should succeed, so the
    * lease should expire before the renewal or the renewal
    * must succeed and notification must be postponed.
    *
    * @param lease this is a contract for a leased resource
    */
    public void expire(Contract<T> lease);
}
