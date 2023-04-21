package quamj.demos.contracttypemgmt;

import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.Vector;

/**
 *
 * @author  Aart van Halteren
 * @version
 */
public class QoSContractTreeModel extends DefaultTreeModel {

    private QoSContractRootNode rootNode;

    /** Creates new QoSContractTypeTreeModel */
    public QoSContractTreeModel(QoSContractRootNode root) {
        super(root);
        this.rootNode = root;
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        reload();
    }

    /** Remove all nodes except the root node. */
    public void refresh() {
        rootNode.removeAllChildren();
        rootNode.populate();
        nodeStructureChanged(rootNode);
        reload();
    }
}
