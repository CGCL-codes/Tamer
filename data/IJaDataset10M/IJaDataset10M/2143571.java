package org.robocup.gamecontroller.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerAdapter implements ActionListener {

    protected MainGUI gui;

    protected byte team;

    protected byte player;

    public PlayerAdapter(MainGUI gui, byte team, byte player) {
        super();
        this.gui = gui;
        this.team = team;
        this.player = player;
    }

    public void actionPerformed(ActionEvent evt) {
        gui.cmdPlayerClicked(team, player);
    }
}
