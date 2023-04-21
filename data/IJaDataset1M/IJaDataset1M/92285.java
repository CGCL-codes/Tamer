package markgame2d.engine;

import javax.swing.JFrame;

public class MarkJFrame extends JFrame {

    MarkGame game;

    public MarkJFrame(MarkGame game, String title) {
        this.game = game;
        add(game.getPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle(title);
    }
}
