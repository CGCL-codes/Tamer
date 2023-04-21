package org.rubypeople.eclipse.shams.runtime;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.eclipse.core.runtime.IProgressMonitor;

public class ShamMonitor implements IProgressMonitor {

    private int worked;

    private int totalWorkArg;

    private String taskNameArg;

    private List subTasks = new ArrayList();

    private int cancelAfterWork;

    private boolean cancel;

    private boolean done;

    public void beginTask(String name, int totalWork) {
        taskNameArg = name;
        totalWorkArg = totalWork;
    }

    public void done() {
        done = true;
    }

    public void internalWorked(double work) {
    }

    public boolean isCanceled() {
        return cancel;
    }

    public void setCanceled(boolean value) {
    }

    public void setTaskName(String name) {
    }

    public void subTask(String name) {
        subTasks.add(name);
    }

    public void worked(int work) {
        worked += work;
        if (worked >= cancelAfterWork && cancelAfterWork > 0) cancel = true;
    }

    public void assertDone(int expectedWorked) {
        Assert.assertEquals("Done", true, done);
        Assert.assertEquals("Work done", expectedWorked, worked);
    }

    public void assertTaskBegun(String expectedTaskName, int expectedTotalWork) {
        Assert.assertEquals("taskName", expectedTaskName, taskNameArg);
        Assert.assertEquals("totalWork", expectedTotalWork, totalWorkArg);
    }

    public void assertSubTasks(List expectedSubTasks) {
        Assert.assertEquals(expectedSubTasks, subTasks);
    }

    public void cancelAfter(int work) {
        cancelAfterWork = work;
    }
}
