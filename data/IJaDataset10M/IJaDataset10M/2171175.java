package org.mobicents.servlet.sip.core.security;

import java.security.Principal;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public interface SipPrincipal extends Principal {

    boolean isUserInRole(String role);
}
