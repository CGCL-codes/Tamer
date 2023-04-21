package uk.ac.ncl.neresc.dynasoar.webService.utlis;

import org.apache.axis.AxisFault;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPHeaderElement;
import org.w3c.dom.NodeList;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import java.util.Vector;

/**
 * utility class for manipulating the dynasoar soap header that is added to all soap messages sent
 * for processing by dynasoar
 *
 * @author Charles Kubicek
 */
public class DynasoarSOAPHeader {

    /**
   * Adds a target service name to the SOAP header of the message
   *
   * @param targetService the name of the service
   * @param message       the SOAP message
   * @return
   *
   * @throws SOAPException
   */
    public static SOAPEnvelope addTargetService(String targetService, SOAPEnvelope message) throws SOAPException {
        SOAPHeader header = message.getHeader();
        SOAPElement dynasoarHeaderElement = getDynasoarHeader(message);
        if (dynasoarHeaderElement == null) {
            dynasoarHeaderElement = header.addChildElement("dynasoar");
        }
        SOAPElement dynasoarHeadermessageIdElement = dynasoarHeaderElement.addChildElement("targetService");
        dynasoarHeadermessageIdElement.setAttribute("value", targetService);
        return message;
    }

    /**
   * Adds a given message Id to the given SOAP message
   *
   * @param messageId
   * @param message
   * @return
   *
   * @throws SOAPException
   */
    public static SOAPEnvelope addMessageId(String messageId, SOAPEnvelope message) throws SOAPException {
        SOAPHeader header = message.getHeader();
        SOAPElement dynasoarHeaderElement = getDynasoarHeader(message);
        if (dynasoarHeaderElement == null) {
            dynasoarHeaderElement = header.addChildElement("dynasoar");
        }
        SOAPElement dynasoarHeaderMessageIdElement = dynasoarHeaderElement.addChildElement("messageId");
        dynasoarHeaderMessageIdElement.setAttribute("value", messageId);
        return message;
    }

    /**
   * Adds a message Id and target service to a SOAP message, but if the header already contains
   * values for the given parameters, they are not overwritten
   *
   * @param id
   * @param targetService
   * @param message
   * @return
   *
   * @throws SOAPException
   */
    public static SOAPEnvelope makeHeaderNotOverwrite(String id, String targetService, org.apache.axis.message.SOAPEnvelope message) throws SOAPException {
        SOAPHeader header = message.getHeader();
        SOAPElement dynasoarHeaderElement = getDynasoarHeader(message);
        if (dynasoarHeaderElement == null) {
            dynasoarHeaderElement = header.addChildElement("dynasoar");
        }
        if (getmessageIdFromHeader(message) == null) {
            SOAPElement dynasoarHeadermessageIdElement = dynasoarHeaderElement.addChildElement("messageId");
            dynasoarHeadermessageIdElement.setAttribute("value", id);
        }
        if (getTargetService(message) == null) {
            SOAPElement dynasoarHeaderTargeServiceElement = dynasoarHeaderElement.addChildElement("targetService");
            dynasoarHeaderTargeServiceElement.setAttribute("value", targetService);
        }
        return message;
    }

    /**
   * gets the value of the message Id from a SOAP envelope
   *
   * @param message SOAP
   * @return id, null if no id exists
   *
   * @throws SOAPException
   */
    public static String getmessageIdFromHeader(org.apache.axis.message.SOAPEnvelope message) throws SOAPException {
        try {
            SOAPHeaderElement e = (SOAPHeaderElement) message.getHeaders().get(0);
            NodeList l = e.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                MessageElement me = (MessageElement) l.item(i);
                if (me.getName().equals("messageId")) return me.getAttribute("value");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * gets the whole SOAPHeaderElement object from a SOAP envelope
   *
   * @param message
   * @return
   */
    private static SOAPHeaderElement getDynasoarHeader(org.apache.axis.message.SOAPEnvelope message) {
        try {
            Vector headers = message.getHeaders();
            for (int i = 0; i < headers.size(); i++) {
                SOAPHeaderElement element = (SOAPHeaderElement) headers.elementAt(i);
                if (element.getLocalName().equals("dynasoar")) return element;
            }
            return null;
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            return null;
        }
    }

    /**
   * gets the target service from the SOAP header in a message
   *
   * @param message SOAP
   * @return target service, or null if the value hasn't been set
   *
   * @throws SOAPException
   */
    public static String getTargetService(org.apache.axis.message.SOAPEnvelope message) throws SOAPException {
        try {
            SOAPHeaderElement e = (SOAPHeaderElement) message.getHeaders().get(0);
            NodeList l = e.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                MessageElement me = (MessageElement) l.item(i);
                if (me.getName().equals("targetService")) return me.getAttribute("value");
            }
            return null;
        } catch (Exception e) {
            throw new SOAPException("error getting target service from soap header, mesage: " + message.toString());
        }
    }
}
