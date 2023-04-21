package org.myrpg.holy_factory.gui;

import org.myrpg.holy_factory.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Main application window.
 *
 * @author	Lo�c Lef�vre
 */
public class MainWindow extends JFrame {

    /** SWING Components. */
    HolyFactory holyFactory;

    JMenuBar menuBar;

    JMenu menu;

    JMenuItem menuItem;

    WindowCloser windowCloser;

    NewProjectHandler newProjectHandler;

    JTextArea logTextArea;

    JTextArea xmlTextArea;

    public static Border etched = BorderFactory.createEtchedBorder();

    /**
	 * Builds the main window.
	 *
	 * @param	holyFactory	the application.
	 */
    public MainWindow(HolyFactory holyFactory) {
        super("Holy Factory");
        this.holyFactory = holyFactory;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
        windowCloser = new WindowCloser(this);
        newProjectHandler = new NewProjectHandler(this);
        buildGUI();
        setSize(getToolkit().getScreenSize());
        addWindowListener(windowCloser);
    }

    /**
	 * Builds the GUI's application.
	 */
    protected void buildGUI() {
        Container cp;
        cp = getContentPane();
        cp.setLayout(new BorderLayout());
        buildMenu();
        cp.add(buildTabs(), BorderLayout.CENTER);
        pack();
    }

    protected void buildMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("FILE");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("Description");
        menuBar.add(menu);
        menuItem = new JMenuItem("New project...", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Create a new project");
        menuItem.addActionListener(newProjectHandler);
        menu.add(menuItem);
        menuItem = new JMenuItem("Open project...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Create a project");
        menu.add(menuItem);
        menuItem = new JMenuItem("Close project...", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Close a project");
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Save project...", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Save the active project");
        menu.add(menuItem);
        menuItem = new JMenuItem("Save project as...", KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Save the active project with a new name");
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Quit from Holy Factory");
        menuItem.addActionListener(windowCloser);
        menu.add(menuItem);
        setJMenuBar(menuBar);
    }

    private JPanel buildTabs() {
        JPanel panel = new JPanel();
        ImageIcon icon = new ImageIcon("images/middle.gif");
        JTabbedPane tabbedPane = new JTabbedPane();
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane editScrollPane = new JScrollPane(logTextArea);
        editScrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        editScrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        tabbedPane.addTab("Log", icon, editScrollPane, "Description");
        xmlTextArea = new JTextArea();
        xmlTextArea.setEditable(true);
        editScrollPane = new JScrollPane(xmlTextArea);
        editScrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        editScrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        tabbedPane.addTab("XML", icon, editScrollPane, "Description");
        Component panel2 = makeTextPanel("[Layout]");
        tabbedPane.addTab("Layout", icon, panel2, "Description");
        JPanel panel3 = new JPanel(new BorderLayout());
        TitledBorder title3 = BorderFactory.createTitledBorder(etched, " Current tile ");
        panel3.setBorder(title3);
        tabbedPane.addTab("Library", icon, panel3, "Tiles library");
        Component panel4 = makeTextPanel("[Builder]");
        tabbedPane.addTab("Builder", icon, panel4, "Description");
        Component panel5 = makeTextPanel("[Renderer]");
        tabbedPane.addTab("Renderer", icon, panel5, "Description");
        panel.setLayout(new GridLayout(1, 1));
        panel.add(tabbedPane);
        tabbedPane.setSelectedIndex(0);
        return panel;
    }

    protected Component makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public void log(String message) {
        logTextArea.append(message + "\n");
    }

    public void exitSystem() {
        if (holyFactory != null) holyFactory.exitSystem(); else System.exit(0);
    }
}
