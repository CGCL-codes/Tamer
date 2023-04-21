package org.ikasan.connector.sftp.producer;

import javax.resource.ResourceException;
import org.ikasan.spec.endpoint.EndpointFactory;
import org.ikasan.spec.endpoint.EndpointManager;
import org.ikasan.spec.endpoint.EndpointActivator;
import org.ikasan.spec.endpoint.Producer;

/**
 * Endpoint manager for SFTP producer endpoint implementations based on an 
 * Sftp protocol and configuration.
 * @author Ikasan Development Team
 */
public class SftpProducerEndpointManager implements EndpointManager<Producer<?>, SftpProducerConfiguration> {

    /** producer factory */
    private EndpointFactory<Producer<?>, SftpProducerConfiguration> endpointFactory;

    /** configuration */
    private SftpProducerConfiguration sftpConfiguration;

    /** producer endpoint */
    private Producer<?> producer;

    /**
     * Constructor
     * @param producerFactory
     * @param sftpConfiguration
     */
    public SftpProducerEndpointManager(EndpointFactory<Producer<?>, SftpProducerConfiguration> endpointFactory, SftpProducerConfiguration sftpConfiguration) {
        this.endpointFactory = endpointFactory;
        if (endpointFactory == null) {
            throw new IllegalArgumentException("endpointFactory cannot be 'null'");
        }
        this.sftpConfiguration = sftpConfiguration;
        if (sftpConfiguration == null) {
            throw new IllegalArgumentException("sftpConfiguration cannot be 'null'");
        }
    }

    public void start() throws ResourceException {
        this.producer = this.endpointFactory.createEndpoint(sftpConfiguration);
        if (this.producer instanceof EndpointActivator) {
            ((EndpointActivator) this.producer).activate();
        }
    }

    public Producer<?> getEndpoint() {
        return this.producer;
    }

    public void stop() throws ResourceException {
        if (this.producer != null && this.producer instanceof EndpointActivator) {
            try {
                ((EndpointActivator) producer).deactivate();
            } finally {
                this.producer = null;
            }
        }
    }

    public void setConfiguration(SftpProducerConfiguration sftpConfiguration) {
        this.sftpConfiguration = sftpConfiguration;
    }

    public SftpProducerConfiguration getConfiguration() {
        return this.sftpConfiguration;
    }
}
