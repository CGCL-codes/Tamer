package org.rg.workflow.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rg.common.datatypes.Event;
import org.rg.common.threads.LoadBalancedJob;
import org.rg.workflow.planner.IPlanner;
import org.rg.workflow.planner.RgJob;
import org.rg.workflow.service.IProcedureService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * the job executable that wraps a service in a job as part of a workflow.
 * @author redman
 */
public class JobExecutable implements LoadBalancedJob, ApplicationContextAware {

    /** logger */
    private static final Log log = LogFactory.getLog(JobExecutable.class);

    /** the job handled. */
    private RgJob job = null;

    /** Priority value for this executable while in the command queue. */
    private int priority = 100;

    /** job aproc name. */
    private String _aprocName = null;

    /**
    * Reference to the Application Context.
    */
    private ApplicationContext context = null;

    /** the key uniquely identifies the job. */
    private String key = null;

    /** the planner object. */
    private IPlanner planner = null;

    /**
    * Return a reference to the planner object.
    * @return a reference to the planner object.
    */
    public IPlanner getPlanner() {
        return planner;
    }

    /**
    * Set the planner.
    * @param planner the planner object.
    */
    public void setPlanner(IPlanner planner) {
        this.planner = planner;
    }

    /**
    * default does nothing.
    */
    public JobExecutable() {
    }

    /**
    * given the job this executable works.
    * @param njob the job to execute.
    */
    public JobExecutable(RgJob njob) {
        this.job = njob;
        _aprocName = job.getNextAnalyticalProcedure();
    }

    /**
    * given a job and a priority.
    * @param njob the job.
    * @param prior the priority.
    */
    public JobExecutable(RgJob njob, int prior) {
        this.job = njob;
        setPriority(prior);
        _aprocName = job.getNextAnalyticalProcedure();
    }

    /**
    * Set the application context (this is done by Spring).
    * @param ctx ApplicationContext
    */
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

    /** @return the name of the current procedure. */
    public RgJob getJob() {
        return job;
    }

    /**
    * Execute the code for this executable object.
    */
    public void execute() {
        try {
            job.startAnalyticalProcedure(job.getNextAnalyticalProcedure());
            String ap = job.getCurrentAnalyticalProcedure();
            IProcedureService bean = (IProcedureService) context.getBean(ap);
            _aprocName = ap;
            log.debug("JobExecutable.execute on " + this.toString());
            job = bean.execProcedure(job);
            if (job == null) {
                return;
            }
            job.finishAnalyticalProcedure(ap);
            _aprocName = job.getNextAnalyticalProcedure();
            planner.handleAnalyticsJob(job, "RgAutomated");
        } catch (ClassCastException e) {
            log.error(_aprocName + " is not a WorkflowService, yet it is referenced in a workflow!", e);
            Object obj = job.getPayload();
            if (obj instanceof Event) ((Event) obj).setError(e.getMessage());
            planner.handleAnalyticsJob(job, "RgAutomated");
        } catch (Throwable e) {
            log.error("Throwable in JobExecutable.execute()", e);
            Object obj = job.getPayload();
            if (obj instanceof Event) ((Event) obj).setError(e.getMessage());
            planner.handleAnalyticsJob(job, "RgAutomated");
        }
    }

    /**
    * Set the priority for this executable.
    * @param p the priority of the job, must be an integer in the range of 0 to 100.
    */
    public void setPriority(int p) {
        if ((p >= 0) && (p <= 100)) {
            priority = p;
        } else {
            log.error("Invalid value to JobExecutable.setPriority() " + p + ". Set operation ignored -- priority remains: " + getPriority());
        }
    }

    /**
    * Get the priority for this executable.
    * @return int
    */
    public int getPriority() {
        return priority;
    }

    public String toString() {
        return hashCode() + " :: " + this.getPriority() + " :: " + _aprocName;
    }

    /**
    * Set the RgJob for this executable.
    * @param p int Must be non null.
    */
    public void setJob(RgJob p) {
        if (p == null) {
            log.error("Invalid value to JobExecutable.setJob " + p + ". Set operation ignored.");
            return;
        }
        job = p;
        _aprocName = job.getNextAnalyticalProcedure();
    }

    /** the load incurred on the file system. */
    private float fsload = 0.0f;

    /** load on the network. */
    private float netload = 0.0f;

    /** CPU load. */
    private float cpuload = 1.0f;

    /** CPU load, by default assuming like 512k. */
    private float memoryLoad = 0.0001f;

    /** the priority. */
    private boolean serial = false;

    /**
    * @see org.rg.common.threads.LoadBalancedJob#getFileSystemLoad()
    */
    public final float getFileSystemLoad() {
        return fsload;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#getNetworkLoad()
    */
    public final float getNetworkLoad() {
        return netload;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#getProcessorLoad()
    */
    public final float getProcessorLoad() {
        return cpuload;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#getSerial()
    */
    public final boolean getSerial() {
        return serial;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#setFileSystemLoad(float)
    */
    public void setFileSystemLoad(float fsl) {
        this.fsload = fsl;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#setNetworkLoad(float)
    */
    public void setNetworkLoad(float nl) {
        this.netload = nl;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#setProcessorLoad(float)
    */
    public void setProcessorLoad(float cpu) {
        this.cpuload = cpu;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#setSerial(boolean)
    */
    public void setSerial(boolean ser) {
        this.serial = ser;
    }

    /**
    * Set the key that identifies the job.
    * @param key the jobs unique key.
    * @see org.rg.common.threads.LoadBalancedJob#getJobExcutableKey()
    */
    public void setJobExecutableKey(String key) {
        this.key = key;
        Object bean = context.getBean(key);
        if (bean instanceof IProcedureService) {
            ServiceLoads sl = ((IProcedureService) bean).getLoads();
            this.setProcessorLoad(sl.cpuLoad);
            this.setNetworkLoad(sl.netLoad);
            this.setMemoryLoad(sl.memoryLoad);
            this.setFileSystemLoad(sl.fileLoad);
            this.setSerial(sl.serialize);
        } else {
            final float fsload = 0.0f;
            final float netload = 0.0f;
            final float cpuload = 1.0f;
            final float memoryload = 0.0001f;
            this.setMemoryLoad(memoryload);
            this.setProcessorLoad(cpuload);
            this.setNetworkLoad(netload);
            this.setFileSystemLoad(fsload);
        }
    }

    /**
    * the key that uniquely ids this job is the name of this class.
    * @see org.rg.common.threads.LoadBalancedJob#getJobExcutableKey()
    */
    public String getJobExcutableKey() {
        return this.key;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#getMemoryLoad()
    */
    public float getMemoryLoad() {
        return memoryLoad;
    }

    /**
    * @see org.rg.common.threads.LoadBalancedJob#setMemoryLoad(float)
    */
    public void setMemoryLoad(float memoryLoad) {
        this.memoryLoad = memoryLoad;
    }
}
