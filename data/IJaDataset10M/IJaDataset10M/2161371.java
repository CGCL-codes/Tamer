package rabbit.linkchecker;

import java.net.*;
import rabbit.http.*;
import rabbit.util.*;

/** A very simple logger for the link checker.
 */
public class SimpleLogger implements Logger {

    public synchronized void log(URL url, String msg) {
        System.out.println("[" + url.toString() + "] => [" + msg + "]");
    }

    public synchronized void log(URL url, HTTPHeader header) {
        System.out.println("[" + url.toString() + "] => [" + header.getStatusLine() + "][" + header.getHeader("Content-Type") + "][" + header.getHeader("Content-length") + "]");
    }

    /** This method is called when this Logger is started.
     */
    public void startLogging(Config cfg) {
    }

    /** This method is called when this Logger is stopped.
     */
    public void endLogging() {
    }
}
