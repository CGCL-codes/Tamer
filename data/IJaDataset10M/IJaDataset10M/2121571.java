package org.ourgrid.broker.commands.executors;

import org.ourgrid.broker.commands.SchedulerData;
import org.ourgrid.broker.dao.JobDAO;
import org.ourgrid.broker.dao.PeerDAO;
import org.ourgrid.broker.dao.PeerEntry;
import org.ourgrid.broker.dao.WorkerDAO;
import org.ourgrid.common.interfaces.Worker;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.job.JobSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

public class UnwantWorkerExecutor extends SchedulerCommandExecutor {

    private String pID;

    private String wID;

    private long requestID;

    private int jobID;

    private int requiredWorkers;

    private int maxFails;

    private int maxReplicas;

    /**
	 * <UNWANT_WORKER peerID='String' workerID='String' requestID='long' jobID='int' requiredWorkers='int'
	 *          maxFails='int' maxReplicas='int'/>
	 */
    public void execute(SchedulerData data, ServiceManager manager) {
        fillData(data.getXml());
        DeploymentID peerID = new DeploymentID(pID);
        DeploymentID workerID = new DeploymentID(wID);
        String workerPublicKey = manager.getDAO(WorkerDAO.class).getWorkerPublicKey(workerID.getServiceID());
        workerID.setPublicKey(workerPublicKey);
        JobSpec jobSpec = manager.getDAO(JobDAO.class).getJobSpec(jobID);
        RequestSpec spec = new RequestSpec(jobID, jobSpec, requestID, jobSpec.getRequirements(), requiredWorkers, maxFails, maxReplicas, manager.getMyCertPath());
        if (peerID != null && workerID != null) {
            PeerEntry peerEntry = manager.getDAO(PeerDAO.class).getPeerEntry(peerID.getContainerID());
            peerEntry.getLocalWorkerProvider().unwantedWorker(workerID.getServiceID(), spec);
            Object stub = manager.getStub(workerID.getServiceID(), Worker.class);
            manager.release(stub);
            manager.getDAO(WorkerDAO.class).removeWorker(workerID);
        }
    }

    private void fillData(String xml) {
        Document logDocFile = super.getXMl(xml);
        Element element = logDocFile.getDocumentElement();
        pID = element.getAttribute("peerID");
        wID = element.getAttribute("workerID");
        requestID = Long.valueOf(element.getAttribute("requestID"));
        jobID = Integer.valueOf(element.getAttribute("jobID"));
        requiredWorkers = Integer.valueOf(element.getAttribute("requiredWorkers"));
        maxFails = Integer.valueOf(element.getAttribute("maxFails"));
        maxReplicas = Integer.valueOf(element.getAttribute("maxReplicas"));
    }
}
