package de.uni_bremen.informatik.p2p.peeranha42.core.plugin.gui.preferences;

import javax.swing.tree.DefaultMutableTreeNode;
import de.uni_bremen.informatik.p2p.peeranha42.core.plugin.Info;

/**
 * This class represents a node in the tree of the peeranha42 configuration
 * panel.
 *
 * @author M.Wuerthele
 */
public class PlugInTreeNode extends DefaultMutableTreeNode {

    /** Info Object from plugin represented by this object */
    private Info info;

    /**
     * Creates a new PlugInTreeNode object.
     *
     * @param plugInInfo Info Object
     */
    public PlugInTreeNode(Info plugInInfo) {
        super(plugInInfo.getName());
        this.info = plugInInfo;
    }

    /**
     * returns the plugin info object
     *
     * @return Info Object
     */
    public Info getInfo() {
        return info;
    }

    /**
     * sets the plugin info object
     *
     * @param info Object
     */
    public void setInfo(Info info) {
        this.info = info;
    }
}
