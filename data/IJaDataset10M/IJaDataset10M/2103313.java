package com.googlecode.jumpnevolve.graphics;

import java.awt.SplashScreen;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author niklas
 * 
 */
public class SlickEngine extends AppGameContainer implements AbstractEngine {

    private static SlickEngine instance;

    /**
	 * @return Die geteilte Instanz der Grafikengine.
	 * 
	 * @throws GraphicsError
	 *             Wenn ein OpenGL Fehler beim ersten Erzeugen der Instanz
	 *             auftrat
	 */
    public static SlickEngine getInstance() {
        if (instance == null) {
            try {
                instance = new SlickEngine(new StateBasedGame("") {

                    @Override
                    public void initStatesList(GameContainer container) throws SlickException {
                    }
                });
            } catch (SlickException e) {
                throw new GraphicsError(e);
            }
            Engine.makeCurrent(instance);
        }
        return instance;
    }

    private StateBasedGame states;

    private SlickEngine(StateBasedGame states) throws SlickException {
        super(states);
        this.states = states;
    }

    public void addState(AbstractState state) {
        if (!containsState(state)) {
            this.states.addState(state);
        }
    }

    public boolean containsState(AbstractState state) {
        return this.states.getState(state.getID()) != null;
    }

    public void switchState(AbstractState state) {
        if (this.states.getState(state.getID()) == null) {
            this.states.addState(state);
        }
        if (this.states.getCurrentStateID() != state.getID()) {
            this.states.enterState(state.getID());
        }
    }

    public AbstractState getCurrentState() {
        GameState state = this.states.getCurrentState();
        if (!(state instanceof AbstractState)) {
            throw new RuntimeException("The current state does not inherit AbstractState.");
        }
        return (AbstractState) state;
    }

    @Override
    public void start() {
        try {
            super.setDisplayMode(1000, 600, false);
        } catch (SlickException e) {
            throw new GraphicsError(e);
        }
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            try {
                splash.close();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        try {
            super.start();
        } catch (SlickException e) {
            throw new GraphicsError(e);
        }
    }
}
