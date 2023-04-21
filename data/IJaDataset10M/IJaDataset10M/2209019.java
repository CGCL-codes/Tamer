package org.icenigrid.gridsam.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icenigrid.gridsam.core.ConfigurationException;
import org.icenigrid.gridsam.core.JobState;
import org.icenigrid.gridsam.core.hostenv.FileProviderUtil;
import org.icenigrid.gridsam.core.plugin.JobContext;

/**
 * <strong>Purpose:</strong><br>
 * cancle the file job for the GridSAM Job
 * 
 * @version 1.0.1 2008-7-21<br>
 * @author Liu Jie<br>
 * 
 */
public class FileJobCancelUtil {

    private static Log log = LogFactory.getLog(FileJobCancelUtil.class);

    /**
	 * stop all file job.
	 * 
	 * @param pJob
	 * @return
	 */
    public static boolean stopAllFileJob(JobContext pJob) {
        FileProviderUtil fileProviderUtil = null;
        try {
            fileProviderUtil = new FileProviderUtil();
        } catch (ConfigurationException e) {
            log.error("Error when new FileProviderUtil() : " + e.getMessage());
        }
        if (pJob.getJobInstance().isInStage(JobState.STAGING_IN)) {
            Map stageInFileMap = (Map) pJob.getJobInstance().getProperties().get(HPCGJobPropertyConstants.STAGEIN_FILE_MAP);
            if (stageInFileMap != null) {
                Set jobNameSet = stageInFileMap.keySet();
                Iterator it = jobNameSet.iterator();
                while (it.hasNext()) {
                    String jobName = (String) it.next();
                    String fileJobStatus = (String) stageInFileMap.get(jobName);
                    if (fileJobStatus.equalsIgnoreCase("done") || fileJobStatus.equalsIgnoreCase("failed") || fileJobStatus.equalsIgnoreCase("completed")) {
                        continue;
                    } else {
                        try {
                            fileProviderUtil.cancelFileJob(jobName);
                        } catch (Exception e) {
                            log.error("Error when cancel aftJob. jobName : " + jobName + ". Exception message :" + e.getMessage());
                        }
                    }
                }
            }
        }
        if (pJob.getJobInstance().isInStage(JobState.STAGING_OUT)) {
            Map stageOutFileList = (Map) pJob.getJobInstance().getProperties().get(HPCGJobPropertyConstants.STAGEOUT_FILE_MAP);
            if (stageOutFileList == null) {
                return true;
            }
            Set sourceURL = stageOutFileList.keySet();
            Iterator it = sourceURL.iterator();
            while (it.hasNext()) {
                String jobName = (String) it.next();
                String fileJobStatus = (String) stageOutFileList.get(jobName);
                if (fileJobStatus.equalsIgnoreCase("done") || fileJobStatus.equalsIgnoreCase("failed") || fileJobStatus.equalsIgnoreCase("completed")) {
                    continue;
                } else {
                    try {
                        fileProviderUtil.cancelFileJob(jobName);
                    } catch (Exception e) {
                        log.warn("Error when cancel aftJob. jobName : " + jobName + ". Exception message :" + e.getMessage());
                    }
                }
            }
        }
        return true;
    }
}
