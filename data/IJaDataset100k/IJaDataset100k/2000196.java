package com.unicont.cardio;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.unicont.cardio.ui.Main;

/**
 * Singleton to manage logging 
 */
public class LoggerAdapter {

    private LoggerAdapterListener loggerAdapterListener;

    private static LoggerAdapter self;

    private boolean isLoggingDetailed = false;

    private LoggerAdapter() {
    }

    public static LoggerAdapter getInstance() {
        if (self == null) {
            self = new LoggerAdapter();
        }
        return self;
    }

    /**
	 * Sets logging level as detailed or brief
	 * @param isDetailed true if logging detailed 
	 */
    public void setDetailedLogging(boolean isDetailed) {
        isLoggingDetailed = isDetailed;
        if (isDetailed) {
            Logger.getLogger(Main.CARDIO_TERMINAL_LOGGER).setLevel(Level.FINEST);
        } else {
            Logger.getLogger(Main.CARDIO_TERMINAL_LOGGER).setLevel(Level.FINER);
        }
        fireLoggingEvent(isDetailed);
    }

    public boolean isLoggingDetailed() {
        return isLoggingDetailed;
    }

    protected void fireLoggingEvent(boolean isDetailed) {
        if (loggerAdapterListener == null) return;
        synchronized (loggerAdapterListener) {
            loggerAdapterListener.onChangeLoggerLevel(isDetailed);
        }
    }

    public void setLoggerAdapterListener(LoggerAdapterListener loggerAdapterListener) {
        this.loggerAdapterListener = loggerAdapterListener;
    }

    public LoggerAdapterListener getLoggerAdapterListener() {
        return loggerAdapterListener;
    }
}
