package org.nomadpim.money.core.test;

import junit.framework.TestCase;
import org.nomadpim.core.CoreFacade;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.IEntityContainer;
import org.nomadpim.core.util.test.IExecutorTestChangeSupport;
import org.nomadpim.core.util.threading.DirectExecutor;
import org.nomadpim.core.util.threading.IExecutor;
import org.nomadpim.module.money.account.Account;
import org.nomadpim.module.money.transaction.Transaction;

public abstract class AbstractMoneyPluginTest extends TestCase {

    protected IEntityContainer accountContainer;

    protected IEntityContainer transactionContainer;

    private IExecutor oldAccountContainerExecutor;

    private IExecutor oldTransactionContainerExecutor;

    protected void setUp() throws Exception {
        super.setUp();
        accountContainer = CoreFacade.getContainer(Account.TYPE_NAME);
        transactionContainer = CoreFacade.getContainer(Transaction.TYPE_NAME);
        oldAccountContainerExecutor = ((IExecutorTestChangeSupport) accountContainer).getExecutor();
        ((IExecutorTestChangeSupport) accountContainer).setExecutor(new DirectExecutor());
        oldTransactionContainerExecutor = ((IExecutorTestChangeSupport) transactionContainer).getExecutor();
        ((IExecutorTestChangeSupport) transactionContainer).setExecutor(new DirectExecutor());
    }

    protected void tearDown() throws Exception {
        for (IEntity transaction : transactionContainer.get()) {
            transactionContainer.remove(transaction);
        }
        for (IEntity account : accountContainer.get()) {
            accountContainer.remove(account);
        }
        ((IExecutorTestChangeSupport) accountContainer).setExecutor(oldAccountContainerExecutor);
        ((IExecutorTestChangeSupport) transactionContainer).setExecutor(oldTransactionContainerExecutor);
        super.tearDown();
    }
}
