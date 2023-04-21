package net.sf.buildbox.buildrobot.impl;

import net.sf.buildbox.reactor.api.JobQueryDao;
import net.sf.buildbox.reactor.model.ExecutionState;
import net.sf.buildbox.reactor.model.JobExecution;
import net.sf.buildbox.reactor.model.JobId;
import java.util.*;
import java.util.logging.Logger;

public class JobEliminator {

    private final JobQueryDao jobQueryDao;

    private static final Logger LOGGER = Logger.getLogger(JobEliminator.class.getName());

    public JobEliminator(JobQueryDao jobQueryDao) {
        this.jobQueryDao = jobQueryDao;
    }

    public Set<JobExecution> findEliminationCandidates(Set<JobExecution> reducedPlan) {
        final Map<String, BatchInfo> plannedBatchesByProject = new HashMap<String, BatchInfo>();
        for (JobExecution jobExecution : reducedPlan) {
            final JobId jobId = jobExecution.getJobId();
            final String projectId = jobId.getProjectId();
            BatchInfo batchInfo = plannedBatchesByProject.get(projectId);
            if (batchInfo == null) {
                batchInfo = new BatchInfo(jobExecution.getBatchId());
                plannedBatchesByProject.put(projectId, batchInfo);
            }
            batchInfo.addExecution(jobExecution);
        }
        final Set<JobExecution> candidates = new HashSet<JobExecution>();
        for (String projectId : plannedBatchesByProject.keySet()) {
            LOGGER.finer("Seeking candidates for " + projectId);
            final BatchInfo plannedBatchInfo = plannedBatchesByProject.get(projectId);
            final Collection<JobExecution> jobExecutions = jobQueryDao.getExecutions(projectId, null, null, null, null, ExecutionState.WAITING, ExecutionState.READY);
            final Map<String, BatchInfo> olderBatchesByBatchId = new LinkedHashMap<String, BatchInfo>();
            for (JobExecution jobExecution : jobExecutions) {
                final String batchId = jobExecution.getBatchId();
                if (batchId.equals(plannedBatchInfo.getBatchId())) continue;
                if (jobExecution.getPriority() > plannedBatchInfo.getPriority()) continue;
                if (jobExecution.getExecutionState() == ExecutionState.RUNNING) continue;
                if (jobExecution.getTimeLastImpact().after(plannedBatchInfo.getTimeLastImpact())) continue;
                BatchInfo olderBatchInfo = olderBatchesByBatchId.get(batchId);
                if (olderBatchInfo == null) {
                    olderBatchInfo = new BatchInfo(batchId);
                    olderBatchesByBatchId.put(batchId, olderBatchInfo);
                }
                olderBatchInfo.addExecution(jobExecution);
            }
            for (BatchInfo olderBatchInfo : olderBatchesByBatchId.values()) {
                if (olderBatchInfo.getJobset().equals(plannedBatchInfo.getJobset())) {
                    candidates.addAll(olderBatchInfo.getExecutions());
                    LOGGER.finer(" - found batch: " + olderBatchInfo.getBatchId());
                } else {
                    LOGGER.finer(String.format(" - different jobsets: %s%s <> %s%s", olderBatchInfo.getBatchId(), olderBatchInfo.getJobset(), plannedBatchInfo.getBatchId(), plannedBatchInfo.getJobset()));
                }
            }
        }
        return candidates;
    }

    private static class BatchInfo {

        private final String batchId;

        private final Set<String> jobset = new HashSet<String>();

        private final Set<JobExecution> executions = new HashSet<JobExecution>();

        private int priority = 0;

        private Date timeLastImpact;

        public BatchInfo(String batchId) {
            this.batchId = batchId;
        }

        public void addExecution(JobExecution jobExecution) {
            final Date newTimeLastImpact = jobExecution.getTimeLastImpact();
            if (timeLastImpact == null) {
                timeLastImpact = newTimeLastImpact;
            } else if (timeLastImpact.after(newTimeLastImpact)) {
                timeLastImpact = newTimeLastImpact;
            }
            jobset.add(jobExecution.getJobId().getTargetName());
            executions.add(jobExecution);
            final int p = jobExecution.getPriority();
            if (p > priority) {
                priority = p;
            }
        }

        public String getBatchId() {
            return batchId;
        }

        public Set<String> getJobset() {
            return jobset;
        }

        public int getPriority() {
            return priority;
        }

        public Date getTimeLastImpact() {
            return timeLastImpact;
        }

        public Set<JobExecution> getExecutions() {
            return executions;
        }
    }
}
