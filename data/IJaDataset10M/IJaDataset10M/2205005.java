package edu.caltech.sbw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Service} class represents a <i>service</i>, an interface to
 * resources inside a module. Obtaining an instance of a {@link Service} object
 * implies that a module implementing the service has been launched by the SBW
 * Broker.
 * 
 * @author Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.3 $
 */
public class Service {

    /**
	 * Indicates whether some other object is "equal to" this one. This method
	 * implements equality for {@link Service} objects, overriding the method of
	 * the same name on the basic Java {@link Object} class. If the given object
	 * is a {@link Service} object, and both its module instance identifier is
	 * equal to this service's module and its service identifier is equal to
	 * this service, then this method returns true, else it returns false. This
	 * equivalence means that the two module proxy objects refer to the same
	 * running instance of a module.
	 * <p>
	 * 
	 * @param other
	 *            the object with which to compare this one.
	 * @return true if the argument is a {@link Service} object that refers to
	 *         the same service as this object, false otherwise.
	 * @see java.lang.Object#equals
	 */
    public boolean equals(Object other) {
        return (other instanceof Service) && (((Service) other).moduleId == moduleId) && (((Service) other).serviceId == serviceId);
    }

    /**
	 * Returns a {@link ServiceDescriptor} object corresponding to this service.
	 * The service descriptor holds information about this service's unique
	 * name, display name, and other attributes.
	 * <p>
	 * 
	 * @return a service descriptor object describing this service
	 * @throws SBWCommunicationException
	 *             if a communications problem occurs in contacting the SBW
	 *             Broker or the module
	 * @throws SBWModuleNotFoundException
	 *             if the SBW Broker cannot find the module that is supposed to
	 *             implement this service
	 * @throws SBWServiceNotFoundException
	 *             if the service corresponding to this service descriptor
	 *             cannot be found on the corresponding module
	 * @throws SBWException
	 *             if another problem arises during the execution of this method
	 */
    public ServiceDescriptor getDescriptor() throws SBWCommunicationException, SBWModuleNotFoundException, SBWServiceNotFoundException, SBWException {
        Module module = new Module(moduleId);
        String[] names = SBW.getServiceNamesFromModule(moduleId);
        String servName = names[serviceId];
        BrokerInterface broker = SBW.getBrokerService();
        List record = broker.getServiceDescriptor(moduleId, servName);
        String moduleName = (String) record.get(0);
        List mdList = broker.getModuleDescriptor(moduleName, true);
        return new ServiceDescriptor((String) record.get(1), (String) record.get(2), (String) record.get(3), new ModuleDescriptor(mdList), (String) record.get(4));
    }

    /**
	 * Returns the methods on this service as an array of {@link ServiceMethod}
	 * objects. The index of each method in the array corresponds one-to-one
	 * with the method identifier.
	 * <p>
	 * 
	 * @return an array of {@link ServiceMethod} objects.
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the module.
	 */
    public ServiceMethod[] getMethods() throws SBWCommunicationException {
        String[] methodSigs = SBW.getSignatureStringsFromModule(moduleId, serviceId);
        ServiceMethod[] methods = new ServiceMethod[methodSigs.length];
        for (int i = 0; i < methods.length; i++) {
            DataBlockWriter args = new DataBlockWriter();
            try {
                args.add(serviceId);
                args.add(i);
                DataBlockReader result = rpc.call(moduleId, SBWLowLevel.SYSTEM_SERVICE, SBWLowLevel.GET_METHOD_HELP_METHOD, args);
                methods[i] = new ServiceMethod(methodSigs[i], result.getString());
            } catch (SBWSignatureSyntaxException e) {
                SBWLog.error("Bad signature: " + methodSigs[i]);
                break;
            } catch (SBWException e) {
                String msg = "Unable to get method help for method " + i + " on service id " + serviceId + " of module instance id " + moduleId;
                SBWLog.trace(msg, e);
                throw new SBWCommunicationException(msg, e.getDetailedMessage());
            } finally {
                args.release();
            }
        }
        return methods;
    }

