package com.lbf.middlesim.handlers;

import com.lbf.middlesim.MSException;
import com.lbf.middlesim.fom.OCInstance;
import com.lbf.middlesim.fom.OCMetadata;
import com.lbf.middlesim.msg.REQ_Register;
import com.lbf.commons.messaging.MessageContext;
import com.lbf.commons.messaging.MessageHandler;

/**
 * This handler will process requests to register an object instance. On a successful call to the
 * RTI, it will create an {@link OCInstance OCInstance} and store it in the
 * {@link com.lbf.middlesim.Repository Repository}. The instance will then be placed in the
 * MessageContext it is processing as the result.
 */
@MessageHandler(modules = "middlesim", keywords = { "middlesim" }, sinks = "proxy-request", messages = REQ_Register.class)
public class Request_Register extends AbstractMSHandler {

    public Request_Register() {
        super("Request_Register");
    }

    public void process(MessageContext context) throws MSException {
        Object request = context.getRequest();
        if ((request instanceof REQ_Register) == false) {
            String msg = "Invalid message for handler [" + getClass() + "]: " + request.getClass();
            plogger.error(msg);
            error(context, msg);
            return;
        }
        REQ_Register register = (REQ_Register) request;
        String ocName = register.getClassName();
        if (register.isUsingPremade()) {
            ocName = register.getPremadeInstance().getType().getQualifiedName();
            plogger.trace("ATTEMPT Register object of class [" + ocName + "] (using premade)");
        } else {
            plogger.trace("ATTEMPT Register object of class [" + ocName + "]");
        }
        OCMetadata metadata = mshome.getFOM().getObjectClass(ocName);
        if (metadata == null) {
            String msg = "Can't register instance: Unable to find metadata for [" + ocName + "]";
            plogger.error("FAILURE " + msg);
            error(context, msg);
            return;
        }
        try {
            int objectId = getRTI().registerObjectInstance(metadata.getHandle());
            OCInstance instance = null;
            if (register.isUsingPremade()) {
                instance = register.getPremadeInstance();
            } else {
                instance = metadata.createInstance();
            }
            instance.setHandle(objectId);
            instance.setName(getRTI().getObjectInstanceName(objectId));
            instance.setOwned(true);
            mshome.getRepository().addInstance(instance);
            plogger.debug("SUCCESS Registered instance of [" + ocName + "] (id: " + objectId + ")");
            success(context, instance);
        } catch (Exception e) {
            plogger.error("FAILURE Register instance [" + ocName + "]: " + e.getMessage(), e);
            error(context, e);
            return;
        }
    }
}
