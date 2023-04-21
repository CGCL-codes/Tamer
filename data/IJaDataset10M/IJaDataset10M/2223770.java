package com.dsoft.jca.eis;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import org.apache.log4j.Logger;

/**
 * @author Sadi Melbouci
 *
 */
public class EisResourceAdapter implements ResourceAdapter {

    static Logger log = Logger.getLogger(EisResourceAdapter.class);

    private BootstrapContext bootstrapContext = null;

    private String hostname = null;

    private String username = null;

    private String password = null;

    private String port = null;

    private EisActivationSpec eisActivationSpec = null;

    private EisXAResource eisXAResource = null;

    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException {
        this.eisActivationSpec = (EisActivationSpec) activationSpec;
        System.out.println("EISResourceAdapter/endpointActivation " + messageEndpointFactory + ", " + activationSpec);
        this.eisXAResource = new EisXAResource();
        MessageEndpoint messageEndpoint = messageEndpointFactory.createEndpoint(this.eisXAResource);
    }

    public void endpointDeactivation(MessageEndpointFactory arg0, ActivationSpec activationSpec) {
    }

    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        log.info("Starting JCA Connector: " + this.getClass().getName());
        this.bootstrapContext = bootstrapContext;
    }

    public void stop() {
        log.info("\nStopping EisResourceAdapter");
    }

    /**
     * @return the eisActivationSpec
     */
    public EisActivationSpec getEisActivationSpec() {
        return eisActivationSpec;
    }

    /**
     * @param eisActivationSpec the eisActivationSpec to set
     */
    public void setEisActivationSpec(EisActivationSpec eisActivationSpec) {
        this.eisActivationSpec = eisActivationSpec;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException {
        return null;
    }

    /**
     * @return the eisXAResource
     */
    public EisXAResource getEisXAResource() {
        return eisXAResource;
    }

    /**
     * @param eisXAResource the eisXAResource to set
     */
    public void setEisXAResource(EisXAResource eisXAResource) {
        this.eisXAResource = eisXAResource;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
}
