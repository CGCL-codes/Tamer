package org.juddi.response;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
import org.juddi.UDDIException;
import org.juddi.base.*;
import org.juddi.request.*;
import org.juddi.response.*;

/**
 * BusinessDetailHandler
 *
 * @author  Alex Ceponkus
 * @author  Steve Viens
 * @author  Graeme Riddell
 * @author  Chris Dellario
 * @version 0.1 9/14/2000
 * @since   JDK1.2.2
 */
public class BusinessDetailExtResponseHandler implements UDDIXMLHandler {

    private static UDDIXMLHandlerMaker maker = null;

    public BusinessDetailExtResponseHandler() {
    }

    public Object create(Node node) throws UDDIException {
        BusinessDetailExtResponse businessDetailExtResponse = new BusinessDetailExtResponse();
        maker = UDDIXMLHandlerMaker.getInstance();
        Element element = (Element) node;
        if (element.getAttributeNode("operator") != null) {
            Attr attr = element.getAttributeNode("operator");
            maker = UDDIXMLHandlerMaker.getInstance();
            OperatorHandler operatorHandler = (OperatorHandler) maker.lookup("operator");
            Operator op = (Operator) operatorHandler.create((Node) attr);
            businessDetailExtResponse.setOperator(op);
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("businessEntityExt")) {
                    BusinessEntityExtHandler h = (BusinessEntityExtHandler) maker.lookup("businessEntityExt");
                    BusinessEntityExt businessEntityExt = (BusinessEntityExt) h.create(children.item(i));
                    businessDetailExtResponse.addBusinessEntityExt(businessEntityExt);
                }
            }
        }
        return businessDetailExtResponse;
    }

    /**
   * test driver
   */
    public static void main(String args[]) {
        final String xmlFile = "temp.xml";
        final String xml = "" + "<businessDetailExt operator=\"frederick\">" + "<businessEntityExt>" + "<businessEntity " + "businessKey=\"34D599999999-4444-4444-4444-88888888\" " + "operator=\"operator1\" " + "authorizedName=\"authorizedName1\" " + ">" + "<discoveryURLs>" + "<discoveryURL useType=\"businessEntity\" >" + "http://wwww.businessweb.com" + "</discoveryURL>" + "</discoveryURLs>" + "<name>testName</name>" + "<description>description1</description>" + "<description>description2</description>" + "<contacts>" + "<contact useType=\"myUseType\" >" + "<personName>Bob Whatever</personName>" + "<phone>(603)559-1901</phone>" + "<email>bob@whatever.com</email>" + "<address useType=\"myUseType\" sortCode=\"mySortCode\" >" + "<addressLine>addressLine1</addressLine>" + "<addressLine>addressLine2</addressLine>" + "</address>" + "<description>Bob is a big fat jerk</description>" + "</contact>" + "</contacts>" + "<businessServices>" + "<businessService businessKey=\"34D599999999-4444-4444-4444-88888888\" " + "serviceKey=\"34D599999999-4444-4444-4444-88888888\">" + "<name>fred</name>" + "<description>desc one</description>" + "<bindingTemplates>" + "<bindingTemplate bindingKey=\"34D599999999-4444-4444-4444-33333333\">" + " serviceKey=\"34D599999999-4444-4444-4444-22222222\">" + "<description>whatever1</description>" + "<description>whatever2</description>" + "<hostingRedirector bindingKey=\"999999999999-4444-4444-4444-88888888\"></hostingRedirector>" + "<tModelInstanceDetails>" + "<tModelInstanceInfo tModelKey=\"uuid:34D599999999-4444-5555-4444-88888888\">" + "<description>GR Proprietary XML purchase order</description>" + "<description> d1 </description>" + "</tModelInstanceInfo>" + "<tModelInstanceInfo tModelKey=\"uuid:34D599999999-4444-4444-4444-88888888\">" + "<description>Fred Proprietary XML purchase order</description>" + "<description> d2 </description>" + "</tModelInstanceInfo>" + "</tModelInstanceDetails>" + "</bindingTemplate>" + "</bindingTemplates>" + "<categoryBag>" + "<keyedReference tModelKey=\"uuid:999999999999-4444-4444-4444-88888888\" " + "keyName=\"name\" keyValue=\"value\"></keyedReference>" + "<keyedReference tModelKey=\"uuid:119999999999-4444-4444-4444-88888888\" " + "keyName=\"name of key\" keyValue=\"val\"></keyedReference>" + "</categoryBag>" + "</businessService>" + "</businessServices>" + "<identifierBag>" + "<keyedReference tModelKey=\"uuid:999999999999-4444-4444-4444-88888888\" " + "keyName=\"name\" keyValue=\"value\"></keyedReference>" + "<keyedReference tModelKey=\"uuid:999999999999-2222-4444-4444-88888888\" " + "keyName=\"name\" keyValue=\"value\"></keyedReference>" + "</identifierBag>" + "<categoryBag>" + "<keyedReference tModelKey=\"uuid:999999999999-4444-4444-4444-88888888\" " + "keyName=\"name\" keyValue=\"value\"></keyedReference>" + "<keyedReference tModelKey=\"uuid:119999999999-4444-4444-4444-88888888\" " + "keyName=\"name of key\" keyValue=\"val\"></keyedReference>" + "</categoryBag>" + "</businessEntity>" + "</businessEntityExt>" + "</businessDetailExt>\n";
        try {
            BufferedWriter f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile)));
            f.write(xml);
            f.close();
            System.out.println(xml + "\n");
            DOMParser parser = new DOMParser();
            parser.parse(xmlFile);
            Document doc = parser.getDocument();
            Element element = doc.getDocumentElement();
            BusinessDetailExtResponseHandler h = new BusinessDetailExtResponseHandler();
            BusinessDetailExtResponse obj = (BusinessDetailExtResponse) h.create(element);
            System.out.println("\n" + obj.toXML());
        } catch (Exception e) {
            System.out.println("caught exception in main: " + e.getMessage());
        }
    }
}
