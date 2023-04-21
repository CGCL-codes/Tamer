package org.demo;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;
import org.wso2.xfer.WSTransferConstants;

public class AttributeClient {

    public Attribute getAttribute(String attribute, String pricipal, String appliesTo) throws AxisFault {
        ServiceClient serviceClient = new ServiceClient();
        OperationClient opClient = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
        Options options = opClient.getOptions();
        EndpointReference epr = new EndpointReference("http://localhost:9763/services/AttributeService");
        options.setTo(epr);
        MessageContext msgCtx = new MessageContext();
        opClient.addMessageContext(msgCtx);
        SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();
        SOAPEnvelope env = factory.getDefaultEnvelope();
        opClient = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
        options = opClient.getOptions();
        options.setTo(epr);
        options.setAction(WSTransferConstants.ACTION_URI_GET);
        env = factory.getDefaultEnvelope();
        msgCtx = new MessageContext();
        opClient.addMessageContext(msgCtx);
        OMElement attributeIdHeader = factory.createOMElement(Attribute.Q_ELEM_ATTRIBUTE_ID.getLocalPart(), Attribute.Q_ELEM_ATTRIBUTE_ID.getNamespaceURI(), "fed");
        attributeIdHeader.setText("province shyameni1 http://localhost:8084/axis2/services/greenTestService");
        env.getHeader().addChild(attributeIdHeader);
        msgCtx.setEnvelope(env);
        System.out.println("Retriving the Attribute with CustomId - fd");
        opClient.execute(true);
        MessageContext inMsgCtx = opClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        OMElement element = inMsgCtx.getEnvelope().getBody().getFirstElement();
        Attribute attribute2 = AttributeUtil.fromOM(element);
        AttributeUtil.printAttributeInfo(attribute2);
        return attribute2;
    }
}
