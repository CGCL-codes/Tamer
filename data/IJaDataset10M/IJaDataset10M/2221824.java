package net.preindl.figtex.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import net.preindl.figtex.model.Image;

/**
 * The GUI menu of the main frame
 * 
 * @author bastianpreindl
 * 
 */
public class GUIMenu {

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu editMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem addMenuItem = null;

    private JMenuItem copyMenuItem = null;

    private JMenuItem pasteMenuItem = null;

    private JMenuItem saveMenuItem = null;

    private JDialog aboutDialog = null;

    private JPanel aboutContentPane = null;

    private JLabel aboutVersionLabel = null;

    private FigTexGUI gui = null;

    private JMenu jMenu = null;

    private JMenuItem selectCSVMenuItem = null;

    private JMenuItem selectFolderMenuItem = null;

    private JMenuItem cleanupMenuItem = null;

    /**
     * Constructor
     * 
     * @param gui
     *            Main GUI
     */
    public GUIMenu(FigTexGUI gui) {
        this.gui = gui;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    public JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            jJMenuBar.add(getHelpMenu());
            jJMenuBar.add(getJMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getSelectCSVMenuItem());
            fileMenu.add(getSelectFolderMenuItem());
            fileMenu.add(getCleanupMenuItem());
            fileMenu.add(getSaveMenuItem());
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getEditMenu() {
        if (editMenu == null) {
            editMenu = new JMenu();
            editMenu.setText("Edit");
            editMenu.add(getCopyMenuItem());
            editMenu.add(getPasteMenuItem());
            editMenu.add(getAddMenuItem());
        }
        return editMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    gui.serviceAtShutdown();
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            aboutMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = gui.getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(gui.getJFrame(), true);
            aboutDialog.setTitle("About FigTexGUI");
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

    /**
     * This method initializes aboutContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (aboutContentPane == null) {
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(new BorderLayout());
            aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
        }
        return aboutContentPane;
    }

    /**
     * This method initializes aboutVersionLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (aboutVersionLabel == null) {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel.setText("FigTex GUI Version 0.1 (c)2010 Bastian Preindl (bastian@preindl.net)");
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAddMenuItem() {
        if (addMenuItem == null) {
            addMenuItem = new JMenuItem();
            addMenuItem.setText("Add");
            addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            addMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()");
                    Image img = new Image();
                    gui.getContainer().addImage(img);
                    gui.getTableModel().fireTableDataChanged();
                    gui.getImageForm().setImage(img);
                    gui.getImageForm().setVisible(true);
                }
            });
        }
        return addMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCopyMenuItem() {
        if (copyMenuItem == null) {
            copyMenuItem = new JMenuItem();
            copyMenuItem.setText("Copy");
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            copyMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }
        return copyMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getPasteMenuItem() {
        if (pasteMenuItem == null) {
            pasteMenuItem = new JMenuItem();
            pasteMenuItem.setText("Paste");
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
        }
        return pasteMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText("Save");
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()");
                    try {
                        System.out.println("Persis");
                        gui.getContainer().persist();
                    } catch (Exception e1) {
                        throw new RuntimeException("Could not persist image database", e1);
                    }
                }
            });
        }
        return saveMenuItem;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenu() {
        if (jMenu == null) {
            jMenu = new JMenu();
        }
        return jMenu;
    }

    /**
     * This method initializes selectCSVMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSelectCSVMenuItem() {
        if (selectCSVMenuItem == null) {
            selectCSVMenuItem = new JMenuItem();
            selectCSVMenuItem.setText("Select database file");
            selectCSVMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        gui.getContainer().loadcsv(false);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            });
        }
        return selectCSVMenuItem;
    }

    /**
     * This method initializes selectFolderMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSelectFolderMenuItem() {
        if (selectFolderMenuItem == null) {
            selectFolderMenuItem = new JMenuItem();
            selectFolderMenuItem.setText("Select image file folder");
            selectFolderMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        gui.getContainer().setFileRoot(null);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            });
        }
        return selectFolderMenuItem;
    }

    /**
     * This method initializes cleanupMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCleanupMenuItem() {
        if (cleanupMenuItem == null) {
            cleanupMenuItem = new JMenuItem();
            cleanupMenuItem.setText("Clean up image database");
            cleanupMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        gui.getContainer().cleanup();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return cleanupMenuItem;
    }
}
