package fedora.services.sipcreator;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import fedora.services.sipcreator.metadata.Metadata;

/**
 * This class exists to represent a Metadata element in the JTree that is the
 * main view of most of the SIPCreator system.  Metadata elements can exist
 * under any SelectableEntry element, and always have no children.
 * <br><br>
 * @author Andy Scukanec - (ags at cs dot cornell dot edu)
 */
public class MetadataNode implements TreeNode, Observer {

    /** This is a convenience object for implementing TreeNode */
    private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {

        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            return null;
        }
    };

    /** The node above this node in the tree */
    private SelectableEntryNode parent;

    /** The actual metadata this node represents */
    private Metadata metadata;

    /**
     * The tree model in which this node exists.  Necessary so that changes to
     * the underlying Metadata object can be reflected immediately in the tree.
     */
    private DefaultTreeModel model;

    /**
     * This constructor requires that metadata the new node represents, its
     * parent, and the tree model in which this node will exist be known at
     * instantiation.  The tree model is necessary so that changes to the
     * metadata will be reflected in the tree model.  Note that this
     * constructor does not insert the resulting MetadataNode into the
     * tree model.
     * <br><br>
     * @param newParent The SelectableEntryNode immediately above this node.
     * @param newMetadata The metadata this object represents in the tree.
     * @param newModel The tree model in which this node will exist.
     */
    public MetadataNode(SelectableEntryNode newParent, Metadata newMetadata, DefaultTreeModel newModel) {
        model = newModel;
        parent = newParent;
        metadata = newMetadata;
        metadata.addObserver(this);
    }

    /**
     * Returns the metadata this node represents.
     * <br><br>
     * @return The metadata this node represents.
     */
    public Metadata getMetadata() {
        return metadata;
    }

    public int getChildCount() {
        return 0;
    }

    public boolean getAllowsChildren() {
        return false;
    }

    public boolean isLeaf() {
        return true;
    }

    public Enumeration children() {
        return EMPTY_ENUMERATION;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    public int getIndex(TreeNode node) {
        return -1;
    }

    public String toString() {
        return "(" + metadata.getID() + ") " + metadata.getShortName();
    }

    public void update(Observable o, Object arg) {
        model.nodeChanged(this);
    }
}
