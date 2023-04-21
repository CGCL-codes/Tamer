package aitgame.tilegame.minigames.calcgame;

import aitgame.tilegame.minigames.PopupGame;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import aitgame.input.InputManager;

public class CalcGame extends PopupGame {

    public CalcGame(JFrame frame, InputManager inputManager) {
        super(frame, inputManager, "Matematik");
        setDescription("Här testas dina mattekunskaper");
    }
}
