package org.apache.openjpa.sdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.event.EndTransactionListener;
import org.apache.openjpa.event.TransactionEvent;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.Closeable;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.concurrent.AbstractConcurrentEventManager;
import org.apache.openjpa.util.UserException;
import commonj.sdo.DataObject;

/**
 * Manager that can be used to track and notify {@link RemoteCommitListener}s on
 * remote commit events. If remote events are enabled, this manager should be
 * installed as a transaction listener on all brokers so that it knows when
 * commits are made.
 * 
 * @author Sergio Tabanelli
 */
public class SDOEndTransactionListener implements EndTransactionListener {

    private static final Localizer _loc = Localizer.forPackage(SDOEndTransactionListener.class);

    SDOEntityManager em;

    private Set<Object> cacheremove = new HashSet<Object>();

    private Set<Object> cachesync = new HashSet<Object>();

    /**
     * Constructor. Supply configuration.
     */
    public SDOEndTransactionListener(SDOEntityManager em) {
        this.em = em;
    }

    public void afterCommit(TransactionEvent event) {
        Broker broker = (Broker) event.getSource();
        Collection trans = event.getTransactionalObjects();
        cacheremove.clear();
        cachesync.clear();
        if (trans.isEmpty()) return;
        Object obj;
        OpenJPAStateManager sm;
        for (Iterator itr = trans.iterator(); itr.hasNext(); ) {
            obj = itr.next();
            sm = broker.getStateManager(obj);
            if (sm == null || !sm.isPersistent() || !sm.isDirty()) continue;
            if (sm.isDeleted()) cacheremove.add(obj); else cachesync.add(obj);
        }
    }

    public void beforeCommit(TransactionEvent event) {
    }

    public void afterRollback(TransactionEvent event) {
        Broker broker = (Broker) event.getSource();
        Collection trans = event.getTransactionalObjects();
        cacheremove.clear();
        cachesync.clear();
        if (trans.isEmpty()) return;
        Object obj;
        OpenJPAStateManager sm;
        for (Iterator itr = trans.iterator(); itr.hasNext(); ) {
            obj = itr.next();
            sm = broker.getStateManager(obj);
            if (sm == null || !sm.isDirty()) continue;
            if (!sm.isNew()) cachesync.add(obj); else cacheremove.add(obj);
        }
    }

    public void afterCommitComplete(TransactionEvent event) {
        sync();
    }

    public void afterRollbackComplete(TransactionEvent event) {
        sync();
    }

    public void afterStateTransitions(TransactionEvent event) {
    }

    private void sync() {
        if (cacheremove.size() > 0) em.removeCachedPC(cacheremove);
        if (cachesync.size() > 0) em.sync(cachesync);
    }
}
