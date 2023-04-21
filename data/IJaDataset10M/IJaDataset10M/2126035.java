package net.sf.istcontract.wsimport.server.provider;

import net.sf.istcontract.wsimport.api.SOAPVersion;
import net.sf.istcontract.wsimport.api.message.Message;
import net.sf.istcontract.wsimport.api.message.Packet;
import net.sf.istcontract.wsimport.fault.SOAPFaultBuilder;

/**
 * @author Kohsuke Kawaguchi
 */
final class MessageProviderArgumentBuilder extends ProviderArgumentsBuilder<Message> {

    private final SOAPVersion soapVersion;

    public MessageProviderArgumentBuilder(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    @Override
    protected Message getParameter(Packet packet) {
        return packet.getMessage();
    }

    @Override
    protected Message getResponseMessage(Message returnValue) {
        return returnValue;
    }

    @Override
    protected Message getResponseMessage(Exception e) {
        return SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, null, e);
    }
}
