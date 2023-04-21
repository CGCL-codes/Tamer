package org.nexopenframework.core.scheduling.quartz.tx;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.nexopenframework.core.scheduling.Scheduler;
import org.nexopenframework.core.scheduling.SchedulerException;
import org.nexopenframework.core.scheduling.SchedulerJob;
import org.nexopenframework.core.scheduling.context.SchedulerContext;
import org.nexopenframework.core.scheduling.quartz.SchedulerService;
import org.nexopenframework.transaction.NullPlatformTransactionManager;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public class SchedulerFactoryBeanTest {

    @Test
    public void processWithAnnotationAtClassLevel() throws Exception {
        final TxSchedulerFactoryBean schfb = new TxSchedulerFactoryBean();
        schfb.setTransactionManager(new NullPlatformTransactionManager());
        final GenericApplicationContext mock = new GenericApplicationContext();
        mock.refresh();
        schfb.setApplicationContext(mock);
        schfb.afterPropertiesSet();
        final SchedulerService service = new SchedulerService();
        service.setScheduler((org.quartz.Scheduler) schfb.getObject());
        final Scheduler sch = new Scheduler(true, "0/1 * * * * ?");
        sch.setName("example");
        service.schedule(sch, MyExampleJob.class);
        mock.close();
        Thread.sleep(1000);
    }

    @Test
    public void processWithAnnotationAtMethodLevel() throws Exception {
        final TxSchedulerFactoryBean schfb = new TxSchedulerFactoryBean();
        schfb.setTransactionManager(new NullPlatformTransactionManager());
        final GenericApplicationContext mock = new GenericApplicationContext();
        mock.refresh();
        schfb.setApplicationContext(mock);
        schfb.afterPropertiesSet();
        final SchedulerService service = new SchedulerService();
        service.setScheduler((org.quartz.Scheduler) schfb.getObject());
        final Scheduler sch = new Scheduler(true, "0/1 * * * * ?");
        sch.setName("example.method");
        service.schedule(sch, MyExampleMethodJob.class);
        mock.close();
        Thread.sleep(1000);
    }

    @Transactional
    public static class MyExampleJob implements SchedulerJob {

        public void executeJob(final SchedulerContext context) throws SchedulerException {
            System.out.println("MyExampleJob.executeJob() :: " + TransactionAspectSupport.currentTransactionStatus());
            assertTrue(TransactionAspectSupport.currentTransactionStatus().isNewTransaction());
        }
    }

    public static class MyExampleMethodJob implements SchedulerJob {

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void executeJob(final SchedulerContext context) throws SchedulerException {
            System.out.println("MyExampleMethodJob.executeJob() :: annotation at method level <> " + TransactionAspectSupport.currentTransactionStatus());
            assertTrue(TransactionAspectSupport.currentTransactionStatus().isNewTransaction());
        }
    }
}
