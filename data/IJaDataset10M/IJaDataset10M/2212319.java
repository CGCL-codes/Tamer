package org.ourgrid.broker.commands.executors;

import org.ourgrid.broker.BrokerConstants;
import org.ourgrid.broker.commands.SchedulerData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.processor.filetransfer.OutgoingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferSender;

public class StartTransferExecutor extends SchedulerCommandExecutor {

    private long handleId;

    private String localFileName;

    private String destinationFileName;

    private String description;

    private String id;

    /**
	 * <START_TRANSFER handleId='long' localFileName='String' destinationFileName='String' 
	 * description='String' id='String'/> 
	 */
    public void execute(SchedulerData data, ServiceManager manager) {
        fillData(data.getXml());
        OutgoingTransferHandle tHandle = new OutgoingTransferHandle(handleId, localFileName, destinationFileName, description, new DeploymentID(id));
        TransferSender sender = (TransferSender) manager.getObjectDeployment(BrokerConstants.WORKER_CLIENT).getObject();
        manager.startTransfer(tHandle, sender);
    }

    private void fillData(String xml) {
        Document logDocFile = super.getXMl(xml);
        Element element = logDocFile.getDocumentElement();
        handleId = Long.valueOf(element.getAttribute("handleId"));
        localFileName = element.getAttribute("localFileName");
        destinationFileName = element.getAttribute("destinationFileName");
        description = element.getAttribute("description");
        id = element.getAttribute("id");
    }
}
