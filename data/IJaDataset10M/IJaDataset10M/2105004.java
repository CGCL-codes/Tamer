package org.ourgrid.broker.ui.sync.command;

import static org.ourgrid.common.interfaces.Constants.LINE_SEPARATOR;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;
import org.ourgrid.broker.status.GridProcessStatusInfo;
import org.ourgrid.broker.status.JobStatusInfo;
import org.ourgrid.broker.status.PeerStatusInfo;
import org.ourgrid.broker.status.TaskStatusInfo;
import org.ourgrid.broker.ui.sync.BrokerSyncApplicationClient;
import org.ourgrid.common.command.UIMessages;
import org.ourgrid.common.interfaces.to.BrokerCompleteStatus;
import org.ourgrid.common.interfaces.to.JobsPackage;
import org.ourgrid.common.interfaces.to.PeersPackage;
import org.ourgrid.common.spec.peer.PeerSpec;
import org.ourgrid.common.status.CompleteStatus;
import org.ourgrid.common.util.StringUtil;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.command.AbstractCommand;

public class BrokerStatusCommand extends AbstractCommand<BrokerSyncApplicationClient> {

    private static final String JOB_ID_FILTER_FLAG = "-i=";

    private static final String JOB_LABEL_FILTER_FLAG = "-l=";

    private static final String JOB_STATUS_FILTER_FLAG = "-s=";

    private static final String VALID_EXECUTION_STATES = "FAILED|FINISHED|ABORTED|CANCELLED|UNSTARTED|RUNNING";

    private boolean hasFilter = false;

    private Integer jobIdFilter;

    private String jobLabelFilter;

    private String jobStatusFilter;

    public BrokerStatusCommand(BrokerSyncApplicationClient application) {
        super(application);
    }

    private void printStatusMessage() {
        BrokerCompleteStatus completeStatus = getComponentClient().getBrokerCompleteStatus();
        System.out.println("Broker status" + LINE_SEPARATOR);
        System.out.println("Uptime: " + StringUtil.getTimeAsText(completeStatus.getUpTime()) + LINE_SEPARATOR);
        printConfiguration(completeStatus);
        printPeers(completeStatus.getPeersPackage());
        printJobs(completeStatus.getJobsPackage());
    }

    private void printConfiguration(CompleteStatus completeStatus) {
        System.out.println("Configuration: " + LINE_SEPARATOR + completeStatus.getConfiguration());
    }

    private void getFilters(String[] params) {
        for (String param : params) {
            if (param.startsWith(JOB_ID_FILTER_FLAG)) {
                String jobIdWanted = null;
                try {
                    jobIdWanted = param.substring(JOB_ID_FILTER_FLAG.length());
                    this.jobIdFilter = new Integer(jobIdWanted);
                    hasFilter = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid job id filter: " + jobIdWanted);
                    throw new IllegalArgumentException("Invalid status parameter: " + param, e);
                }
            } else if (param.startsWith(JOB_STATUS_FILTER_FLAG)) {
                String statusWanted = param.substring(JOB_STATUS_FILTER_FLAG.length());
                Pattern pattern = Pattern.compile(VALID_EXECUTION_STATES, Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(statusWanted).matches()) {
                    this.jobStatusFilter = statusWanted;
                    hasFilter = true;
                } else {
                    System.out.println("Invalid job status filter: " + statusWanted + ". Valid job status filters (case insensitive): " + VALID_EXECUTION_STATES);
                    throw new IllegalArgumentException("Invalid status parameter: " + param);
                }
            } else if (param.startsWith(JOB_LABEL_FILTER_FLAG)) {
                this.jobLabelFilter = param.substring(JOB_LABEL_FILTER_FLAG.length());
                hasFilter = true;
            } else if (param.length() != 0) {
                throw new IllegalArgumentException("Invalid status parameter: " + param);
            }
        }
    }

    private void printJob(JobStatusInfo job) {
        System.out.println(LINE_SEPARATOR + "\t" + "Job " + job.getJobId() + ": " + job.getSpec().getLabel() + " [" + JobStatusInfo.getState(job.getState()) + "]");
        for (TaskStatusInfo task : job.getTasks()) {
            printTask(task);
        }
    }

    private void printJobs(JobsPackage jobsPackage) {
        Map<Integer, JobStatusInfo> jobs = jobsPackage.getJobs();
        System.out.println(LINE_SEPARATOR + "Jobs (" + jobs.size() + "):");
        if (hasFilter) System.out.println("\tUsing filter: " + LINE_SEPARATOR + "\t\t" + (jobIdFilter != null ? "id == " + jobIdFilter : "") + (jobIdFilter != null && (jobStatusFilter != null || jobLabelFilter != null) ? " && " : "") + (jobStatusFilter != null ? "status == " + jobStatusFilter.toUpperCase() : "") + (jobStatusFilter != null && jobLabelFilter != null ? " && " : "") + (jobLabelFilter != null ? "Label == " + jobLabelFilter + "  " : ""));
        for (JobStatusInfo job : jobs.values()) {
            if ((jobIdFilter != null ? jobIdFilter.equals(job.getJobId()) : true) && (jobStatusFilter != null ? jobStatusFilter.equalsIgnoreCase(JobStatusInfo.getState(job.getState())) : true) && (jobLabelFilter != null ? jobLabelFilter.equals(job.getSpec().getLabel()) : true)) {
                printJob(job);
            }
        }
        System.out.println();
    }

    private void printPeers(PeersPackage peers) {
        Collection<PeerStatusInfo> peersCollection = peers.getPeers();
        System.out.println(LINE_SEPARATOR + "Peers (" + peersCollection.size() + "):");
        for (PeerStatusInfo peer : peersCollection) {
            printPeerSpec(peer);
        }
    }

    private void printPeerSpec(PeerStatusInfo peerEntry) {
        String peerLabel = peerEntry.getPeerSpec().getAttribute(PeerSpec.ATT_LABEL);
        System.out.println(LINE_SEPARATOR + "\t" + (peerLabel == null ? "---" : peerLabel) + " => " + peerEntry.getPeerSpec().getUserAndServer() + " [ " + peerEntry.getState() + " ]");
    }

    private void printReplica(GridProcessStatusInfo execution) {
        System.out.println("\t\t\t" + "Replica " + execution.getId() + ": [" + execution.getState() + "] - assigned to " + execution.getWorkerInfo().getWorkerSpec().getUserAndServer());
    }

    private void printTask(TaskStatusInfo task) {
        System.out.println("\t\t" + "Task " + task.getTaskId() + ": [" + task.getState() + "]");
        for (GridProcessStatusInfo replica : task.getGridProcesses()) {
            printReplica(replica);
        }
    }

    protected void execute(String[] params) {
        if (isComponentStarted()) printStatusMessage(); else printNotStartedMessage();
    }

    protected void validateParams(String[] params) throws Exception {
        if (params == null) {
            throw new IllegalArgumentException(UIMessages.INVALID_PARAMETERS_MSG);
        }
        if (params.length > 0) {
            getFilters(params);
        }
    }

    private void printNotStartedMessage() {
        System.out.println("Ourgrid Broker is not started.");
    }
}
