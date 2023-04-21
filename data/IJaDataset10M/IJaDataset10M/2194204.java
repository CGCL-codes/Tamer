package org.vizzini.example.sudoku.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Action;
import javax.swing.JToolBar;
import org.vizzini.example.sudoku.Injector;
import org.vizzini.game.IAgent;
import org.vizzini.game.IGame;
import org.vizzini.ui.ActionManager;
import org.vizzini.ui.ApplicationSupport;
import org.vizzini.ui.game.IEnvironmentUI;
import org.vizzini.ui.game.boardgame.AbstractBoardgameUISwing;
import org.vizzini.util.IProvider;

/**
 * Provides base functionality for a Swing Tic-Tac-Toe user interface in the
 * game framework.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class GameUISwing extends AbstractBoardgameUISwing {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Construct this object with the given parameter.
     *
     * @param  game              Game.
     * @param  environmentUI     Environment user interface.
     * @param  agentProviders    List of agent providers.
     * @param  initialDimension  Initial dimension of the GUI.
     *
     * @since  v0.4
     */
    public GameUISwing(IGame game, IEnvironmentUI environmentUI, List<IProvider<IAgent>> agentProviders, Dimension initialDimension) {
        super(game, environmentUI, agentProviders, initialDimension);
        setCenteredOnScreen(true);
        setConcedeAvailable(false);
        setToolBarUsed(true);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.1
     */
    public static void main(String[] args) {
        Injector injector = new Injector();
        GameUISwing applet = new GameUISwing(injector.injectGame(), null, null, null);
        applet.doMain(args);
    }

    /**
     * @see  org.vizzini.ui.game.AbstractGameUISwing#newActionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void newActionPerformed(ActionEvent event) {
        super.newActionPerformed(event);
        getGame().getEngine().pause();
        Action action = getPauseAction();
        action.putValue(Action.NAME, ApplicationSupport.getResource("STRING_resumeAction"));
        action.putValue(Action.SHORT_DESCRIPTION, ApplicationSupport.getResource("STRING_resumeAction"));
        action.putValue(Action.SMALL_ICON, ApplicationSupport.getIcon("ICON_resume"));
    }

    /**
     * Perform the pause action.
     *
     * @param  event  Action event.
     *
     * @since  v0.1
     */
    public void pauseActionPerformed(ActionEvent event) {
        Action action = getPauseAction();
        if (getGame().getEngine().isPaused()) {
            getGame().getEngine().resume();
            action.putValue(Action.NAME, ApplicationSupport.getResource("STRING_pauseAction"));
            action.putValue(Action.SHORT_DESCRIPTION, ApplicationSupport.getResource("STRING_pauseAction"));
            action.putValue(Action.SMALL_ICON, ApplicationSupport.getIcon("ICON_pause"));
        } else {
            getGame().getEngine().pause();
            action.putValue(Action.NAME, ApplicationSupport.getResource("STRING_resumeAction"));
            action.putValue(Action.SHORT_DESCRIPTION, ApplicationSupport.getResource("STRING_resumeAction"));
            action.putValue(Action.SMALL_ICON, ApplicationSupport.getIcon("ICON_resume"));
        }
        checkActions();
    }

    /**
     * @see  org.vizzini.ui.game.AbstractGameUISwing#fillToolBar()
     */
    @Override
    protected void fillToolBar() {
        super.fillToolBar();
        JToolBar toolBar = ApplicationSupport.getToolBar();
        toolBar.addSeparator();
        toolBar.add(getPauseAction());
    }

    /**
     * @return  the pause action.
     *
     * @since   v0.3
     */
    protected Action getPauseAction() {
        return ActionManager.getAction(this, "pause");
    }
}
