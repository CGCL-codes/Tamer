package frontend.dialogs;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import frontend.controller.Controller;

/**
 * @author Foo Inc
 *
 */
@SuppressWarnings("serial")
public class Jif extends JFrame {

    private Controller m_controller = null;

    public JTabbedPane m_tabbedPane = null;

    private Jif() {
        m_controller = new Controller(this);
        initComponents();
    }

    public static Jif getInstance() {
        return (new Jif());
    }

    private void initComponents() {
        setTitle("JIF");
        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setJMenuBar(m_controller.initMenuBar());
        m_tabbedPane = new JTabbedPane();
        this.add(m_tabbedPane);
        this.pack();
        this.setVisible(true);
    }
}
