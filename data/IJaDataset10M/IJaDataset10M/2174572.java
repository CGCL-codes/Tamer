/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author julio
 */
public class TranscriptMessageHandler implements SOAPHandler<SOAPMessageContext> {
     public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            try {
                SOAPBody body = messageContext.getMessage().getSOAPBody();
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                dfactory.setNamespaceAware(true);
                dfactory.setValidating(true);
                DocumentBuilder builder = dfactory.newDocumentBuilder();
                Document document = builder.parse("C:/Users/julio/Documents/Maestria KTH/Courses/Spring2012/ID2208 Programming Web Services/Homeworks/WSP_HW2_FINAL/src/java/xml/transcript.xml");
                SOAPBodyElement docElement = body.addDocument(document);
            } catch (SOAPException | SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(EmploymentMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("\nInbound message:");
        }
        return true;
    }
    
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }
    
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }
    
    public void close(MessageContext context) {
    }
}
