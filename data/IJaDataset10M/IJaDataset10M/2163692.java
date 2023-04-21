package rtm4java.impl;

import rtm4java.Identified;
import rtm4java.Identifier;
import rtm4java.impl.util.UnitOfWork;

/**
 * Basic domain object
 * 
 * @author <a href="mailto:nerab@gmx.at">Andreas E. Rabenau</a>
 */
public abstract class DomainObject implements Identified {

    private Identifier _id;

    /**
	 * Constructor for DomainObject.
	 * 
	 * There is no need to have the constructor public, we can't create DomainObject's anyway
	 */
    protected DomainObject() {
        markNew();
    }

    protected DomainObject(Identifier id) {
        _id = id;
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
    public boolean equals(Object other) {
        if (null == other) return false;
        if (this == other) return true;
        if (!(other instanceof DomainObject)) return false;
        DomainObject otherDomainObject = (DomainObject) other;
        return (_id.equals(otherDomainObject._id));
    }

    public void remove() {
        markRemoved();
    }

    protected void markNew() {
        UnitOfWork uow = UnitOfWork.getCurrent();
        if (null != uow) uow.registerNew(this);
    }

    protected void markClean() {
        UnitOfWork uow = UnitOfWork.getCurrent();
        if (null != uow) uow.registerClean(this);
    }

    protected void markDirty() {
        UnitOfWork uow = UnitOfWork.getCurrent();
        if (null != uow) uow.registerDirty(this);
    }

    protected void markRemoved() {
        UnitOfWork uow = UnitOfWork.getCurrent();
        if (null != uow) uow.registerRemoved(this);
    }

    public Identifier getId() {
        return _id;
    }
}
