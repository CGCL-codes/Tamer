package net.hawk.digiextractor.GUI.DirectoryTree;

import java.io.File;
import java.io.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A class implementing the nodes in a directory tree.
 * 
 * @author Hawk
 */
public class DirectoryTreeNode extends DefaultMutableTreeNode {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** true if the children of this node have been loaded. */
    private boolean allreadyLoaded = false;

    /** The true if this node is the root node. */
    private boolean isRoot = false;

    /**
	 * Create a new root node.
	 */
    public DirectoryTreeNode() {
        super("root");
        isRoot = true;
        for (File f : File.listRoots()) {
            add(new DirectoryTreeNode(f));
        }
        loadGrandChildren();
    }

    /**
	 * The Constructor.
	 * 
	 * @param userObject the user object
	 */
    public DirectoryTreeNode(final Object userObject) {
        super(userObject);
    }

    /**
	 * The Constructor.
	 * 
	 * @param userObject the user object
	 * @param allowsChildren the allows children
	 */
    public DirectoryTreeNode(final Object userObject, final boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    /**
	 * Load children.
	 */
    public final void loadChildren() {
        removeAllChildren();
        if (isRoot) {
            for (File f : File.listRoots()) {
                add(new DirectoryTreeNode(f));
            }
        } else if (!allreadyLoaded) {
            File[] children = ((File) getUserObject()).listFiles(new FileFilter() {

                public boolean accept(final File pathname) {
                    return pathname.isDirectory();
                }
            });
            if (children != null) {
                for (File dir : children) {
                    add(new DirectoryTreeNode(dir));
                }
            }
        }
    }

    /**
	 * Load grand children.
	 */
    public final void loadGrandChildren() {
        if (children != null) {
            for (Object o : children) {
                if (o instanceof DirectoryTreeNode) {
                    ((DirectoryTreeNode) o).loadChildren();
                }
            }
        }
    }

    /**
	 * Gets the child by file.
	 * 
	 * @param f the file for which we should get the node
	 * 
	 * @return the child this file specifies
	 */
    public final DirectoryTreeNode getChildByFile(final File f) {
        DirectoryTreeNode result = null;
        if (children != null) {
            for (Object node : children) {
                DirectoryTreeNode candidate = (DirectoryTreeNode) node;
                if (candidate.contains(f)) {
                    result = candidate;
                }
            }
        }
        return result;
    }

    /**
	 * Contains.
	 * 
	 * @param f the f
	 * 
	 * @return true, if successful
	 */
    public final boolean contains(final File f) {
        return f.equals(userObject);
    }

    /**
	 *  Returns a string representation of this object.
	 *  
	 *  @return the String representation.
	 * @see javax.swing.tree.DefaultMutableTreeNode#toString()
	 */
    public final String toString() {
        if (isRoot) {
            return "root";
        } else {
            File f = (File) getUserObject();
            if (f.getName().isEmpty()) {
                return f.toString();
            } else {
                return f.getAbsolutePath();
            }
        }
    }
}
