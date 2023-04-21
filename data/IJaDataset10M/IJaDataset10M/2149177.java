package org.starobjects.jpa.runtime.persistence.objectstore.command;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;

public final class JpaDeleteObjectCommand extends AbstractObjectCommand implements DestroyObjectCommand {

    private static final Logger LOG = Logger.getLogger(JpaDeleteObjectCommand.class);

    public JpaDeleteObjectCommand(NakedObject adapter, Session hibernateSession) {
        super(adapter, hibernateSession);
    }

    public void execute(final NakedObjectTransaction context) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("destroy object - executing command for " + onObject());
        }
        getHibernateSession().delete(onObject().getObject());
    }

    @Override
    public String toString() {
        return "DestroyObjectCommand [adapter=" + onObject() + "]";
    }
}
