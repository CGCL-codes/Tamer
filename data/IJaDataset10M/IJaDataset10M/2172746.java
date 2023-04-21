package gui;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author tophe
 */
public class LogsTree extends JTree {

    public LogsTree() {
        super();
    }

    private void logsJTreeMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 0) {
            TreePath path = ((JTree) evt.getSource()).getPathForLocation(evt.getX(), evt.getY());
            if (null != path) {
                Object o = path.getLastPathComponent();
                System.out.println(o.toString());
            }
        }
    }

    private void logsJTreeKeyPressed(java.awt.event.KeyEvent evt) {
    }
}
