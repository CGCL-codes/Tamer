package hu.sztaki.lpds.pgportal.service.workflow;

import hu.sztaki.lpds.information.com.ServiceType;
import hu.sztaki.lpds.information.local.InformationBase;
import hu.sztaki.lpds.storage.service.carmen.commons.FileUtils;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Using the WorkflowDownloadPortlet and the
 * WorkflowUploadPortlet. (helper class)
 *
 * @author lpds
 */
public class WorkflowUpDownloadUtils {

    private static WorkflowUpDownloadUtils instance = null;

    private String sep;

    /**
 * Constructor, creating the singleton instance
 */
    public WorkflowUpDownloadUtils() {
        if (instance == null) {
            instance = this;
            sep = FileUtils.getInstance().getSeparator();
        }
    }

    /**
     * Returns the WorkflowDownloadUtils instance.
     *
     * @return
     */
    public static WorkflowUpDownloadUtils getInstance() {
        if (instance == null) {
            instance = new WorkflowUpDownloadUtils();
        }
        return instance;
    }

    /**
     * Returns a storageID (service url)
     * @return storageID
     */
    public String getStorageID() {
        ServiceType st = InformationBase.getI().getService("storage", "portal", new Hashtable(), new Vector());
        return st.getServiceUrl();
    }

    /**
     * Returns a wfsID (service url)
     * @return WFS service ID(URL)
     */
    public String getWfsID() {
        ServiceType st = InformationBase.getI().getService("wfs", "portal", new Hashtable(), new Vector());
        return st.getServiceUrl();
    }
}
