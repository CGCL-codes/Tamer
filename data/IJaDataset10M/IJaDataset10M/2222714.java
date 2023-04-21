package org.nilisoft.jftp4i.monitor;

import org.nilisoft.jftp4i.InitServer;
import org.nilisoft.jftp4i.event.IEvent;
import org.nilisoft.jftp4i.event.JFTPClientEvent;

/**
 * <p> This class implements the default actions necessary to monitor a specific
 * FTP event initialize at the user connection. </p>
 * <p> This class will be controling the connection for each FTP event 
 * created by the client. </p>
 * <p> Created on October 4, 2004, 3:08 PM </p>
 *
 * @author  Jlopes
 */
public class FTPMonitor extends DefaultMonitor {

    /** 
   * <p> Creates a new instance of FTPMonitor </p>
   *
   * @param event The event that should be watched
   * @param timeout The monitor timeout
   */
    public FTPMonitor(IEvent event, String id, long timeout) {
        super("org.nilisoft.jftp4i.monitor.FTPMonitor", id, event, timeout);
        if (event instanceof JFTPClientEvent) this.ftpEvent = (JFTPClientEvent) event; else throw new ClassCastException("The event implemented in the FTP Monitor must be of type: org.nilisoft.jftp4i.event.JFTPClientEvent. Type passed as parameter: " + event.getClass().getName());
    }

    /**
   * <p> Executes the actions necessary to monitor a specific FTP client
   * connection. </p>
   */
    public boolean do_it(boolean status) {
        boolean flag = false;
        log.info("[name: " + identifier + "]. Running monitor rotines");
        if (ftpEvent.isEnded()) {
            finalizeEvent();
            log.info("EVENT [" + super.identifier + "] interrupted successfully");
            flag = true;
        } else {
            long lastTimeout = ftpEvent.lastTimeoutMillis;
            long diffTimeout = (System.currentTimeMillis() - lastTimeout);
            if (diffTimeout > getTimeout()) {
                log.warning("MONITOR [" + super.identifier + "]: The maximum permited timeout " + "was reached (" + (double) (getTimeout() / 60000) + "), the monitor will terminate " + "the event. Time elapsed: " + diffTimeout);
                finalizeEvent();
                flag = true;
            } else log.info("MONITOR [" + super.identifier + "]: HEART BEAT (" + diffTimeout + ")");
        }
        return flag;
    }

    private void finalizeEvent() {
        ftpEvent.end = true;
        Thread threadList[] = new Thread[Thread.activeCount()];
        Thread.enumerate(threadList);
        for (int n = 0; n < threadList.length; n++) {
            if (threadList[n] != null) {
                String name = threadList[n].getName();
                if (name.trim().equalsIgnoreCase(super.identifier)) {
                    if (!threadList[n].interrupted()) threadList[n].interrupt(); else log.info("EVENT [" + super.identifier + "] already interrupted");
                }
            }
        }
    }

    private JFTPClientEvent ftpEvent;
}
