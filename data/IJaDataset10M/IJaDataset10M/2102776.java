package org.apache.axis2.jaxws.calculator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.util.List;

@Addressing
@WebService(endpointInterface = "org.apache.axis2.jaxws.calculator.Calculator", serviceName = "CalculatorService", portName = "CalculatorServicePort", targetNamespace = "http://calculator.jaxws.axis2.apache.org", wsdlLocation = "META-INF/CalculatorService.wsdl")
public class CalculatorService implements Calculator {

    @Resource
    private WebServiceContext context;

    public W3CEndpointReference getTicket() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Element element = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            element = document.createElementNS("http://calculator.jaxws.axis2.apache.org", "TicketId");
            element.appendChild(document.createTextNode("123456789"));
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            throw new WebServiceException("Unable to create ticket.", pce);
        }
        return (W3CEndpointReference) getContext().getEndpointReference(element);
    }

    public int add(int value1, int value2) throws AddNumbersException_Exception {
        List list = (List) getContext().getMessageContext().get(MessageContext.REFERENCE_PARAMETERS);
        if (list.isEmpty()) {
            AddNumbersException faultInfo = new AddNumbersException();
            faultInfo.setMessage("No ticket found.");
            throw new AddNumbersException_Exception(faultInfo.getMessage(), faultInfo);
        }
        Element element = (Element) list.get(0);
        if (!"123456789".equals(element.getTextContent())) {
            AddNumbersException faultInfo = new AddNumbersException();
            faultInfo.setMessage("Invalid ticket: " + element.getTextContent());
            throw new AddNumbersException_Exception(faultInfo.getMessage(), faultInfo);
        }
        System.out.println("value1: " + value1 + " value2: " + value2);
        return value1 + value2;
    }

    private WebServiceContext getContext() {
        return context;
    }
}
