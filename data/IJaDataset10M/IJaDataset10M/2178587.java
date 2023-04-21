package com.grt192.event.component;

/**
 *
 * @author GRTstudent
 */
public interface EncoderListener {

    public void countDidChange(EncoderEvent e);

    public void rotationDidStart(EncoderEvent e);

    public void rotationDidStop(EncoderEvent e);

    public void changedDirection(EncoderEvent e);
}
