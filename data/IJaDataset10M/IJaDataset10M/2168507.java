package net.sf.amemailchecker.gui.messageviewer.foldertree;

import net.sf.amemailchecker.app.extension.DefaultMailActionContext;
import net.sf.amemailchecker.app.extension.ExtensionProvider;
import net.sf.amemailchecker.app.extension.viewer.MessageViewerContext;
import net.sf.amemailchecker.app.extension.viewer.ViewerExtensionPoint;
import net.sf.amemailchecker.gui.messageviewer.MessageViewer;
import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FolderTreeExtensionMouseListener extends MouseAdapter {

    public void mouseClicked(MouseEvent e) {
        JTree tree = (JTree) e.getComponent();
        TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
        TreePath selectionPath = tree.getSelectionPath();
        if (treePath == null || selectionPath == null || !selectionPath.equals(treePath)) {
            return;
        }
        List<ViewerExtensionPoint> extensions = ExtensionProvider.Provider.getViewerExtensionPoints();
        MessageViewerContext viewerContext = MessageViewer.Viewer.getContext();
        DefaultMailActionContext context = new DefaultMailActionContext();
        context.setAccount(MessageViewer.Viewer.getContext().getAccount());
        context.setFolder(MessageViewer.Viewer.getContext().getFolder());
        if (e.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            int count = 0;
            for (ViewerExtensionPoint extension : extensions) {
                Action[] actions = extension.onFolder(viewerContext, context);
                JMenu actionsMenu = extension.menu(context);
                if (actions == null || actions.length <= 0) continue;
                if (actionsMenu != null) {
                    for (Action action : actions) actionsMenu.add(new JMenuItem(action));
                    menu.add(actionsMenu);
                    continue;
                }
                if (count > 0) menu.add(new JSeparator(JSeparator.HORIZONTAL));
                boolean added = false;
                for (Action action : actions) {
                    if (action.isEnabled()) {
                        menu.add(new JMenuItem(action));
                        added = true;
                    }
                }
                if (added) ++count;
            }
            if (menu.getComponentCount() <= 0) return;
            menu.show(tree, e.getX(), e.getY());
        }
    }
}
