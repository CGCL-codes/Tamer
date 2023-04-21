package jpm.combatforce.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author 527843
 */
public class MainMenu extends jtbs.ui.MainMenu {

    public void createScreen() {
        setTitle("CombatForce");
        getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        panel.add(new JButton(), c);
        getContentPane().add(panel, BorderLayout.WEST);
        getContentPane().add(null, BorderLayout.EAST);
    }

    public void initialize() {
        createScreen();
        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }
        });
        this.pack();
        this.validate();
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);
        this.setVisible(true);
    }
}
