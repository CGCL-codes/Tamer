package meteor.batch;

import java.util.List;
import java.util.Set;
import meteor.dao.BatchLockDAO;
import meteor.model.BatchLockModel;

/**
 * @author Thomas Rawyler
 * 
 */
public class BatchLockManager {

    private Integer batchJobPerforming;

    private Integer batchJobDone;

    private BatchLockDAO batchLockDAO;

    /**
	 * Use on runBatch() execution to register the batch job in the data base.
	 * TODO implement a wait method
	 * 
	 * @param batchjob
	 * @throws PeriBatchJobDependencyException
	 */
    @SuppressWarnings("unchecked")
    public synchronized void lockMe(IBatchJob batchjob) throws BatchJobDependencyException {
        Set<BatchLockModel> statusData = batchLockDAO.getAll();
        List<String> dependencies = batchjob.getDependsOn();
        if (dependencies != null) {
            for (String dependsOn : dependencies) {
                for (BatchLockModel batchLockModel : statusData) {
                    if (dependsOn.equals(batchLockModel.getName())) {
                        if (batchLockModel.getStatus().equals(batchJobPerforming)) {
                            throw new BatchJobDependencyException("Depending batch job was not available or still running!");
                        }
                    }
                }
            }
        }
        batchLockDAO.saveStatusForName(batchjob.getName(), batchJobPerforming);
    }

    /**
	 * The batch job gets unregistered and its status is set to "done", so that
	 * other depending batch jobs are free to start.
	 * 
	 * @param batchjob
	 */
    public synchronized void unlockMe(IBatchJob batchjob) {
        batchLockDAO.saveStatusForName(batchjob.getName(), batchJobDone);
    }

    /**
	 * @return the batchJobDone
	 */
    public Integer getBatchJobDone() {
        return batchJobDone;
    }

    /**
	 * @param batchJobDone
	 *            the batchJobDone to set
	 */
    public void setBatchJobDone(Integer batchJobDone) {
        this.batchJobDone = batchJobDone;
    }

    /**
	 * @return the batchJobPerforming
	 */
    public Integer getBatchJobPerforming() {
        return batchJobPerforming;
    }

    /**
	 * @param batchJobPerforming
	 *            the batchJobPerforming to set
	 */
    public void setBatchJobPerforming(Integer batchJobPerforming) {
        this.batchJobPerforming = batchJobPerforming;
    }

    /**
	 * @return the batchLockDAO
	 */
    public BatchLockDAO getBatchLockDAO() {
        return batchLockDAO;
    }

    /**
	 * @param batchLockDAO
	 *            the batchLockDAO to set
	 */
    public void setBatchLockDAO(BatchLockDAO batchLockDAO) {
        this.batchLockDAO = batchLockDAO;
    }
}
