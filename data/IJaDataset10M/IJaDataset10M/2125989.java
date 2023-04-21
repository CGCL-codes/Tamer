package org.jjsip.tools;

/** Listens for a Timer events */
public interface TimerListener {

    /** When the Timer exceeds */
    public void onTimeout(Timer t);
}
