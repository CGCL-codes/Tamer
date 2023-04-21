package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class ApplicationFrame extends JFrame {

    private static final long serialVersionUID = -5448247878632634544L;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu editMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem cutMenuItem = null;

    private JMenuItem copyMenuItem = null;

    private JMenuItem pasteMenuItem = null;

    private JMenuItem saveMenuItem = null;

    private JDialog aboutDialog = null;

    private JPanel aboutContentPane = null;

    private JLabel aboutVersionLabel = null;

    private VControler vControler = new VControler();

    private JTabbedPane jTabbedPane = null;

    private MainDisplay jPanelMainDisplay;

    private DoubleBookedDisplay jPanelDoubleBookedDisplay;

    private NewBookingDisplay jPanelNewBookingDisplay;

    private JLabel statusBar;

    public ApplicationFrame(VControler view) {
        super();
        vControler = view;
        vControler.setParentFrame(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(getJJMenuBar());
        setSize(800, 545);
        setContentPane(getJContentPane());
        setTitle("Scheduler");
        validate();
    }

    /**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setEnabled(true);
            jTabbedPane.addTab(new String("Main Display"), null, getJPanelMainDisplay(), null);
            jTabbedPane.addTab(new String("Double Booked"), null, getJPanelDoubleBookedDisplay(), null);
            jTabbedPane.addTab(new String("New Booking"), null, getJPanelNewBookingDisplay(), null);
        }
        return jTabbedPane;
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private MainDisplay getJPanelMainDisplay() {
        if (jPanelMainDisplay == null) {
            jPanelMainDisplay = new MainDisplay(vControler);
        }
        return jPanelMainDisplay;
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private DoubleBookedDisplay getJPanelDoubleBookedDisplay() {
        if (jPanelDoubleBookedDisplay == null) {
            jPanelDoubleBookedDisplay = new DoubleBookedDisplay(vControler);
        }
        return jPanelDoubleBookedDisplay;
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private NewBookingDisplay getJPanelNewBookingDisplay() {
        if (jPanelNewBookingDisplay == null) {
            jPanelNewBookingDisplay = new NewBookingDisplay(vControler);
        }
        return jPanelNewBookingDisplay;
    }

    protected void populateTable() {
        MainDisplay md = (MainDisplay) jTabbedPane.getComponent(0);
        JPanel jp = (JPanel) md.getComponent(0);
        jp.remove(0);
        jp.add(new JTable(vControler.getBookingsTableRowData(), vControler.getBookingsColumnsNames()));
        jp.validate();
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            statusBar = new JLabel("Welcome to SchedulerApp");
            jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
            jContentPane.add(statusBar, BorderLayout.SOUTH);
            populateTable();
            jContentPane.validate();
        }
        return jContentPane;
    }

    /**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            jJMenuBar.add(getHelpMenu());
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
            editMenu.add(getCutMenuItem());
            editMenu.add(getCopyMenuItem());
            editMenu.add(getPasteMenuItem());
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
                    Point loc = getLocation();
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
            aboutDialog = new JDialog(this, true);
            aboutDialog.setTitle("About");
            aboutDialog.setSize(new Dimension(170, 85));
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
            aboutVersionLabel.setText("Version 1.0");
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getCutMenuItem() {
        if (cutMenuItem == null) {
            cutMenuItem = new JMenuItem();
            cutMenuItem.setText("Cut");
            cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, true));
        }
        return cutMenuItem;
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
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, true));
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
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, true));
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
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, true));
        }
        return saveMenuItem;
    }

    public void showNewBookingDisplay() {
    }

    public void showDoubleBookingDisplay() {
    }

    public void showMainDisplay() {
        jTabbedPane.setSelectedIndex(0);
    }

    public void setStatus(String string) {
        statusBar.setText(string);
    }
}
