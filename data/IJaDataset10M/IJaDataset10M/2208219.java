package org.gbif.portal.harvest.workflow.activity.col;

import org.gbif.portal.dao.DataResourceDAO;
import org.gbif.portal.util.workflow.BaseActivity;
import org.gbif.portal.util.workflow.ProcessContext;

/**
 * This syncs the resource with the named parameters and then adds it the mapped ids in the context 
 * 
 * @author trobertson
 */
public class CreateAndAddResourceToMapActivity extends BaseActivity {

    /**
	 * Names are self explainatory
	 */
    protected String fileUrl;

    protected String keyForCreatedResource = "0";

    protected DataResourceDAO dataResourceDAO;

    /**
     * @see org.gbif.portal.util.workflow.BaseMapContextActivity#doExecute(org.gbif.portal.util.workflow.MapContext)
     */
    @SuppressWarnings("unchecked")
    public ProcessContext execute(ProcessContext context) throws Exception {
        return context;
    }

    /**
	 * @return Returns the dataResourceDAO.
	 */
    public DataResourceDAO getDataResourceDAO() {
        return dataResourceDAO;
    }

    /**
	 * @param dataResourceDAO The dataResourceDAO to set.
	 */
    public void setDataResourceDAO(DataResourceDAO dataResourceDAO) {
        this.dataResourceDAO = dataResourceDAO;
    }

    /**
	 * @return Returns the fileUrl.
	 */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
	 * @param fileUrl The fileUrl to set.
	 */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
	 * @return Returns the keyForCreatedResource.
	 */
    public String getKeyForCreatedResource() {
        return keyForCreatedResource;
    }

    /**
	 * @param keyForCreatedResource The keyForCreatedResource to set.
	 */
    public void setKeyForCreatedResource(String keyForCreatedResource) {
        this.keyForCreatedResource = keyForCreatedResource;
    }
}
