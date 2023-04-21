package net.sourceforge.sqlexplorer.dbstructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.sqlexplorer.dbstructure.actions.AbstractDBTreeContextAction;
import net.sourceforge.sqlexplorer.dbstructure.nodes.INode;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.plugin.views.DatabaseStructureView;
import net.sourceforge.sqlexplorer.util.ImageUtil;
import net.sourceforge.sqlexplorer.util.TextUtil;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.ActionGroup;

/**
 * ActionGroup for Database Structure View. This group controls what context
 * menu actions are being shown for which node.
 * 
 * @author Davy Vanherbergen
 */
public class DBTreeActionGroup extends ActionGroup {

    private TreeViewer _treeViewer;

    private DatabaseStructureView _view;

    /**
     * Construct a new action group for a given database structure outline.
     * 
     * @param treeViewer TreeViewer used for this outline.
     */
    public DBTreeActionGroup(TreeViewer treeViewer, DatabaseStructureView view) {
        _treeViewer = treeViewer;
        _view = view;
    }

    /**
     * Fill the node context menu with all the correct actions.
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void fillContextMenu(IMenuManager menu) {
        IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
        if (selection == null) {
            return;
        }
        ArrayList selectedNodes = new ArrayList();
        Iterator it = selection.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof INode) {
                selectedNodes.add(object);
            }
        }
        if (selectedNodes.size() == 0) {
            return;
        }
        INode[] nodes = (INode[]) selectedNodes.toArray(new INode[] {});
        IAction[] actions = getContextActions(nodes);
        for (int i = 0; i < actions.length; i++) {
            menu.add(actions[i]);
        }
    }

    /**
     * Loop through all extensions and add the appropriate actions.
     * 
     * Actions are selected by database product name, node type and
     * availability.
     * 
     * @param nodes currently selected nodes
     * @return array of actions
     */
    private IAction[] getContextActions(INode[] nodes) {
        String databaseProductName = nodes[0].getSession().getRoot().getDatabaseProductName().toLowerCase().trim();
        String nodeType = nodes[0].getType().toLowerCase().trim();
        List actions = new ArrayList();
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("net.sourceforge.sqlexplorer", "nodeContextAction");
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IExtension e = extensions[i];
            IConfigurationElement[] ces = e.getConfigurationElements();
            for (int j = 0; j < ces.length; j++) {
                try {
                    boolean isValidProduct = false;
                    boolean isValidNodeType = false;
                    String id = ces[j].getAttribute("id");
                    String[] validProducts = ces[j].getAttribute("database-product-name").split(",");
                    String[] validNodeTypes = ces[j].getAttribute("node-type").split(",");
                    String imagePath = ces[j].getAttribute("icon");
                    for (int k = 0; k < validProducts.length; k++) {
                        String product = validProducts[k].toLowerCase().trim();
                        if (product.length() == 0) {
                            continue;
                        }
                        if (product.equals("*")) {
                            isValidProduct = true;
                            break;
                        }
                        String regex = TextUtil.replaceChar(product, '*', ".*");
                        if (databaseProductName.matches(regex)) {
                            isValidProduct = true;
                            break;
                        }
                    }
                    if (!isValidProduct) {
                        continue;
                    }
                    for (int k = 0; k < validNodeTypes.length; k++) {
                        String type = validNodeTypes[k].toLowerCase().trim();
                        if (type.length() == 0) {
                            continue;
                        }
                        if (type.equals("*")) {
                            isValidNodeType = true;
                            break;
                        }
                        String regex = TextUtil.replaceChar(type, '*', ".*");
                        if (nodeType.matches(regex)) {
                            isValidNodeType = true;
                            break;
                        }
                    }
                    if (!isValidNodeType) {
                        continue;
                    }
                    AbstractDBTreeContextAction action = (AbstractDBTreeContextAction) ces[j].createExecutableExtension("class");
                    action.setSelectedNodes(nodes);
                    action.setTreeViewer(_treeViewer);
                    action.setView(_view);
                    String fragmentId = id.substring(0, id.indexOf('.', 28));
                    if (imagePath != null && imagePath.trim().length() != 0) {
                        action.setImageDescriptor(ImageUtil.getFragmentDescriptor(fragmentId, imagePath));
                    }
                    if (action.isAvailable()) {
                        actions.add(action);
                    }
                } catch (Throwable ex) {
                    SQLExplorerPlugin.error("Could not create menu action", ex);
                }
            }
        }
        return (IAction[]) actions.toArray(new IAction[] {});
    }
}
