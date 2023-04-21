package xpg.tools.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import xpg.tools.ui.swing.actions.About;
import xpg.tools.ui.swing.actions.Exit;

public class MainMenu extends JMenuBar {

    public MainMenu() {
        JMenuItem menuItem;
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menu.add(new Exit());
        add(menu);
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuItem = new JMenuItem("Preferences ...", KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);
        add(menu);
        menu = new JMenu("Help");
        menuItem = new JMenuItem("Contents", KeyEvent.VK_F1);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
        menu.add(menuItem);
        menu.add(new About());
        add(menu);
    }
}
