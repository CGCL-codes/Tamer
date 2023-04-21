package foa.layout.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class NoRegionsPopupMenu extends JPopupMenu {

    private Container container;

    public NoRegionsPopupMenu(Container c, String pageName, String region) {
        container = c;
        JMenuItem menuItem;
        menuItem = add(new NewRegionAction(c, pageName));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription("New Region");
        menuItem = add(new ModifyRegionAction(c, pageName, region));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription("Modify Region");
    }
}
