package org.dyndns.doujindb.log.impl;

import java.io.*;
import java.util.*;
import org.dyndns.doujindb.log.*;

/**  
* StdOutLogger.java - Logger writing on the standard output.
* @author  nozomu
* @version 1.0
*/
final class StdOutLogger implements Logger {

    private Vector<Logger> loggers = new Vector<Logger>();

    private OutputStream stream = System.out;

    StdOutLogger() {
    }

    @Override
    public void log(Event event) {
        try {
            String level_string = "";
            switch(event.getLevel()) {
                case INFO:
                    level_string = "Message";
                    break;
                case WARNING:
                    level_string = "Warning";
                    break;
                case ERROR:
                    level_string = "Error";
                    break;
            }
            stream.write(String.format(new java.sql.Time(event.getTime()) + ":%s - %s ! %s\r\n", level_string, event.getSource(), event.getMessage()).getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        for (Logger logger : loggers) logger.log(event);
    }

    @Override
    public synchronized void loggerAttach(Logger logger) {
        if (!loggers.contains(logger)) loggers.add(logger);
    }

    @Override
    public synchronized void loggerDetach(Logger logger) {
        loggers.remove(logger);
    }

    @Override
    public void log(String message, Level level) {
        log(new ImplEvent(message, level));
    }
}
