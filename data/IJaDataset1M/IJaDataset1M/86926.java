package hogs.net.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Just tests the HighScoresManager class
 * @author dapachec
 */
public class TestHsm {

    HighScoresManager m_hsm;

    JFrame m_frame = new JFrame("Test HSM");

    public TestHsm() {
        try {
            m_hsm = new HighScoresManager("Dave-Server");
            m_hsm.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton killA = new JButton("Frick killed Frack");
        killA.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                m_hsm.playerDied("frack");
                m_hsm.playerWon("frick");
            }
        });
        JButton killB = new JButton("Frack killed Frick");
        killB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                m_hsm.playerDied("frick");
                m_hsm.playerWon("frack");
            }
        });
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                m_hsm.interrupt();
                System.exit(0);
            }
        });
        JPanel panel = (JPanel) m_frame.getContentPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(killA);
        panel.add(killB);
        panel.add(quit);
        m_frame.pack();
        m_frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TestHsm();
    }
}
