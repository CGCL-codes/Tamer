package simple.util.lease;

/**
 * The <code>LeaseScheduler</code> object is used to perform the
 * scheduling of lease contracts for the contract duration. This
 * does not perform any processing of scheduled contracts, it 
 * simply uses a <code>Monitor</code> to issue notification that
 * the duration the contract has expired. Keeping this scheduler
 * simple in this way avoids synchronization complications.
 * <p>
 * Contracts scheduled for a specific time period can have that
 * period updated if a contract for the same resource is issued
 * again. This ensures that the scheduler needs to do minimal 
 * processing to generate notifications for changing contracts.
 *
 * @author Niall Gallagher
 */
final class LeaseScheduler extends Thread {

    /** 
    * Prioritizes scheduled objects based on their timeouts.
    */
    private ReactiveQueue queue;

    /**
    * Handles the notifications issued by this scheduler.
    */
    private Monitor monitor;

    /**
    * This is the contract that is waiting to expire next.
    */
    private Contract pending;

    /**
    * Constructor for the <code>LeaseScheduler</code> object.
    * This scheduler makes use of a monitor to perform complex
    * synchronization issues that may occur between the leasing
    * infrastructure and issued <code>Lease</code> objects.
    *
    * @param monitor this handles all notifications issued
    */
    public LeaseScheduler(Monitor monitor) {
        this.queue = new ReactiveQueue();
        this.monitor = monitor;
        this.start();
    }

    /**
    * Schedules the contract for the specified period of time. If
    * the contract or an equivalent contract is scheduled again
    * then the duration of that contract is altered. Equivalence 
    * is determined using <code>Contract.equals</code> method.    
    * <p>
    * This will issue an interrupt to the thread if the incoming
    * contract expires before the pending contract. This is done
    * so that the first contract to expire is the first to be
    * used for notification. Also, this ensures that the incoming
    * contract is never overwritten by the pending contract.
    *
    * @param lease this is the contract that is to be scheduled
    */
    public synchronized void update(Contract lease) {
        if (lease.equals(pending)) {
            pending = lease;
        }
        interrupt(lease);
        process(lease);
    }

    /**
    * Schedules the contract for the specified period of time. If
    * the contract or an equivalent object is scheduled a second
    * time then the duration is altered. Equivalence of the
    * entry is determined using <code>Contract.equals</code>. 
    *
    * @param lease this is the contract that is to be scheduled
    */
    private synchronized void process(Contract lease) {
        long wait = lease.getExpiry();
        long minus = -1L * wait;
        queue.add(lease, minus);
    }

    /**
    * This will interrupt the thread if an incoming contract is
    * likely to expire before the pending contract. The method
    * saves a large amount of interrupts from occurring by using
    * using the pending contract expiry to determine if the 
    * incoming contract expires first. Interrupting the thread
    * for incoming leases is necessary to keep notifications
    * as accurate as possible to the contract expiry.
    * 
    * @param lease this is the incoming contract to be checked
    */
    private synchronized void interrupt(Contract lease) {
        long expiry = lease.getExpiry();
        if (pending != null) {
            if (expiry < pending.getExpiry()) {
                interrupt();
            }
        }
        if (queue.length() == 0) {
            interrupt();
        }
    }

    /**
    * This performs the notification after the expiry period of
    * the given contract has expired. This will put the thread
    * to sleep using the <code>wait</code> method until such 
    * time as the expiry has passed. If during that time a new
    * contract is scheduled that could possibly expire before 
    * the pending contract then this will be interrupted. Once 
    * interrupted the contract is enqueued again, which ensures
    * that expiry notifications are very accurate.
    *
    * @param lease the contract to be used for notification
    */
    private synchronized void wait(Contract lease) {
        long expiry = lease.getExpiry();
        try {
            if (expiry > 0) {
                pending(lease);
                wait(expiry);
            }
            monitor.expire(lease);
        } catch (InterruptedException e) {
            process(pending);
        }
    }

    /**
    * Changes the current pending contract. Keeping track of the
    * pending contract is important because of the possibility
    * of overwriting a recently updated contract. For example if
    * lease "a" is pending and an update is made for lease "a"
    * to reduce the lease duration then it is important to keep
    * the most recent contract for "a". The pending contract 
    * enables the scheduler to keep track of these updates.
    *
    * @param lease this is the contract that is pending expiry
    */
    private synchronized void pending(Contract lease) {
        pending = lease;
    }

    /**
    * This is used to perform all the scheduling tasks. In order
    * to generate notifications for scheduled objects a thread
    * performs the dequeuing of objects before sending the
    * notification to the <code>Monitor</code> object. A single
    * thread is used to perform this task as it is assumed that
    * there should not be an enormous demand for leases.
    */
    public synchronized void run() {
        while (true) {
            Contract lease = (Contract) dequeue();
            try {
                wait(lease);
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**   
    * This dequeuing method provides a means for the thread to 
    * guarantee that an object can be dequeued. This also allows
    * threads to communicate with each other when the queue is
    * empty. If the queue is empty an <code>update</code> will
    * interrupt the waiting thread so that it can do a dequeue.
    *
    * @return the object in the queue with the soonest expiry
    */
    private synchronized Object dequeue() {
        while (queue.length() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                continue;
            }
        }
        return queue.remove();
    }
}
