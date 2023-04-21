package org.datacollection.webapp.action;

import java.util.List;
import org.datacollection.model.metadata.DCProject;
import org.datacollection.service.GenericManager;

/**
 *
 * @author Kunal
 */
public class DCProjectAction extends BaseAction {

    private GenericManager<DCProject, Long> dcProjectManager;

    private List dcProjects;

    public void setDCProjectManager(GenericManager<DCProject, Long> dcProjectManager) {
        this.dcProjectManager = dcProjectManager;
    }

    public List getDCProjects() {
        return dcProjects;
    }

    public String list() {
        dcProjects = dcProjectManager.getAll();
        return SUCCESS;
    }
}
