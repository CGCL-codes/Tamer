package net.empego.zkcdi.persistence;

import java.util.List;
import javax.faces.FacesException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

public class ExecutionListener implements ExecutionInit, ExecutionCleanup {

    private static final String STARTED_TX_KEY = "_started_tx_";

    @Override
    public void init(final Execution exec, final Execution parent) throws Exception {
        startTransaction(exec);
    }

    @Override
    public void cleanup(final Execution exec, final Execution parent, final List<Throwable> errs) throws Exception {
        commitTransaction(exec, errs);
    }

    private UserTransaction getUserTransaction() throws NamingException {
        return (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
    }

    private void startTransaction(final Execution exec) {
        try {
            final UserTransaction utx = getUserTransaction();
            if (utx.getStatus() != Status.STATUS_ACTIVE) {
                utx.begin();
                exec.getAttributes().put(STARTED_TX_KEY, new Object());
            }
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new FacesException(e);
        }
    }

    private void commitTransaction(final Execution exec, final List<Throwable> errs) {
        try {
            final UserTransaction utx = getUserTransaction();
            if (utx.getStatus() == Status.STATUS_ACTIVE && exec.getAttributes().containsKey(STARTED_TX_KEY)) {
                if (errs != null && errs.isEmpty() == false) {
                    utx.rollback();
                    exec.getAttributes().remove(STARTED_TX_KEY);
                } else {
                    utx.commit();
                    exec.getAttributes().remove(STARTED_TX_KEY);
                }
            }
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new FacesException(e);
        }
    }
}
