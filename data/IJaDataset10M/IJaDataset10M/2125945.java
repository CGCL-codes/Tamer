package ti.chimera.registry;

import java.util.*;
import ti.swing.treetable.*;
import ti.chimera.*;
import ti.exceptions.ProgrammingErrorException;

/**
 * A tree model for a subtree of the registry.  The model tracks changes to
 * the form of the registry (ie. link/unlink nodes), and fires the appropriate
 * tree structure change events.
 * <p>
 * XXX add a RegistryTreeFilter, so different registry trees can filter out
 * nodes...
 * <p>
 * XXX add a TreeCellRender plus interface to set icons, etc., for different
 * node contract types... this should be done here, rather than in the user of
 * the RegistryTreeModel in order to force a more consistent user interface,
 * because the icon will be conveying type information, it would be confusing
 * to the user if the same type results in different icons in different places
 * <p>
 * XXX need some way to unsubscribe all DirectorySubscriber-s when the tree
 * model is no longer in use... 
 * 
 * @author Rob Clark
 * @version 0.1
 */
public class RegistryTreeModel extends AbstractTreeModel {

    /**
   * The root-path of this model
   */
    private String path;

    private Main main;

    private Registry registry;

    private WeakHashMap nodeTable = new WeakHashMap();

    /**
   * Dispose of the tree model
   */
    public void dispose() {
        for (Iterator itr = nodeTable.keySet().iterator(); itr.hasNext(); ) {
            Node node = (Node) (itr.next());
            node.unsubscribe((NodeSubscriber) (nodeTable.get(node)));
        }
    }

    /**
   * Class Constructor.
   * 
   * @param path        the path to the root node of the tree
   */
    public RegistryTreeModel(Main main, String path) throws RegistryException {
        super(new NodeWrapper(path, main.getRegistry().resolve(path)));
        this.path = path;
        this.main = main;
        registry = main.getRegistry();
        watch(registry.resolve(path));
    }

    private void watch(Node node) {
        NodeSubscriber s = new DirectorySubscriber();
        nodeTable.put(node, s);
        node.subscribe(s);
    }

    private class DirectorySubscriber implements NodeSubscriber {

        private DirectoryTable oldDt;

        public void publish(Node node, Object value) {
            DirectoryTable newDt = (DirectoryTable) value;
            for (Iterator itr = newDt.notIn(oldDt); itr.hasNext(); ) {
                Node child = newDt.get((String) (itr.next()));
                if (child.getValue() instanceof DirectoryTable) watch(child);
            }
            oldDt = newDt;
            final LinkedList pathList = new LinkedList();
            while (true) {
                pathList.addFirst(new NodeWrapper("foo", node));
                String path = node.getPrimaryPath();
                if (path == null) return;
                if (path.equals(RegistryTreeModel.this.path)) break;
                try {
                    path = node.getPrimaryPath();
                    if (path == null) return;
                    path = registry.dirname(node.getPrimaryPath());
                    if (!registry.exists(path)) return;
                    node = registry.resolve(path);
                } catch (RegistryException e) {
                    throw new ProgrammingErrorException(e);
                }
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    synchronized (registry) {
                        fireTreeStructureChanged(DirectorySubscriber.this, pathList.toArray(), null, null);
                    }
                }
            });
        }
    }

    public boolean isLeaf(Object node) {
        return !(((NodeWrapper) node).getNode().getValue() instanceof DirectoryTable);
    }

    public Object getChild(Object parent, int idx) {
        DirectoryTable dt = (DirectoryTable) ((NodeWrapper) parent).getNode().getValue();
        return new NodeWrapper(dt.getChildName(idx), dt.getChildNode(idx));
    }

    public int getChildCount(Object parent) {
        return ((DirectoryTable) ((NodeWrapper) parent).getNode().getValue()).getChildCount();
    }

    public static class NodeWrapper {

        private String name;

        private Node node;

        NodeWrapper(String name, Node node) {
            this.name = name;
            this.node = node;
        }

        public String toString() {
            return name;
        }

        public Node getNode() {
            return node;
        }

        public int hashCode() {
            return node.hashCode();
        }

        public boolean equals(Object obj) {
            return (obj instanceof NodeWrapper) && ((NodeWrapper) obj).node.equals(node);
        }
    }

    /**
   * A {@link javax.swing.tree.TreeCellRenderer} that uses the icons set
   * with {@link #setFileIcon}.  To make the correct icons be displayed,
   * when you create a new tree, you need to set it's renderer to be an
   * instance of this class (or a subclass).
   * <pre>
   *   JTree tree = new JTree( new RegistryTreeModel(...) )
   *   tree.setCellRenderer( new RegistryTreeModel.RegistryTreeCellRenderer() );
   * </pre>
   */
    public static class RegistryTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {

        public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            return this;
        }
    }
}
