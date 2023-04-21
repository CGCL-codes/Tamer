package org.quartz.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Wraps a <code>JobDetail</code> and <code>Trigger</code>.
 * 
 * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
 * @author James House
 */
public class JobSchedulingBundle {

    protected JobDetail jobDetail;

    protected List triggers = new ArrayList();

    public JobSchedulingBundle() {
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public List getTriggers() {
        return triggers;
    }

    public void setTriggers(List triggers) {
        this.triggers = triggers;
    }

    public void addTrigger(Trigger trigger) {
        if (trigger.getStartTime() == null) {
            trigger.setStartTime(new Date());
        }
        triggers.add(trigger);
    }

    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
    }

    public String getName() {
        if (getJobDetail() != null) {
            return getJobDetail().getName();
        } else {
            return null;
        }
    }

    public String getFullName() {
        if (getJobDetail() != null) {
            return getJobDetail().getFullName();
        } else {
            return null;
        }
    }

    public boolean isValid() {
        return ((getJobDetail() != null) && (getTriggers() != null));
    }
}
