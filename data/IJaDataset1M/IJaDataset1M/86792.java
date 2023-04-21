package com.sun.jini.norm;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.jini.collection.WeakTable;
import com.sun.jini.landlord.LeasedResource;
import com.sun.jini.thread.InterruptedStatusThread;
import com.sun.jini.thread.WakeupManager;

/**
 * Lease manager that aggressively expires leases as their expiration times
 * occur. Also schedules and manages expiration warning events.
 * <p>
 * Note, unlike Mahalo's <code>LeaseExpirationManager</code> (which this was
 * seeded from), we make no attempt to make it generic because of the need to
 * schedule expiration warning events.
 * 
 * @author Sun Microsystems, Inc.
 */
class LeaseExpirationMgr implements WeakTable.KeyGCHandler {

    /** Logger for logging messages for this class */
    static final Logger logger = Logger.getLogger("com.sun.jini.norm");

    /**
	 * Map of sets to task tickets.
	 * <p>
	 * A Note on Synchronization
	 * <p>
	 * Whenever we operate on the <code>ticketMap</code> we hold the lock on the
	 * key being used. This is necessary because expiration and warning sender
	 * tasks need to remove tickets from the map but at the same time a renewal
	 * may be updating the map to associate the set with a new ticket. If we
	 * don't synchronize there is a small window where a task could remove the
	 * ticket for its replacement.
	 */
    private WeakTable ticketMap = new WeakTable(this);

    /** Ref to the main server object has all the top level methods */
    private NormServerBaseImpl server;

    /** Queue of tasks, ordered by time */
    private WakeupManager runQueue = new WakeupManager();

    /** Queue of tasks to expire sets */
    final List expireQueue = new LinkedList();

    /** Thread to expire sets */
    private final Thread expireThread = new ExpirationThread();

    /**
	 * Create a <code>LeaseExpirationMgr</code> to aggressively expire the
	 * leases of the passed <code>NormServerBaseImpl</code>
	 */
    LeaseExpirationMgr(NormServerBaseImpl server) {
        this.server = server;
        expireThread.start();
    }

    /**
	 * Terminate the <code>LeaseExpirationMgr</code>, killing any threads it has
	 * started
	 */
    void terminate() {
        runQueue.stop();
        runQueue.cancelAll();
        expireThread.interrupt();
    }

    /**
	 * Notifies the manager of a new lease being created.
	 * 
	 * @param resource
	 *            the resource associated with the new lease
	 */
    void register(LeasedResource resource) {
        synchronized (resource) {
            schedule(resource);
        }
    }

    /**
	 * Notifies the manager of a lease being renewed.
	 * <p>
	 * 
	 * This method assumes the lock on <code>set</code> is owned by the current
	 * thread.
	 * 
	 * @param resource
	 *            the set for which tasks have to be rescheduled
	 */
    void reschedule(LeasedResource resource) {
        WakeupManager.Ticket ticket = (WakeupManager.Ticket) ticketMap.remove(resource);
        if (ticket != null) {
            runQueue.cancel(ticket);
        }
        schedule(resource);
    }

    /**
	 * Schedule a leased resource to be reaped in the future. Called when a
	 * resource gets a lease, a lease is renewed, and during log recovery.
	 * <p>
	 * This method assumes the lock on <code>resource</code> is owned by the
	 * current thread.
	 */
    void schedule(LeasedResource resource) {
        WakeupManager.Ticket ticket;
        final LeaseSet set = (LeaseSet) resource;
        MgrTask task;
        if (set.haveWarningRegistration()) {
            task = new SendWarning(set);
            ticket = runQueue.schedule(set.getWarningTime(), task);
        } else {
            task = new QueueExpiration(set);
            ticket = runQueue.schedule(set.getExpiration(), task);
        }
        task.setTicket(ticket);
        ticketMap.getOrAdd(set, ticket);
    }

    public void keyGC(Object value) {
        final WakeupManager.Ticket ticket = (WakeupManager.Ticket) value;
        runQueue.cancel(ticket);
    }

