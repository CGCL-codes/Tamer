package DE.FhG.IGD.semoa.net;

import DE.FhG.IGD.util.URL;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Maintains a table of the known hosts and their status.
 * The auto ping code is self synchronizing and tries to
 * minimize the actual number of PINGs sent to the network
 * by sensing and utilizing PINGs sent by other hosts on
 * top of registering ALIVE packets.<p>
 *
 * This class uses a custom URL class rather than the one
 * in <code>java.net</code>. This is because the JDK version
 * requires protocol handlers for all URL types to deal with.
 * We want to deal with URLs for which we potentially have
 * no protocol and stream handlers.
 *
 * @author Volker Roth
 * @version "$Id: Pingd.java 708 2002-10-16 15:25:22Z upinsdor $"
 */
public class Pingd extends Thread {

    /**
     * The map that is used to index host table entries
     * by sender names.
     */
    protected Map entries_ = new HashMap();

    /**
     * The time of the most recent sweep through the table.
     */
    protected long lastPing_ = System.currentTimeMillis();

    /**
     * The delay between pings in millis. The default value
     * is 5 seconds.
     */
    protected volatile long pingDelay_ = 5000L;

    /**
     * The time span until an entry expires. This time should
     * be longer than the ping delay.
     */
    protected volatile long timeout_ = 6000L;

    /**
     * The controller that is used to send PING packets.
     */
    protected Multicastd controller_;

    /**
     * Creates an instance that uses the given <code>Multicastd
     * </code> to send PING packets.
     */
    protected Pingd(Multicastd controller) {
        super("Ping Daemon");
        if (controller == null) {
            throw new NullPointerException("No controller!");
        }
        controller_ = controller;
    }

    /**
     * @return The <code>Entry</code> of the host with
     *   the given name, or <code>null</code> if the name is
     *   not known. The name is the string representation of
     *   the X.501 Distinguished Name of the hosts's signing
     *   certificate. The returned entry is a clone of the
     *   one maintained by this instance.
     */
    public Entry get(String name) {
        Entry entry;
        if (name == null) {
            throw new NullPointerException("name");
        }
        synchronized (entries_) {
            entry = (Entry) entries_.get(name);
            if (entry != null) {
                return (Entry) entry.clone();
            }
            return null;
        }
    }

    /**
     * Returns a snapshot of current host table entries.
     * The entries are of type {@link Entry Entry}.
     *
     * @return Copies of the current host entries.
     */
    public List entryList() {
        ArrayList list;
        Iterator i;
        synchronized (entries_) {
            list = new ArrayList(entries_.size());
            for (i = entries_.values().iterator(); i.hasNext(); ) {
                list.add(((Entry) i.next()).clone());
            }
        }
        return list;
    }

    /**
     * Removes the entry with the given name if it is in the
     * host table. This method calls <code>doExpire(entry)
     * </code> where <code>entry</code> is the removed entry.
     *
     * @param name The name of the entry that shall be removed.
     */
    protected void remove(String name) {
        Entry entry;
        if (name == null) {
            throw new NullPointerException("Name is null!");
        }
        synchronized (entries_) {
            entry = (Entry) entries_.remove(name);
        }
        if (entry != null) {
            controller_.doExpire(entry);
        }
    }

    /**
     * Updates the host table by means of the given ping
     * packet. This method looks for an existing table
     * entry that refers to the same host and updates the
     * timestamps accordingly. If no entry exists that
     * matches the given packet then a new entry is added
     * to the table of known hosts, and
     * <code>doSense(Entry)</code> is invoked on the
     * controller with the new entry as its argument.
     *
     * @param entry The received <code>PingPacket</code>..
     */
    protected void update(PingPacket packet) {
        boolean sensed;
        String label;
        String name;
        Entry entry;
        long timestamp;
        int type;
        URL url;
        type = packet.getPacketType();
        name = packet.getSenderDName().getName();
        timestamp = packet.getTimestamp();
        sensed = false;
        url = null;
        if (type == PingPacket.PING || type == PingPacket.ALIVE) {
            label = packet.getLabel();
            if (label != null) {
                try {
                    url = new URL(label);
                } catch (MalformedURLException e) {
                }
            }
        }
        synchronized (entries_) {
            if (type == PingPacket.PING) {
                lastPing_ = System.currentTimeMillis();
            }
            entry = (Entry) entries_.get(name);
            if (entry == null) {
                sensed = true;
                entry = new Entry();
                entry.name_ = name;
                entry.timestamp_ = timestamp;
                entry.modified_ = System.currentTimeMillis();
                entry.url_ = url;
                entries_.put(name, entry);
            } else if (timestamp > entry.timestamp_) {
                entry.modified_ = System.currentTimeMillis();
                entry.timestamp_ = timestamp;
                if (url != null) {
                    if (!url.equals(entry.url_)) {
                        sensed = true;
                    }
                    entry.url_ = url;
                }
            }
        }
        if (sensed) {
            controller_.doSense(entry);
        }
    }

