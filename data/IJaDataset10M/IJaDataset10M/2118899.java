package edu.kgi.biobridge.sbwdriver;

import edu.kgi.biobridge.app.SBWTools;
import edu.kgi.biobridge.gum.*;

/**
 * SBWPrivateMethod is the SBW driver's other implementation of the
 * Method interface. It is used only with methods of private modules,
 * i.e. those modules that are SELF_MANAGED. Because any number of private modules
 * may be running, and clients have no knowledge of module IDs, an extra
 * parameter is added to each method of a private module to specify which
 * module to send the call to. This parameter is always the first. Clients
 * can get a number through the ManagementService's startServicePrivate()
 * method.</p>
 * 
 * @author Cameron Wellock
 */
class SBWPrivateMethod extends SBWMethod {

    /**
 * Create a new SBWPrivateMethod.
 * @param signature Signature of the method.
 * @param helpString Help string for the method.
 * @param serviceId Service ID of the method.
 * @param methodId Method ID for the method.
 * @param toSbw Handle to the SBW low-level API.
 * @throws EInvalidSBWType
 */
    public SBWPrivateMethod(String signature, String helpString, int serviceId, int methodId, SBWTools toSbw) throws EInvalidSBWType {
        super(signature, helpString, 0, serviceId, methodId, toSbw);
    }

    /**
 * Overridden convertParameters() method. Adds an extra "module identifier"
 * parameter at the front of the input parameter list.
 */
    protected void convertParameters(String sig, SBWParameters params) throws EInvalidSBWType {
        params.add(new Parameter("moduleId", ParameterType.INTEGER));
        super.convertParameters(sig, params);
    }

    /**
 * Overridden prepareCall() method. Returns a SBWPrivateCall object instead
 * of a SBWCall object.
 * 
 * @see SBWPrivateCall
 */
    public Call prepareCall() {
        return new SBWPrivateCall(serviceId, methodId, inParameters, toSbw);
    }
}
