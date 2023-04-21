package com.ibm.realtime.flexotask.distribution;

/**
 * Interface implemented by distribution systems for use by communicators in exchanging values
 *   with peer communicators on other machines
 */
public interface FlexotaskChannel {

    /**
   * Exchange the current value of a communicator with that of another remote communicator, obtaining
   *   a newer value if possible
   * @param oldValue the current value
   * @return the new value
   */
    public Object exchangeValue(Object oldValue);
}
