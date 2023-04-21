package net.sf.magicmap.client.gui.utils.menu;

import net.sf.magicmap.client.model.node.Node;

/**
 * Class ${class}
 *
 * @author jan
 * @date 10.02.2007
 */
public class NodeMenuTypePredicate implements NodeMenuPredicate {

    private final int type;

    public NodeMenuTypePredicate(int type) {
        this.type = type;
    }

    public boolean show(Object context) {
        return context != null && (context instanceof Node) && ((Node) context).getType() == type;
    }
}
