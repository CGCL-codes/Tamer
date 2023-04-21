package org.jpokerparser.application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.jpokerparser.gui.components.OpacityCheckboxAction;
import org.jpokerparser.gui.components.TransparentBackground;

public class JPokerParser {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);
        menuItem = new JMenuItem("New Tournament", KeyEvent.VK_T);
        menuItem.setAction(new OpacityCheckboxAction(frame));
        menu.add(menuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(500, 500);
        frame.setVisible(true);
    }
}
