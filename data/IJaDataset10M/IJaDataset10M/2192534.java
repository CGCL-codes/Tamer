package ihaterobots.game;

import ihaterobots.game.env.GameEnv;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author charly
 */
public class GameOverState extends BasicGameState {

    private static final int TIMER_MAX = 10000;

    private static final String GAMEOVER_STRING = "Game Over";

    private final GameEnv env;

    private UnicodeFont font;

    private int timer;

    private GameContainer container;

    private float stringWidth;

    private float stringHeight;

    public GameOverState(GameEnv env) {
        this.env = env;
    }

    @Override
    public int getID() {
        return Utils.STATE_ID_GAMEOVER;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
    }

    public void initStuff() {
        try {
            font = new UnicodeFont("assets/font/homespun.ttf", "assets/font/homespun.hiero");
            font.addAsciiGlyphs();
            font.addGlyphs(400, 600);
            font.loadGlyphs();
            stringWidth = font.getWidth(GAMEOVER_STRING);
            stringHeight = font.getHeight(GAMEOVER_STRING);
        } catch (SlickException ex) {
            Logger.getLogger(LevelEntranceState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        env.getDrawableManager().draw(g);
        float ry = (float) timer / (float) TIMER_MAX;
        float posy = ry * container.getHeight();
        float alpha = .2f + (1 - 2 * Math.abs(ry - .5f)) * .8f;
        g.setFont(font);
        g.setColor(new Color(1f, .5f, 0f, alpha));
        g.pushTransform();
        g.translate(container.getWidth() / 2 - 4 * stringWidth / 2, posy - 4 * stringHeight / 2);
        g.pushTransform();
        g.scale(4f, 4f);
        g.drawString(GAMEOVER_STRING, 0, 0);
        g.popTransform();
        g.popTransform();
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        env.getPlayer().update(delta);
        timer += delta;
        if (timer >= TIMER_MAX) {
            exitState();
            return;
        }
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        timer = 0;
        env.getSoundManager().stopAllLoops();
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
    }

    private void exitState() {
        env.getGameManager().setGameRunning(false);
        env.getGame().enterState(Utils.STATE_ID_END);
    }
}
