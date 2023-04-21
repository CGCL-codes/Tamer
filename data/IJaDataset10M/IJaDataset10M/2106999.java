package org.nexopenframework.scheduling.quartz.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nexopenframework.module.kernel.spring3.Spring3Kernel;
import org.nexopenframework.scheduling.quartz.SchedulerFactoryBean;
import org.nexopenframework.scheduling.quartz.model.QuartzCronTrigger;
import org.nexopenframework.test.jdbc.DerbyEmbeddedDatabaseConfigurer;
import org.nexopenframework.test.jdbc.JdbcTestSupport;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.support.MethodInvokingRunnable;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 *  <p>NexOpen Framework</p>
 * 
 * <p>Just a simple TestCase for {@link NexOpenBeanJobFactory} and transactional 
 * extension {@link TxNexOpenBeanJobFactory}</p>
 * 
 * @see org.nexopenframework.scheduling.quartz.support.NexOpenBeanJobFactory
 * @see org.nexopenframework.scheduling.quartz.support.TxNexOpenBeanJobFactory
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-08-26 14:18:44 +0100 $ 
 * @since 2.0.0.GA
 */
public class NexOpenBeanJobFactoryTest {

    static final Log logger = LogFactory.getLog(NexOpenBeanJobFactoryTest.class);

    /***/
    private ConfigurableApplicationContext applicationContext;

    final DataSourceTransactionManager tm = new DataSourceTransactionManager();

    /***/
    private final Spring3Kernel kernel = new Spring3Kernel();

    @Test
    public void testTxNexOpenBeanJobFactory() throws Exception {
        final DataSource ds = createDataSource();
        final DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(ds);
        final SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setDataSource(ds);
        final ClassLoader cls = Thread.currentThread().getContextClassLoader();
        final InputStream is = cls.getResourceAsStream("quartz.properties");
        try {
            Resource configLocation = new InputStreamResource(is);
            factoryBean.setConfigLocation(configLocation);
            final TxNexOpenBeanJobFactory jobFactory = createTxNexOpenBeanJobFactory(tm);
            factoryBean.setJobFactory(jobFactory);
            factoryBean.setApplicationContext(applicationContext);
            factoryBean.afterPropertiesSet();
            factoryBean.start();
            final Scheduler scheduler = factoryBean.getObject();
            assertNotNull(scheduler);
            assertTrue(scheduler.isStarted());
            final CronTrigger trigger = new CronTrigger();
            trigger.setName("example." + System.currentTimeMillis());
            trigger.setGroup("example");
            trigger.setCronExpression("0/1 * * * * ?");
            scheduler.scheduleJob(JobDetailFactory.createJobDetail(new TxTask(), new QuartzCronTrigger("0/2 * * * * ?"), "example"), trigger);
            logger.info("Non clustered Scheduler Factory started");
            Thread.sleep(500);
            factoryBean.destroy();
        } finally {
            is.close();
        }
    }

    @Test
    public void testTxNexOpenBeanJobFactoryMethodInvoker() throws Exception {
        final DataSource ds = createDataSource();
        tm.setDataSource(ds);
        final SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setDataSource(ds);
        final ClassLoader cls = Thread.currentThread().getContextClassLoader();
        final InputStream is = cls.getResourceAsStream("quartz.properties");
        try {
            final Resource configLocation = new InputStreamResource(is);
            factoryBean.setConfigLocation(configLocation);
            final TxNexOpenBeanJobFactory jobFactory = createTxNexOpenBeanJobFactory(tm);
            factoryBean.setJobFactory(jobFactory);
            factoryBean.setApplicationContext(applicationContext);
            factoryBean.afterPropertiesSet();
            factoryBean.start();
            final Scheduler scheduler = factoryBean.getObject();
            assertNotNull(scheduler);
            assertTrue(scheduler.isStarted());
            final CronTrigger trigger = new CronTrigger();
            trigger.setName("example." + System.currentTimeMillis());
            trigger.setGroup("example");
            trigger.setCronExpression("0/1 * * * * ?");
            final MethodInvokingRunnable runnable = new MethodInvokingRunnable();
            runnable.setTargetClass(MyComponentIf.class);
            runnable.setTargetMethod("doBusiness");
            runnable.afterPropertiesSet();
            scheduler.scheduleJob(JobDetailFactory.createJobDetail(runnable, new QuartzCronTrigger("0/2 * * * * ?"), "example"), trigger);
            logger.info("Non clustered Scheduler Factory started");
            Thread.sleep(500);
            factoryBean.destroy();
        } finally {
            is.close();
        }
    }

