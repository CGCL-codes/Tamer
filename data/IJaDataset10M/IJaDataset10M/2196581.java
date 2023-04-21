package it.txt.soa4all.serviceinvoker;

import org.w3c.dom.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import com.ibm.wsdl.xml.WSDLReaderImpl;

/**
 * @author TXT - G.Di Matteo, M. Villa, E. Del Grosso
 * Dynamically invocation of semantic WSDL services 
 */
public class ServiceInvoker {

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static String GROUNDING_SERVICE_ENDPOINT = "http://stronghold.ontotext.com:8080/groundingService";

    public static String SOAPVERSION = SOAPConstants.SOAP_1_1_PROTOCOL;

    public String executeWsdlService(String wsdlURL, String serviceName, String operationName, String lifted_input, String lifting_schema_url, String lowering_schema_url) throws Exception {
        String lifted_output = "";
        String lowered_input = "";
        String lowered_output = "";
        String format = "rdf/xml-abbrev";
        String Lifting_requestURL = GROUNDING_SERVICE_ENDPOINT + "/doLifting/";
        ;
        String Lowering_requestURL = GROUNDING_SERVICE_ENDPOINT + "/doLowering/";
        ;
        HttpClient httpclient = new HttpClient();
        PostMethod LOWpostMtd = new PostMethod(Lowering_requestURL);
        LOWpostMtd.addRequestHeader("xslt-url", URLEncoder.encode(lowering_schema_url, "UTF-8"));
        RequestEntity RE = new ByteArrayRequestEntity(lifted_input.getBytes(), "xml/application");
        LOWpostMtd.setRequestEntity(RE);
        int LOWresult = httpclient.executeMethod(LOWpostMtd);
        System.out.println("LOWERING SERVICE response status code: " + LOWresult);
        if (LOWresult != 200) {
            System.out.println("LOWERING SERVICE Error message: ");
            System.out.println(LOWpostMtd.getResponseHeader("Error"));
            lowered_input = "";
        } else {
            System.out.println("LOWERING Response OK");
            lowered_input = LOWpostMtd.getResponseBodyAsString();
            System.out.println("LOWERED input = " + lowered_input);
            lowered_output = interactWsdlService(wsdlURL, serviceName, operationName, lowered_input);
            System.out.println("LOWERED output = " + lowered_output);
        }
        LOWpostMtd.releaseConnection();
        if (lowered_output != "") {
            PostMethod LIFTpostMtd = new PostMethod(Lifting_requestURL);
            LIFTpostMtd.addRequestHeader("xslt-url", URLEncoder.encode(lifting_schema_url, "UTF-8"));
            LIFTpostMtd.addRequestHeader("format", URLEncoder.encode(format, "UTF-8"));
            RequestEntity RE2 = new ByteArrayRequestEntity(lowered_output.getBytes(), "xml/application");
            LIFTpostMtd.setRequestEntity(RE2);
            int LIFTresult_code = httpclient.executeMethod(LIFTpostMtd);
            System.out.println("LIFTING SERVICE Response status code: " + LIFTresult_code);
            if (LIFTresult_code != 200) {
                System.out.println("LIFTING SERVICE Error message: ");
                System.out.println(LIFTpostMtd.getResponseHeader("Error"));
                lifted_output = "";
            } else {
                System.out.println("LIFTING SERVICE Response OK");
                lifted_output = LIFTpostMtd.getResponseBodyAsString();
                System.out.println("LIFTED OUTPUT = " + lifted_output);
            }
            LIFTpostMtd.releaseConnection();
        }
        return lifted_output;
    }

    public static String interactWsdlService(String wsdlURL, String serviceName, String operationName, String lowered_XML) {
        String result = "";
        String namespace = "";
        QName portQN = new QName(namespace, "");
        WSDLReaderImpl WRead = new WSDLReaderImpl();
        try {
            System.out.println("\n---wsdlURL: " + wsdlURL);
            System.out.println("\n---serviceName: " + serviceName);
            System.out.println("\n---operationName: " + operationName);
            System.out.println("\n---lowered_XML: " + lowered_XML);
            javax.wsdl.Definition WDef = WRead.readWSDL(wsdlURL);
            namespace = WDef.getTargetNamespace();
            QName serviceQN = new QName(namespace, serviceName);
            Service service = Service.create(new URL(wsdlURL), serviceQN);
            for (Iterator<QName> i = service.getPorts(); i.hasNext(); ) {
                portQN = i.next();
                System.out.println("Found port: " + portQN.toString());
            }
            System.out.println("Selected PortQN: " + portQN);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(portQN, SOAPMessage.class, Service.Mode.MESSAGE);
            MessageFactory mf = MessageFactory.newInstance(SOAPVERSION);
            SOAPMessage request = mf.createMessage();
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope env = part.getEnvelope();
            SOAPHeader header = env.getHeader();
            SOAPBody body = env.getBody();
            SOAPElement operation = body.addChildElement(operationName, "", namespace);
            String strDOM = lowered_XML;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document d = parser.parse(new ByteArrayInputStream(strDOM.getBytes()));
            Element e = d.getDocumentElement();
            BrowseNodeChildren((Node) e, operation);
            request.saveChanges();
            System.out.println("INVOKING Service: " + serviceQN.toString());
            System.out.println("INVOKE request: \n" + request.getSOAPPart().getEnvelope().toString());
            SOAPMessage response = dispatch.invoke(request);
            String prefix = response.getSOAPBody().getFirstChild().getPrefix() + ":";
            System.out.println("Removing prefix from SOAP response: " + prefix);
            result = response.getSOAPBody().getFirstChild().toString().replaceAll(prefix, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void BrowseNode(Node xmlNode, SOAPElement eleSOAP) {
        try {
            {
                {
                    if (xmlNode.getNodeType() == Node.TEXT_NODE) {
                        String strTextValue = xmlNode.getNodeValue().trim();
                        if (!strTextValue.equals("")) eleSOAP.addTextNode(strTextValue);
                    } else {
                        if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
                            String strNameTreeNode = xmlNode.getNodeName();
                            SOAPElement value = eleSOAP.addChildElement(strNameTreeNode);
                            if (xmlNode.hasChildNodes()) {
                                NodeList nodeList = xmlNode.getChildNodes();
                                for (int i = 0; i < nodeList.getLength(); i++) {
                                    Node xNode = nodeList.item(i);
                                    BrowseNode(xNode, value);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void BrowseNodeChildren(Node xmlNode, SOAPElement eleSOAP) {
        try {
            if (xmlNode.hasChildNodes()) {
                NodeList nodeList = xmlNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                        String strTextValue = nodeList.item(i).getNodeValue().trim();
                        if (!strTextValue.equals("")) eleSOAP.addTextNode(strTextValue);
                    } else {
                        Node xNode = nodeList.item(i);
                        if (xNode.getNodeType() == Node.ELEMENT_NODE) {
                            String strNameTreeNode = xNode.getNodeName();
                            SOAPElement value = eleSOAP.addChildElement(strNameTreeNode);
                            BrowseNodeChildren(xNode, value);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
