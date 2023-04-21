package de.sopra.view.tree;

import de.sopra.controller.ControllerFactory;
import de.sopra.controller.facade.ControllerInterface;
import de.sopra.model.preparation.ReadableTestElement;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * A {@link TreeSelectionListener} for a tree of
 * {@link ReadableTestElement ReadableTestElements}. A {@link DataTreeModel} is
 * needed by this listener to communicate the last selected paths to. This in
 * turn is necessary for incremental updates of the tree.
 *
 * @author Falko K&ouml;tter
 *
 */
public class DataTreeSelectionListener implements TreeSelectionListener {

    /**
     * The TreeModel to which the listener communicates the selected paths.
     */
    private DataTreeModel treeModel;

    /**
     * Creates a new DataTreeSelectionListener.
     *
     * @param treeModel
     *            The TreeModel which will be notified of all selected paths by
     *            this listener. Note that the listener and the TreeModel should
     *            belong to the same tree.
     */
    public DataTreeSelectionListener(DataTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    /**
     * Notifies the {@link ControllerInterface controller} and the
     * <code>treeModel</code> of the new selection.
     *
     * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            treeModel.setLastSelected(e.getPath());
            ControllerFactory.getTOMController().changeDisplayedTestElement(((ReadableTestElement) e.getPath().getLastPathComponent()));
        }
    }
}
