package org.gridbus.broker.services.compute.condor;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.log4j.Logger;
import org.gridbus.broker.common.Job;
import org.gridbus.broker.constants.JobStatus;
import org.gridbus.broker.constants.MiddlewareType;
import org.gridbus.broker.services.compute.fork.ForkComputeServer;
import org.gridbus.broker.services.compute.fork.ForkJobWrapper;
import org.gridbus.broker.services.compute.fork.ForkWindowsDriver;
import org.gridbus.broker.util.BrokerUtil;
import org.gridbus.broker.util.SleepUtil;
import org.gridbus.broker.util.dispatchers.DispatchUtil;

/**
 * This class represents a compute server running Condor
 * @author Xingchen Chu (xchu@csse.unimelb.edu.au), Tianchi Ma (tcma@cs.mu.oz.au)
 * @version 3.0
 */
public class CondorComputeServer extends ForkComputeServer {

    /**
	 * Logger for this class
	 */
    private final Logger logger = Logger.getLogger(CondorComputeServer.class);

    /**
	 * Default Constructor
	 */
    public CondorComputeServer() {
        this("localhost", false);
    }

    /**
	 * @param hostname
	 */
    public CondorComputeServer(String hostname) {
        this(hostname, false);
    }

    /**
	 * @param hostname
	 * @param sharedFileSystem
	 */
    private CondorComputeServer(String hostname, boolean sharedFileSystem) {
        super(hostname);
        if (hostname != null && hostname.trim().equalsIgnoreCase("localhost")) {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                logger.debug("CondorComputeServer Error Couldnot get inetaddress of localhost");
            }
        }
    }

    /**
	 * Reads the query status from the file,
	 * and returns the job-status. One of the constants defined in JobStatus class.
	 * @param queryStatusFile
	 * @return
	 * @throws IOException
	 */
    protected int parseStatus(Job job, String result) {
        int status = JobStatus.UNKNOWN;
        if (result == null || result.trim().length() == 0) {
            status = JobStatus.STAGE_OUT;
        } else {
            if (result.trim().equalsIgnoreCase("1")) status = JobStatus.PENDING; else if (result.trim().equalsIgnoreCase("2")) status = JobStatus.ACTIVE;
        }
        return status;
    }

    /**
	 * @see org.gridbus.broker.farming.common.ComputeServer#getMiddlewareType()
	 */
    public int getMiddlewareType() {
        return MiddlewareType.CONDOR;
    }

    /**
	 * @see org.gridbus.broker.common.ComputeServer#queryJobStatus(org.gridbus.broker.common.Job)
	 */
    public int queryJobStatus(Job job) throws Exception {
        DispatchUtil dispatcher = null;
        int status = JobStatus.UNKNOWN;
        int retryQuery = 3;
        try {
            String hostname = getHostname();
            dispatcher = DispatchUtil.getInstance(hostname);
            dispatcher.connect(hostname, job.getUserCredential());
            String condorQueryCmd = "condor_q " + job.getHandle() + " -format %d JobStatus";
            while (retryQuery-- > 0) {
                String result = dispatcher.executeAndGetResult(condorQueryCmd, 1000);
                if (logger.isDebugEnabled()) logger.debug("*********************** query status result for handle " + job.getHandle() + " = " + result);
                status = parseStatus(job, result);
                if (status == JobStatus.STAGE_OUT) {
                    SleepUtil.safeSleep(SleepUtil.HALFSMALL_SLEEP);
                } else {
                    break;
                }
            }
        } finally {
            if (dispatcher != null) {
                dispatcher.disconnect();
                dispatcher = null;
            }
        }
        return status;
    }
}
