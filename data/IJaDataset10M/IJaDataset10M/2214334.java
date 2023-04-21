package net.confex.html;

import net.confex.action.TranslatableAction;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

/**
 * �������� "�������� HTML text"
 */
public class AddHtmlTextTreeNodeAction extends TranslatableAction {

    private NavigationView navigation_view;

    protected String getID() {
        return "net.confex.html.AddHtmlTextTreeNodeAction";
    }

    protected String getTextKey() {
        return "ACTION_ADD_HTMLTEXTNODE";
    }

    protected String getIconFileName() {
        return HtmlTextNode.getDefaultImageName();
    }

    public AddHtmlTextTreeNodeAction(NavigationView navigation_view) {
        super();
        this.navigation_view = navigation_view;
    }

    public void run() {
        ISelection selection = navigation_view.getTreeViewer().getSelection();
        TreeSelection ss = (TreeSelection) selection;
        Object obj = null;
        if (ss.size() == 0) {
            obj = navigation_view.getConfigTree().getRoot();
        } else {
            if (!navigation_view.cantDoWhenNotOneSelected(ss)) return;
            obj = ss.getFirstElement();
        }
        HtmlTextNode tree_node = new HtmlTextNode(navigation_view.getConfigTree(), navigation_view);
        if (obj instanceof ITreeNode) {
            ((ITreeNode) obj).addChild(tree_node);
            tree_node.openPropertyDialog(navigation_view, navigation_view.getSite().getShell(), true);
            navigation_view.getTreeViewer().refresh(obj);
            navigation_view.getTreeViewer().setExpandedState(obj, true);
        }
    }
}
