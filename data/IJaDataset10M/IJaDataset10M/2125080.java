package com.mycila.jdbc.tx;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TransactionInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = Logger.getLogger(TransactionInterceptor.class.getName());

    private TransactionDefinitionBuilder transactionDefinitionBuilder;

    private TransactionManager transactionManager;

    @Inject
    public void setTransactionDefinitionBuilder(TransactionDefinitionBuilder transactionDefinitionBuilder) {
        this.transactionDefinitionBuilder = transactionDefinitionBuilder;
    }

    @Inject
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionDefinition definition = transactionDefinitionBuilder.build(invocation.getMethod(), invocation.getThis().getClass());
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("TX: " + invocation.getMethod() + " - begin TX");
        Transaction transaction = transactionManager.beginTransaction(definition);
        TransactionHolder.push(transaction);
        Object retVal;
        try {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("TX: " + invocation.getMethod() + " - proceed...");
            retVal = invocation.proceed();
        } catch (Throwable throwable) {
            if (definition.rollbackOn(throwable)) {
                try {
                    if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("TX: " + invocation.getMethod() + " - rollback because of: " + throwable.getMessage());
                    transaction.rollback();
                } catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            } else {
                try {
                    if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("TX: " + invocation.getMethod() + " - commit");
                    transaction.commit();
                } catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            }
            throw throwable;
        } finally {
            TransactionHolder.pop();
        }
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("TX: " + invocation.getMethod() + " - commit");
        transaction.commit();
        return retVal;
    }
}
