package es.caib.bpm.jmx;

import org.jboss.system.ServiceMBean;
import org.jbpm.JbpmConfiguration;

public interface JBPMServiceMBean extends ServiceMBean {

    public int getInterval();

    public void setInterval(int interval);

    public int getScheduledInterval();

    public void setScheduledInterval(int interval);

    public int getSchedulerThreads();

    public void setSchedulerThreads(int schedulerThreads);

    public void start() throws Exception;

    public void stop();
}
