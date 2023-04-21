package com.subshell.persistence.mapper;

import junit.framework.TestCase;
import org.easymock.MockControl;
import com.subshell.persistence.exception.PersistenceException;
import com.subshell.persistence.transaction.Transaction;
import com.subshell.persistence.transaction.TransactionContext;

public class TransactionContextPersistenceMapperFactoryTest extends TestCase {

    public void testGetMapper() throws PersistenceException {
        MockControl configurationControl = MockControl.createControl(PersistenceMapperConfiguration.class);
        PersistenceMapperConfiguration configuration = (PersistenceMapperConfiguration) configurationControl.getMock();
        Class<Integer> clazz1 = Integer.class;
        configuration.getMapperForClass(clazz1);
        configurationControl.setReturnValue(Mock1Mapper.class);
        MockControl transactionControl = MockControl.createNiceControl(Transaction.class);
        Transaction transaction = (Transaction) transactionControl.getMock();
        PersistenceMapperFactory mapperFactory = new TransactionContextPersistenceMapperFactory(configuration);
        configurationControl.replay();
        transactionControl.replay();
        PersistenceMapper mapper1;
        PersistenceMapper mapper2;
        try {
            TransactionContext context = new TransactionContext(transaction);
            TransactionContext.setCurrentContext(context);
            mapper1 = mapperFactory.getMapper(Integer.class);
            mapper2 = mapperFactory.getMapper(Integer.class);
        } finally {
            TransactionContext.clearCurrentContext();
        }
        assertSame(mapper1, mapper2);
        configurationControl.verify();
        transactionControl.verify();
    }
}
