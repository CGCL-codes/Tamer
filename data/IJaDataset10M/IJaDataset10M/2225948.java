package org.xactor.test.ws.usertx.ejb;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;

/**
 * Client-side handler that injects the client ID into outgoing SOAP messages.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class ClientHandler extends GenericHandler {

    private static final QName IDQN = SessionCounterBean.IDQN;

    private static QName[] headerNames = new QName[] {};

    private static ThreadLocal<Integer> threadLocalData = new ThreadLocal<Integer>();

    public static void setClientId(Integer clientId) {
        threadLocalData.set(clientId);
    }

    public static void unsetClientId() {
        threadLocalData.set(null);
    }

    @Override
    public QName[] getHeaders() {
        return headerNames;
    }

    @Override
    public boolean handleRequest(MessageContext context) {
        try {
            Integer clientId = threadLocalData.get();
            if (clientId == null) throw new IllegalStateException("Cannot obtain client ID");
            SOAPElement element = SOAPFactory.newInstance().createElement(IDQN);
            element.setValue(clientId.toString());
            SOAPHeader soapHeader = ((SOAPMessageContext) context).getMessage().getSOAPHeader();
            soapHeader.addChildElement(element);
        } catch (SOAPException e) {
            throw new IllegalStateException("Cannot handle request", e);
        }
        return true;
    }
}
