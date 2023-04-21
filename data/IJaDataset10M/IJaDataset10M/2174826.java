package de.dgrid.bisgrid.common.performance.handler;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.handler.AbstractHandler;
import org.codehaus.xfire.handler.OutMessageSender;
import org.codehaus.xfire.handler.Phase;
import de.dgrid.bisgrid.common.BISGridConstants;
import de.dgrid.bisgrid.common.performance.ExternalInvokation;

public class ExternalInvokatonPerfMonitoringOutHandler extends AbstractHandler {

    public ExternalInvokatonPerfMonitoringOutHandler() {
        this.setPhase(Phase.SEND);
        this.before(OutMessageSender.class.getName());
        this.before(BISGridConstants.axisMessageSenderClass);
    }

    @Override
    public void invoke(MessageContext context) throws Exception {
        if (context.getProperty(ExternalInvokation.contextKey) != null) {
            ExternalInvokation extInvokation = (ExternalInvokation) context.getProperty(ExternalInvokation.contextKey);
            extInvokation.setStartCallTimestamp(System.currentTimeMillis());
        }
    }
}
