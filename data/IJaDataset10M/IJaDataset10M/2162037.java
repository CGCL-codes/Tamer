package com.vitria.test.jca;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vitria.test.jca.helper.DummyXAResourceImpl;

public class ResourceAdapterImpl implements ResourceAdapter {

    private static Log LOG = LogFactory.getLog(ResourceAdapterImpl.class);

    private BootstrapContext bootContext_;

    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec activeSpec) throws ResourceException {
        LOG.info("In ResourceAdapterImpl.endpointActivation, message end point deploly with spec \n" + activeSpec);
        ((MessageEndPointFactoryAssociation) activeSpec).setEndPointFactory(endpointFactory);
        try {
            ((SourceConnector) activeSpec).start();
        } catch (ConnectorException ce) {
            LOG.error("fail to start", ce);
            throw new ResourceException(ce);
        }
    }

    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec activeSpec) {
        LOG.info("In ResourceAdapterImpl.endpointDeactivation, message end point undeploly with spec \n" + activeSpec);
        try {
            ((SourceConnector) activeSpec).stop();
        } catch (ConnectorException ce) {
            LOG.error("fail to stop", ce);
        }
    }

    public XAResource[] getXAResources(ActivationSpec[] activeSpecs) throws ResourceException {
        int size = activeSpecs.length;
        XAResource[] xas = new XAResource[size];
        for (int i = 0; i < xas.length; i++) {
            xas[i] = new DummyXAResourceImpl("recover_" + i);
        }
        return xas;
    }

    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        bootContext_ = ctx;
        LOG.info("In ResourceAdapterImpl.start, resource adapter starts ");
    }

    public void stop() {
        LOG.info("In ResourceAdapterImpl.stop, resource adapter stops");
    }

    public String toString() {
        String str = Integer.toHexString(System.identityHashCode(this));
        return "ResourceAdapter ---> " + str;
    }

    public BootstrapContext getBootstrapContext() {
        return bootContext_;
    }
}
