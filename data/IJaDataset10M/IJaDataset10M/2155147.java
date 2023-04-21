package org.arch4j.naming;

import org.arch4j.baseservices.BaseServices;
import org.arch4j.core.EnvironmentalException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Get J2EE Container InitialContext
 *
 * @author James Roome
 * @version 1.0
 */
public class J2EEContainerBaseServices implements BaseServices {

    /** Creates a new instance of JbossBaseServices */
    public J2EEContainerBaseServices() {
    }

    /** Gets a <code>Context</code>
     * @return the context
     */
    public Context getContext() {
        try {
            return new InitialContext();
        } catch (NamingException ne) {
            throw new EnvironmentalException("Failed to instantiate an InitialContext", ne);
        }
    }
}
