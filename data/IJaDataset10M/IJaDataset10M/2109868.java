package ro.fortech.peaa.repository.api;

import java.util.List;
import ro.fortech.peaa.domain.Job;

public interface IJobMapper {

    public static final String ID = "JOB_ID";

    public static final String TITLE = "JOB_TITLE";

    public static final String MIN_SALARY = "MIN_SALARY";

    public static final String MAX_SALARY = "MAX_SALARY";

    public static final String TABLE = "JOBS";

    /**
	 * Get the job with the given id 
	 * @param id
	 * @return
	 */
    public Job find(String id);

    /**
	 * Get all jobs
	 * @return the list of jobs
	 */
    public List<Job> findAll();

    /**
	 * Get all jobs only with the id and title fields set
	 * @return the list of jobs with only the id and title attributes set
	 */
    public List<Job> findAllIdAndTitle();

    /**
	 * Insert a job
	 * @param job	the job that will be inserted
	 */
    public void insert(Job job);

    /**
	 * Delete the given job
	 * @param job	the job that will be deleted
	 */
    public void delete(Job job);

    /**
	 * Update the given job
	 * @param job	the job that will be updated
	 */
    public void update(Job job);
}