    /**
	 * Returns a {@link ServiceMethod} object corresponding to the single method
	 * described by the partial signature <code>nameAndArgs</code>. The
	 * parameter may consist of only a method name, or a name and arguments or a
	 * complete signature. Formally <code>nameAndArgs</code> has the syntax:
	 * 
	 * <pre>
	 * SName
	 * </pre>
	 * 
	 * or the syntax:
	 * 
	 * <pre>
	 *   [ReturnType Space] SName Space? '(' VarArgList ')'
	 * </pre>
	 * 
	 * where the arguments list is used to locate specific methods with the same
	 * name. The optional return type is ignored when matching signatures. If
	 * the syntax is incorrect, this method will throw
	 * {@link SBWSignatureSyntaxException}.
	 * <p>
	 * The given name (and optional arguments) is compared to the parsed
	 * signatures of the methods on the service. If more than one method on the
	 * service matches the given argument, this method throws
	 * {@link SBWMethodAmbiguousException}.
	 * <p>
	 * Since obtaining the method list requires contacting the module instance
	 * and the SBW Broker, this method may throw an {@link
	 * SBWCommunicationException} if something goes wrong during communications.
	 * <p>
	 * 
	 * @param nameAndArgs
	 *            a string giving the name and optionally the arguments of the
	 *            method being sought
	 * @throws SBWCommunicationException
	 *             if a communications problem occurs in contacting the SBW
	 *             Broker or the module
	 * @throws SBWMethodAmbiguousException
	 *             if the given partial signature is not enough to disambiguate
	 *             between multiple candidate methods provided by the service
	 * @throws SBWSignatureSyntaxException
	 *             if the syntax of parameter <code>nameAndArgs</code> is
	 *             incorrect
	 */
    public ServiceMethod getMethod(String nameAndArgs) throws SBWCommunicationException, SBWMethodAmbiguousException, SBWSignatureSyntaxException {
        boolean haveArgs = false;
        Signature soughtAfter;
        nameAndArgs = nameAndArgs.trim();
        if (nameAndArgs.indexOf('(') == -1) soughtAfter = new Signature("void " + nameAndArgs + "()"); else {
            haveArgs = true;
            try {
                soughtAfter = new Signature(nameAndArgs);
            } catch (SBWSignatureSyntaxException e) {
                soughtAfter = new Signature("void " + nameAndArgs);
            }
        }
        ServiceMethod[] serviceMethods = getMethods();
        ArrayList matches = new ArrayList();
        for (int i = 0; i < serviceMethods.length; i++) if (soughtAfter.getName().equals(serviceMethods[i].getName())) matches.add(serviceMethods[i]);
        int numMatches = matches.size();
        if (numMatches < 1) return null;
        if (!haveArgs) {
            if (numMatches == 1) return (ServiceMethod) matches.get(0); else throw new SBWMethodAmbiguousException("More than one method named '" + soughtAfter.getName() + "'", "");
        } else {
            SignatureElement[] soughtArgs = soughtAfter.getArguments();
            for (int i = 0; i < numMatches; i++) {
                ServiceMethod sm = (ServiceMethod) matches.get(i);
                SignatureElement[] args = sm.getSignature().getArguments();
                testformatch: if (soughtArgs.length != args.length) continue; else {
                    for (int j = 0; j < args.length; j++) if (!soughtArgs[j].equals(args[j])) break testformatch;
                    return sm;
                }
            }
            return null;
        }
    }

    /**
	 * Returns a {@link Module} class object corresponding to the module that
	 * implements this service. Since the module instance must already be
	 * running, this action does not result in a new instance being launched;
	 * however, it does create a new object of class {@link Module} referring to
	 * the same instance.
	 * <p>
	 * 
	 * @return the {@link Module} object representing the module that implements
	 *         this service
	 */
    public Module getModule() {
        return new Module(moduleId);
    }

    /**
	 * Returns a proxy object for this service. The proxy object will have the
	 * interface specified by argument <code>interfaceClass</code>. The
	 * methods of this object will be the methods specified by the interface.
	 * <p>
	 * 
	 * @param interfaceClass
	 *            the definition of the interface to be used for constructing
	 *            the proxy
	 */
    public Object getServiceObject(Class interfaceClass) throws SBWCommunicationException, SBWServiceNotFoundException, SBWMethodNotFoundException, SBWUnsupportedObjectTypeException {
        try {
            return getServiceObject(new Class[] { interfaceClass });
        } catch (SBWMethodAmbiguousException e) {
            SBWLog.error("Unexpected exception", e);
            return null;
        }
    }

    /**
	 * Returns a proxy object for this service. The proxy object will have the
	 * interface specified by the union of the arguments
	 * <code>interfaceClasses</code>. The methods of this object will be the
	 * methods specified by the set of interfaces.
	 * <p>
	 * Implementation note: this uses the handy {@link java.lang.reflect.Proxy}
	 * class to implement an object that acts as a proxy for the remote service.
	 * <p>
	 * 
	 * @param interfaceClasses
	 *            array of interface classes, to be used in defining the
	 *            interface of the proxy object returned.
	 * @throws SBWCommunicationException
	 *             if a communications problem occurs in contacting the SBW
	 *             Broker or the module
	 * @throws SBWServiceNotFoundException
	 *             if the service corresponding to this service descriptor
	 *             cannot be found on the corresponding module
	 * @throws SBWMethodAmbiguousException
	 *             if the same method appears in more than one of the interfaces
	 *             in argument <code>interfaceClasses</code>
	 * @throws SBWMethodNotFoundException
	 *             if one or more of the methods in the set of interfaces in
	 *             <code>interfaceClasses</code> has no equivalent in this
	 *             service
	 * @throws SBWUnsupportedObjectTypeException
	 *             if there is an unsupported data type in a method declaration
	 *             in the set of interfaces in <code>interfaceClasses</code>
	 */
    public Object getServiceObject(Class interfaceClasses[]) throws SBWCommunicationException, SBWServiceNotFoundException, SBWMethodAmbiguousException, SBWMethodNotFoundException, SBWUnsupportedObjectTypeException {
        try {
            String[] methodSigs = SBW.getSignatureStringsFromModule(moduleId, serviceId);
            InvocationHandler proxyHandler = new ServiceInvocationHandler(serviceId, moduleId, interfaceClasses, methodSigs, rpc);
            return Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, proxyHandler);
        } catch (SBWSignatureSyntaxException e) {
            SBWLog.error("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            SBWLog.error("Unexpected exception", e);
        } catch (NullPointerException e) {
            String msg = "Interface is null";
            SBWLog.exception(msg, e);
            throw new SBWServiceNotFoundException(msg, e.getMessage());
        }
        return null;
    }

    /**
	 * Internal constructor.
	 * <p>
	 * 
	 * @param moduleId
	 *            the identifier for the module
	 * @param serviceId
	 *            the identifier for the service
	 */
    Service(int moduleId, int serviceId) {
        this.moduleId = moduleId;
        this.serviceId = serviceId;
    }

    /** Internal variables. * */
    private Object serviceObj;

    private int serviceId;

    private int moduleId;

    private static SBWLowLevel rpc = SBW.getLowLevelAPI();

    static {
        Config.recordClassVersion(Service.class, "$Id: Service.java,v 1.3 2007/07/24 23:08:23 fbergmann Exp $");
    }
}
