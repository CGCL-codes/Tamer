package net.sf.ajaxplus.tool.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBarFactory {

    private JFrame root;

    public JMenuBar init(JFrame root) {
        this.root = root;
        JMenuBar menubar = new JMenuBar();
        menubar.add(createMenuFile());
        menubar.add(createMenuView());
        menubar.add(createMenuGenerator());
        menubar.add(createMenuTools());
        menubar.add(createMenuWindow());
        menubar.add(createMenuHelp());
        return menubar;
    }

    private JMenu createMenuHelp() {
        JMenu menuFile = new JMenu("Help");
        return menuFile;
    }

    private JMenu createMenuWindow() {
        JMenu menuFile = new JMenu("Window");
        return menuFile;
    }

    private JMenu createMenuTools() {
        JMenu menuFile = new JMenu("Tools");
        return menuFile;
    }

    private JMenu createMenuGenerator() {
        JMenu menuFile = new JMenu("Generator");
        return menuFile;
    }

    private JMenu createMenuView() {
        JMenu menuFile = new JMenu("View");
        return menuFile;
    }

    private JMenu createMenuFile() {
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        JMenu menuItemFileNew = new JMenu("New");
        menuItemFileNew.setMnemonic(KeyEvent.VK_M);
        JMenuItem menuItemFileNewProject = new JMenuItem("Project...");
        JMenuItem menuItemFileNewFile = new JMenuItem("File");
        JMenuItem menuItemFileNewDBConnection = new JMenuItem("Database Connection");
        menuItemFileNew.add(menuItemFileNewProject);
        menuItemFileNew.add(menuItemFileNewFile);
        menuItemFileNew.addSeparator();
        menuItemFileNew.add(menuItemFileNewDBConnection);
        menuItemFileNewDBConnection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                WizardDBConnection wizard = new WizardDBConnection(root, true, 100);
            }
        });
        JMenuItem menuItemFileOpen = new JMenuItem("Open");
        JMenuItem menuItemFileClose = new JMenuItem("Close");
        JMenuItem menuItemFileSaveAs = new JMenuItem("Save as");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("exit.png"));
        JMenuItem menuItemFileExit = new JMenuItem("Exit", icon);
        menuItemFileExit.setMnemonic(KeyEvent.VK_X);
        menuItemFileExit.setToolTipText("Exit");
        menuItemFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        menuFile.add(menuItemFileNew);
        menuFile.add(menuItemFileOpen);
        menuFile.add(menuItemFileClose);
        menuFile.addSeparator();
        menuFile.add(menuItemFileSaveAs);
        menuFile.addSeparator();
        menuFile.add(menuItemFileExit);
        return menuFile;
    }
}
