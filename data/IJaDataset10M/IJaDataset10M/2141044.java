package userguide.mex.clients;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.dataretrieval.DRConstants;
import org.apache.axis2.dataretrieval.client.MexClient;

/**
 * Sample to demostrate  using User-defined Service level WSDL dialect specific 
 * Data Locator to perform data retrievalfor the WS-MEX GetMetadata request.
 * 
 * In the example, dialectLocator for dialect="http://schemas.xmlsoap.org/wsdl/"
 * element was added in the services.xml of the target Service.
 * 
 * Before running the sample, you must first deploy the Axis 2 sample "WSDLDataLocatorDemoService" service i.e.
 * WSDLDataLocatorDemoService.aar file.  
 * 
 */
public class DemoWSDLServiceDataLocator {

    private static EndpointReference targetEPR = new EndpointReference("http://127.0.0.1:8080/axis2/services/WSDLDataLocatorDemoService");

    public static void main(String[] args) {
        try {
            System.out.println("Test getMetadata for " + targetEPR.getAddress());
            System.out.println("Service WSDL specific DataLocator was configured");
            MexClient serviceClient = new MexClient();
            Options options = new Options();
            options.setAction(DRConstants.SPEC.Actions.GET_METADATA_REQUEST);
            options.setTo(targetEPR);
            serviceClient.setOptions(options);
            String identifier = null;
            OMElement method = serviceClient.setupGetMetadataRequest(DRConstants.SPEC.DIALECT_TYPE_WSDL, identifier);
            OMElement result = serviceClient.sendReceive(method);
            System.out.println(result);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }
}
