package networkSimulator;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import org.jgraph.example.GraphEd;

public class ModePanel extends JPanel implements ActionListener {

    private NetworkSimulatorGUI nsg;

    private JRadioButton configurationButton;

    private JRadioButton simulationButton;

    public ModePanel(NetworkSimulatorGUI nsg) {
        super(new GridLayout(7, 1));
        this.nsg = nsg;
        this.nsg.addSelectModeIcon("images/attentionBack.png");
        this.add(new JLabel(" Select mode:"));
        configurationButton = new JRadioButton("Configuration mode");
        configurationButton.setActionCommand("Configuration mode");
        configurationButton.setSelected(true);
        this.add(configurationButton);
        simulationButton = new JRadioButton("Simulation mode");
        simulationButton.setActionCommand("Simulation mode");
        simulationButton.setSelected(false);
        this.add(simulationButton);
        ButtonGroup group = new ButtonGroup();
        group.add(configurationButton);
        group.add(simulationButton);
        ImageIcon icon;
        URL iconUrl;
        JPanel buttonPanel = new JPanel();
        iconUrl = GraphEd.class.getClassLoader().getResource("images/accept.png");
        icon = new ImageIcon(iconUrl);
        JButton button = new JButton();
        button.setActionCommand("OK");
        button.addActionListener(this);
        button.setIcon(icon);
        buttonPanel.add(button);
        this.add(buttonPanel);
        this.add(Box.createRigidArea(new Dimension(400, 400)));
        this.setPreferredSize(new Dimension(200, 200));
        this.setSize(new Dimension(200, 200));
        this.setMaximumSize(new Dimension(200, 200));
        this.setMinimumSize(new Dimension(200, 200));
    }

    public void actionPerformed(ActionEvent e) {
        this.nsg.deleteSelectModeIcon();
        if ("OK".equals(e.getActionCommand())) {
            if (this.configurationButton.isSelected()) {
                this.nsg.selectMode(NetworkSimulatorGUI.CONFIGURATION_MODE);
            } else if (this.simulationButton.isSelected()) {
                this.nsg.selectMode(NetworkSimulatorGUI.SIMULATION_MODE);
            }
        }
    }
}
