package org.juddi.response;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
import org.juddi.UDDIException;
import org.juddi.base.*;
import org.juddi.request.*;
import org.juddi.response.*;

/**
 * ServiceListResponseHandler
 *
 * @author  Alex Ceponkus
 * @author  Steve Viens
 * @author  Graeme Riddell
 * @author  Chris Dellario
 * @version 0.1 9/14/2000
 * @since   JDK1.2.2
 */
public class ServiceListResponseHandler implements UDDIXMLHandler {

    private ServiceListResponse serviceListResponse = null;

    private static UDDIXMLHandlerMaker maker = null;

    public ServiceListResponseHandler() {
    }

    public Object create(Node node) throws UDDIException {
        serviceListResponse = new ServiceListResponse();
        maker = UDDIXMLHandlerMaker.getInstance();
        Element element = (Element) node;
        if (element.getAttributeNode("operator") != null) {
            Attr attr = element.getAttributeNode("operator");
            maker = UDDIXMLHandlerMaker.getInstance();
            OperatorHandler operatorHandler = (OperatorHandler) maker.lookup("operator");
            Operator op = (Operator) operatorHandler.create((Node) attr);
            serviceListResponse.setOperator(op);
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("serviceInfos")) {
                    ServiceInfosHandler h = (ServiceInfosHandler) maker.lookup("serviceInfos");
                    ServiceInfos serviceInfos = (ServiceInfos) h.create(children.item(i));
                    serviceListResponse.setServiceInfos(serviceInfos);
                }
            }
        }
        return serviceListResponse;
    }

    /**
   * test driver
   */
    public static void main(String args[]) {
        final String xmlFile = "temp.xml";
        final String xml = "<serviceList generic=\"1.0\" operator=\"fred\" " + "xmlns=\"urn:uddi-org.api\">\n" + "<serviceInfos>\n" + "<serviceInfo serviceKey=\"XXD599999999-4444-4444-4444-8888888X\"" + " businessKey=\"XXD599999999-4444-4444-4444-8888888X\">\n" + "<name>XX Proprietary XML purchase order XX</name>\n" + "</serviceInfo>\n" + "<serviceInfo serviceKey=\"YYD599999999-4444-4444-4444-8888888Y\"" + " businessKey=\"XXD599999999-4444-4444-4444-8888888y\">\n" + "<name>YY Proprietary XML purchase order YY</name>\n" + "</serviceInfo>\n" + "<serviceInfo serviceKey=\"ZZD599999999-4444-4444-4444-8888888Z\">\n" + "<name>ZZ Proprietary XML purchase order ZZ</name>\n" + "</serviceInfo>\n" + "</serviceInfos>\n" + "</serviceList>";
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
            ServiceListResponseHandler h = new ServiceListResponseHandler();
            ServiceListResponse obj = (ServiceListResponse) h.create(element);
            System.out.println("\n" + obj.toXML());
        } catch (Exception e) {
            System.out.println("caught exception in main: " + e.getMessage());
        }
    }
}
