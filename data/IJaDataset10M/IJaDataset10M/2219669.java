package codec.asn1;

import java.security.BasicPermission;

/**
 * This permission is for controlling access to functionality of the ASN.1
 * package. In particular, registering and unregistering {@link OIDRegistry
 * ASN.1 Object Identifier Registries} is protected using this permission.
 * Malicious code may not access the global OIDRegistries, nor register new ones
 * or remove registered ones.
 * <p>
 * 
 * This permission is a simple named permission. The names distinguished are:
 * <ul>
 * <li> <code>OIDRegistry.add</code>
 * <li> <code>OIDRegistry.remove</code>
 * </ul>
 * 
 * @author Volker Roth
 * @version "$Id: ASN1Permission.java,v 1.2 2000/12/06 17:47:25 vroth Exp $"
 */
public class ASN1Permission extends BasicPermission {

    /**
     * Creates an instance with the given name and actions list.
     * 
     * @param name
     *                The name of the permission.
     * @param actions
     *                The actions (not used).
     */
    public ASN1Permission(String name, String actions) {
        super(name, actions);
    }

    /**
     * Creates an instance with the given name and actions list.
     * 
     * @param name
     *                The name of the permission.
     */
    public ASN1Permission(String name) {
        super(name);
    }
}
