package at.suas.sepiaxweb.server.service;

import it.naica.sbvr2wf.TransformationInterface;
import java.net.URL;
import org.sun.dbe.ClientHelper;
import at.opaals.sbvrserventservice.SBVR2GrailsResult;
import at.opaals.sbvrserventservice.SBVR2GrailsService;
import at.suas.sepiaxweb.client.ServentSettings;
import at.suas.sepiaxweb.client.service.ServiceException;
import at.suas.sepiaxweb.client.service.XPDLService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import it.naica.sbvr2wf.TransformationServiceImpl;

public class XPDLServiceImpl extends RemoteServiceServlet implements XPDLService {

    public Boolean initService() {
        return true;
    }

    public String tranformSbvr2Xpdl(ServentSettings serventSettings, String sbvrVocabular, String sbvrRules, String processName, String processVersion) throws ServiceException {
        String res = "";
        TransformationServiceImpl ds = new TransformationServiceImpl();
        res = ds.getXPDL(sbvrVocabular, sbvrRules, processName, processVersion);
        return res;
    }
}
