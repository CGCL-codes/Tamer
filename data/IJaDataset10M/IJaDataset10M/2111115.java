package net.sf.association4j.test2;

import net.sf.association4j.AbstractAssociatedObject;
import net.sf.association4j.AssociationEndpoint;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class Attribute extends AbstractAssociatedObject {

    private final AssociationEndpoint<Attribute, Element> parent = new AssociationEndpoint<Attribute, Element>(this, 1, 1, "elementAttribute");

    public Element getParent() {
        return parent.getTarget();
    }
}
