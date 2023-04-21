package sa;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import sa.gui.JOELibStructureViewer;
import sa.gui.MarvinSketcher;
import sa.gui.MarvinStructureViewer;
import sa.gui.MySQLLogin;
import sa.gui.StructuralFilterFrame;
import sa.gui.StructureViewer;
import java.io.File;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import java.awt.Color;

/**
 * Main GUI class containing JDesktopPane and the StructureViewer
 *
 * @author     Aur?lien MONGE
 */
public class ScreeningAssistantGUI extends JFrame implements ActionListener {

    public static final String SA_ENV_PATH = setSAPath();

    private static final String setSAPath() {
        String path = System.getenv("SCREENING_ASSISTANT");
        if (path == null || path.equals("")) {
            path = "." + File.separatorChar;
        } else {
            if (path.charAt(path.length() - 1) != File.separatorChar) {
                path = path + File.separatorChar;
            }
        }
        return new String(path);
    }

    public JDesktopPane desktop;

    public StructureViewer structureViewer;

    public Dimension screenSize;

    public ScreeningAssistantGUI() {
        super("ScreeningAssistant " + SA_ENV_PATH);
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.width = screenSize.width - 5;
        screenSize.height = screenSize.height - 50;
        setBounds(0, 0, screenSize.width, screenSize.height);
        desktop = new JDesktopPane();
        if (StructureViewer.isMarvinHere()) {
            structureViewer = new MarvinStructureViewer();
        } else {
            if (!StructureViewer.isJava3DInstalled()) {
                JOptionPane.showMessageDialog(null, "Java3D is not installed, the JOELib viewer wont work! \n\n" + "You have to install JAVA3D so the 3D viewer could work." + "\n\nAlternatively, you can install MarvinBeans and add\n" + "MarvinBeans.jar to your classpath so. Screening assistant\n" + "will then use Marvin as viewer (and just viewer).");
                structureViewer = new StructureViewer() {
                };
                structureViewer.setBackground(Color.WHITE);
            } else {
                structureViewer = new JOELibStructureViewer();
            }
        }
        desktop.add(structureViewer);
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        setContentPane(desktop);
        setJMenuBar(createMenuBar());
        setIconImage(new ImageIcon(SA_ENV_PATH + "icons" + File.separator + "desktop_old.png").getImage());
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setResizable(true);
    }

    /**
	 * Create the menu bar.
	 */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDatabase = new JMenu("Database");
        menuDatabase.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menuDatabase);
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setIcon(new ImageIcon(SA_ENV_PATH + "icons" + File.separator + "window_new.png"));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menuDatabase.add(menuItem);
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setIcon(new ImageIcon(SA_ENV_PATH + "icons" + File.separator + "kill.png"));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menuDatabase.add(menuItem);
        JMenu menuFilter = new JMenu("Configure");
        menuDatabase.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFilter);
        menuItem = new JMenuItem("Structural Filters");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.setIcon(new ImageIcon(SA_ENV_PATH + "icons" + File.separator + "configure.png"));
        menuItem.setActionCommand("Structural");
        menuItem.addActionListener(this);
        menuFilter.add(menuItem);
        JMenu menuQuestionMark = new JMenu("?");
        menuQuestionMark.setMnemonic(KeyEvent.VK_Q);
        menuBar.add(menuQuestionMark);
        menuItem = new JMenuItem("About...");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuItem.setIcon(new ImageIcon(SA_ENV_PATH + "icons" + File.separator + "identity.png"));
        menuItem.setActionCommand("about");
        menuItem.addActionListener(this);
        menuQuestionMark.add(menuItem);
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        menuBar.putClientProperty(PlasticXPLookAndFeel.DEFAULT_THEME_KEY, Boolean.FALSE);
        return menuBar;
    }

    /**
	 * React to menu selections.
	 *
	 * @param     e		ActionEvent
	 */
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) {
            MySQLLogin login = new MySQLLogin(this);
            desktop.add(login);
            try {
                login.setSelected(true);
            } catch (java.beans.PropertyVetoException pve) {
            }
        } else if ("quit".equals(e.getActionCommand())) {
            quit();
        } else if ("Structural".equals(e.getActionCommand())) {
            MarvinSketcher sketcher = null;
            StructuralFilterFrame sFilter = new StructuralFilterFrame(this);
            sFilter.setVisible(true);
            this.getContentPane().add(sFilter);
            try {
                sFilter.setSelected(true);
            } catch (java.beans.PropertyVetoException pve) {
            }
            sFilter.moveToFront();
        } else if ("about".equals(e.getActionCommand())) {
            JOptionPane.showMessageDialog(this, "ScreeningAssistant\nVersion: beta 0.2\n\nLaboratory of Molecular Modelling and Chemometrics\nInstitut de Chimie Organique et Analytique (ICOA) UMR CNRS 6005\nUniversit? d'Orl?ans, France\n\nAuthor : Aur?lien MONGE\nDirector : Pr Luc Morin-Allory");
        }
    }

    /**
	 * Quit the application.
	 */
    protected void quit() {
        System.exit(0);
    }

    /**
	 * Create the GUI and show it
	 */
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        ScreeningAssistantGUI frame = new ScreeningAssistantGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
	 * Get the screen size.
	 *
	 * @return     Dimension	
	 */
    public Dimension getScreenSize() {
        return screenSize;
    }

    /**
	 * Get the JDesktopPane.
	 *
	 * @return     JDesktopPane
	 */
    public JDesktopPane getDesktop() {
        return desktop;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
