package com.intel.gpe.clients.api;

import org.w3c.dom.Element;

/**
 * The abstraction of the incarnation information.
 * 
 * @author Alexander Lukichev
 * @version $Id: Incarnation.java,v 1.1 2005/10/10 08:44:42 lukichev Exp $
 */
public interface Incarnation {

    /**
     * Get the XML representation of the incarnation.
     * @return The DOM Element representation
     */
    public Element toElement();
}
