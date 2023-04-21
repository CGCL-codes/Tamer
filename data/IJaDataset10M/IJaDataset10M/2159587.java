package org.starobjects.jpa.runtime.persistence.objectstore.command;

import org.hibernate.Session;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.PersistenceCommandAbstract;

public abstract class AbstractObjectCommand extends PersistenceCommandAbstract {

    private Session hibernateSession;

    AbstractObjectCommand(final NakedObject adapter, Session hibernateSession) {
        super(adapter);
        this.hibernateSession = hibernateSession;
    }

    public abstract void execute(final NakedObjectTransaction context);

    public Session getHibernateSession() {
        return hibernateSession;
    }
}
