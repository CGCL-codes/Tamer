package org.deft.widgets.importdialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;

/**
 * With the SegmentNode it is possible to build simple tree structures for
 * example file trees. We use this to make a hierarchical structure out of the
 * flat IPath representation. So we can merge several of these SegmentNode trees
 * to get a full file tree.
 * 
 */
public class SegmentNode {

    private static Logger logger = Logger.getLogger(SegmentNode.class);

    /**
	 * The parent of this Node
	 */
    private SegmentNode parent;

    /**
	 * The children of this Node
	 */
    private List<SegmentNode> children = new ArrayList<SegmentNode>();

    /**
	 * The absolute path. Only available if this Node represents a leaf (file)
	 */
    private IPath path;

    /**
	 * The segment of the path which is represented through this node
	 */
    private String segment;

    /**
	 * Creates a List of SegmentNodes which represent a path to a file
	 * 
	 * @param path
	 * @return
	 */
    public static SegmentNode createTree(IPath path) {
        SegmentNode result = new SegmentNode("base", null);
        SegmentNode lastNode = result;
        for (int i = 0; i < path.segmentCount(); i++) {
            String segment = path.segment(i);
            SegmentNode newNode = new SegmentNode(segment, lastNode);
            if (i + 1 == path.segmentCount()) {
                newNode.setPath(path);
            }
            lastNode = newNode;
        }
        return result;
    }

    /**
	 * Helper to print a tree to the console
	 * 
	 * @param tree
	 *            The root node of the tree to print
	 * @param separator
	 *            Give an empty String here
	 */
    public static void printTree(SegmentNode tree, String separator) {
        logger.debug(separator + " " + tree.getSegment());
        separator += "-";
        for (SegmentNode child : tree.getChildren()) {
            printTree(child, separator);
        }
    }

    /**
	 * Creates a SegmentNode tree out of a collection of filepaths
	 * 
	 * @param paths
	 *            A collection of complete file paths
	 * 
	 * @return A tree structure based on SegmentNodes
	 */
    public static SegmentNode createFileTree(Collection<IPath> paths) {
        SegmentNode baseNode = new SegmentNode("base", null);
        for (IPath path : paths) {
            SegmentNode node = createTree(path);
            mergeTree(baseNode, node);
        }
        while (baseNode.getChildren().size() == 1) {
            SegmentNode child = baseNode.getChildren().get(0);
            if (child.getChildren().size() != 0) {
                baseNode = child;
            } else {
                break;
            }
        }
        return baseNode;
    }

    /**
	 * Recursion for doing the real work of createFileTree()
	 * 
	 * @param result
	 *            The Tree into which to insert the new SubTree
	 * @param list
	 *            The SubTree which should be inserted into the result Tree
	 * @return true, if root node of the SubTree was matched, false otherwise
	 * 
	 */
    private static boolean mergeTree(SegmentNode result, SegmentNode list) {
        if (result.getSegment().equals(list.getSegment())) {
            if (result.getChildren().size() == 0) {
                result.addChild(list.getChildren().get(0));
            } else {
                boolean couldbeMerged = false;
                for (SegmentNode child : result.getChildren()) {
                    couldbeMerged = couldbeMerged || mergeTree(child, list.getChildren().get(0));
                }
                if (!couldbeMerged) {
                    result.addChild(list.getChildren().get(0));
                }
            }
            return true;
        }
        return false;
    }

    /**
	 * 
	 * @param label
	 * @param parent
	 */
    public SegmentNode(String label, SegmentNode parent) {
        this.segment = label;
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public SegmentNode getParent() {
        return parent;
    }

    public List<SegmentNode> getChildren() {
        return children;
    }

    public void addChild(SegmentNode child) {
        children.add(child);
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }

    public void setPath(IPath path) {
        this.path = path;
    }

    /**
	 * If the Segment represents a File this will return the Path, if it
	 * represent a Folder it will return null.
	 * 
	 * @return Path if it is a Path, null if it is a Folder
	 */
    public IPath getPath() {
        return path;
    }

    public boolean hasChildren() {
        if (children.size() > 0) {
            return true;
        }
        return false;
    }
}
