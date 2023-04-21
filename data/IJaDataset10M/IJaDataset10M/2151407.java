package fiswidgets.fistabbedpane;

import fiswidgets.fisgui.*;
import fiswidgets.fisutils.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
  * FisTabbedPane is a meta-fiswidget that can be used to create a set of tabs from FisWidgets that
  * have already been created.  So if you have made several FisWidgets, this a way of 
  * organizing those FisWidgets into a single place.  Each tab is a seperate FisWidget.
  */
public class FisTabbedPane extends JFrame implements ActionListener, WindowListener, ChangeListener {

    private GridBagLayout gbl;

    private GridBagConstraints gbc;

    private Vector fisbases = new Vector();

    private Vector allMenus = new Vector();

    private Vector preferred_sizes = new Vector();

    private boolean standalone = true;

    private JTabbedPane tabs;

    private JMenuBar menubar;

    private int height_diff;

    private int width_diff;

    private JMenu help;

    private JMenu file;

    private JMenuItem about;

    private String aboutmessage = (new FisBase()).getAboutMessage();

    public static int LEFT = JTabbedPane.LEFT;

    public static int RIGHT = JTabbedPane.RIGHT;

    public static int TOP = JTabbedPane.TOP;

    public static int BOTTOM = JTabbedPane.BOTTOM;

    /**
      * Constructs the FisTabbedPane to which the FisWidgets will be added.
      */
    public FisTabbedPane() {
        super("FisWidget Tools");
        addWindowListener(this);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addChangeListener(this);
        pane.add(tabs, BorderLayout.CENTER);
        menubar = new JMenuBar();
        file = new JMenu("File");
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(this);
        menubar.add(file);
        help = new JMenu("Help");
        JMenuItem helpitem = new JMenuItem("Help");
        helpitem.addActionListener(this);
        help.add(helpitem);
        help.addSeparator();
        helpitem.setEnabled(false);
        about = new JMenuItem("About");
        about.addActionListener(this);
        help.add(about);
        allMenus.addElement(file);
        menubar.add(Box.createHorizontalGlue());
        menubar.add(help);
        setJMenuBar(menubar);
        setResizable(false);
        pack();
        setVisible(true);
        setLocation(100, 100);
    }

    /**
     *  Adds the given message to the about dialog.
     *  @param message the added information to be added to the about.
     */
    public void addToAbout(String message) {
        aboutmessage = message + "\n\n" + aboutmessage;
    }

    /**
     *  This is used by the about message dialog to get the about message for the fisapp
     */
    public String getAboutMessage() {
        return aboutmessage;
    }

    /**
     * This can be used to place your own menuitem into the help menu.
     * @param helpItem a JMenuItem to be used as the Help menu item.
     */
    public void setHelpItem(JMenuItem helpItem) {
        help.removeAll();
        help.add(helpItem);
        help.add(new JSeparator());
        help.add(about);
    }

    /**
     * Sets the position of the tabs, TOP, BOTTOM, LEFT, RIGHT.
     *@param location this is one of FisTabbedPane.TOP, FisTabbedPane.BOTTOM, FisTabbedPane.LEFT, FisTabbedPane.RIGHT.  The default is TOP.
     */
    public void setTabPlacement(int location) {
        tabs.setTabPlacement(location);
    }

    /**
     * This adds a menu to the menubar at the top of the FisTabbedPane
     * @param menu The menu name to be added to the menubar
     */
    public void addMenu(String menu) {
        menubar.removeAll();
        JMenu m = new JMenu(menu);
        allMenus.addElement(m);
        for (int i = 0; i < allMenus.size(); i++) menubar.add((JMenu) allMenus.elementAt(i));
        menubar.add(Box.createHorizontalStrut(20));
        menubar.add(help);
    }

    /**
     * Adds a menu item to a created menu.
     * @param menuItem the menu item to be added to menu.
     * @param menu where menuItem will be put.
     */
    public void addMenuItem(String menuItem, String menu) {
        for (int i = 0; i < allMenus.size(); i++) {
            JMenu test = (JMenu) allMenus.elementAt(i);
            if (test.getText().equals(menu)) {
                JMenuItem item = new JMenuItem(menuItem);
                item.addActionListener(this);
                test.add(item);
            }
        }
    }

