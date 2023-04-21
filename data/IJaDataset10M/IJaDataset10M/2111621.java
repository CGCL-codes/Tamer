package com.ianroessle.googleearthinterface;

import com4j.*;

/**
 * ITourControllerGE Interface
 */
@IID("{D08577E0-365E-4216-B1A4-19353EAC1602}")
public interface ITourControllerGE extends Com4jObject {

    /**
     * method PlayOrPause
     */
    @VTID(7)
    void playOrPause();

    /**
     * method Stop
     */
    @VTID(8)
    void stop();

    /**
     * property Speed
     */
    @VTID(9)
    double speed();

    /**
     * property Speed
     */
    @VTID(10)
    void speed(double pSpeed);

    /**
     * property PauseDelay
     */
    @VTID(11)
    double pauseDelay();

    /**
     * property PauseDelay
     */
    @VTID(12)
    void pauseDelay(double pPauseDelay);

    /**
     * property Cycles
     */
    @VTID(13)
    int cycles();

    /**
     * property Cycles
     */
    @VTID(14)
    void cycles(int pCycles);
}
