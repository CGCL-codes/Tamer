package owlwatcher.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * This factory creates popups that will appear when a class node receives right click
 * @author peter
 */
public class ClassPopupFactory {

    static JTree tx;

    static ConcreteTreeNode closestNode = null;

    public ClassPopupFactory() {
    }

    /**
     * Returns a popup menu for this tree
     * @param t tree associated with the popup returned
     * @return a popup menu for nodes in this tree
     */
    public static JPopupMenu getClassPopup(JTree t) {
        tx = t;
        JPopupMenu result = new JPopupMenu("Modify Class");
        result.addPopupMenuListener(new ClassPopupMenuListener());
        JMenuItem addSubClassItem = new JMenuItem("Add subclass");
        ActionListener actionListener = new ClassPopupActionListener();
        addSubClassItem.addActionListener(actionListener);
        result.add(addSubClassItem);
        JMenuItem deleteItem = new JMenuItem("Delete Class");
        deleteItem.addActionListener(actionListener);
        result.add(deleteItem);
        JMenuItem instancesItem = new JMenuItem("Show Instances");
        instancesItem.addActionListener(actionListener);
        result.add(instancesItem);
        return result;
    }

    /**
     * 
     * @author peter
     * created Mar 25, 2008
     *
     */
    static class ClassPopupActionListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            if (closestNode != null) {
                String command = actionEvent.getActionCommand();
                if (command.compareTo("Add subclass") == 0) closestNode.processCommand(ClassPane.Command.addsubclass); else if (command.compareTo("Delete Class") == 0) closestNode.processCommand(ClassPane.Command.deletesubclass); else if (command.compareTo("Show Instances") == 0) closestNode.processCommand(ClassPane.Command.showinstances);
            }
        }
    }

    /**
     * Class to handle events for the menu
     * @author peter
     * created Mar 25, 2008
     *
     */
    static class ClassPopupMenuListener implements PopupMenuListener {

        /**
         * handles menu cancel events
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            closestNode = null;
        }

        /**
         * handles menu becoming visible by capturing the nearest tree node
         */
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            if (e.getSource() instanceof JPopupMenu) {
                Point foo = tx.getMousePosition();
                TreePath closestPath = tx.getClosestPathForLocation(foo.x, foo.y);
                Object closestTN = closestPath.getLastPathComponent();
                if (closestTN instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode closestDMTN = (DefaultMutableTreeNode) closestTN;
                    if (closestDMTN.getUserObject() instanceof ConcreteTreeNode) {
                        closestNode = (ConcreteTreeNode) closestDMTN.getUserObject();
                    }
                }
            }
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }
    }
}
