package org.robocup.gamecontroller.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropInAdapter implements ActionListener {

    protected MainGUI gui;

    protected byte team;

    public DropInAdapter(MainGUI gui, byte team) {
        super();
        this.gui = gui;
        this.team = team;
    }

    public void actionPerformed(ActionEvent evt) {
        gui.setDropIn(team);
    }
}
