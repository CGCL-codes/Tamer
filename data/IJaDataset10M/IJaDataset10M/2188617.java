package org.gbif.portal.harvest.workflow.activity.control;

import java.util.Date;
import org.gbif.portal.dao.ResourceAccessPointDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * Sets the params in the DB that are necessary when harvesting 
 * 
 * @author trobertson
 */
public class StartHarvestActivity extends BaseActivity {

    /**
	 * DAOs
	 */
    protected ResourceAccessPointDAO resourceAccessPointDAO;

    /**
	 * Context keys
	 */
    protected String contextKeyRAPId = "resourceAccessPointId";

    public ProcessContext execute(ProcessContext context) throws Exception {
        long id = (Long) context.get(contextKeyRAPId, Long.class, true);
        resourceAccessPointDAO.setStartHarvest(new Date(), id);
        return context;
    }

    /**
	 * @return Returns the resourceAccessPointDAO.
	 */
    public ResourceAccessPointDAO getResourceAccessPointDAO() {
        return resourceAccessPointDAO;
    }

    /**
	 * @param resourceAccessPointDAO The resourceAccessPointDAO to set.
	 */
    public void setResourceAccessPointDAO(ResourceAccessPointDAO resourceAccessPointDAO) {
        this.resourceAccessPointDAO = resourceAccessPointDAO;
    }
}
