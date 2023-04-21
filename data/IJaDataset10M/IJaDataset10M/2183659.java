package net.teqlo.lifecycle;

import net.teqlo.TeqloException;
import net.teqlo.events.AbstractTeqloSource;
import net.teqlo.events.TeqloSource;
import net.teqlo.events.runtime.LifecycleCloseEvent;
import net.teqlo.events.runtime.LifecycleOpenEvent;
import net.teqlo.events.runtime.LifecycleReopenEvent;
import net.teqlo.events.runtime.LifecycleSuspendEvent;
import net.teqlo.util.Loggers;

public abstract class AbstractLifecycleWithEvents extends AbstractTeqloSource implements Lifecycle {

    private transient boolean isOpen = false;

    protected transient boolean isSuspended = false;

    private transient TeqloSource eventFirer = this;

    /**
	 * Allows a subclass to nominate another object to be the one that fires events to its listeners
	 * @param eventFirer to use
	 */
    protected void setEventFirer(TeqloSource eventFirer) {
        this.eventFirer = eventFirer;
    }

    /**
	 * Subclass must provide this method containing stuff to do upon close
	 * @throws TeqloException if any error occurs
	 */
    protected abstract void actionsOnClose() throws TeqloException;

    /**
	 * Subclass must provide this method containing stuff to do upon open
	 * @throws TeqloException if any error occurs
	 */
    protected abstract void actionsOnOpen() throws TeqloException;

    /**
	 * Subclass must provide this method containing stuff to do on reopen
	 * @throws TeqloException if any error occurs
	 */
    protected abstract void actionsOnReopen() throws TeqloException;

    /**
	 * Subclass must provide this method containing stuff to do on suspend
	 * @throws TeqloException if any error occurs
	 */
    protected abstract void actionsOnSuspend() throws TeqloException;

    /**
	 * Throws exception if the entity is not open
	 * @throws TeqloException
	 */
    public synchronized void assertOpen() throws TeqloException {
        if (!this.isOpen) throw new TeqloException(this, "This entity is not open", null, "Entity must be open");
    }

    /**
	 * Closes the entity
	 */
    public synchronized void close() throws TeqloException {
        if (!this.isOpen && !this.isSuspended) return;
        this.eventFirer.fireTeqloEventNoVeto(new LifecycleCloseEvent(this));
        try {
            this.actionsOnClose();
        } catch (Throwable e) {
            Loggers.XML_RUNTIME.error("Error closing: " + this, e);
        }
        this.isOpen = false;
        this.isSuspended = false;
        Loggers.XML_RUNTIME.debug("Closed: " + this);
    }

    /**
	 * Returns true if the entity is open, false otherwise
	 */
    public synchronized boolean isOpen() {
        return this.isOpen;
    }

    /**
	 * Returns true if the entity is suspended, false otherwise
	 */
    public synchronized boolean isSuspended() {
        return this.isSuspended;
    }

    /**
	 * Opens the entity
	 */
    public synchronized void open() throws TeqloException {
        if (this.isOpen || this.isSuspended) throw new TeqloException(this, "This entity is not closed", null, "Can only open closed entities");
        this.actionsOnOpen();
        this.isOpen = true;
        this.isSuspended = false;
        if (eventFirer == null) eventFirer = this;
        this.eventFirer.fireTeqloEventNoVeto(new LifecycleOpenEvent(this));
        Loggers.XML_RUNTIME.debug("Opened: " + this);
    }

    /**
	 * Reopens the entity
	 */
    public synchronized void reopen() throws TeqloException {
        if (!this.isSuspended) throw new TeqloException(this, "This entity is not suspended", null, "Can only reopen suspended entities");
        this.actionsOnReopen();
        this.isOpen = true;
        this.isSuspended = false;
        this.eventFirer.fireTeqloEventNoVeto(new LifecycleReopenEvent(this));
        Loggers.XML_RUNTIME.debug("Reopened: " + this);
    }

    /**
	 * Suspends the entity
	 */
    public synchronized void suspend() throws TeqloException {
        this.assertOpen();
        this.isOpen = false;
        this.isSuspended = true;
        this.actionsOnSuspend();
        this.eventFirer.fireTeqloEventNoVeto(new LifecycleSuspendEvent(this));
        Loggers.XML_RUNTIME.debug("Suspended: " + this);
    }
}
