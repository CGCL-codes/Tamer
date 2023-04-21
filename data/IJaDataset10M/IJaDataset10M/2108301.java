package org.hawksee.javase.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.hawksee.javase.MainFrame;

public class MainMenu extends JMenuBar implements ActionListener {

    public static final long serialVersionUID = 0;

    private static final String ACTION_BLEND = "Blend";

    private static final String ACTION_OPEN = "Open";

    private static final String ACTION_QUIT = "Quit";

    private static final String ACTION_RESET = "Reset";

    private static final String ACTION_SET_TARGET = "SetTarget";

    private MainFrame mainFrame;

    public MainMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        JMenu fileMenu = new JMenu("File");
        ArrayList<String> fileItems = new ArrayList<String>();
        fileItems.add(ACTION_OPEN);
        fileItems.add(ACTION_QUIT);
        for (String fileItem : fileItems) {
            JMenuItem menuItem = new JMenuItem(fileItem);
            menuItem.setActionCommand(fileItem);
            menuItem.addActionListener(this);
            fileMenu.add(menuItem);
        }
        JMenu actionsMenu = new JMenu("Actions");
        ArrayList<String> actionsItems = new ArrayList<String>();
        actionsItems.add(ACTION_BLEND);
        actionsItems.add(ACTION_RESET);
        actionsItems.add(ACTION_SET_TARGET);
        for (String actionItem : actionsItems) {
            JMenuItem menuItem = new JMenuItem(actionItem);
            menuItem.setActionCommand(actionItem);
            menuItem.addActionListener(this);
            actionsMenu.add(menuItem);
        }
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.add(new RegistratorMenu(mainFrame));
        this.add(fileMenu);
        this.add(actionsMenu);
        this.add(toolsMenu);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
        if (actionCmd == MainMenu.ACTION_OPEN) {
            this.mainFrame.openImage();
        } else if (actionCmd == MainMenu.ACTION_RESET) {
            this.mainFrame.getManager().reset();
        } else if (actionCmd == MainMenu.ACTION_SET_TARGET) {
            this.mainFrame.getManager().targetImage();
        } else if (actionCmd == MainMenu.ACTION_QUIT) {
            this.mainFrame.quit();
        }
        this.mainFrame.repaint();
    }
}
