package chromolite;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import chromolite.ui.Metrics;

public class GameOverPage extends BasicGameState {

    public static final int ID = Chromolite.gameStateCounter++;

    private Color backgroundColor;

    @Override
    public int getID() {
        return ID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        backgroundColor = new Color(0f, 0f, 0f, 0.7f);
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        GameState inGameState = ((Chromolite) game).getState(InGamePage.ID);
        inGameState.render(container, game, g);
        Color color = g.getColor();
        g.setColor(backgroundColor);
        int padding = 5;
        g.fillRect(padding, padding, Metrics.SCREEN_WIDTH - padding * 2, Metrics.SCREEN_HEIGHT - padding * 2);
        g.setColor(color);
        g.drawString("GAME OVER", 340, 220);
        g.drawString("(press any key)", 330, 250);
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    }

    @Override
    public void keyPressed(int key, char c) {
        InGamePage inGamePage = (InGamePage) Chromolite.instanceOf().getState(InGamePage.ID);
        inGamePage.resetGame();
        Chromolite.instanceOf().enterState(FrontPage.ID);
    }
}
