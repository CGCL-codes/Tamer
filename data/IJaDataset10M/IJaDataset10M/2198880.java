package org.omg.PortableServer.portable;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

/**
 * Each {@link Servant} has an associated delegate, where the most of the calls
 * are forwarded. The delegate is responsible for providing the actual
 * functionality. This class is required to supports a conceptions of
 * the CORBA 2.3.1 Servant.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public interface Delegate {

    /**
  * Returns the root POA of the ORB instance, associated with this servant.
  * It is the same POA that would be returned by resolving the initial
  * reference "RootPOA" for that orb. The default {@link Servant#default_POA}
  * method forwards call to the delegate can be overridden to
  * obtain the alternative default POA.
  *
  * @see ORB.resolve_initial_references
  */
    POA default_POA(Servant a_servant);

    /**
  * Get the interface repository defintion
  * <code>InterfaceDef</code> for this Object.
  */
    org.omg.CORBA.Object get_interface_def(Servant a_servant);

    /**
  * Checks if the passed servant is an instance of the given CORBA IDL type.
  *
  * @param a_servant a servant to check.
  * @param an_id a repository ID, representing an IDL type for that the
  * servant must be checked.
  *
  * @return true if the servant is an instance of the given type, false
  * otherwise.
  */
    boolean is_a(Servant a_servant, String an_id);

    /**
   * Determines if the server object for this reference has already
   * been destroyed.
   *
   * @return true if the object has been destroyed, false otherwise.
   */
    boolean non_existent(Servant a_servant);

    /**
  * Return the invocation target object identifier as a byte array.
  */
    byte[] object_id(Servant a_servant);

    /**
  * Returns the ORB that is directly associated with the given servant.
  */
    ORB orb(Servant a_servant);

    /**
  * Get POA that is directly associated with the given servant.
  */
    POA poa(Servant a_servant);

    /**
  * Obtains the CORBA object reference that is an invocation target for the
  * given servant.
  */
    org.omg.CORBA.Object this_object(Servant a_servant);
}
