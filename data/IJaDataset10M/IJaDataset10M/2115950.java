package eu.nexofra.wp8.poc.discovery;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.apache.axis2.AxisFault;
import org.junit.Before;
import org.junit.Test;
import eu.soa4all.construction.discovery.client.SemanticDiscoveryClient;
import eu.soa4all.construction.reasoner.wsl.types.WSLService;

public class NEXOF_RA_PoC_ServiceDiscovery {

    protected static final String endpointLocal = "http://localhost:9082/axis2/services/SemanticDiscovery?wsdl";

    protected static final String endpointRemote = "http://nexof-ra.atosorigin.es/axis2/services/SemanticDiscovery?wsdl";

    protected static SemanticDiscoveryClient sdc = null;

    private String endpoint = endpointLocal;

    @Before
    public void setUp() throws URISyntaxException, AxisFault {
        sdc = SemanticDiscoveryClient.getInstance(endpoint);
    }

    @Test
    public void testNEXOFRADiscovery() throws RemoteException {
        ArrayList<String> fClassNames = new ArrayList<String>();
        fClassNames.add("http://www.nexofra.eu/airport_crisis_management#AirportManagement");
        fClassNames.add("http://www.nexofra.eu/airport_crisis_management#FlightManagement");
        fClassNames.add("http://www.nexofra.eu/airport_crisis_management#TravelAgencyCommunication");
        String[] inputs = new String[] { "info" };
        String[] outputs = new String[] { "report" };
        String precondition = "http://www.nexofra.eu/airport_crisis_management#Information(info)";
        String postcondition = "http://www.nexofra.eu/airport_crisis_management#Report(report)";
        ;
        long ti = System.currentTimeMillis();
        List<WSLService> results = sdc.discover(fClassNames.toArray(new String[fClassNames.size()]), inputs, outputs, precondition, postcondition);
        long tf = System.currentTimeMillis();
        assert (results != null);
        System.out.print("SD invocation lasts " + (tf - ti) / 1000.0 + " seconds\n");
        printResults(results, "testNEXOFRADiscovery");
    }

    private void printResults(List<WSLService> results, String testName) {
        if (results == null || results.isEmpty()) {
            System.out.println("Test: " + testName + ". Results null or empty");
            return;
        }
        int i = 0;
        for (Object result : results) {
            System.out.println("Test: " + testName + ". Result# " + (i++) + "= " + result);
        }
    }
}
