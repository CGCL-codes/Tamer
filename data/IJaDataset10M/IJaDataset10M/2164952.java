package de.suse.swamp.modules.scheduledjobs;

import java.util.*;
import org.apache.turbine.services.schedule.*;
import de.suse.swamp.core.container.*;
import de.suse.swamp.core.security.*;
import de.suse.swamp.core.tasks.*;
import de.suse.swamp.core.util.*;
import de.suse.swamp.core.workflow.*;
import de.suse.swamp.util.*;

public class TimeTriggers extends SWAMPScheduledJob {

    /**
     * Constructor
     */
    public TimeTriggers() {
    }

    /**
     * Run the Jobentry from the scheduler queue. From ScheduledJob.
     *
     * @param job - The job to run.
     */
    public void run(JobEntry job) throws Exception {
        results.reset();
        Logger.DEBUG("Starting scheduled time triggers");
        Date start = new Date(System.currentTimeMillis());
        int i = 0, j = 0;
        List tasks = TaskManager.getAllActiveTasks();
        for (Iterator it = tasks.iterator(); it.hasNext(); ) {
            WorkflowTask task = (WorkflowTask) it.next();
            if (task.getActionType().equals("SendEventAction")) {
                i++;
                task.act();
                if (((SendEventActionResult) task.getResult()).isDone()) {
                    j++;
                    TaskManager.finishTask(task, SWAMPUser.SYSTEMUSERNAME, new ResultList());
                }
            }
        }
        results.addResult(ResultList.MESSAGE, "Triggered " + j + " of " + i + " SendEventActions.");
        Logger.DEBUG("Scheduled Task " + job.getTask() + " ran @: " + start + " and triggered " + j + " of " + i + " SendEventActions");
    }
}