    private boolean makeFisBase(String className) {
        FisBase tempBase;
        try {
            tempBase = (FisBase) ((Class.forName(className)).newInstance());
            tempBase.dispose();
            tempBase.setStandAlone(false);
            fisbases.addElement(tempBase);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
      * addTab is used to add a fiswidget to the tabbedpane
      * @param className is the class name of the fiswidget to be added
      * @param tablabel is the String that will appear on the tab
      */
    public boolean addTab(String className, String tablabel) {
        if (makeFisBase(className)) {
            FisBase tempBase = (FisBase) fisbases.elementAt(fisbases.size() - 1);
            tempBase.setTabbedPane(this);
            JPanel tempp = new JPanel();
            tempp.setLayout(gbl);
            gbc.gridx = 0;
            gbc.gridy = 0;
            JPanel cp = tempBase.getComponentPanel();
            gbl.setConstraints(cp, gbc);
            tempp.add(cp);
            gbc.gridy++;
            JPanel bp = tempBase.getButtonPanel();
            gbl.setConstraints(bp, gbc);
            tempp.add(bp);
            JPanel temp_panel = new JPanel();
            temp_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            temp_panel.add(tempp);
            tabs.addTab(tablabel, temp_panel);
            Dimension tmp = temp_panel.getPreferredSize();
            if (fisbases.size() == 1) {
                Dimension first_dim = this.getPreferredSize();
                height_diff = first_dim.height - tmp.height;
                width_diff = first_dim.width - tmp.width;
                preferred_sizes.addElement(first_dim);
            } else {
                tmp.height += height_diff;
                tmp.width += width_diff;
                preferred_sizes.addElement(tmp);
            }
            pack();
            setVisible(true);
            return true;
        }
        return false;
    }

    /**
      * addTab is used to add a fiswidget to the tabbedpane
      * @param className is the class name of the fiswidget to be added
      * @param tablabel is the String that will appear on the tab
      * @param icon is an Icon that will appear to the left of the tab label
      */
    public boolean addTab(String className, String tablabel, Icon icon) {
        if (makeFisBase(className)) {
            FisBase tempBase = (FisBase) fisbases.elementAt(fisbases.size() - 1);
            tempBase.setTabbedPane(this);
            JPanel tempp = new JPanel();
            tempp.setLayout(gbl);
            gbc.gridx = 0;
            gbc.gridy = 0;
            JPanel cp = tempBase.getComponentPanel();
            gbl.setConstraints(cp, gbc);
            tempp.add(cp);
            gbc.gridy++;
            JPanel bp = tempBase.getButtonPanel();
            gbl.setConstraints(bp, gbc);
            tempp.add(bp);
            JPanel temp_panel = new JPanel();
            temp_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            temp_panel.add(tempp);
            tabs.addTab(tablabel, icon, temp_panel);
            Dimension tmp = temp_panel.getPreferredSize();
            if (fisbases.size() == 1) {
                Dimension first_dim = this.getPreferredSize();
                height_diff = first_dim.height - tmp.height;
                width_diff = first_dim.width - tmp.width;
                preferred_sizes.addElement(first_dim);
            } else {
                tmp.height += height_diff;
                tmp.width += width_diff;
                preferred_sizes.addElement(tmp);
            }
            pack();
            setVisible(true);
            return true;
        }
        return false;
    }

    /**
     * Removes a FisWidget from the FisTabbedPane.
     * @param tablabel The Label of the tab to be removed.
     */
    public void removeTab(String tablabel) {
        int count = tabs.getTabCount();
        for (int i = 0; i < count; i++) {
            String title = tabs.getTitleAt(i);
            title = title.trim();
            if (title.equals(tablabel)) {
                tabs.removeTabAt(i);
                fisbases.removeElementAt(i);
                preferred_sizes.removeElementAt(i);
                i--;
                count--;
            }
        }
        pack();
        setVisible(true);
    }

    /**
     * Returns the FisBases that are on the FisTabbedPane.
     */
    public Vector getFisBases() {
        return fisbases;
    }

    public void exit() {
        if (standalone) System.exit(0); else dispose();
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Exit")) {
            exit();
        } else if (action.equals("Help")) {
            FisProperties props = new FisProperties();
            try {
                props.loadProperties();
            } catch (Exception ex) {
                return;
            }
            if (!props.hasProperty("BROWSER") || !props.hasProperty("FISDOC_PATH")) return;
            String browser = System.getProperty("BROWSER");
            String docpath = System.getProperty("FISDOC_PATH");
            try {
                Runtime.getRuntime().exec(browser + " " + docpath + "/tabbedpane.html");
            } catch (Exception ex) {
                return;
            }
        } else if (action.equals("About")) {
            AboutDialog ad = new AboutDialog(this);
        }
    }

    public void stateChanged(ChangeEvent e) {
        tabResize();
    }

    /**
      * doneCreating can be called at the end of adding all of the tools that you want to 
      * appear in this toolchest.
      */
    public void doneCreating() {
        tabResize();
    }

    private void tabResize() {
        if (preferred_sizes.size() != 0) {
            Dimension tmp = ((Dimension) preferred_sizes.elementAt(tabs.getSelectedIndex()));
            this.setSize(tmp);
            this.getRootPane().setSize(tmp);
        }
    }

    /**
     * setStandAlone is used to realize if this app is a standalone app or not to deal with exiting
     */
    public void setStandAlone(boolean alone) {
        standalone = alone;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (standalone) System.exit(0); else dispose();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
