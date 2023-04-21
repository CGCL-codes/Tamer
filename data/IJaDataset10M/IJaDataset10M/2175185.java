package eu.planets_project.ifr.core.wdt.gui.faces;

import java.util.logging.Logger;
import eu.planets_project.ifr.core.wdt.common.faces.JSFUtil;
import eu.planets_project.ifr.core.wdt.gui.faces.TemplateContainer;
import eu.planets_project.ifr.core.wdt.api.WorkflowBean;
import eu.planets_project.ifr.core.wdt.impl.wf.WFTemplate;

/**
 	* @author Rainer Schmidt
 	* wrapper class providing access to the currently loaded workflow
 	* don't put any other logic here!
 	*/
public class CurrentWorkflowBean implements WorkflowBean {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public CurrentWorkflowBean() {
    }

    protected WorkflowBean getCurrentWorkflowBean() {
        TemplateContainer templateContainer = (TemplateContainer) JSFUtil.getManagedObject("templateContainer");
        WFTemplate currentTemplate = templateContainer.getCurrentTemplate();
        WorkflowBean wfBean = (WorkflowBean) JSFUtil.getManagedObject(currentTemplate.getBeanInstance());
        return wfBean;
    }

    public boolean isWorkflowBeanSelected() {
        TemplateContainer templateContainer = (TemplateContainer) JSFUtil.getManagedObject("templateContainer");
        if (templateContainer.getCurrentTemplate() == null) return false;
        return true;
    }

    public void addInputData(String localFileRef) {
        WorkflowBean wfBean = getCurrentWorkflowBean();
        wfBean.addInputData(localFileRef);
    }

    public void resetInputData() {
        WorkflowBean wfBean = getCurrentWorkflowBean();
        wfBean.resetInputData();
    }

    public void lookupServices() {
        WorkflowBean wfBean = getCurrentWorkflowBean();
        wfBean.lookupServices();
    }

    public void resetServices() {
        WorkflowBean wfBean = getCurrentWorkflowBean();
        wfBean.resetServices();
    }

    public String invokeService() {
        WorkflowBean wfBean = getCurrentWorkflowBean();
        return wfBean.invokeService();
    }
}
