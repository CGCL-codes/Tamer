package hu.sztaki.lpds.wfi.net.wsaxis13.eticsmultinode;

import hu.sztaki.lpds.wfi.com.WorkflowInformationBean;
import hu.sztaki.lpds.wfi.com.WorkflowRuntimeBean;
import hu.sztaki.lpds.wfi.inf.WfiPortalService;
import hu.sztaki.lpds.wfi.service.eticsmultinode.SubmitThread;
import java.util.Vector;

public class WfiPortalServiceImpl implements WfiPortalService {

    @Override
    public String submitWorkflow(WorkflowRuntimeBean pWorkflowData) {
        String res = "Etics-SingleNode" + System.currentTimeMillis() + System.nanoTime();
        new SubmitThread(pWorkflowData, res);
        return res;
    }

    @Override
    public void abortWorkflow(String pRuntimeID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rescueWorkflow(WorkflowRuntimeBean pWorkflowData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getWaitingJob(String pURL) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<WorkflowInformationBean> getInformation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
