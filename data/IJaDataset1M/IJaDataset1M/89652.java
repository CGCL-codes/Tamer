package client_new;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextField;

/**
 *
 * @author Kayvan
 */
public class DebugPanel extends JFrame {

    private JLabel networkLabel;

    /** shows status of the network connection */
    protected JLabel networkValue;

    private JLabel gameLabel;

    /** shows status of the game */
    protected JLabel gameValue;

    private JLabel fpsLabel;

    /** shows the current FPS value of the Game (0 if the game hasn't started yet) */
    protected JLabel fpsValue;

    private JPanel panel;

    /** Creates a new instance of DebugPanel */
    public DebugPanel(NetworkConnection network, Client client) {
        setTitle("Debug Window");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createLoginWindow();
        pack();
        setResizable(true);
        setVisible(true);
    }

    private void createLoginWindow() {
        this.networkLabel = new JLabel("Network Status : ");
        this.networkValue = new JLabel("not connected yet");
        this.gameLabel = new JLabel("Game Status : ");
        this.gameValue = new JLabel("game not launched yet");
        this.fpsLabel = new JLabel("FPS : ");
        this.fpsValue = new JLabel("0");
        this.panel = (JPanel) this.getContentPane();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints constraints;
        constraints = new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(networkLabel, constraints);
        constraints = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(networkValue, constraints);
        constraints = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(gameLabel, constraints);
        constraints = new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(gameValue, constraints);
        constraints = new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(fpsLabel, constraints);
        constraints = new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(fpsValue, constraints);
    }
}
