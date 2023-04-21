package fedora.services.sipcreator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import fedora.services.sipcreator.acceptor.SelectableEntryAcceptor;
import fedora.services.sipcreator.metadata.Metadata;

/**
 * This class represents a SelectableEntry node which exists inside a tree
 * model.  Each tree model built on top of a tree of SelectableEntry objects
 * should use a single SelectableEntryAcceptor to filter the nodes which
 * actually appear in that particular view of the tree.  Note that the tree
 * model is actually required to be passed to the Node upon instantiating the
 * node.  This is required so that changes to the underlying SelectableEntry
 * object are reflected immediately in the tree.
 * <br>
 * The metadata children of this node are displayed before any
 * SelectableEntryNode children.  Thus, if this node has 3 metadata children
 * and 4 SENode children, accessing the fifth child node would result in the
 * 2nd SENode. 
 * <br><br>
 * @author Andy Scukanec - (ags at cs dot cornell dot edu)
 */
public class SelectableEntryNode implements TreeNode, Observer {

    /** The entry which this node represents in the tree */
    private SelectableEntry entry;

    /** The parent node of this node, or null if this node is the root */
    private SelectableEntryNode parent;

    /** This hashtable maps from a String ID to a SelectableEntryNode object */
    private Hashtable childrenNodeTable = new Hashtable();

    /** This hashtable maps from a String ID to a Metadata object */
    private Hashtable metadataNodeTable = new Hashtable();

    /** The filter which defines the working set of nodes */
    private SelectableEntryAcceptor acceptor;

    /** The tree model in which this node exists */
    private DefaultTreeModel model;

    /**
     * This constructor requries that the entry, parent, acceptor, and tree
     * model all be known at the time this object is instantiated.  Only the
     * parent may be null and only if this node is the root.  Note that this
     * constructor does not actually insert the resulting node into the tree
     * model.
     * <br><br>
     * @param newEntry The entry which this node represents in the tree.
     * @param newParent The parent node, or null if this node is the root.
     * @param newAcceptor The filter defining the working set of nodes in the
     * tree.
     * @param newModel The tree model in which this node will exist.
     */
    public SelectableEntryNode(SelectableEntry newEntry, SelectableEntryNode newParent, SelectableEntryAcceptor newAcceptor, DefaultTreeModel newModel) {
        model = newModel;
        entry = newEntry;
        entry.addObserver(this);
        parent = newParent;
        acceptor = newAcceptor;
    }

    /**
     * Returns the entry which this node represents in the tree.
     * <br><br>
     * @return The entry which this node represents in the tree.
     */
    public SelectableEntry getEntry() {
        return entry;
    }

    public int getChildCount() {
        if (!acceptor.acceptsMetadata()) {
            return entry.getChildCount(acceptor);
        }
        return entry.getMetadataCount() + entry.getChildCount(acceptor);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public Enumeration children() {
        return new Enumeration() {

            private int ctr = 0;

            public boolean hasMoreElements() {
                return ctr < getChildCount();
            }

            public Object nextElement() {
                return getChildAt(ctr++);
            }
        };
    }

    public int getIndex(TreeNode node) {
        if (acceptor.acceptsMetadata() && node instanceof SelectableEntryNode) {
            SelectableEntryNode casted = (SelectableEntryNode) node;
            return entry.getIndex(casted.getEntry(), acceptor) - entry.getMetadataCount();
        } else if (acceptor.acceptsMetadata()) {
            MetadataNode metadataNode = (MetadataNode) node;
            return entry.indexOfMetadata(metadataNode.getMetadata());
        } else {
            SelectableEntryNode casted = (SelectableEntryNode) node;
            return entry.getIndex(casted.getEntry(), acceptor);
        }
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getChildAt(int index) {
        if (acceptor.acceptsMetadata() && index >= entry.getMetadataCount()) {
            SelectableEntry childEntry = getEntry().getChildAt(index - entry.getMetadataCount(), acceptor);
            SelectableEntryNode node = (SelectableEntryNode) childrenNodeTable.get(childEntry.getID());
            if (node == null) {
                node = new SelectableEntryNode(childEntry, this, acceptor, model);
                childrenNodeTable.put(childEntry.getID(), node);
            }
            return node;
        } else if (acceptor.acceptsMetadata()) {
            Metadata metadata = entry.getMetadata(index);
            MetadataNode node = (MetadataNode) metadataNodeTable.get(metadata.getID());
            if (node == null) {
                node = new MetadataNode(this, metadata, model);
                metadataNodeTable.put(metadata.getID(), node);
            }
            return node;
        } else {
            SelectableEntry childEntry = getEntry().getChildAt(index, acceptor);
            SelectableEntryNode node = (SelectableEntryNode) childrenNodeTable.get(childEntry.getID());
            if (node == null) {
                node = new SelectableEntryNode(childEntry, this, acceptor, model);
                childrenNodeTable.put(childEntry.getID(), node);
            }
            return node;
        }
    }

    public String toString() {
        return entry.getShortName();
    }

    public void update(Observable o, Object arg) {
        model.nodeStructureChanged(this);
    }
}
