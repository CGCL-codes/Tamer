package pl.edu.agh.uddiProxy.init;

import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import pl.edu.agh.uddiProxy.UserManager;

public class UserManagerInitializer implements ServletContextListener {

    private static final int TRIGGER_PERIOD = 24;

    private static Logger logger = Logger.getLogger(ParameterManagerInitializer.class);

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            initializeTimeServies();
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    private void initializeTimeServies() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        JobDetail jobDetail = new JobDetail("userManager", null, UserManager.class);
        Trigger trigger = TriggerUtils.makeHourlyTrigger(TRIGGER_PERIOD);
        trigger.setName("UserManagerTrigger");
        trigger.setStartTime(new Date());
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
