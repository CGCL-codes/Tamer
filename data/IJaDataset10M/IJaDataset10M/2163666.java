package org.xactor.ws.atomictx;

import java.net.URI;
import org.xactor.ws.Constants;
import org.xactor.ws.coordination.PluggableCoordinationType;
import org.xactor.ws.coordination.element.CreateCoordinationContext;
import org.xactor.ws.coordination.element.CreateCoordinationContextResponse;
import org.xactor.ws.coordination.element.Register;
import org.xactor.ws.coordination.element.RegisterResponse;

/**
 * The WS-AtomicTransaction coordination type implementation.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class WSATCoordinationType implements PluggableCoordinationType, Constants {

    /** The WS-AtomicTransaction activation servant. */
    private WSATServant.ActivationServant activationServant;

    /** The WS-AtomicTransaction registration servant. */
    private WSATServant.RegistrationServant registrationServant;

    public WSATCoordinationType() {
        this.activationServant = WSATServant.getSingleton().getActivationServant();
        this.registrationServant = WSATServant.getSingleton().getRegistrationServant();
    }

    /** @see org.xactor.ws.coordination.PluggableCoordinationType#getURI() */
    public URI getURI() {
        return WSAT.CoordinationTypes.WSAT_COORDINATION_TYPE;
    }

    /** @see org.xactor.ws.coordination.PluggableCoordinationType#supportsCoordinationProtocol(
    *  java.net.URI) */
    public boolean supportsCoordinationProtocol(URI coordinationProtocol) {
        return WSAT.Protocols.COMPLETION.equals(coordinationProtocol) || WSAT.Protocols.VOLATILE_2PC.equals(coordinationProtocol) || WSAT.Protocols.DURABLE_2PC.equals(coordinationProtocol);
    }

    /**
    * @see org.xactor.ws.coordination.PluggableCoordinationType#createCoordinationContext(
    * org.xactor.ws.coordination.element.CreateCoordinationContext)
    */
    public CreateCoordinationContextResponse createCoordinationContext(CreateCoordinationContext create) {
        return activationServant.createCoordinationContext(create);
    }

    /**
    * @see org.xactor.ws.coordination.PluggableCoordinationType#register(java.lang.String,
    *      org.xactor.ws.coordination.element.Register)
    */
    public RegisterResponse register(String coordinatedActivityID, Register register) {
        return registrationServant.register(coordinatedActivityID, register);
    }
}
