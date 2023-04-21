package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

/**
 * Dialog, which appears if the IP is wrong. Gives the possibilities to start the SettingsWindow or exit jDreambox.
 * @author Lukas Probst
 *
 */
public class DreamboxNotFoundDialog extends JDialog implements ActionListener, WindowListener {

    MainWindow owner;

    JPanel masterPanel;

    JLabel descriptionLabel;

    JLabel whatToDoLabel;

    JButton exitButton;

    JButton settingsButton;

    /**
	 * Display the Error and the two buttons.
	 * @param owner MainWindow, which owns this DreamboxNotFoundDialog.
	 */
    public DreamboxNotFoundDialog(MainWindow owner) {
        super(owner, "Error: Dreambox not found.");
        this.owner = owner;
        masterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        descriptionLabel = new JLabel("There's no Dreambox on the selected IP.");
        masterPanel.add(descriptionLabel, constraints);
        constraints.gridy = 1;
        whatToDoLabel = new JLabel("Please change the IP in the settings or exit jDreambox.");
        masterPanel.add(whatToDoLabel, constraints);
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        exitButton = new JButton("Exit jDreambox");
        exitButton.addActionListener(this);
        masterPanel.add(exitButton, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.0;
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(this);
        masterPanel.add(settingsButton, constraints);
        this.setContentPane(masterPanel);
        this.pack();
        this.addWindowListener(this);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.out.println("Quit by DreamboxNotFoundDialog.");
            System.exit(ERROR);
        } else if (e.getSource() == settingsButton) {
            this.owner.openSettingsWindow();
            this.dispose();
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent arg0) {
    }

    public void windowClosing(WindowEvent arg0) {
        System.out.println("Quit by DreamboxNotFoundDialog.");
        System.exit(ERROR);
    }

    public void windowDeactivated(WindowEvent arg0) {
    }

    public void windowDeiconified(WindowEvent arg0) {
    }

    public void windowIconified(WindowEvent arg0) {
    }

    public void windowOpened(WindowEvent arg0) {
    }
}
