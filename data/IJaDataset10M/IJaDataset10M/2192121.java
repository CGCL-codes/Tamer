package tests.tool;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class TestManager extends JFrame {

    private static final long serialVersionUID = 838843769623506106L;

    JPanel pane = new JPanel();

    private JFrame newPeerWizard;

    /**
	 * 
	 */
    public TestManager() {
        super("TestManager");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container con = this.getContentPane();
        con.add(pane);
        addMenu();
        initAddPeerPopup();
        setVisible(true);
    }

    /**
	 * 
	 */
    private void initAddPeerPopup() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        newPeerWizard = new NewPeerWizard();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        newPeerWizard.setLocation(x, y);
    }

    /**
	 * 
	 */
    public static void main(String args[]) {
        new TestManager();
    }

    /**
	 * 
	 */
    private void addMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenu projectMenu = new JMenu("Project");
        projectMenu.setMnemonic('P');
        menubar.add(fileMenu);
        menubar.add(projectMenu);
        JMenuItem addPeer = projectMenu.add("AddPeer");
        addPeer.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
        addPeer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.out.println("ACtion: " + arg0.getActionCommand());
                newPeerWizard.setVisible(true);
                popupActivated();
            }
        });
        JMenuItem startTest = projectMenu.add("StartTest");
        JMenuItem loadConfig = fileMenu.add("LoadConfiguration");
        loadConfig.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        setJMenuBar(menubar);
    }

    /**
	 * 
	 */
    private void popupActivated() {
        System.out.println("Popup have been opened!");
    }
}
