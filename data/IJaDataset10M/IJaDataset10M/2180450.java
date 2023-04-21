package de.searchworkorange.lib;

import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.lib.timer.MyTimer;
import de.searchworkorange.lib.timer.TimerConfiguration;
import de.searchworkorange.lib.timer.WrongConfigurationException;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class Timer extends MyTimer {

    private static final boolean CLASSDEBUG = false;

    /**
     *
     * @param loggerCol
     * @param config
     * @param name
     * @param timeoutLength
     * @param socketAction
     */
    public Timer(LoggerCollection loggerCol, TimerConfiguration config, String name, int timeoutLength, SocketAction socketAction) throws WrongConfigurationException {
        super(loggerCol, config, name, timeoutLength, socketAction);
        socketAction.setTimer(this);
    }
}