    /**
	 * Expires sets queued for expiration. Perform the expiration in a separate
	 * thread because the operation will block if a snapshot is going on. It's
	 * OK for an expiration to block other expirations, which need not be
	 * timely, but using the separate thread avoids blocking renewal warnings,
	 * which should be timely.
	 */
    private class ExpirationThread extends InterruptedStatusThread {

        ExpirationThread() {
            super("expire lease sets thread");
            setDaemon(true);
        }

        public void run() {
            while (!hasBeenInterrupted()) {
                try {
                    Runnable task;
                    synchronized (expireQueue) {
                        if (expireQueue.isEmpty()) {
                            expireQueue.wait();
                            continue;
                        }
                        task = (Runnable) expireQueue.remove(0);
                    }
                    task.run();
                } catch (InterruptedException e) {
                    return;
                } catch (Throwable t) {
                    logger.log(Level.INFO, "Exception in lease set expiration thread -- " + "attempting to continue", t);
                }
            }
        }
    }

    /**
	 * Utility base class for our tasks, mainly provides the the proper locking
	 * for manipulating the ticketMap.
	 */
    private abstract class MgrTask implements Runnable {

        /** Resource this task is to operate on */
        protected final WeakReference resourceRef;

        /** Ticket for this task */
        private WakeupManager.Ticket ticket;

        /**
		 * Simple constructor.
		 * 
		 * @param set
		 *            the set this task is to operate on
		 */
        protected MgrTask(LeaseSet set) {
            resourceRef = new WeakReference(set);
        }

        /** Set the ticket associated with this task. */
        private void setTicket(WakeupManager.Ticket ticket) {
            this.ticket = ticket;
        }

        /**
		 * Removes this task's ticket from the ticket map iff this task's ticket
		 * is in the map. Returns the <code>LeaseSet</code> this task is to
		 * operate on or <code>null</code> if this task should stop.
		 */
        protected LeaseSet removeOurTicket() {
            final LeaseSet set = (LeaseSet) resourceRef.get();
            if (set != null) {
                synchronized (set) {
                    final WakeupManager.Ticket currentTicket = (WakeupManager.Ticket) ticketMap.get(set);
                    if (ticket.equals(currentTicket)) {
                        ticketMap.remove(set);
                    } else {
                        return null;
                    }
                }
            }
            return set;
        }

        public abstract void run();
    }

    /** Task that queues a task to expire a lease set. */
    private class QueueExpiration extends MgrTask {

        QueueExpiration(LeaseSet set) {
            super(set);
        }

        public void run() {
            LeaseSet set = removeOurTicket();
            if (set != null) {
                synchronized (expireQueue) {
                    expireQueue.add(new Expiration(set));
                    expireQueue.notifyAll();
                }
            }
        }
    }

    /**
	 * Objects that do the actual expiration of the set in question, stuck in
	 * <code>expireQueue</code>.
	 */
    private class Expiration implements Runnable {

        private LeaseSet set;

        /**
		 * Create a <code>Expiration</code> task for the passed resource.
		 * 
		 * @param set
		 *            the set this task is to operate on
		 */
        private Expiration(LeaseSet set) {
            this.set = set;
        }

        public void run() {
            server.expireIfTime(set);
        }
    }

    /**
	 * Objects that do the schedule the warning events, also schedules an
	 * expiration task.
	 */
    private class SendWarning extends MgrTask {

        /**
		 * Create a <code>SendWarning</code> task for the passed resource.
		 * 
		 * @param set
		 *            the set this task is to operate on
		 */
        private SendWarning(LeaseSet set) {
            super(set);
        }

        public void run() {
            final LeaseSet s = (LeaseSet) resourceRef.get();
            if (s == null) {
                return;
            }
            synchronized (s) {
                final LeaseSet set = removeOurTicket();
                if (set == null) {
                    return;
                }
                server.sendWarningEvent(set);
                final MgrTask task = new QueueExpiration(set);
                final WakeupManager.Ticket newTicket = runQueue.schedule(set.getExpiration(), task);
                task.setTicket(newTicket);
                ticketMap.getOrAdd(set, newTicket);
            }
        }
    }
}
