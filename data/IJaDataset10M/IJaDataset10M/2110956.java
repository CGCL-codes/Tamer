package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.tree.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.tools.Messages;

/**
 * Adds the selected attributes (or attributes that are under selected nodes) to
 * the current context.
 * <UL>
 * <LI>Gets the list of attributes that are under one of the selected tree
 * nodes; if that list is empty, do nothing.
 * <LI>Adds those attributes to the current ContextAttributesTreeModel instance.
 * <LI>Logs the action's success or failure.
 * </UL>
 * 
 * @author CLAISSE
 */
public class AddSelectedContextAttributesAction extends BensikinAction {

    static final Logger logger = LoggerFactory.getLogger(AddSelectedContextAttributesAction.class);

    private static final long serialVersionUID = -5872593224821475505L;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name
     */
    public AddSelectedContextAttributesAction(final String name) {
        putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        final PossibleAttributesTree leftTree = PossibleAttributesTree.getInstance();
        final Vector<TreePath> listToAdd = leftTree.getListOfAttributesTreePathUnderSelectedNodes(false);
        if (listToAdd.size() != 0) {
            final ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance(false);
            try {
                model.addSelectedAttributes(listToAdd, true);
                model.reload();
                if (ContextAttributesTree.getInstance() != null) {
                    ContextAttributesTree.getInstance().expandAll(true);
                }
                final String msg = Messages.getLogMessage("ADD_SELECTED_CONTEXT_ATTRIBUTES_ACTION_OK");
                logger.debug(msg);
            } catch (final Exception e) {
                final String msg = Messages.getLogMessage("ADD_SELECTED_CONTEXT_ATTRIBUTES_ACTION_KO");
                logger.error(msg, e);
                return;
            }
            ContextActionPanel.getInstance().allowPrint(false);
        }
    }
}
