package org.ourgrid.peer.controller;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.ourgrid.common.interfaces.WorkerSpecListener;
import org.ourgrid.common.interfaces.to.LocalWorkerState;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.common.statistics.beans.peer.Worker;
import org.ourgrid.common.statistics.control.WorkerControl;
import org.ourgrid.peer.controller.messages.WorkerMessages;
import org.ourgrid.peer.dao.AllocationDAO;
import org.ourgrid.peer.dao.LocalWorkersDAO;
import org.ourgrid.peer.dao.statistics.WorkerDAO;
import org.ourgrid.peer.to.AllocableWorker;
import org.ourgrid.peer.to.LocalAllocableWorker;
import org.ourgrid.peer.to.LocalWorker;
import org.ourgrid.peer.to.RemoteAllocableWorker;
import org.ourgrid.reqtrace.Req;
import br.edu.ufcg.lsd.commune.api.InvokeOnDeploy;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.identification.ServiceID;
import br.edu.ufcg.lsd.commune.network.xmpp.XMPPProperties;

/**
 * Performs Worker Spec Listener Controller actions
 */
public class WorkerSpecListenerController implements WorkerSpecListener {

    private ServiceManager serviceManager;

    @SuppressWarnings("unused")
    @InvokeOnDeploy
    public void init(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    /** 
	 * Updates the worker specification, with the new values for dynamic attributes
	 * @param workerSpec New values for dynamic attributes
	 * @param workerPublicKey Worker public key
	 */
    @Req("REQ107")
    public void updateWorkerSpec(WorkerSpec workerSpec) {
        String workerPublicKey = serviceManager.getSenderPublicKey();
        ServiceID senderServiceID = serviceManager.getSenderServiceID();
        String address = senderServiceID.getUserName() + "@" + senderServiceID.getServerName();
        AllocableWorker allocableWorker = serviceManager.getDAO(AllocationDAO.class).getAllocableWorker(workerPublicKey);
        Worker worker = WorkerControl.getInstance().findActiveWorker(address, serviceManager.getDAO(WorkerDAO.class));
        if (worker != null && getMasterPeerAddress(serviceManager.getContainerContext()).equals(worker.getPeer().getAddress())) {
            LocalWorker localWorker = WorkerControl.getInstance().getLocalWorker(serviceManager.getSenderServiceID(), serviceManager.getDAO(LocalWorkersDAO.class));
            if ((localWorker == null || localWorker.getStatus().equals(LocalWorkerState.CONTACTING)) && (allocableWorker == null || allocableWorker.isWorkerLocal())) {
                serviceManager.getLog().debug(WorkerMessages.getUnknownWorkerUpdatingSpecMessage(workerPublicKey));
                return;
            }
            if (localWorker != null) {
                DeploymentID localWorkerDID = serviceManager.getStubDeploymentID(localWorker.getWorkerManagement());
                WorkerSpec newWorkerSpec = updateWorkerSpecAttributes(workerSpec, localWorker.getAttributes(), localWorkerDID);
                localWorker.setWorkerSpec(newWorkerSpec);
                LocalAllocableWorker localAllocableWorker = serviceManager.getDAO(AllocationDAO.class).getLocalAllocableWorker(localWorkerDID.getPublicKey());
                if (localAllocableWorker != null) {
                    localAllocableWorker.getLocalWorker().setWorkerSpec(newWorkerSpec);
                }
                WorkerControl.getInstance().updateWorker(newWorkerSpec, serviceManager);
                return;
            }
        }
        RemoteAllocableWorker remoteAllocWorker = (RemoteAllocableWorker) allocableWorker;
        if (remoteAllocWorker == null) {
            serviceManager.getLog().debug(WorkerMessages.getUnknownWorkerUpdatingSpecMessage(workerPublicKey));
            return;
        }
        WorkerSpec rWorkerSpec = remoteAllocWorker.getWorkerSpec();
        DeploymentID remoteWorkerDID = serviceManager.getStubDeploymentID(allocableWorker.getWorkerManagement());
        WorkerSpec newWorkerSpec = updateWorkerSpecAttributes(workerSpec, rWorkerSpec.getAttributes(), remoteWorkerDID);
        remoteAllocWorker.setWorkerSpec(newWorkerSpec);
    }

    private String getMasterPeerAddress(ContainerContext context) {
        String address = context.getProperty(XMPPProperties.PROP_USERNAME) + "@" + context.getProperty(XMPPProperties.PROP_XMPP_SERVERNAME);
        return address;
    }

    /**
	 * Updates the worker especification attributes
	 * @param workerSpec New values for dynamic attributes 
	 * @param oldAttributes Old attributes
	 * @param workerObjectID Worker object identification
	 * @return New worker especification updated
	 */
    @Req("REQ107")
    private WorkerSpec updateWorkerSpecAttributes(WorkerSpec workerSpec, Map<String, String> oldAttributes, DeploymentID workerObjectID) {
        Map<String, String> newAttributes = workerSpec.getAttributes();
        Collection<String> newAttributeKeys = newAttributes.keySet();
        Map<String, String> attributes = new LinkedHashMap<String, String>(oldAttributes);
        for (String newAttributeKey : newAttributeKeys) {
            if (newAttributeKey.equalsIgnoreCase(WorkerSpec.ATT_USERNAME) || newAttributeKey.equalsIgnoreCase(WorkerSpec.ATT_SERVERNAME)) {
                newAttributes.remove(newAttributeKey);
                continue;
            }
            String newAttributeValue = newAttributes.get(newAttributeKey);
            if (newAttributeValue == null) {
                attributes.remove(newAttributeKey);
                continue;
            }
            attributes.put(newAttributeKey, newAttributeValue);
        }
        serviceManager.getLog().debug(WorkerMessages.getWorkerSpecUpdatedMessage(workerObjectID, newAttributes));
        return new WorkerSpec(attributes);
    }
}
