package org.objectstyle.cayenne.project;

import java.util.ArrayList;
import java.util.List;
import org.objectstyle.cayenne.access.DataNode;

/**
 * FlatProjectView converts a project tree into a list of nodes,
 * thus flattening the tree. Normally used as a singleton.
 * 
 * @author Andrei Adamchik
 */
public class FlatProjectView {

    protected static FlatProjectView instance = new FlatProjectView();

    /** 
     * Returns a FlatProjectView singleton.
     */
    public static FlatProjectView getInstance() {
        return instance;
    }

    /**
     * Returns flat tree view.
     */
    public List flattenProjectTree(Object rootNode) {
        List nodes = new ArrayList();
        TraversalHelper helper = new TraversalHelper(nodes);
        new ProjectTraversal(helper).traverse(rootNode);
        return nodes;
    }

    /**
     * Helper class that serves as project traversal helper.
     */
    class TraversalHelper implements ProjectTraversalHandler {

        protected List nodes;

        public TraversalHelper(List nodes) {
            this.nodes = nodes;
        }

        public void projectNode(ProjectPath path) {
            nodes.add(path);
        }

        /**
         * Returns true unless an object is a DataNode.
         */
        public boolean shouldReadChildren(Object node, ProjectPath parentPath) {
            return !(node instanceof DataNode);
        }
    }
}
