package org.apache.hadoop.mapred;

import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.tools.rumen.JobStory;

/**
 * A static ({@link JobID}, {@link JobStory}) mapping, used by {@link JobClient}
 * and {@link JobTracker} for job submission.
 */
public class SimulatorJobCache {

    private static Map<JobID, JobStory> submittedJobs = new HashMap<JobID, JobStory>();

    /**
   * Put ({@link JobID}, {@link JobStory}) into the mapping.
   * @param jobId id of the job.
   * @param job {@link JobStory} object of the job.
   */
    public static void put(JobID jobId, JobStory job) {
        submittedJobs.put(jobId, job);
    }

    /**
   * Get the job identified by {@link JobID} and remove it from the mapping.
   * @param jobId id of the job.
   * @return {@link JobStory} object of the job.
   */
    public static JobStory get(JobID jobId) {
        return submittedJobs.remove(jobId);
    }

    /**
   * Check the job at the head of queue, without removing it from the mapping.
   * @param jobId id of the job.
   * @return {@link JobStory} object of the job.
   */
    public static JobStory peek(JobID jobId) {
        return submittedJobs.get(jobId);
    }
}
