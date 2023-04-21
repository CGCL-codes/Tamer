package core;

import core.event.LocalScoreListener;
import javax.swing.*;

public class GameStart implements Runnable {

    JFrame window;

    GamePanel panel;

    LocalScoreListener scoreListener;

    public GameStart(LocalScoreListener scoreListener) {
        this.scoreListener = scoreListener;
        window = new JFrame("");
        window.setSize(1024, 576);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void gameStart() {
        panel = new GamePanel();
        window.getContentPane().add(panel);
        if (scoreListener != null) panel.setScoreListener(scoreListener);
        panel.startGame();
    }

    @Override
    public void run() {
        this.gameStart();
        panel.closeGame();
    }
}
