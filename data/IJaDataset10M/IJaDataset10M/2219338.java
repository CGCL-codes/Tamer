package org.omg.CosNaming;

import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * The extended naming context operations, defined since 1.4.
 * The extensions are focused on providing the simplier way
 * to use naming service with the string-based names and
 * addresses.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public interface NamingContextExtOperations extends NamingContextOperations {

    /**
   * Resolve the name, represented in the form of the string.
   * The components of the composite name are separated by
   * slash ('/').
   *
   * @param a_name_string the name to resolve.
   * @return the object, referenced by the name.
   */
    org.omg.CORBA.Object resolve_str(String a_name_string) throws NotFound, CannotProceed, InvalidName;

    /**
   * Converts the name, represented in the form of the string,
   * into the older name representation (array of the name
   * components).
   *
   * @param a_name_string the stringified form of the name.
   *
   * @return the component array form of the name.
   *
   * @throws InvalidName if the name is invalid.
   */
    NameComponent[] to_name(String a_name_string) throws InvalidName;

    /**
   * Converts the older representation for the name (array
   * of the name components) into the string form of the name.
   *
   * @param a_name the name, as an array of components.
   *
   * @return the same name as a string.
   *
   * @throws InvalidName if the name is invalid.
   */
    String to_string(NameComponent[] a_name) throws InvalidName;

    String to_url(String an_address, String a_name_string) throws InvalidAddress, InvalidName;
}
