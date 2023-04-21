package client.gui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import both.Global;

/**
 * Game-Over Dialog beim Spielende.
 * @author aqualuk
 *
 */
public class GameOverDialog extends JDialog {

    private JLabel header;

    /**
	 * Konstruktor
	 * @param owner Referenz zum Besitzerfenster.
	 */
    public GameOverDialog(JFrame owner) {
        super(owner, "JarWars - Game Over");
        this.setModal(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        header = new JLabel(new ImageIcon(Global.getWorkingDirectory() + "data/pics/gameend.png"));
        this.add(header, constraints);
        this.pack();
        this.setResizable(false);
        this.setLocation(owner.getLocation().x + (owner.getSize().width / 2) - (this.getSize().width / 2), owner.getLocation().y + (owner.getSize().height / 2) - (this.getSize().height / 2));
    }

    /**
	 * Statische Aufrufmethode.
	 * @param owner Referenz zum Besitzerfenster
	 */
    public static void showGameOverDialog(JFrame owner) {
        GameOverDialog gameOverDialog = new GameOverDialog(owner);
        gameOverDialog.setVisible(true);
    }
}
