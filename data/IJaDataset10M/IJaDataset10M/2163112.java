package de.molimo.client.wings.finder;

import java.rmi.RemoteException;
import org.wings.SComponent;
import org.wings.STree;
import org.wings.tree.SDefaultTreeCellRenderer;
import de.molimo.container.exceptions.IExceptionHandler;
import de.molimo.container.exceptions.IExceptionHandlerContainer;

/**
 * Created by .
 * User: Manowar
 * Date: Oct 23, 2003
 * Time: 4:01:43 AM
 * To change this template use Options | File Templates.
 */
public class FinderItemTreeCellRenderer extends SDefaultTreeCellRenderer implements IExceptionHandlerContainer {

    private IExceptionHandler handler;

    public void setExceptionHandler(IExceptionHandler handler) {
        this.handler = handler;
    }

    public SComponent getTreeCellRendererComponent(STree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        FinderItemTreeNode node = (FinderItemTreeNode) value;
        try {
            if (node.getFinderItem().isExpanded() != expanded) {
                node.getFinderItem().setExpanded(expanded);
                node.createForm();
            }
        } catch (RemoteException re) {
            handler.handle(re);
        }
        return node.getContainer();
    }
}
