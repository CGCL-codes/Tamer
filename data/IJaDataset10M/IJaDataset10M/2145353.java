package br.uniriotec.propid.rpc.handler;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UsernameHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String NAMESPACE = "http://br.unirio.webservice";

    private static final String HEADER_ELEMENT_NAME = "username";

    public static final ThreadLocal<String> _username = new ThreadLocal<String>();

    /**
	 * M�todo que recebe a mensagem SOAP atrav�s do atributo context do tipo SOAPMessageContext.
	 * Atrav�s deste atributo este m�todo inspeciona a mensagem de requisi��o(Consumidor -> Servi�o)
	 * verifica a exist�ncia de um elemento no header que obede�a os atributos NAMESPACE e HEADER_ELEMENT_NAME.
	 * Caso exista, armazena o conte�do deste elemento no atributo _username.
	 */
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            boolean isOutboundDirection = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
            if (!isOutboundDirection) {
                SOAPHeader soapHeader = context.getMessage().getSOAPHeader();
                NodeList headerElements = soapHeader.getElementsByTagNameNS(NAMESPACE, HEADER_ELEMENT_NAME);
                String value = null;
                if ((headerElements != null) && (headerElements.getLength() > 0)) {
                    Node node = headerElements.item(0);
                    if (node != null) {
                        value = node.getFirstChild().getNodeValue();
                        _username.set(value);
                    }
                }
            } else {
                if (_username.get() != null) {
                    SOAPMessage msg = context.getMessage();
                    SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
                    SOAPHeader soapHeader = env.getHeader();
                    if (soapHeader == null) soapHeader = env.addHeader();
                    QName qname = new QName(NAMESPACE, HEADER_ELEMENT_NAME);
                    SOAPHeaderElement headerElement = soapHeader.addHeaderElement(qname);
                    headerElement.addTextNode(_username.get().toString());
                    msg.saveChanges();
                }
            }
        } catch (SOAPException e) {
            System.out.println(e);
        }
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public void close(MessageContext context) {
    }
}
