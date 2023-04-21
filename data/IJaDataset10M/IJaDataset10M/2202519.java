package org.tripcom.security.stubs;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.security.util.Util;

/**
 * A dummy JavaSpace which does nothing.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class DummyJavaSpace implements JavaSpace {

    /** A shared log object used to notify operations and exceptions. */
    private static Log log = LogFactory.getLog(DummyJavaSpace.class);

    /** The name of the space, used in order to provide more readable logs. */
    private String name;

    /**
	 * Creates a new instance an unnamed dummy space.
	 */
    public DummyJavaSpace() {
        this(null);
    }

    /**
	 * Creates a new instance for a named dummy space.
	 * 
	 * @param name an optional name for the space, to be used in the log.
	 */
    public DummyJavaSpace(String name) {
        this.name = name;
    }

    /**
	 * Return the name of this space. The name is only used for logging
	 * purposes, in order to produce a more readable output.
	 * 
	 * @return the configured name of this space.
	 */
    public String getName() {
        return name;
    }

    /**
	 * {@inheritDoc}
	 */
    public Lease write(Entry entry, Transaction txn, long lease) throws TransactionException, RemoteException {
        if (entry == null) {
            throw new NullPointerException();
        }
        if (log.isInfoEnabled()) {
            log.info("Written " + entry.getClass().getSimpleName() + " to space " + name);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public Entry read(Entry tmpl, Transaction txn, long timeout) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
        if (tmpl == null) {
            throw new NullPointerException();
        }
        Util.sleep(Long.MAX_VALUE);
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public Entry readIfExists(Entry tmpl, Transaction txn, long timeout) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
        if (tmpl == null) {
            throw new NullPointerException();
        }
        if (log.isInfoEnabled()) {
            log.info("No entry available for read on space " + name);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public Entry take(Entry tmpl, Transaction txn, long timeout) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
        if (tmpl == null) {
            throw new NullPointerException();
        }
        Util.sleep(Long.MAX_VALUE);
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public Entry takeIfExists(Entry tmpl, Transaction txn, long timeout) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
        if (tmpl == null) {
            throw new NullPointerException();
        }
        if (log.isInfoEnabled()) {
            log.info("No entry available for take on space " + name);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings(value = "unchecked")
    public EventRegistration notify(Entry tmpl, Transaction txn, RemoteEventListener listener, long lease, MarshalledObject handback) throws TransactionException, RemoteException {
        if ((tmpl == null) || (listener == null)) {
            throw new NullPointerException();
        }
        if (log.isInfoEnabled()) {
            log.info("Registered notify listener for space " + name);
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public Entry snapshot(Entry e) throws RemoteException {
        if (e == null) {
            throw new NullPointerException();
        }
        return e;
    }
}
