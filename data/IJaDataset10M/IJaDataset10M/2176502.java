package com.intel.gpe.client2.expert.workfloweditor.requests;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import com.intel.gpe.client2.expert.i18n.Messages;
import com.intel.gpe.client2.expert.i18n.MessagesKeys;
import com.intel.gpe.client2.expert.workfloweditor.ApplyInputPanelValuesVisitor;
import com.intel.gpe.client2.expert.workfloweditor.MainWorkflow;
import com.intel.gpe.client2.requests.BaseRequest;

/**
 * @version $Id: SaveWorkflowRequest.java,v 1.9 2007/02/06 11:45:13 vashorin Exp $
 * @author Valery Shorin
 */
public class SaveWorkflowRequest extends BaseRequest {

    private String fileName;

    private MainWorkflow workflow;

    private JAXBContext jc;

    public SaveWorkflowRequest(String fileName, MainWorkflow workflow, JAXBContext jc) {
        super(Messages.getString(MessagesKeys.expert_workfloweditor_requests_SaveWorkflowRequest_Saving_workflow));
        this.fileName = fileName;
        this.workflow = workflow;
        if (jc == null) {
            throw new RuntimeException("JAXBContext can't be null");
        }
        this.jc = jc;
    }

    public Object perform() throws Throwable {
        workflow.accept(new ApplyInputPanelValuesVisitor());
        Marshaller m = jc.createMarshaller();
        System.out.println(jc.toString());
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File outputFile = new File(fileName);
        FileOutputStream os = new FileOutputStream(outputFile);
        m.marshal(workflow, os);
        os.close();
        return null;
    }
}
