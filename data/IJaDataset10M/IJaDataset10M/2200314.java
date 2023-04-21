package org.juddi.response;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
import org.juddi.UDDIException;
import org.juddi.base.*;
import org.juddi.request.*;
import org.juddi.response.*;

/**
 * ServiceDetailResponseHandler
 *
 * @author  Alex Ceponkus
 * @author  Steve Viens
 * @author  Graeme Riddell
 * @author  Chris Dellario
 * @version 0.1 9/14/2000
 * @since   JDK1.2.2
 */
public class ServiceDetailResponseHandler implements UDDIXMLHandler {

    private ServiceDetailResponse serviceDetailResponse = null;

    private static UDDIXMLHandlerMaker maker = null;

    public ServiceDetailResponseHandler() {
    }

    public Object create(Node node) throws UDDIException {
        serviceDetailResponse = new ServiceDetailResponse();
        maker = UDDIXMLHandlerMaker.getInstance();
        Element element = (Element) node;
        if (element.getAttributeNode("operator") != null) {
            Attr attr = element.getAttributeNode("operator");
            maker = UDDIXMLHandlerMaker.getInstance();
            OperatorHandler operatorHandler = (OperatorHandler) maker.lookup("operator");
            Operator op = (Operator) operatorHandler.create((Node) attr);
            serviceDetailResponse.setOperator(op);
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("businessService")) {
                    BusinessServiceHandler h = (BusinessServiceHandler) maker.lookup("businessService");
                    BusinessService businessService = (BusinessService) h.create(children.item(i));
                    serviceDetailResponse.addBusinessService(businessService);
                }
            }
        }
        return serviceDetailResponse;
    }

    /**
   * test driver
   */
    public static void main(String args[]) {
        final String xmlFile = "temp.xml";
        final String xml = "<serviceDetail generic=\"1.0\" operator=\"fred\" " + "xmlns=\"urn:uddi-org.api\">\n" + "<businessService businessKey=\"XXD599999999-5555-4444-4444-8888888X\"" + " serviceKey=\"XXD599999999-4444-4444-4444-8888888X\">\n" + "<name>XX Proprietary XML purchase order XX</name>\n" + "</businessService>\n" + "<businessService businessKey=\"YYD599999999-5555-4444-4444-8888888X\"" + " serviceKey=\"YYD599999999-4444-4444-4444-8888888X\">\n" + "<name>YY Proprietary XML purchase order XX</name>\n" + "</businessService>\n" + "</serviceDetail>";
        try {
            BufferedWriter f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile)));
            f.write(xml);
            f.close();
            System.out.println(xml + "\n");
            DOMParser parser = new DOMParser();
            parser.parse(xmlFile);
            Document doc = parser.getDocument();
            Element element = doc.getDocumentElement();
            NodeList nodelist = element.getChildNodes();
            Node node = nodelist.item(0);
            ServiceDetailResponseHandler h = new ServiceDetailResponseHandler();
            ServiceDetailResponse obj = (ServiceDetailResponse) h.create(element);
            System.out.println("\n" + obj.toXML());
        } catch (Exception e) {
            System.out.println("caught exception in main: " + e.getMessage());
        }
    }
}
