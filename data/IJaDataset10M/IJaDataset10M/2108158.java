package org.adapit.wctoolkit.fomda.events.popups;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.adapit.wctoolkit.infrastructure.diagram.AbstractDiagramInternalFrame;
import org.adapit.wctoolkit.infrastructure.popups.AbstractElementPopupMenu;
import org.adapit.wctoolkit.infrastructure.treecontrollers.DefaultElementTreeController;

public class FeaturesModelPopupMenu extends AbstractElementPopupMenu implements ActionListener {

    public FeaturesModelPopupMenu(DefaultElementTreeController controller, org.adapit.wctoolkit.uml.ext.core.IElement element, Component component, DefaultMutableTreeNode node, JTree tree) {
        super(controller, element, component, node, tree);
        this.element = ((org.adapit.wctoolkit.uml.classes.kernel.Class) element);
    }

    public FeaturesModelPopupMenu() {
        super();
    }

    public JPopupMenu getPopup() {
        if (popup != null) return popup;
        popup = new JPopupMenu();
        JMenuItem menu = new JMenuItem("Edit");
        menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    ((AbstractDiagramInternalFrame) element).setTitle(element.getName());
                    controller.getDiagramDesktopObserver().addInternalFrame((JInternalFrame) element);
                    ((AbstractDiagramInternalFrame) element).updateUI();
                    ((AbstractDiagramInternalFrame) element).notifyElementChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        popup.add(menu);
        return popup;
    }

    public void showPopupMenu(int x, int y) {
        popup.show(component, x, y);
    }

    public void actionPerformed(ActionEvent e) {
    }
}
