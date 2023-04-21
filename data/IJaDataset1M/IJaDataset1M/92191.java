package org.heartstorming.bada.playlist.command;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.heartstorming.bada.playlist.PlayList;

public class NextAction extends AbstractAction {

    PlayList playList;

    public NextAction(String text, ImageIcon icon, String desc, Integer mnemonic, PlayList playList) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.playList = playList;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Action for Next");
        playList.next();
    }
}
