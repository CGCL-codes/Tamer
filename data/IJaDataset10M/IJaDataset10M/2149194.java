package org.omg.PortableServer;

import org.omg.CORBA.portable.IDLEntity;

/**
 * <p>
 * The Portable Object Adapter (POA) provides more control on the request
 * processing than it is possible when connecting objects directly to the
 * ORB. For details, see the general description of the
 * <code>org.omg.PortableServer</code> package.
 * </p><p>
 * The operations, supported by POA are defined
 * separately in {@link POAOperations}. In the simpliest case, the servant
 * implementation is connected to POA by
 * {@link POAOperations#servant_to_reference}, the returned object being a
 * target of remote and local invocations, despite the numerous other
 * strategies are possible.
 * </p>
 *
 * @see org.omg.CORBA.ORB.resolve_initial_references
 * @see POAOperations.servant_to_reference
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public interface POA extends POAOperations, IDLEntity, org.omg.CORBA.Object {
}
