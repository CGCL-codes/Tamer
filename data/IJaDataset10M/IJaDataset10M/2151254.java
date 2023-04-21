package foa.layout.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class PagePopupMenu extends JPopupMenu {

    private Container c;

    private String pageName;

    public PagePopupMenu(Container c, String pageName) {
        this.c = c;
        this.pageName = pageName;
        JMenuItem menuItem;
        menuItem = add(new NewPageAction(c));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.getAccessibleContext().setAccessibleDescription("New Page");
        menuItem = add(new ModifyPageAction(c, pageName));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription("Modify Page");
        menuItem = add(new DeletePageAction(c, pageName));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Page");
    }
}
