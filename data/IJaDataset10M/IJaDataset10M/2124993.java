package org.nakedobjects.plugins.hibernate.objectstore;

import java.util.List;
import org.apache.log4j.Logger;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.PersistenceCommand;

/**
 * Same as {@link HibernateObjectStore} except doesn't create commands to save/update objects 
 * at the end of the NOF transaction, instead it does the update there and then.
 */
public class HibernateObjectStoreImmediate extends HibernateObjectStore {

    private static final Logger LOG = Logger.getLogger(HibernateObjectStoreImmediate.class);

    public HibernateObjectStoreImmediate() {
        super();
    }

    @Override
    protected boolean startHibernateTransaction() {
        boolean started = false;
        if (!HibernateUtil.inTransaction()) {
            HibernateUtil.startTransaction();
            started = true;
        }
        boolean flushed = getTransactionManager().flushTransaction();
        return started && !flushed;
    }

    @Override
    public void endTransaction() {
        if (HibernateUtil.inTransaction()) {
            HibernateUtil.commitTransaction();
        }
        super.endTransaction();
    }

    @Override
    public void execute(final List<PersistenceCommand> commands) {
        LOG.debug("execute " + commands.size() + " commands");
        if (commands.size() > 0) {
            if (!HibernateUtil.inTransaction()) {
                HibernateUtil.startTransaction();
            }
            executeCommands(commands);
            HibernateUtil.commitTransaction();
        }
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }
}
