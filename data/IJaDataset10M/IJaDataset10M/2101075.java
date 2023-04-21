package lif.core.handler.gateway;

import lif.core.Constants;
import lk.survey.gis.service.impl.GISServiceStub;
import org.apache.log4j.Logger;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import javax.xml.stream.XMLStreamException;

public class GISGateway {

    private Logger logger = Logger.getLogger(GISGateway.class);

    private boolean isDebugEnabled = logger.isDebugEnabled();

    public String getLatLong(String locationId) throws RemoteException {
        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(Constants.LOCAL_REPO + "/local_repo/repository", null);
        GISServiceStub stub = new GISServiceStub(ctx, "http://localhost:8001/axis2/services/GISService");
        ServiceClient client = stub._getServiceClient();
        client.engageModule("rampart");
        client.engageModule("addressing");
        Options op = client.getOptions();
        try {
            op.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(Constants.LOCAL_REPO + "/local_repo/policy-2.xml"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (XMLStreamException e1) {
            e1.printStackTrace();
        }
        GISServiceStub.GetLocations getLocations0 = new GISServiceStub.GetLocations();
        getLocations0.setParam0(locationId);
        GISServiceStub.GetLocationsResponse res = stub.getLocations(getLocations0);
        return res.get_return();
    }

    public String addLocation(String insertInfo) throws AxisFault {
        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(Constants.LOCAL_REPO + "/local_repo/repository", null);
        GISServiceStub stub = new GISServiceStub(ctx, "http://localhost:8001/axis2/services/GISService");
        ServiceClient client = stub._getServiceClient();
        client.engageModule("rampart");
        client.engageModule("addressing");
        Options op = client.getOptions();
        try {
            op.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(Constants.LOCAL_REPO + "/local_repo/policy-2.xml"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (XMLStreamException e1) {
            e1.printStackTrace();
        }
        GISServiceStub.AddLocation addLocation = new GISServiceStub.AddLocation();
        addLocation.setParam0(insertInfo);
        String response = "";
        try {
            GISServiceStub.AddLocationResponse insertRes = stub.addLocation(addLocation);
            response = insertRes.get_return();
            System.out.println(" response ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void debug(Object str) {
        if (isDebugEnabled) {
            logger.debug("\n" + str + "\n");
        }
    }

    public static Policy loadPolicy(String xpath) throws FileNotFoundException, XMLStreamException {
        StAXOMBuilder builder = new StAXOMBuilder(xpath);
        return PolicyEngine.getPolicy(builder.getDocumentElement());
    }
}
