package org.nexopenframework.core.scheduling;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import junit.framework.TestCase;
import org.easymock.MockControl;
import org.nexopenframework.business.BusinessService;
import org.nexopenframework.core.scheduling.annotations.Scheduler;
import org.nexopenframework.core.scheduling.commonj.MockTimerManager;
import org.nexopenframework.core.scheduling.commonj.management.CommonJManagedObjectMBean;
import org.nexopenframework.core.scheduling.commonj.management.CommonJSchedulerManager;
import org.nexopenframework.core.scheduling.quartz.MethodSchedulerService;
import org.nexopenframework.core.scheduling.quartz.SchedulerFactoryBean;
import org.nexopenframework.core.scheduling.quartz.management.QuartzManagedObjectMBean;
import org.nexopenframework.core.scheduling.quartz.management.QuartzSchedulerManager;
import org.nexopenframework.spring.context.BootstrapDispatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;
import commonj.timers.TimerManager;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Test for scheduler implementations</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.1
 * @since 1.0
 */
public class MethodSchedulerTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testScheduleQuartz() throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer("schedulerTest");
        ObjectName objName = new ObjectName("openfrwk.core:service=schedulerInfo");
        try {
            SchedulerFactoryBean sfb = new SchedulerFactoryBean();
            sfb.setSchedulerName("openfrwk");
            Properties quartzProperties = new Properties();
            quartzProperties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
            quartzProperties.setProperty("org.quartz.threadPool.threadCount", "1");
            quartzProperties.setProperty("org.quartz.threadPool.threadPriority", "5");
            quartzProperties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
            sfb.setQuartzProperties(quartzProperties);
            sfb.afterPropertiesSet();
            org.quartz.Scheduler quartzScheduler = (org.quartz.Scheduler) sfb.getObject();
            final MethodSchedulerService schedulerService = new MethodSchedulerService();
            QuartzSchedulerManager manager = new QuartzSchedulerManager();
            manager.setScheduler(quartzScheduler);
            server.registerMBean(manager, objName);
            schedulerService.setSchedulerManager(manager);
            schedulerService.setScheduler(quartzScheduler);
            final MyScheduledService myService = new MyScheduledService();
            myService.setProperty("some lovely property");
            ReflectionUtils.doWithMethods(MyScheduledService.class, new ReflectionUtils.MethodCallback() {

                public void doWith(Method m) {
                    if (m.isAnnotationPresent(Scheduler.class)) {
                        Scheduler scheduler = m.getAnnotation(Scheduler.class);
                        schedulerService.schedule(scheduler, myService, m);
                    }
                }
            });
            System.out.println("[DEBUG] Thread name :: " + Thread.currentThread().getName());
            BootstrapDispatcher dispatcher = new BootstrapDispatcher();
            dispatcher.setListeners(Collections.singletonList(manager));
            ApplicationContext ac = (ApplicationContext) MockControl.createControl(ApplicationContext.class).getMock();
            dispatcher.onApplicationEvent(new ContextRefreshedEvent(ac));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            List<ObjectName> managedObjects = manager.listSchedulerManagedObjects();
            for (ObjectName objectName : managedObjects) {
                QuartzManagedObjectMBean qm = (QuartzManagedObjectMBean) MBeanServerInvocationHandler.newProxyInstance(server, objectName, QuartzManagedObjectMBean.class, false);
                qm.executeScheduler();
                qm.pauseScheduler();
                qm.resumeScheduler();
                qm.executeScheduler();
            }
            manager.destroy();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } finally {
            if (server.isRegistered(objName)) {
                try {
                    server.unregisterMBean(objName);
                } catch (Throwable e) {
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testScheduleCommonJ() {
        try {
            TimerManager tm = new MockTimerManager();
            MBeanServer server = MBeanServerFactory.createMBeanServer("schedulerTest");
            ObjectName objName = new ObjectName("openfrwk.core:service=schedulerInfo");
            CommonJSchedulerManager manager = new CommonJSchedulerManager();
            manager.setTimerManager(tm);
            server.registerMBean(manager, objName);
            final org.nexopenframework.core.scheduling.commonj.MethodSchedulerService schedulerService = new org.nexopenframework.core.scheduling.commonj.MethodSchedulerService();
            schedulerService.setSchedulerManager(manager);
            schedulerService.setTimerManager(tm);
            final MyScheduledService myService = new MyScheduledService();
            myService.setProperty("some lovely property");
            ReflectionUtils.doWithMethods(MyScheduledService.class, new ReflectionUtils.MethodCallback() {

                public void doWith(Method m) {
                    if (m.isAnnotationPresent(Scheduler.class)) {
                        Scheduler scheduler = m.getAnnotation(Scheduler.class);
                        schedulerService.schedule(scheduler, myService, m);
                    }
                }
            });
            System.out.println("[DEBUG] Thread name :: " + Thread.currentThread().getName());
            List<ObjectName> managedObjects = manager.listSchedulerManagedObjects();
            for (ObjectName objectName : managedObjects) {
                CommonJManagedObjectMBean qm = (CommonJManagedObjectMBean) MBeanServerInvocationHandler.newProxyInstance(server, objectName, CommonJManagedObjectMBean.class, false);
                qm.executeScheduler();
            }
            manager.destroy();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }

    public static class MyScheduledService implements BusinessService {

        private String property;

        public void setProperty(String property) {
            this.property = property;
        }

        public String getName() {
            return null;
        }

        public List<String> doSomething() {
            List<String> values = new ArrayList<String>(2);
            values.add(new Date().toString());
            values.add(Locale.getDefault().toString());
            return values;
        }

        @Scheduler(period = 500, delay = 1000, executeOnStartup = true)
        public void doScheduling() {
            List<String> values = this.doSomething();
            System.out.println("[DEBUG][doScheduling][" + Thread.currentThread().getName() + "] property ::" + property);
            System.out.println("[DEBUG][doScheduling][" + Thread.currentThread().getName() + "]  Values :: \n " + values + "");
        }

        @Scheduler(period = 250, delay = 1000)
        public void anotherScheduling() {
            List<String> values = this.doSomething();
            System.out.println("[DEBUG][anotherScheduling][" + Thread.currentThread().getName() + "] property ::" + property);
            System.out.println("[DEBUG][anotherScheduling][" + Thread.currentThread().getName() + "]  Values :: \n " + values + "");
        }
    }
}
