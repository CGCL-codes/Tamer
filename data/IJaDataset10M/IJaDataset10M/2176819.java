package client.gui.play;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import client.gui.Window;
import client.gui.WindowMenu;

public class PlayWindowMenu extends WindowMenu {

    public PlayWindowMenu(Window window) {
        super(window);
    }

    @Override
    public void initialize() {
        super.initialize();
        JMenu menu = new JMenu("Match");
        JMenuItem menuItem = new JMenuItem("Replay");
        menuItem.setActionCommand("Replay");
        menuItem.addActionListener(window);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Cancel");
        menuItem.setActionCommand("CancelAction");
        menuItem.addActionListener(window);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menu.add(menuItem);
        menuItem = new JMenuItem("Start");
        menuItem.setActionCommand("Start");
        menuItem.addActionListener(window);
        menu.add(menuItem);
        menuItem = new JMenuItem("Pause");
        menuItem.setActionCommand("Pause");
        menuItem.addActionListener(window);
        menu.add(menuItem);
        menuItem = new JMenuItem("Stop");
        menuItem.setActionCommand("Stop");
        menuItem.addActionListener(window);
        menu.add(menuItem);
        this.add(menu);
    }
}
