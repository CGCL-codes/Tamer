package net.persister.transaction;

import net.persister.connection.Batcher;
import net.persister.connection.Connectional;
import net.persister.mediator.Mediator;
import net.persister.persistentContext.InnerPersistentContext;

/**
 * @author Park, chanwook
 *
 */
public interface TransactionContext extends TransactionBehavior, Connectional {

    public Transaction getTransaction();

    public Mediator getMediator();

    public void begin(Transaction transaction);

    public Batcher getBatcher();

    public InnerPersistentContext getInnerPersistentContext();

    public void close();

    public boolean bindedTransaction();
}
