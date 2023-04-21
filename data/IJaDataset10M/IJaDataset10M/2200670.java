package jhomenet.server.polling;

import java.util.List;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import jhomenet.commons.polling.PollingIntervals;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.commons.hw.mngt.HardwareManagerException;

/**
 * Generic hardware polling job. This class is called by the job scheduler
 * on regular polling intervals. The job extracts the particular polling
 * type and the list of sensor hardware to be polled. The class then manages
 * the actual polling of the hardware.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class HardwarePollingJob implements Job {

    /**
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(HardwarePollingJob.class.getName());

    /** 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        PollingIntervals pollingType = (PollingIntervals) jobDataMap.get(PollingService.POLLING_INTERVAL_ID);
        logger.debug("Executing " + pollingType.getDescription() + " job");
        PollingService service = (PollingService) jobDataMap.get(PollingService.POLLING_SERVICE_ID);
        List<String> hardwareToPoll = service.getPollingList(pollingType);
        if (hardwareToPoll != null) {
            HardwareManager hardwareManager = (HardwareManager) jobDataMap.get(PollingService.HW_MANAGER_ID);
            try {
                logger.debug("Polling " + hardwareToPoll.size() + " sensors at " + pollingType.toString() + " interval");
                hardwareManager.captureAndPersistSensorData(hardwareToPoll);
            } catch (HardwareManagerException hme) {
                throw new JobExecutionException(hme);
            }
        } else {
            logger.debug("No sensors to poll at " + pollingType.toString() + " interval");
        }
    }
}
