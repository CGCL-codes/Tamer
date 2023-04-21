package org.appspy.admin.jobs;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appspy.server.bo.scheduler.JobExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public abstract class AbstractAppspyJob implements Job {

    protected static final Log sLog = LogFactory.getLog(AbstractAppspyJob.class);

    public JobExecution getJobExecution(JobExecutionContext jec) {
        return AppspyJobUtils.getJobExecution(jec);
    }

    public BeanFactory getBeanFactory(JobExecutionContext jec) {
        return AppspyJobUtils.getBeanFactory(jec);
    }

    public Object getBean(JobExecutionContext jec, String beanName) {
        return AppspyJobUtils.getBean(jec, beanName);
    }

    public String getJobExecutionParamAsString(JobExecutionContext jec, String paramName) {
        return AppspyJobUtils.getJobExecutionParamAsString(jec, paramName);
    }

    public Date getJobExecutionParamAsDate(JobExecutionContext jec, String paramName) {
        return AppspyJobUtils.getJobExecutionParamAsDate(jec, paramName);
    }

    public Date getJobExecutionParamAsDateTime(JobExecutionContext jec, String paramName) {
        return AppspyJobUtils.getJobExecutionParamAsDateTime(jec, paramName);
    }
}
