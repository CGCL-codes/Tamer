package org.jumpmind.symmetric.transport.handler;

import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jumpmind.symmetric.model.NodeSecurity;
import org.jumpmind.symmetric.service.IDataExtractorService;
import org.jumpmind.symmetric.service.IDataService;
import org.jumpmind.symmetric.service.INodeService;
import org.jumpmind.symmetric.service.IRegistrationService;
import org.jumpmind.symmetric.transport.IOutgoingTransport;

public class PullResourceHandler extends AbstractTransportResourceHandler {

    private static final Log logger = LogFactory.getLog(PullResourceHandler.class);

    private INodeService nodeService;

    private IDataService dataService;

    private IDataExtractorService dataExtractorService;

    private IRegistrationService registrationService;

    public void pull(String nodeId, OutputStream outputStream) throws Exception {
        INodeService nodeService = getNodeService();
        NodeSecurity nodeSecurity = nodeService.findNodeSecurity(nodeId);
        if (nodeSecurity != null) {
            if (nodeSecurity.isRegistrationEnabled()) {
                registrationService.registerNode(nodeService.findNode(nodeId), outputStream, false);
            } else {
                if (nodeSecurity.isInitialLoadEnabled()) {
                    dataService.insertReloadEvent(nodeService.findNode(nodeId));
                }
                IOutgoingTransport outgoingTransport = createOutgoingTransport(outputStream);
                dataExtractorService.extract(nodeService.findNode(nodeId), outgoingTransport);
                outgoingTransport.close();
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("Node %s does not exist.", nodeId));
            }
        }
    }

    private INodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(INodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setRegistrationService(IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void setDataService(IDataService dataService) {
        this.dataService = dataService;
    }

    public void setDataExtractorService(IDataExtractorService dataExtractorService) {
        this.dataExtractorService = dataExtractorService;
    }
}
