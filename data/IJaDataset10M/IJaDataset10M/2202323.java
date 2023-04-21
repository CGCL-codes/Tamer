package net.sf.repbot;

import net.sf.repbot.server.*;

/** This LineListener makes sure there is some activity on the connection,
 *  so we can time it out to discover problems.
 *  <p>Note this is different from {@link Heartbeat}, which generates
 *  activity to keep fibs from logging us out.
 *
 *  @author  Avi Kivity
 */
public class ConnectionActivity implements LineListener, TimeoutListener {

    private long timeout;

    private FibsListener fibs;

    private boolean suspected = true;

    /** Creates a new instance of ConnectionActivity */
    public ConnectionActivity(long timeout, FibsListener fibs) {
        this.timeout = timeout;
        this.fibs = fibs;
        fibs.addTimeout(this, timeout);
    }

    /** Called on a received line.  Reschedules the timeout. */
    public void onLine(String line, FibsListener connection) {
        suspected = false;
        fibs.removeTimeout(this);
        fibs.addTimeout(this, timeout);
    }

    /** Called on a timeout. Sends some dummy command. */
    public void onTimeout() {
        if (suspected) fibs.reconnect();
        fibs.addTimeout(this, timeout);
        suspected = true;
        try {
            fibs.send("time");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate activity to fibs", e);
        }
    }
}
