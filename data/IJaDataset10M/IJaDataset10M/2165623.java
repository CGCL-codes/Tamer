package org.opensih.temporizador;

import javax.ejb.Local;

@Local
public interface IAlarmScheduler {

    /** starts the timer */
    public void startUpTimer();

    /** stops all the timers */
    public void shutDownTimer();
}
