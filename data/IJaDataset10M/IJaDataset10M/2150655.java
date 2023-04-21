package hidb2.gui.node;

import hidb2.gui.ressources.RscMan;
import hidb2.kern.Attribut;

/**
 * Represents an Attribut in various tree views.
 *
 */
public class AttributNode extends DefaultNode {

    public AttributNode(Attribut attr) {
        super(attr.getName() + ":" + attr.getType().name, RscMan.IN_BRUSH);
    }
}
