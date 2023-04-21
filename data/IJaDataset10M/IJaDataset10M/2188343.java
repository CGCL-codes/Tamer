package jam.framework;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author rambaut
 *         Date: Dec 26, 2004
 *         Time: 11:01:40 AM
 *
 * @version $Id: DefaultEditMenuFactory.java 959 2008-11-30 18:47:28Z rambaut $
 */
public class DefaultEditMenuFactory implements MenuFactory {

    public String getMenuName() {
        return "Edit";
    }

    public void populateMenu(JMenu menu, AbstractFrame frame) {
        JMenuItem item;
        menu.setMnemonic('E');
        item = new JMenuItem(frame.getCutAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, MenuBarFactory.MENU_MASK));
        menu.add(item);
        item = new JMenuItem(frame.getCopyAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, MenuBarFactory.MENU_MASK));
        menu.add(item);
        item = new JMenuItem(frame.getPasteAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, MenuBarFactory.MENU_MASK));
        menu.add(item);
        item = new JMenuItem(frame.getDeleteAction());
        menu.add(item);
        item = new JMenuItem(frame.getSelectAllAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, MenuBarFactory.MENU_MASK));
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(frame.getFindAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, MenuBarFactory.MENU_MASK));
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(frame.getPreferencesAction());
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, MenuBarFactory.MENU_MASK));
        menu.add(item);
    }

    public int getPreferredAlignment() {
        return LEFT;
    }
}
