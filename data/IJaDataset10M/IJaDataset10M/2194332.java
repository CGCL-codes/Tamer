package org.middleheaven.core.wiring;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.domain.store.DomainStoreServiceActivator;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;
import org.middleheaven.persistance.DataPersistanceServiceActivator;
import org.middleheaven.persistance.db.datasource.DataSourceServiceActivator;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.transactions.AutoCommitTransactionServiceActivator;
import org.middleheaven.transactions.TransactionService;

/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase {

    @Test
    public void testInit() {
        this.getWiringService().addConfiguration(new BindConfiguration() {

            @Override
            public void configure(Binder binder) {
                binder.bind(NameDirectoryService.class).in(Shared.class).to(JNDINameDirectoryService.class);
                binder.bind(DataSourceServiceActivator.class).in(Shared.class).to(DataSourceServiceActivator.class);
                binder.bind(DataPersistanceServiceActivator.class).in(Shared.class).to(DataPersistanceServiceActivator.class);
                binder.bind(DomainStoreServiceActivator.class).in(Shared.class).to(DomainStoreServiceActivator.class);
                binder.bind(AutoCommitTransactionServiceActivator.class).in(Shared.class).to(AutoCommitTransactionServiceActivator.class);
            }
        });
    }
}
