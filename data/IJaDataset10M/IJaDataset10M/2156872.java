package org.modulefusion.featurepack.hibernate.internal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.modulefusion.featurepack.hibernate.ThreadLocalEntityManagerService;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionInterceptor implements MethodInterceptor {

    final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);

    private ServiceTracker tracker;

    public TransactionInterceptor(BundleContext context) {
        tracker = new ServiceTracker(context, ThreadLocalEntityManagerService.class.getName(), null);
        tracker.open();
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        ThreadLocalEntityManagerService service = (ThreadLocalEntityManagerService) tracker.getService();
        if (service == null) {
            RuntimeException e = new NullPointerException("ThreadLocalEntityManagerService must not be null!");
            logger.error("ThreadLocalEntityManagerService not available!", e);
            throw e;
        }
        try {
            logger.trace("Executing transactional method '{}'", mi.getMethod().getName());
            boolean txOpenedByThisMethod = false;
            boolean activeTx = service.getEntityManager().getTransaction().isActive();
            if (!activeTx) {
                logger.trace("Starting transaction for method call");
                service.beginTransaction();
                txOpenedByThisMethod = true;
            } else {
                logger.trace("Found Active transaction");
            }
            Object result = mi.proceed();
            if (txOpenedByThisMethod) {
                service.commitTransaction();
            }
            return result;
        } catch (Throwable e) {
            logger.error("Error while executing transactional method. Setting rollback-only", e);
            service.setRollbackOnlyTransaction();
            throw new RuntimeException(e);
        }
    }
}
