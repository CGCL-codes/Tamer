package com.lbf.middlesim.handlers;

import java.util.Map;
import com.lbf.middlesim.MSException;
import com.lbf.middlesim.core.MSState;
import com.lbf.middlesim.core.callback.TimeEvent;
import com.lbf.middlesim.msg.FED_Constrained;
import com.lbf.commons.config.ConfigurationException;
import com.lbf.commons.messaging.MessageContext;
import com.lbf.commons.messaging.MessageHandler;

/**
 * Processes {@link FED_Constrained FED_Constrained} messages that are sent when the fedamb
 * is notified that time constrained has been enabled. This handler will set the <code>constrained
 * </code> variable of the {@link MSState MSState} to <code>true</code> and set its current time
 * to the given value.
 */
@MessageHandler(modules = "middlesim", keywords = { "middlesim" }, sinks = "proxy-fedamb", messages = FED_Constrained.class)
public class Federate_Constrained extends AbstractMSHandler {

    private MSState state;

    public Federate_Constrained() {
        super("Federate_Constrained");
    }

    public void initialize(Map<String, Object> properties) throws ConfigurationException {
        super.initialize(properties);
        this.state = super.mshome.getState();
    }

    public void process(MessageContext context) throws MSException {
        Object request = context.getRequest();
        if ((request instanceof FED_Constrained) == false) {
            String msg = "Invalid message for handler [" + getClass() + "]: " + request.getClass();
            plogger.error(msg);
            error(context, msg);
            return;
        }
        FED_Constrained constrained = (FED_Constrained) request;
        double time = binding.convertTime(constrained.getTime());
        plogger.trace("CALLBACK Time constrained enabled [fedtime: " + time + "]");
        state.setCurrentTime(time);
        state.setConstrained(true);
        getCallbackManager().queueEvent(TimeEvent.eventConstrained(time));
    }
}
