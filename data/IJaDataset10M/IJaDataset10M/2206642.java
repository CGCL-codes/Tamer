package org.chargecar.lcddisplay;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
public interface ButtonPanelEventListener {

    void handleOKEvent();

    void handleCancelEvent();

    void handleUpEvent();

    void handleRightEvent();

    void handleDownEvent();

    void handleLeftEvent();

    void handleAccessoryOneEvent();

    void handleAccessoryTwoEvent();

    void handleAccessoryThreeEvent();
}
