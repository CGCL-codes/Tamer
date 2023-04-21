package demo.handlers.common;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private PrintStream out;

    public LoggingHandler() {
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
        if (outboundProperty.booleanValue()) {
            out.println("\nOutbound message:");
        } else {
            out.println("\nInbound message:");
        }
        SOAPMessage message = smc.getMessage();
        try {
            message.writeTo(out);
            out.println();
        } catch (Exception e) {
            out.println("Exception in handler: " + e);
        }
    }
}
