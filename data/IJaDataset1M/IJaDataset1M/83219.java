package org.openkonnect.applications.openbravo.ws.test;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import org.openkonnect.applications.openbravo.ws.IGatewayWS;
import org.openkonnect.applications.openbravo.ws.OpenkException;

@WebServiceClient(wsdlLocation = "http://localhost:8180/openbravo/webservices/gateway?wsdl")
public class WSClientTest extends Service {

    static String wsdlLoc = "http://localhost:8180/openbravo/webservices/gateway?wsdl";

    static String tns = "http://objectcode.de/openk/webservices";

    static String sn = "GatewayWSService";

    static String port = "GatewayWSPort";

    static IGatewayWS service;

    protected WSClientTest(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    public WSClientTest() throws Exception {
        this(new URL(wsdlLoc), new QName(tns, sn));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(WSClientTest.class);
    }

    @Test(expected = WebServiceException.class)
    public void testTestWSClient() throws OpenkException {
        service = this.getPort(new QName(tns, port), IGatewayWS.class);
        System.out.println("Invoking the echo operations ...");
        String name = "No Name";
        System.out.println(name);
        String sresponse = service.echoString(name);
        System.out.println(sresponse);
        boolean response = service.echo();
        System.out.println(response);
        response = service.echoBool(false);
        System.out.println(service.echoString("Test Done"));
    }

    public static void main(String[] args) {
        try {
            WSClientTest client = new WSClientTest();
            service = client.getPort(new QName(tns, "GatewayWSPort"), IGatewayWS.class);
            client.doTest(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTest(String[] args) throws OpenkException {
        System.out.println("Invoking the echo operations ...");
        String name;
        if (null != args && args.length > 0) {
            name = args[0];
        } else {
            name = "No Name";
        }
        System.out.println(name);
        String sresponse = service.echoString(name);
        System.out.println(sresponse);
        boolean response = service.echo();
        System.out.println(response);
        try {
            response = service.echoBool(false);
        } catch (WebServiceException e) {
            System.out.println("Had this nasty WebService Exception");
        }
        System.out.println(service.echoString("Test Done"));
    }
}
