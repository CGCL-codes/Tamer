package lokahi.agent.task;

import lokahi.agent.AbstractAgentTask;
import lokahi.agent.callable.tomcat.StopTomcatCallable;
import lokahi.agent.dao.AgentJob;
import lokahi.agent.dao.TaskType;
import lokahi.agent.interfaces.AgentTomcat;
import lokahi.agent.util.ProcessFactory;
import lokahi.agent.util.TMCTaskException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Stephen Toback
 * @version $Id: StopTomcatTask.java,v 1.1 2006/03/02 19:19:43 drtobes Exp $
 */
public class StopTomcatTask extends AbstractAgentTask<Boolean> {

    StopTomcatTask(int jobId, TaskType t, AgentTomcat tc, int flowLevel) {
        this.task = new FutureTask<Boolean>(new StopTomcatCallable(tc, flowLevel));
        this.setJobId(jobId);
        this.setType(t);
    }

    StopTomcatTask(int jobId, TaskType t, AgentTomcat tc) {
        this(jobId, t, tc, 0);
    }

    public StopTomcatTask(AgentJob j) {
        this.setJobId(j.getPk());
        this.setType(j.getType());
        int flowLevel = 0;
        AgentTomcat tc = ProcessFactory.getTomcatByHttpPort(j.getOptions()[0]);
        if (j.getOptions().length >= 2) {
            flowLevel = Integer.parseInt(j.getOptions()[1]);
        }
        this.task = new FutureTask<Boolean>(new StopTomcatCallable(tc, flowLevel));
    }

    public StopTomcatTask() {
    }

    public String getResult() throws TMCTaskException, ExecutionException, InterruptedException {
        String ret;
        if (task.get()) {
            ret = "JVM stopped successfully.";
        } else {
            ret = "JVM was unsuccessfully stopped.";
        }
        return ret;
    }
}
