package com.game.states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Principal, aonde inicia o jogo
 * 
 * @author Erick Zanardo
 *
 */
public class Main extends StateBasedGame {

    public static final int GAME = 1;

    public static final int MENU = 2;

    public static final int CONFIG = 3;

    public static final int GAME_OVER = 4;

    public Main(String name) {
        super(name);
        addState(new Game(GAME));
        addState(new Menu(MENU));
        addState(new Config(CONFIG));
        addState(new GameOver(GAME_OVER));
        enterState(MENU);
    }

    public void initStatesList(GameContainer gc) throws SlickException {
        getState(MENU).init(gc, this);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Main("Snake"));
        app.setDisplayMode(Game.GAME_WIDTH, Game.GAME_HEIGHT, false);
        app.setShowFPS(false);
        app.start();
    }
}
