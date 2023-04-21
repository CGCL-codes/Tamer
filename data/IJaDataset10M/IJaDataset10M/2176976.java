package com.ibm.realtime.flexotask.validation;

import com.ibm.realtime.flexotask.AtomicFlexotask;

/**
 * Interface that must be implemented by all objects that will function as public interfaces to
 *   AtomicFlexotasks.  The actual guard object remains on the external heap (pinned) and points to the
 *   AtomicFlexotask delegate.
 */
public interface AtomicFlexotaskGuard {

    /**
   * Method to store the AtomicFlexotask "delegate" for which this guard object is a guard.  The
   *   delegate pointer must not be leaked.  This is checked (conservatively) during validation.
   * @param delegate the delegate or null (during termination)
   */
    public void setDelegate(AtomicFlexotask delegate);
}
