package org.xactor.tm;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkCompletedException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

/**
 * An <code>org.jboss.tm.JBossXATerminator</code> to
 * <code>org.xactor.tm.JBossXATerminator</code> adapter.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class JBossXATerminatorAdapter implements org.jboss.tm.JBossXATerminator {

    private org.xactor.tm.JBossXATerminator delegate;

    public JBossXATerminatorAdapter(org.xactor.tm.JBossXATerminator delegate) {
        this.delegate = delegate;
    }

    public void registerWork(Work work, Xid xid, long timeout) throws WorkCompletedException {
        delegate.registerWork(work, xid, timeout);
    }

    public void startWork(Work work, Xid xid) throws WorkCompletedException {
        delegate.startWork(work, xid);
    }

    public void endWork(Work work, Xid xid) {
        delegate.endWork(work, xid);
    }

    public void cancelWork(Work work, Xid xid) {
        delegate.cancelWork(work, xid);
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        delegate.commit(xid, onePhase);
    }

    public void forget(Xid xid) throws XAException {
        delegate.forget(xid);
    }

    public int prepare(Xid xid) throws XAException {
        return delegate.prepare(xid);
    }

    public Xid[] recover(int flag) throws XAException {
        return delegate.recover(flag);
    }

    public void rollback(Xid xid) throws XAException {
        delegate.rollback(xid);
    }
}
