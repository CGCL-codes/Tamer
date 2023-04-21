package org.vizzini.example.gin.ui;

import org.vizzini.example.gin.Injector;
import org.vizzini.example.gin.SimpleComputerAgentProvider;
import org.vizzini.game.DefaultEvaluator;
import org.vizzini.game.IAgent;
import org.vizzini.game.IEvaluator;
import org.vizzini.game.IGame;
import org.vizzini.game.action.DefaultActionGenerator;
import org.vizzini.game.action.IActionGenerator;
import org.vizzini.ui.ApplicationSupport;
import org.vizzini.ui.game.IEnvironmentUI;
import org.vizzini.ui.game.IGameUIInjector;
import org.vizzini.util.IProvider;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an injector for Swing Qubic.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class InjectorSwing implements IGameUIInjector {

    /** Game injector. */
    private final Injector _gameInjector;

    /**
     * Construct this object with the given parameter.
     *
     * @param  gameInjector  Game injector.
     *
     * @since  v0.4
     */
    public InjectorSwing(Injector gameInjector) {
        _gameInjector = gameInjector;
        ApplicationSupport.setStringBundleName("org.vizzini.example.gin.ui.resources");
        ApplicationSupport.setConfigBundleName("org.vizzini.example.gin.ui.config");
    }

    /**
     * @see  org.vizzini.ui.game.IGameUIInjector#injectAgentProviders()
     */
    public List<IProvider<IAgent>> injectAgentProviders() {
        List<IProvider<IAgent>> answer = new ArrayList<IProvider<IAgent>>();
        {
            answer.add(new MouseHumanAgentProvider());
        }
        {
            IActionGenerator actionGenerator = new DefaultActionGenerator();
            IEvaluator evaluator = new DefaultEvaluator();
            answer.add(new SimpleComputerAgentProvider(actionGenerator, evaluator));
        }
        return answer;
    }

    /**
     * @see  org.vizzini.ui.game.IGameUIInjector#injectEnvironmentUI()
     */
    public IEnvironmentUI injectEnvironmentUI() {
        EnvironmentUISwing answer = new EnvironmentUISwing(_gameInjector.injectAdjudicator(), _gameInjector.injectEnvironment());
        answer.setBackground(injectBackgroundColor());
        answer.setPreferredSize(injectInitialDimension());
        return answer;
    }

    /**
     * @see  org.vizzini.ui.game.IGameUIInjector#injectGame()
     */
    public IGame injectGame() {
        return _gameInjector.injectGame();
    }

    /**
     * @see  org.vizzini.ui.game.IGameUIInjector#injectInitialDimension()
     */
    public Dimension injectInitialDimension() {
        return new Dimension(480, 640);
    }

    /**
     * @return  a new background color.
     *
     * @since   v0.4
     */
    private Color injectBackgroundColor() {
        return new Color(0, 128, 0);
    }
}
