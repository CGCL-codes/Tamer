package org.xaware.ide.xadev.gui.actions;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.datamodel.XATreeNode;
import org.xaware.ide.xadev.gui.DNDTreeHandler;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This action collapses the selected element of the tree completely. The
 * selected element, and recursively every child element will be fully
 * collapsed.
 *
 * @author Satish K
 * @author Chandresh Gandhi
 * @version 1.0
 */
public class CollapseAllAction extends GlobalTreeEditAction {

    /** Holds instance of XA_Designer_Plugin translator */
    private static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Xaware Logger. */
    private final XAwareLogger logger = XAwareLogger.getXAwareLogger(CollapseAllAction.class.getName());

    /**
     * Creates a new CollapseSelectedElementAction object.
     */
    public CollapseAllAction() {
        super();
        setText(translator.getString("Collapse All"));
        setToolTipText(translator.getString("Collapse all levels of this element"));
    }

    /**
     * All levels of the selected node of the tree will be collapsed.
     */
    @Override
    public void run() {
        try {
            final DNDTreeHandler theTree = getTree();
            XA_Designer_Plugin.makeBusy(XA_Designer_Plugin.getShell(), "Collapse All Action.");
            final XATreeNode selectedNodes[] = theTree.getSelectedNodes();
            for (int i = 0; i < selectedNodes.length; i++) {
                if (selectedNodes[i] != null) {
                    theTree.getTreeViewer().collapseToLevel(selectedNodes[i], AbstractTreeViewer.ALL_LEVELS);
                }
            }
        } catch (final Exception exception) {
            logger.finest("Exception occured while collapsing the selected Element", exception);
        } finally {
            XA_Designer_Plugin.makeUnBusy();
        }
    }
}
