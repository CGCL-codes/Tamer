package org.nakedobjects.application;

import org.nakedobjects.application.DomainObjectContainer;

/**
 * Provide a super class for all domain objects that wish to interact with the container.
 * 
 * @see org.nakedobjects.application.DomainObjectContainer
 */
public abstract class DomainObject {

    private DomainObjectContainer container;

    /**
     * Life cycle call back to indicate that object has been logically created.
     */
    protected void created() {
    }

    /**
     * Life cycle call back to indicate that object has been logically destroyed - it is no longer persisted.
     */
    protected void destroyed() {
    }

    /**
     * Display the specified message to the user, which we cannot ensuring that it will be seen.
     */
    protected void informUser(String message) {
        container.informUser(message);
    }

    /**
     * Determine if this object is persistent.
     */
    protected boolean isPersistent() {
        return container.isPersistent(this);
    }

    protected void objectChanged() {
        container.objectChanged(this);
    }

    /**
     * Life cycle call back to indicate that this transient object has been added to persistent store.
     */
    protected void persisted() {
    }

    /**
     * Display the specified message as an error to the user, ensuring that it is seen.
     */
    protected void raiseError(String message) {
        container.raiseError(message);
    }

    /**
     * Life cycle call back to indicate that object has been phically recreated (normally during persistence
     * or distribution).
     */
    protected void recreated() {
    }

    protected void resolve() {
        container.resolve(this);
    }

    protected void resolve(final Object object) {
        container.resolve(this, object);
    }

    public void setContainer(final DomainObjectContainer container) {
        this.container = container;
    }

    /**
     * Display the specified message as a warning to the user, ensuring that it is seen.
     */
    protected void warnUser(String message) {
        container.warnUser(message);
    }
}
