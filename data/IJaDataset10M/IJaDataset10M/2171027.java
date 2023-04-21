package dw2;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Alberto
 */
public class MainMenu extends Menu implements ActionListener {

    ImageButton start;

    ImageButton highscore;

    ImageButton help;

    public MainMenu(Main parent, Image[] images) {
        super(parent, images[0]);
        start = new ImageButton(images[1], "Play", 261, 41);
        highscore = new ImageButton(images[2], "Highscores", 278, 43);
        help = new ImageButton(images[3], "Help", 272, 40);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        highscore.setAlignmentX(Component.CENTER_ALIGNMENT);
        help.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(start);
        add(highscore);
        add(help);
    }

    public void init() {
        start.addActionListener(this);
        highscore.addActionListener(this);
        help.addActionListener(this);
        parent.MC.playMenuMusic(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Play")) {
            parent.remove(this);
            parent.validate();
            parent.repaint();
            parent.requestFocusInWindow();
            parent.MC.resetMusic();
            parent.startGame();
        }
        if (e.getActionCommand().equals("Highscores")) {
            parent.remove(this);
            parent.add(parent.hscoreMenu);
            parent.validate();
            parent.repaint();
            parent.MC.playMenuMusic(2);
        }
        if (e.getActionCommand().equals("Help")) {
            parent.remove(this);
            parent.helpMenu.setFromStart(true);
            parent.add(parent.helpMenu);
            parent.validate();
            parent.repaint();
            parent.MC.playMenuMusic(3);
        }
    }
}
