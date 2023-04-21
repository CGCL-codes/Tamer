package edu.princeton.wordnet.browser.tree;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * Lazy loading default mutable node
 * 
 * @author Thierry LEFORT 3 mars 08
 * @author Bernard BOU <bbou@ac-toulouse.fr>
 */
public abstract class LazyDefaultMutableTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    /**
	 * Whether parent node kills itself when expanded to children
	 */
    public static boolean suicide = true;

    /**
	 * Default Constructor
	 */
    public LazyDefaultMutableTreeNode() {
        super();
        setAllowsChildren(true);
    }

    /**
	 * This method will be executed in a background thread.
	 * 
	 * @param thisTree
	 *            tree
	 * @return the Created nodes
	 */
    public abstract MutableTreeNode[] loadChildren(JTree thisTree);

    /**
	 * Perform the loading
	 * 
	 * @param thisTree
	 *            tree (needed to perform changes)
	 */
    public void load(final JTree thisTree) {
        if (areChildrenLoaded()) return;
        setLoading(thisTree);
        final SwingWorker<MutableTreeNode[], Void> thisWorker = createSwingWorker(thisTree);
        thisWorker.execute();
    }

    /**
	 * Define siblings and suicide
	 * 
	 * @param thisTree
	 *            tree
	 * @param theseSiblings
	 *            new sibling nodes
	 */
    protected void setSiblings(final JTree thisTree, final MutableTreeNode... theseSiblings) {
        final DefaultTreeModel thisModel = (DefaultTreeModel) thisTree.getModel();
        final DefaultMutableTreeNode thisParent = (DefaultMutableTreeNode) getParent();
        if (thisParent != null && theseSiblings != null) {
            final int thisPosition = thisModel.getIndexOfChild(thisParent, this);
            int i = thisPosition;
            for (final MutableTreeNode thisSibling : theseSiblings) {
                thisModel.insertNodeInto(thisSibling, thisParent, i++);
            }
        }
        thisModel.removeNodeFromParent(this);
    }

    /**
	 * Define node children
	 * 
	 * @param thisTree
	 *            tree
	 * @param theseChildren
	 *            new children nodes
	 */
    protected void setChildren(final JTree thisTree, final MutableTreeNode... theseChildren) {
        if (theseChildren == null) return;
        final DefaultTreeModel thisModel = (DefaultTreeModel) thisTree.getModel();
        final int thisChildCount = getChildCount();
        if (thisChildCount > 0) {
            for (int i = 0; i < thisChildCount; i++) {
                thisModel.removeNodeFromParent((MutableTreeNode) getChildAt(0));
            }
        }
        int i = 0;
        for (final MutableTreeNode thisChild : theseChildren) {
            thisModel.insertNodeInto(thisChild, this, i++);
        }
    }

    @Override
    public boolean isLeaf() {
        return !getAllowsChildren();
    }

    /**
	 * Whether children are loaded
	 * 
	 * @return <code>true</code> if there are some children
	 */
    protected boolean areChildrenLoaded() {
        return getChildCount() > 0 && getAllowsChildren();
    }

    /**
	 * Create loading node
	 * 
	 * @return a new loading node
	 */
    protected MutableTreeNode createLoadingNode() {
        final MutableTreeNode thisNode = new DefaultMutableTreeNode("Loading ...", false);
        return thisNode;
    }

    /**
	 * Set the loading state
	 */
    private void setLoading(final JTree thisTree) {
        if (!LazyDefaultMutableTreeNode.suicide) {
            setChildren(thisTree, createLoadingNode());
        } else {
            setUserObject("Loading ...");
        }
    }

    /**
	 * Reset node
	 */
    protected void reset(final JTree thisTree) {
        final DefaultTreeModel thisDefaultModel = (DefaultTreeModel) thisTree.getModel();
        final int thisChildCount = getChildCount();
        if (thisChildCount > 0) {
            for (int i = 0; i < thisChildCount; i++) {
                thisDefaultModel.removeNodeFromParent((MutableTreeNode) getChildAt(0));
            }
        }
        setAllowsChildren(true);
    }

    /**
	 * Create worker that will load the nodes
	 * 
	 * @param thisTree
	 *            tree
	 * @return the newly created SwingWorker
	 */
    protected SwingWorker<MutableTreeNode[], Void> createSwingWorker(final JTree thisTree) {
        final SwingWorker<MutableTreeNode[], Void> thisWorker = new SwingWorker<MutableTreeNode[], Void>() {

            @Override
            protected MutableTreeNode[] doInBackground() throws Exception {
                return loadChildren(thisTree);
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        final MutableTreeNode[] theseNodes = get();
                        if (!LazyDefaultMutableTreeNode.suicide) {
                            setAllowsChildren(theseNodes.length > 0);
                            setChildren(thisTree, theseNodes);
                        } else {
                            setSiblings(thisTree, theseNodes);
                        }
                        unRegisterSwingWorkerForCancel(thisTree, this);
                    } else {
                        reset(thisTree);
                    }
                } catch (final InterruptedException e) {
                    System.err.println(e.toString());
                } catch (final ExecutionException e) {
                    System.err.println(e.toString());
                }
            }
        };
        registerSwingWorkerForCancel(thisTree, thisWorker);
        return thisWorker;
    }

    private static final String ESCAPE_ACTION_NAME = "escape";

    private static final KeyStroke ESCAPE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    /**
	 * If the node is cancelable, an escape Action is registered in the tree's InputMap and ActionMap that will cancel the execution
	 * 
	 * @param thisTree
	 *            tree
	 * @param thisWorker
	 *            the worker to cancel
	 */
    @SuppressWarnings("synthetic-access")
    protected void registerSwingWorkerForCancel(final JTree thisTree, final SwingWorker<MutableTreeNode[], ?> thisWorker) {
        thisTree.getInputMap().put(LazyDefaultMutableTreeNode.ESCAPE_KEY, LazyDefaultMutableTreeNode.ESCAPE_ACTION_NAME);
        final Action thisAction = thisTree.getActionMap().get(LazyDefaultMutableTreeNode.ESCAPE_ACTION_NAME);
        if (thisAction == null) {
            final CancelWorkersAction thisCancelWorkerAction = new CancelWorkersAction();
            thisCancelWorkerAction.addSwingWorker(thisWorker);
            thisTree.getActionMap().put(LazyDefaultMutableTreeNode.ESCAPE_ACTION_NAME, thisCancelWorkerAction);
        } else {
            if (thisAction instanceof CancelWorkersAction) {
                final CancelWorkersAction cancelAction = (CancelWorkersAction) thisAction;
                cancelAction.addSwingWorker(thisWorker);
            }
        }
    }

    /**
	 * Remove the swingWorker from the cancelable task of the tree
	 * 
	 * @param thisTree
	 *            tree
	 * @param thisWorker
	 *            worker thread
	 */
    @SuppressWarnings("synthetic-access")
    protected void unRegisterSwingWorkerForCancel(final JTree thisTree, final SwingWorker<MutableTreeNode[], ?> thisWorker) {
        final Action thisAction = thisTree.getActionMap().get(LazyDefaultMutableTreeNode.ESCAPE_ACTION_NAME);
        if (thisAction != null && thisAction instanceof CancelWorkersAction) {
            final CancelWorkersAction thisCancelWorkerAction = new CancelWorkersAction();
            thisCancelWorkerAction.removeSwingWorker(thisWorker);
        }
    }

    /**
	 * ActionMap can only store one Action for the same key, This Action Stores the list of SwingWorker to be canceled if the escape key is pressed.
	 */
    protected static class CancelWorkersAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        /** the SwingWorkers */
        private final Vector<SwingWorker<MutableTreeNode[], ?>> theWorkers = new Vector<SwingWorker<MutableTreeNode[], ?>>();

        /** Default constructor */
        private CancelWorkersAction() {
            super(LazyDefaultMutableTreeNode.ESCAPE_ACTION_NAME);
        }

        /** add a cancelable SwingWorker */
        public void addSwingWorker(final SwingWorker<MutableTreeNode[], ?> thisWorker) {
            this.theWorkers.add(thisWorker);
        }

        /** remove a SwingWorker */
        public void removeSwingWorker(final SwingWorker<MutableTreeNode[], ?> worker) {
            this.theWorkers.remove(worker);
        }

        public void actionPerformed(final ActionEvent e) {
            final Iterator<SwingWorker<MutableTreeNode[], ?>> theseWorkers = this.theWorkers.iterator();
            while (theseWorkers.hasNext()) {
                final SwingWorker<MutableTreeNode[], ?> thisWorker = theseWorkers.next();
                thisWorker.cancel(true);
            }
        }
    }
}
