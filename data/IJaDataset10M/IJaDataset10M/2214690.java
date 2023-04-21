package net.deytan.wofee.gae.persistence.action.impl;

import net.deytan.wofee.exception.PersistenceException;
import net.deytan.wofee.exception.ReflectionException;
import net.deytan.wofee.gae.persistence.ActionsFactory;
import net.deytan.wofee.gae.persistence.CacheService;
import net.deytan.wofee.gae.persistence.DatastoreFactory;
import net.deytan.wofee.gae.persistence.action.UpdateAction;
import net.deytan.wofee.gae.persistence.transaction.DatastoreTransactionManager;
import net.deytan.wofee.persistence.PersistableFactory;
import org.springframework.util.Assert;

public class UpdateActionImpl extends AbstractStoreAction implements UpdateAction {

    public UpdateActionImpl(final DatastoreFactory datastoreFactory, final DatastoreTransactionManager transactionManager, final PersistableFactory persistableFactory, final ActionsFactory actionsFactory, final CacheService cacheService) throws PersistenceException {
        super(datastoreFactory, transactionManager, persistableFactory, actionsFactory, cacheService);
    }

    @Override
    public void update(final Object instance) throws PersistenceException {
        try {
            Assert.notNull(this.getInstanceEncodedKey(instance) != null, "encoded key should not be null for update");
            this.store(instance, null);
        } catch (ReflectionException exception) {
            throw new PersistenceException(exception);
        }
    }
}
