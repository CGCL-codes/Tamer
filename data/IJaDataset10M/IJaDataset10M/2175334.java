package net.sourceforge.gsn_ns2.ui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import net.sourceforge.gsn_ns2.core.Workbench;
import net.sourceforge.gsn_ns2.ui.action.ExitAction;
import net.sourceforge.gsn_ns2.ui.action.NewAction;
import net.sourceforge.gsn_ns2.ui.action.OpenAction;
import net.sourceforge.gsn_ns2.ui.action.SaveAction;
import net.sourceforge.gsn_ns2.ui.action.SaveAsAction;

/**
 * Main window class.
 */
public class MainWindow extends JFrame implements WindowListener {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The workench. */
    private Workbench workbench;

    /** Exit action. */
    private final ExitAction exitAction;

    /** New action. */
    private final NewAction newAction;

    /** Open action. */
    private final OpenAction openAction;

    /** Save action. */
    private final SaveAction saveAction;

    /** Save as action. */
    private final SaveAsAction saveAsAction;

    /**
	 * Default constructor
	 * @param workbench Reference to workbench.
	 */
    public MainWindow(Workbench workbench) {
        this.workbench = workbench;
        exitAction = new ExitAction(workbench);
        newAction = new NewAction(workbench);
        openAction = new OpenAction(workbench);
        saveAction = new SaveAction(workbench);
        saveAsAction = new SaveAsAction(workbench);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        addWindowListener(this);
        createMenuBar();
        pack();
        setLocationRelativeTo(null);
    }

    /**
	 * Creates the main menu bar.
	 */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        menuBar.add(editMenu);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