    @Test
    public void testNexOpenBeanJobFactory() throws Exception {
        final SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        final ClassLoader cls = Thread.currentThread().getContextClassLoader();
        InputStream is = cls.getResourceAsStream("quartz.properties");
        try {
            Resource configLocation = new InputStreamResource(is);
            factoryBean.setConfigLocation(configLocation);
            final NexOpenBeanJobFactory jobFactory = new NexOpenBeanJobFactory();
            jobFactory.setApplicationContext(applicationContext);
            jobFactory.setDependencyCheck(true);
            factoryBean.setJobFactory(jobFactory);
            factoryBean.setApplicationContext(applicationContext);
            factoryBean.afterPropertiesSet();
            factoryBean.start();
            final Scheduler scheduler = factoryBean.getObject();
            assertNotNull(scheduler);
            assertTrue(scheduler.isStarted());
            final CronTrigger trigger = new CronTrigger();
            trigger.setName("example");
            trigger.setGroup("example");
            trigger.setCronExpression("0/1 * * * * ?");
            scheduler.scheduleJob(JobDetailFactory.createJobDetail(new Task(), new QuartzCronTrigger("0/2 * * * * ?"), "example"), trigger);
            logger.info("Non clustered Scheduler Factory started");
            Thread.sleep(500);
            factoryBean.destroy();
        } finally {
            is.close();
        }
    }

    @Before
    public void setUp() {
        JdbcTestSupport.startDatabase(DerbyEmbeddedDatabaseConfigurer.class);
        applicationContext = new GenericApplicationContext();
        final MyService service = new MyService();
        final MyComponent component = new MyComponent();
        component.setService(service);
        final ProxyFactory pf = new ProxyFactory();
        pf.addAdvice(new TransactionInterceptor(tm, new AnnotationTransactionAttributeSource()));
        pf.addInterface(MyComponentIf.class);
        pf.setTarget(component);
        applicationContext.getBeanFactory().registerSingleton("service", service);
        applicationContext.getBeanFactory().registerSingleton("component", pf.getProxy());
        applicationContext.refresh();
        kernel.setApplicationContext(applicationContext);
        kernel.start();
    }

    @After
    public void tearDown() {
        try {
            if (applicationContext != null) {
                applicationContext.close();
                applicationContext = null;
            }
            kernel.destroy();
        } finally {
            JdbcTestSupport.stopDatabase();
        }
    }

    /**
	 * @return
	 */
    final DataSource createDataSource() {
        final DriverManagerDataSource ds = new DriverManagerDataSource();
        {
            ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setUrl("jdbc:derby:nexopen;create=true");
        }
        return ds;
    }

    /**
	 * @param tm
	 * @return
	 */
    final TxNexOpenBeanJobFactory createTxNexOpenBeanJobFactory(final DataSourceTransactionManager tm) {
        final TxNexOpenBeanJobFactory jobFactory = new TxNexOpenBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        final ArrayList<Class<? extends Annotation>> annotations = new ArrayList<Class<? extends Annotation>>();
        annotations.add(Transactional.class);
        jobFactory.setTransactionAnnotations(annotations);
        jobFactory.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        jobFactory.setTransactionManager(tm);
        jobFactory.setDependencyCheck(true);
        return jobFactory;
    }

    public static class MyService {

        public void doBusiness() {
            logger.info("Invoked doBusiness");
        }
    }

    public static class Task implements Runnable {

        private MyService service;

        public void setService(final MyService service) {
            this.service = service;
        }

        public void run() {
            logger.info("Example of execution simple");
            service.doBusiness();
        }
    }

    public static interface MyComponentIf {

        void doBusiness();
    }

    public static class MyComponent implements MyComponentIf {

        private MyService service;

        public void setService(final MyService service) {
            this.service = service;
        }

        @Transactional
        public void doBusiness() {
            logger.info("Example of execution doBusiness from MyComponent");
            service.doBusiness();
        }
    }

    public static class TxTask implements Runnable, Serializable {

        /** */
        private static final long serialVersionUID = 1L;

        private MyService service;

        public void setService(final MyService service) {
            this.service = service;
        }

        @Transactional
        public void run() {
            logger.info("Example of execution simple with TX");
            service.doBusiness();
        }
    }
}
