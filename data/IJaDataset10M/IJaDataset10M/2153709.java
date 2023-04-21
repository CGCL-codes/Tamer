package com.ek.mitapp.ui.action;

import java.util.Map;
import org.apache.log4j.Logger;
import com.ek.mitapp.*;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id: StorePostMitDataToFileAction.java 1660 2006-04-11 14:37:22Z dhirwinjr $
 *
 * @author dhirwinjr
 */
public class StorePostMitDataToFileAction extends AbstractJExcelAction {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(StorePostMitDataToFileAction.class.getName());

    /**
	 * Reference variables
	 */
    private final Map collectedData;

    private final MitigationProjectRegistry projectRegistry;

    private final IntersectionRegistry dataRegistry;

    /**
	 * Default constructor.
	 * 
	 * @param collectedData
	 * @param projectRegistry
	 * @param dataRegistry
	 * @param appSettings
	 */
    public StorePostMitDataToFileAction(Map collectedData, MitigationProjectRegistry projectRegistry, IntersectionRegistry dataRegistry, AppSettings appSettings) {
        super(appSettings);
        if (collectedData == null) throw new IllegalArgumentException("Collected data map cannot be null");
        if (projectRegistry == null) throw new IllegalArgumentException("Project registry cannot be null");
        if (dataRegistry == null) throw new IllegalArgumentException("Data registry cannot be null");
        if (appSettings == null) throw new IllegalArgumentException("Application settings cannot be null");
        this.collectedData = collectedData;
        this.projectRegistry = projectRegistry;
        this.dataRegistry = dataRegistry;
    }

    /**
	 * @see com.ek.mitapp.ui.action.AbstractJExcelAction#runImpl()
	 */
    @Override
    protected void runImpl() throws MitigationAppException {
    }
}
