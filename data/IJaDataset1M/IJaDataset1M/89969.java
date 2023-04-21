package org.simpleframework.util.lease;

import java.util.HashMap;

/**
 * The <code>Processor</code> object provides the implementation of
 * the lease management system. This is responsible for issuing and
 * updating leases. In all, lease management requires two threads,
 * one to manage the scheduling of lease contracts and one to do 
 * the notifications when a lease expires. Once a lease has expired
 * this will send the notification to the <code>Cleaner</code>.
 *
 * @author Niall Gallagher
 */
class Processor<T> extends Thread {

    /**
    * This contains an ordered list of the expired contracts.
    */
    private ContractQueue<T> ready;

    /**
    * Used to interface with the lease scheduling system
    */
    private Notifier<T> pending;

    /**
    * Used to recieve notification of expired leases.
    */
    private Cleaner<T> cleaner;

    /**
	* Contains all the contracts that are currently active.
	*/
    private Registry active;

    /**
    * Constructor for the <code>Processor</code> object. This can
    * be used to issue, update, and expire leases. When a lease
    * expires notification is sent to the <code>Cleaner</code>
    * object provided. This allows an implementation independent
    * means to clean up once a specific lease has expired.
    *
    * @param cleaner this will recieve expiration notifications
    */
    public Processor(Cleaner<T> cleaner) {
        this(cleaner, new ContractQueue<T>());
    }

    /**
    * Constructor for the <code>Processor</code> object. This can
    * be used to issue, update, and expire leases. When a lease
    * expires notification is sent to the <code>Cleaner</code>
    * object provided. This allows an implementation independant
    * means to clean up once a specific lease has expired.
    *
    * @param cleaner this will recieve expiration notifications
    * @param ready this is used to gather the expired contracts
    */
    private Processor(Cleaner<T> cleaner, ContractQueue<T> ready) {
        this.pending = new Notifier<T>(ready);
        this.active = new Registry();
        this.cleaner = cleaner;
        this.ready = ready;
        this.start();
    }

    /**
    * This method will establish a lease for the named resource.
    * If the lease duration expires before it is renewed then a
    * notification is sent, to the issued <code>Cleaner</code>
    * implementation, to signify that the resource should be
    * released. The established lease can also be canceled.
    * 
    * @param lease this is the contract that contains details
    */
    public synchronized void lease(Contract<T> lease) {
        T key = lease.getKey();
        active.put(key, lease);
        pending.update(lease);
    }

    /**
    * This ensures that the lease contract is maintained for the
    * contract duration. The duration is in milliseconds so the
    * actual expiry time is the <code>System</code> time plus the
    * duration. The accuracy of this method will be exact.
    *
    * @param lease this is the contract that contains details
    *
    * @exception LeaseException if the expiry has been passed
    */
    public synchronized void update(Contract<T> lease) throws LeaseException {
        if (!active.containsKey(lease)) {
            throw new LeaseException("Lease expiry");
        }
        T key = lease.getKey();
        active.put(key, lease);
        pending.update(lease);
    }

    /**
    * This is used to acquire an active <code>Contract</code>. If
    * the keyed contract has expired before this is invoked then
    * this will throw an exception to indicate the expiry. This
    * is used to query the details of the lease like its expiry. 
    *
    * @param key this is the key for the contract to acquire
    *
    * @exception LeaseException if the expiry has been passed
    */
    public synchronized Contract<T> lookup(T key) throws LeaseException {
        if (!active.containsKey(key)) {
            throw new LeaseException("Lease expiry");
        }
        return active.get(key);
    }

    /**
    * Once a lease duration has expired this method is invoked. 
    * This will determine whether the lease contract specified 
    * has been updated since the notification was generated, if
    * the lease contract has been updated in the meantime then
    * notification is not sent and the issued lease is ignored.
    *
    * @param lease this is the contract that contains details    
    */
    private synchronized void expire(Contract<T> lease) {
        T key = lease.getKey();
        if (active.containsKey(key)) {
            active.remove(key);
            cleaner.clean(key);
        }
    }

    /**
    * This acquires expired lease contracts from the ready queue
    * and attempts to generate an expiry notification. This thread
    * is used so that a deadlock situation is avoided. This also 
    * has an added benifit of seperating the processing of leases
    * and the generation of notifications, which can be time
    * consuming if the <code>Cleaner.clean</code> is implemented
    * is such a way. This should spend most of its time sleeping.
    */
    public void run() {
        while (true) {
            try {
                Contract<T> top = ready.dequeue();
                expire(top);
            } catch (Throwable e) {
                continue;
            }
        }
    }

    /**
    * The <code>Registry</code> object is used to store the active
    * lease contracts so that they can be acquired. Should the 
    * contract expire it will be removed from the registry so that
    * it can no longer be used to lease the resource object.
    */
    private class Registry extends HashMap<T, Contract<T>> {

        /**
	    * Constructor for the <code>Registry</code> object. This
	    * is used to store all active conract objects managed by
	    * this processor instance. When the contract expires it
	    * is removed from the registry and cleaned.
	    */
        public Registry() {
            super();
        }
    }
}
