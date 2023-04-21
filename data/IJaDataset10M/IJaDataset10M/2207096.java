package org.soda.dpws.handler;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.soda.dpws.DPWSException;
import org.soda.dpws.exchange.InMessage;
import org.soda.dpws.exchange.OutMessage;
import org.soda.dpws.fault.DPWSFault;
import org.soda.dpws.internal.DPWS;
import org.soda.dpws.internal.DPWSContextImpl;
import org.soda.dpws.registry.discovery.handler.DuplicateException;
import org.soda.dpws.transport.Channel;

/**
 * Responsible for taking an exception, turning it into a Fault, then sending
 * (and logging) that fault to the appropriate location.
 *
 */
public class DefaultFaultHandler extends AbstractHandler {

    /**
   *
   */
    public static final String EXCEPTION = "exception";

    public void invoke(DPWSContextImpl context) throws DPWSException {
        Exception e = (Exception) context.getProperty(EXCEPTION);
        InMessage inMess = context.getExchange().getInMessage();
        if (inMess != null) {
            XMLStreamReader reader = inMess.getXMLStreamReader();
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e1) {
                }
            }
        }
        if (e instanceof DuplicateException) return;
        DPWSFault fault = DPWSFault.createFault(e);
        if (!context.getExchange().hasFaultMessage()) {
            sendToDeadLetter(fault, context);
        } else {
            sendFault(fault, context);
        }
    }

    protected void sendToDeadLetter(@SuppressWarnings("unused") DPWSFault fault, @SuppressWarnings("unused") DPWSContextImpl context) {
    }

    protected void sendFault(DPWSFault fault, DPWSContextImpl context) {
        OutMessage outMsg = (OutMessage) context.getExchange().getFaultMessage();
        if (context.getService() != null) outMsg.setSerializer(context.getService().getFaultSerializer());
        outMsg.setBody(fault);
        context.setCurrentMessage(outMsg);
        DPWS dpws = context.getDpws();
        HandlerPipeline faultPipe = new HandlerPipeline(dpws.getFaultPhases());
        faultPipe.addHandlers(dpws.getFaultHandlers());
        Channel faultChannel = context.getExchange().getFaultMessage().getChannel();
        if (faultChannel != null) {
            faultPipe.addHandlers(faultChannel.getTransport().getFaultHandlers());
        }
        if (context.getService() != null) {
            faultPipe.addHandlers(context.getService().getFaultHandlers());
        }
        try {
            faultPipe.invoke(context);
        } catch (Exception e1) {
            e1.printStackTrace();
            DPWSFault fault2 = DPWSFault.createFault(e1);
            faultPipe.handleFault(fault2, context);
            System.out.println("DefaultFaultHandler.sendFault: Could not send fault.");
        }
    }
}
