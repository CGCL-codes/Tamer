package com.intel.gpe.services.jms.workflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Alexander Lukichev
 * @version $Id: IGlobalScope.java,v 1.2 2005/08/18 13:02:23 lukichev Exp $
 */
public interface IGlobalScope extends IScope {

    public Node getVariablePart(String name, String part) throws NoSuchDataException;

    public void setVariablePart(String name, String part, Node value);

    public void add(String name, String part, Element variable);
}
