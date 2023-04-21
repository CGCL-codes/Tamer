package de.rayban.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import de.rayban.core.logic.GameLogic;
import de.rayban.game.states.StrahlBannGameState;

/**
 * Hier wird das Spiel ansich gerendert.
 *
 * @author Daniel
 *
 */
public class InGameState extends StrahlBannGameState {

    public InGameState(final int id) {
        super(id);
    }

    private GameLogic gameLogic;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void render(final GameContainer container, final StrahlBann game, final Graphics g) {
        g.drawString("InGame", 100, 10);
        g.drawString("Score: " + gameLogic.currentScore(), 100, 50);
    }

    @Override
    public void update(final GameContainer container, final StrahlBann game, final int delta) {
        gameLogic.update(delta);
    }

    @Override
    public void init(final GameContainer container, final StrahlBann game) {
        gameLogic = GameLogic.start(game.getEntityManager());
    }
}
