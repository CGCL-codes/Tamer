package org.ebayopensource.twin;

/** 
 * This subclass of TwinException is thrown when we attempt to set an element to an invalid state.
 */
@SuppressWarnings("serial")
public class TwinInvalidElementStateException extends TwinException {

    public TwinInvalidElementStateException(String s) {
        super(s);
    }
}