    /**
     * Runs the auto ping code. This method periodically sweeps
     * through the host table and discards entries which are
     * older than the preset expire time. If necessary, the code
     * issues new pings by means of the {@link #ping ping()}
     * method.<p>
     *
     * This code tries to minimize pings by utilizing ALIVE
     * messages posted by other hosts in response to other
     * hosts' PING messages. Pings are only sent if some host
     * in the table is not yet expired but did not sent an
     * ALIVE message for the configured ping delay either.<p>
     *
     * This method terminates upon interrupts.
     */
    public void run() {
        ArrayList expired;
        Iterator i;
        boolean ping;
        Entry old;
        long sleep;
        long time;
        if (Thread.currentThread() != this) {
            return;
        }
        expired = new ArrayList();
        while (true) {
            synchronized (entries_) {
                time = System.currentTimeMillis();
                for (i = entries_.values().iterator(); i.hasNext(); ) {
                    old = (Entry) i.next();
                    if (old.modified_ + timeout_ < time) {
                        i.remove();
                        expired.add(old);
                    }
                }
            }
            for (i = expired.iterator(); i.hasNext(); ) {
                controller_.doExpire((Entry) i.next());
            }
            expired.clear();
            time = System.currentTimeMillis();
            sleep = lastPing_ + pingDelay_ - time;
            if (sleep < 200L) {
                sleep = pingDelay_;
                lastPing_ = time;
                ping();
            }
            try {
                Thread.currentThread().sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("[pingd] terminated.");
                break;
            }
        }
    }

    /**
     * Pings the other hosts. A PING packet is sent through
     * the controller of this host table.
     */
    protected void ping() {
        try {
            controller_.ping();
        } catch (IOException e) {
            System.out.println("Pingd: " + e.getMessage());
        }
    }

    public void setPingDelay(long millis) {
        pingDelay_ = millis;
    }

    /**
     * Represents a host entry. This class serves as a simply
     * storage structure which keeps a couple of timestamps
     * and host information together. Fields are accessed
     * directly rather than calling instance methods.
     *
     * @author Volker Roth
     * @version "$Id: Pingd.java 708 2002-10-16 15:25:22Z upinsdor $"
     */
    public class Entry extends Object implements Cloneable {

        /**
         * The distinguished name of the owner of the
         * peer host.
         */
        protected String name_ = "";

        /**
         * The time stamp of the last packet from this
         * host.
         */
        protected long timestamp_;

        /**
         * The most recent time this entry was touched.
         */
        protected long modified_;

        /**
         * The preferred gateway for agents into this
         * host (given as URL).
         */
        protected URL url_;

        /**
         * Restrict entry creation to package.
         */
        protected Entry() {
        }

        /**
         * @return A clone of this instance.
         */
        public Object clone() {
            Entry e;
            e = new Entry();
            e.name_ = name_;
            e.timestamp_ = timestamp_;
            e.modified_ = modified_;
            e.url_ = url_;
            return e;
        }

        /**
         * @return <code>true</code> if the name of the given
         *   entry equals the name of this entry.
         */
        public boolean equals(Object o) {
            if (o instanceof Entry) {
                return name_.equals(((Entry) o).name_);
            }
            return false;
        }

        /**
         * @return The hash code of this instance.
         */
        public int hashCode() {
            return name_.hashCode();
        }
    }
}
