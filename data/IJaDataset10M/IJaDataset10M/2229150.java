package org.tripcom.api.ws.client;

import java.net.URL;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * This class is just for testings.
 * 
 * @author Jan-Ole Christian
 *
 */
public class MyClient {

    public static void main(String[] args) throws Exception {
        System.out.println("trying out...");
        String rdfxmlString = "" + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "<rdf:Description rdf:about=\"http://de.wikipedia.org/wiki/Resource_Description_Framework\">\n" + "<dc:title>Resource Description Framework</dc:title>\n" + "</rdf:Description>\n" + "</rdf:RDF>" + "";
        String wsEndpoint = "http://heck.inf.fu-berlin.de:8080/axis/services/Tripcom";
        try {
            wsEndpoint = args[0];
        } catch (Exception e) {
        }
        String wsMethodOut = "out";
        Service service = new Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(new URL(wsEndpoint));
        call.setOperationName(wsMethodOut);
        call.addParameter("rdfxmlStatement", Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("spaceURI", Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("securityInfo", Constants.XSD_STRING, ParameterMode.IN);
        call.setReturnType(Constants.XSD_STRING);
        Object ret;
        ret = call.invoke(new Object[] { rdfxmlString, "tsc://tripcom.inf.fu-berlin.de", "security string" });
        System.out.println(ret);
        System.out.println("trying rd");
        call = (Call) service.createCall();
        call.setTargetEndpointAddress(new URL(wsEndpoint));
        call.setOperationName("rdWithSpace");
        call.addParameter("query", Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("spaceURI", Constants.XSD_STRING, ParameterMode.IN);
        call.addParameter("timeout", Constants.XSD_INT, ParameterMode.IN);
        call.addParameter("securityInfo", Constants.XSD_STRING, ParameterMode.IN);
        call.setReturnType(Constants.XSD_STRING);
        ret = call.invoke(new Object[] { "this is an sparql query", "tsc://tripcom.inf.fu-berlin.de", 100000, "security string" });
        System.out.println(ret);
    }
}
