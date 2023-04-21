package org.vizzini.example.qubic.ui;

import java.util.List;
import org.vizzini.game.IAgent;
import org.vizzini.game.IGame;
import org.vizzini.ui.game.AbstractGameUIText;
import org.vizzini.ui.game.IEnvironmentUI;
import org.vizzini.util.IProvider;

/**
 * Provides functionality for a text Qubic interface in the game framework.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class GameUIText extends AbstractGameUIText {

    /**
     * Construct this object with the given parameter.
     *
     * @param  game            Game.
     * @param  environmentUI   Environment user interface.
     * @param  agentProviders  List of agent providers.
     *
     * @since  v0.4
     */
    public GameUIText(IGame game, IEnvironmentUI environmentUI, List<IProvider<IAgent>> agentProviders) {
        super(game, environmentUI, agentProviders);
    }

    /**
     * Main method allows this component to run as an application.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        GameUIText gameUI = new GameUIText(null, null, null);
        gameUI.init();
        gameUI.start();
    }
}
