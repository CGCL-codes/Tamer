package maze.commons.ee.transactional.delayed.impl;

import maze.commons.ee.delay.CollectorOnlyDelayOperator;
import maze.commons.ee.delay.DelayOperator;
import maze.commons.ee.delay.impl.DelayOperatorImpl;
import maze.commons.ee.hibernate.HibernateTransactionalManFactory;
import maze.commons.ee.hibernate.wrapper.HibernateTransactionalManFactoryWrapper;
import maze.commons.ee.transactional.delayed.DelayedTransactionalManFactory;

/**
 * @author Normunds Mazurs
 * 
 */
public class DelayedTransactionalManFactoryImpl extends HibernateTransactionalManFactoryWrapper implements DelayedTransactionalManFactory {

    private final DelayOperator<HibernateTransactionalManFactory> delayOperator = new DelayOperatorImpl<HibernateTransactionalManFactory>();

    public DelayedTransactionalManFactoryImpl(final HibernateTransactionalManFactory hibernateTransactionalManFactoryToExt) {
        super(hibernateTransactionalManFactoryToExt);
    }

    @Override
    public DelayOperator<HibernateTransactionalManFactory> getFullDelayOperator() {
        return delayOperator;
    }

    @Override
    public CollectorOnlyDelayOperator<HibernateTransactionalManFactory> getDelayOperator() {
        return getFullDelayOperator();
    }
}
