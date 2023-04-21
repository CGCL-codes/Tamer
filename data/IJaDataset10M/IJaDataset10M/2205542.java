package net.sourceforge.jtds.jca;

import java.io.Serializable;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import javax.resource.NotSupportedException;
import net.sourceforge.jtds.util.Logger;

/**
 * Simple ResourceAdapter as required by the JCA 1.5 specification.
 * </p>This is a 'do nothing' class at present. This would be the 
 * ideal place to manage the global XID table (see XASupport) but
 * for now the driver needs to support JCA 1.0 and non JCA usage.
 */
public class ResourceAdapterImpl implements ResourceAdapter, Serializable {

    static final long serialVersionUID = 789979847L;

    public ResourceAdapterImpl() {
        Logger.printMethod(this, null, null);
    }

    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        Logger.printMethod(this, "start", null);
    }

    public void stop() {
        Logger.printMethod(this, "stop", null);
    }

    public void endpointActivation(MessageEndpointFactory endPoint, ActivationSpec spec) throws ResourceException {
        if (Logger.isTraceActive()) {
            Logger.printMethod(this, "endpointActivation", new Object[] { endPoint });
        }
        throw new NotSupportedException("endPointActivation method not supported");
    }

    public void endpointDeactivation(MessageEndpointFactory endPoint, ActivationSpec spec) {
        if (Logger.isTraceActive()) {
            Logger.printMethod(this, "endpointDeactivation", new Object[] { endPoint });
        }
    }

    public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException {
        if (Logger.isTraceActive()) {
            Logger.printMethod(this, "getXAResources", new Object[] { arg0 });
        }
        return new XAResource[0];
    }
}
