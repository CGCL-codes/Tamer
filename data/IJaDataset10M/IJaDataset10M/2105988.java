package gr.bluecore.webwatch;

import java.util.Date;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import com.opensymphony.xwork.Action;

public class StartMonitor implements Action {

    private String message = null;

    public String execute() {
        message = "Monitor Started";
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            PropertiesConfiguration config = new PropertiesConfiguration("sites.properties");
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            int period = config.getInt("runevery");
            sched.addGlobalJobListener(new ListenJob());
            sched.start();
            JobDetail jobDetail = new JobDetail("myURLMonitor", null, Monitor.class);
            Trigger trigger = TriggerUtils.makeMinutelyTrigger(period);
            trigger.setName("myMonitor");
            sched.scheduleJob(jobDetail, trigger);
            System.out.println("Job started " + new Date());
        } catch (SchedulerException e) {
            message = "Start failed <br> " + e.getMessage();
        } catch (ConfigurationException e) {
            message = "Start failed <br> " + e.getMessage();
        }
        return SUCCESS;
    }

    public String stopmonitor() {
        try {
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            sched.deleteJob("myURLMonitor", null);
            sched.shutdown(false);
            message = "Monitor has shutdown";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
