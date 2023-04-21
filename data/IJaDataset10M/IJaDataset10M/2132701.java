package dk.kapetanovic.jaft.transaction;

import java.util.List;
import dk.kapetanovic.jaft.action.Action;
import dk.kapetanovic.jaft.exception.InconsistentStateException;
import dk.kapetanovic.jaft.exception.TransactionException;

public interface Transaction {

    void commit() throws InconsistentStateException, TransactionException;

    void end() throws InconsistentStateException, TransactionException;

    void rollback() throws InconsistentStateException, TransactionException;

    void shouldCommit(boolean commit);

    List<Exception> getCleanupExceptions();

    List<Exception> getCloseExceptions();

    Object registerAction(Action action) throws InconsistentStateException, TransactionException;

    boolean isActive();

    boolean isCommited();

    boolean checkError();

    void setTransactionManager(TransactionManager manager);

    TransactionManager getTransactionManager();
}
