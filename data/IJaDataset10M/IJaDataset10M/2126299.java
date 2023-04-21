package com.uglygreencar.games.replay;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class PlayButton extends Button implements ActionListener {

    private Replay copycat;

    public PlayButton(Replay copycat) {
        super("Play");
        this.copycat = copycat;
        this.addActionListener(this);
        this.setEnabled(true);
        super.setBackground(Color.gray);
    }

    public void actionPerformed(ActionEvent event) {
        this.setEnabled(false);
        this.copycat.getRoundPanel().repaint();
        PlayMemory playMemory = new PlayMemory(this.copycat);
        PlayMemory.SHOW_SEQUENCE = true;
        PlayMemory.PLAY_GAME = true;
        PlayMemory.ROUND = 1;
        PlayMemory.CLICKED = 0;
        playMemory.start();
    }
}
