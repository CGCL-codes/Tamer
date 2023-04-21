package net.sourceforge.musole.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * main program file. It is the main frame that launches everything.
 * @author Yohann
 *
 */
public class MainFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = -5386672923063866690L;

    JDesktopPane desktop;

    JInternalFrame editorFrame;

    TracksContainerPanel tracksContainerPanel;

    JInternalFrame playerFrame;

    PlayerPanel playerPanel;

    public MainFrame() {
        super("MainFrame");
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        desktop = new JDesktopPane();
        createEditorFrame();
        createPlayerFrame();
        setContentPane(desktop);
        setJMenuBar(createMenuBar());
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(fileMenu);
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menuItem.setEnabled(false);
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("open");
        menuItem.addActionListener(this);
        menuItem.setEnabled(false);
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
        TrackMenu trackMenu = new TrackMenu(tracksContainerPanel);
        menuBar.add(trackMenu.getTrackMenu());
        SampleMenu sampleMenu = new SampleMenu(tracksContainerPanel, this);
        menuBar.add(sampleMenu.getSampleMenu());
        ViewMenu viewMenu = new ViewMenu(playerFrame, editorFrame);
        menuBar.add(viewMenu.getViewMenu());
        return menuBar;
    }

    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) {
        } else if ("quit".equals(e.getActionCommand())) {
            quit();
        } else {
            quit();
        }
    }

    protected void createPlayerFrame() {
        playerFrame = new JInternalFrame("Player", true, true, true, true);
        playerFrame.setClosable(false);
        playerPanel = new PlayerPanel(tracksContainerPanel);
        playerPanel.setOpaque(true);
        playerFrame.setContentPane(playerPanel);
        playerFrame.pack();
        playerFrame.setVisible(true);
        desktop.add(playerFrame);
        try {
            playerFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void createEditorFrame() {
        editorFrame = new JInternalFrame("Editor", true, true, true, true);
        editorFrame.setClosable(false);
        tracksContainerPanel = new TracksContainerPanel(new EditorPanel());
        editorFrame.setContentPane(tracksContainerPanel);
        editorFrame.pack();
        editorFrame.setVisible(true);
        editorFrame.setLocation(200, 100);
        desktop.add(editorFrame);
    }

    protected void quit() {
        System.exit(0);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
