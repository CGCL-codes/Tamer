package org.go.core;

import java.util.Calendar;
import java.util.Date;
import org.go.Go;
import org.go.GoContext;
import org.go.Scheduler;
import org.go.Trigger;
import org.go.WorkDataMap;

/**
 * 
 * @author hejie
 *
 */
public class GoContextImpl implements GoContext {

    private WorkDataMap data;

    public GoContextImpl(Go go, Trigger trigger, Scheduler scheduler) {
        data = new WorkDataMap();
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Calendar getCalendar() {
        return null;
    }

    @Override
    public Date getFireTime() {
        return null;
    }

    @Override
    public long getJobRunTime() {
        return 0;
    }

    @Override
    public WorkDataMap getMergedJobDataMap() {
        return data;
    }

    @Override
    public Date getNextFireTime() {
        return null;
    }

    @Override
    public Date getPreviousFireTime() {
        return null;
    }

    @Override
    public int getRefireCount() {
        return 0;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public Date getScheduledFireTime() {
        return null;
    }

    @Override
    public Scheduler getScheduler() {
        return null;
    }

    @Override
    public Trigger getTrigger() {
        return null;
    }

    @Override
    public boolean isRecovering() {
        return false;
    }

    @Override
    public void put(Object key, Object value) {
    }

    @Override
    public void setResult(Object result) {
    }
}
