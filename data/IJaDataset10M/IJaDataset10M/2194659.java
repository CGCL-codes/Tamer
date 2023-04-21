package org.grailrtls.gui.display;

import javax.swing.tree.DefaultMutableTreeNode;
import org.grailrtls.gui.network.TransmitterInfo;

public class TransmitterTreeNode extends DefaultMutableTreeNode {

    public TransmitterInfo transmitterInfo;

    public TransmitterTreeNode(TransmitterInfo transmitterInfo) {
        this.transmitterInfo = transmitterInfo;
    }

    public String toString() {
        return this.transmitterInfo.toString();
    }
}
