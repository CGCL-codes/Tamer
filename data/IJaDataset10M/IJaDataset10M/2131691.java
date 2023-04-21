package com.google.checkout.sample.event;

import java.util.EventObject;

/**
 * The <b>CallBackEvent</b> class is the base class for handling
 * Google Checkout callback events.
 * 
 * @version 1.0 beta
 */
public abstract class CallBackEvent extends EventObject {

    /**
   * Default constructor
   */
    protected CallBackEvent(Object source) {
        super(source);
    }
}
