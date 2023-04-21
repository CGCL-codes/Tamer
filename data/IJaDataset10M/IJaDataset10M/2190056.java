package org.dicom4j.network.association;

import org.dicom4j.network.association.listeners.AssociateRequestHandler;
import org.dolmen.network.transport.acceptor.TransportAcceptor;

/**
 * Configuration of an {@link AssociationAcceptor}
 * 
 * @since 0.0.5
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public interface AssociationAcceptorConfiguration extends AssociationServiceConfiguration {

    /**
	 * return the {@link AssociateRequestHandler}
	 * 
	 * @retrun the handler
	 */
    public AssociateRequestHandler getAssociateRequestHandler();

    /**
	 * return the {@link TransportAcceptor TransportAcceptor} used
	 * 
	 * @return the TransportAcceptor used
	 */
    public TransportAcceptor getTransportAcceptor();

    /**
	 * set the {@link AssociateRequestHandler AssociateRequestHandler}
	 * 
	 * @param associateRequestHandler
	 */
    public void setAssociateRequestHandler(AssociateRequestHandler associateRequestHandler);

    /**
	 * Set the {@link TransportAcceptor TransportAcceptor} to use
	 * 
	 * @param aTransport
	 *          the {@link TransportAcceptor TransportAcceptor} to use
	 */
    public void setTransportAcceptor(TransportAcceptor aTransport);
}
