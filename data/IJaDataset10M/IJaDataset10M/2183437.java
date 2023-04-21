package net.sf.refactorit.jbuilder.optionsui;

import com.borland.primetime.vfs.Url;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

/**
 * A <code>javax.swing.tree.TreeCellRenderer</code> implementation for use with a
 * <code>@see net.sf.refactorit.ui.options.TreeChooser</code>. Is used in
 * <code>JBClasspathChooser</code> in it's "add from projects paths" usecase.
 *
 * @author juri
 */
public class JbUrlTreeCellRenderer extends DefaultTreeCellRenderer {

    private JbUrlsTreeModel urlsModel;

    public JbUrlTreeCellRenderer(JbUrlsTreeModel urlsModel) {
        this.urlsModel = urlsModel;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if ((value instanceof String)) {
            return component;
        }
        Url url = (Url) value;
        if (urlsModel.getRootDataObjects().contains(url)) {
            setText(url.getFullName());
        } else {
            setText(url.getName());
        }
        return component;
    }
}
