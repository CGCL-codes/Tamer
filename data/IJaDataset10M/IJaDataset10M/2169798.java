package org.apache.axis2.jaxws.samples.handler;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

public class LoggingSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    private PrintStream out;

    public LoggingSOAPHandler() {
        setLogStream(System.out);
    }

    protected final void setLogStream(PrintStream ps) {
        out = ps;
    }

    public void init(Map c) {
        System.out.println("LoggingHandler : init() Called....");
    }

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("LoggingHandler : handleMessage Called....");
        logToSystemOut(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("LoggingHandler : handleFault Called....");
        logToSystemOut(smc);
        return true;
    }

    public void close(MessageContext messageContext) {
        System.out.println("LoggingHandler : close() Called....");
    }

    public void destroy() {
        System.out.println("LoggingHandler : destroy() Called....");
    }

    protected void logToSystemOut(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        out.println("===============================================");
        if (outboundProperty.booleanValue()) {
            out.println("Outbound message:");
        } else {
            out.println("Inbound message:");
        }
        SOAPMessage message = smc.getMessage();
        try {
            message.writeTo(out);
            out.println();
        } catch (Exception e) {
            out.println("Exception in handler: " + e);
        }
        out.println("===============================================");
    }
}
