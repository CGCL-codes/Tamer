package org.amlfilter.loader;

import java.util.List;
import org.amlfilter.model.SuspectFileProcessingStatus;

/**
 * This class is responsible for loading the entities
 *
 * @author Harish Seshadri
 * @version $Id: PrivateEntityLoaderInterface.java,v 1.1 2007/01/28 07:13:47 hseshadr Exp $
 */
public interface EntityLoaderInterface {

    /**
	 * Process the entities
	 * @param pSuspectFileProcessingStatusList The suspect file processing status
	 * @param pEntityRecordsFilePath The entity records file path
	 * @param pEntityRecordsSourceDataFilePath The entity records source data file path
	 * @return The processed entities log
	 * @throws Exception
	 */
    public String processEntities(List<SuspectFileProcessingStatus> pSuspectFileProcessingStatusList, String pEntityRecordsFilePath, String pEntityRecordsSourceDataFilePath, String pListName) throws Exception;
}
