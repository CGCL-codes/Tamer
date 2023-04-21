package aurora.plugin.xapool;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.objectweb.jotm.Jotm;
import aurora.transaction.ITransactionService;
import aurora.transaction.UserTransactionImpl;

public class TransactionService implements ITransactionService {

    Jotm jotm = null;

    boolean useTransactionManager;

    public TransactionService(boolean useTransactionManager) throws NamingException {
        this.useTransactionManager = useTransactionManager;
        if (useTransactionManager) {
            jotm = new Jotm(true, false);
        }
    }

    public TransactionManager getTransactionManager() {
        return useTransactionManager ? jotm.getTransactionManager() : null;
    }

    public UserTransaction getUserTransaction() {
        if (useTransactionManager) return jotm.getUserTransaction(); else return new UserTransactionImpl();
    }

    public void stop() {
        if (jotm != null) jotm.stop();
    }
}
