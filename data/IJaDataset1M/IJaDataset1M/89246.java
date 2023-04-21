package be.vds.jtbdive.client.view.panels.browser;

import javax.swing.tree.DefaultMutableTreeNode;
import be.vds.jtbdive.core.core.Dive;

public class DiveTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 8131687785549537571L;

    private Dive dive;

    public DiveTreeNode(Dive dive) {
        super(dive);
        this.dive = dive;
    }

    public Dive getDive() {
        return dive;
    }
}
