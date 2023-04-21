package de.dgrid.bisgrid.common.bpel.adapter.openesb;

import org.apache.log4j.Logger;
import de.dgrid.bisgrid.common.bpel.BPELWorkflowEngine;
import de.dgrid.bisgrid.common.bpel.adapter.Adapter;
import de.dgrid.bisgrid.services.management.deployment.ProcessDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.OtherFilesDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.WsdlFilesDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.XsdFilesDocument;

public class OpenESBAdapter extends Adapter {

    private Logger log = Logger.getLogger(this.getClass());

    public OpenESBAdapter() {
        super(new OpenESBMonitoringAdapter(), new OpenESBDeploymentAdapter());
    }

    @Override
    public String getWorkflowEndpointUrl(ProcessDocument deploymentDescriptor, org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelFile, WsdlFilesDocument wsdlFiles, XsdFilesDocument xsdFiles, OtherFilesDocument otherFiles, BPELWorkflowEngine engine) {
        log.info("determining service endpoint for workflow in OpenESB");
        try {
            OpenESBDeploymentAdapter adapter = new OpenESBDeploymentAdapter();
            return adapter.getWorkflowEndpointUrl(deploymentDescriptor, bpelFile, wsdlFiles, xsdFiles, otherFiles, engine);
        } catch (Exception e) {
            log.error(e);
            return e.getMessage();
        }
    }
}
