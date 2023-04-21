package pl.edu.mimuw.xqtav.exec.helpers;

import pl.edu.mimuw.xqtav.exec.ExecEvent;
import pl.edu.mimuw.xqtav.exec.ExecException;
import pl.edu.mimuw.xqtav.exec.HandlerException;
import pl.edu.mimuw.xqtav.exec.api.ErrorHandler;

/**
 * @author marchant
 *
 * Jesli warning - wypisanie
 * Jesli error - wstrzymanie przetwarzania
 * 
 */
public class ConsoleErrorHandler implements ErrorHandler {

    public void registerEvent(ExecEvent ev) throws ExecException {
        switch(ev.priority) {
            case ExecEvent.PRI_CRITICAL:
                {
                    System.err.println("** FATAL ERROR: " + ev.toString());
                    if (ev.eventData instanceof Exception) {
                        System.err.println("Data is exception, dumping stack trace");
                        ((Exception) ev.eventData).printStackTrace();
                    }
                    throw new ExecException("Critical Error: " + ev.toString());
                }
            case ExecEvent.PRI_ERROR:
                {
                    System.err.println("** FATAL ERROR: " + ev.toString());
                    if (ev.eventData instanceof Exception) {
                        System.err.println("Data is exception, dumping stack trace");
                        ((Exception) ev.eventData).printStackTrace();
                    }
                    throw new ExecException("Error: " + ev.toString());
                }
            case ExecEvent.PRI_WARNING:
                {
                    System.err.println("* WARNING: " + ev.toString());
                    if (ev.eventData instanceof Exception) {
                        System.err.println("Data is exception, dumping stack trace");
                        ((Exception) ev.eventData).printStackTrace();
                    }
                    return;
                }
            default:
                {
                    return;
                }
        }
    }

    public void close() throws HandlerException {
        System.err.flush();
    }

    public void flush() throws HandlerException {
        System.err.flush();
    }

    public void reset() throws HandlerException {
    }
}
