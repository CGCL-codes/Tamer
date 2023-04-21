package tchoukstats.client.gui.replay;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import tchoukstats.client.gui.Window;
import tchoukstats.client.gui.WindowMenu;
import java.awt.event.ActionEvent;

public class ReplayWindowMenu extends WindowMenu {

    public ReplayWindowMenu(Window window) {
        super(window);
    }

    @Override
    public void initialize() {
        super.initialize();
        JMenu menu = new JMenu("Match");
        JMenuItem menuItem = new JMenuItem("Play");
        menuItem.setActionCommand("Play");
        menuItem.addActionListener(window);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menu.add(menuItem);
        this.add(menu);
    }
}
