package kanjitori;

import com.jme.util.TextureManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.SwingUtilities;
import kanjitori.gamestate.FinishedGameState;
import kanjitori.gamestate.MainGameState;
import kanjitori.gamestate.PauseGameState;
import kanjitori.gamestate.StartupGameState;
import kanjitori.gamestate.controller.MoveController;
import kanjitori.lesson.Lesson;
import kanjitori.graphics.ortho.hud.DefaultHud;
import kanjitori.map.Map;
import kanjitori.preferences.KeyMap;
import kanjitori.stats.Statistics;
import kanjitori.util.SoundManager;

/**
 *
 * @author Pirx
 */
public class Main {

    private static Lesson lesson;

    private static MainGameState mainGameState;

    private static PauseGameState pauseGameState;

    private static FinishedGameState finishedGameState;

    private static StartupGameState startupGameState;

    private static StandardGame game;

    public static void startGame(Lesson lesson, Map map) {
        Main.lesson = lesson;
        com.jme.util.LoggingSystem.getLoggingSystem().setLevel(java.util.logging.Level.SEVERE);
        game = new StandardGame("Kanjitori", StandardGame.GameType.GRAPHICAL);
        game.start();
        game.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        startupGameState = new StartupGameState();
        startupGameState.setActive(true);
        GameStateManager.getInstance().attachChild(startupGameState);
        mainGameState = new MainGameState("main");
        GameStateManager.getInstance().attachChild(mainGameState);
        initGame(map);
        mainGameState.setActive(true);
        startupGameState.setActive(false);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    pauseGameState = new PauseGameState();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pauseGameState.setActive(false);
        GameStateManager.getInstance().attachChild(pauseGameState);
        finishedGameState = new FinishedGameState();
        finishedGameState.setActive(false);
        GameStateManager.getInstance().attachChild(finishedGameState);
    }

    private static void initGame(Map map) {
        SoundManager.getManager().setOn(true);
        game.getDisplay().getRenderer().getQueue().setTwoPassTransparency(false);
        new DefaultHud(mainGameState.getRootNode());
        LevelManager.init(map, lesson, mainGameState.getRootNode());
        MoveController.setContentMatrix(map.getContentMatrix());
    }

    public static void pauseGame() {
        mainGameState.setPause(true);
        pauseGameState.setActive(true);
    }

    public static void unpauseGame() {
        mainGameState.setPause(false);
        pauseGameState.setActive(false);
    }

    public static void exitGame() {
        game.shutdown();
        mainGameState.getRootNode().detachAllChildren();
        pauseGameState.getRootNode().detachAllChildren();
        finishedGameState.getRootNode().detachAllChildren();
        startupGameState.getRootNode().detachAllChildren();
        TextureManager.clearCache();
        System.exit(0);
    }

    public static void finishGame(Statistics stats) {
        mainGameState.setActive(false);
        pauseGameState.setActive(false);
        finishedGameState.setStatistics(stats);
        finishedGameState.setActive(true);
    }

    public static void loadingActivity(int percent, String activity) {
        startupGameState.updateScreen(percent, activity);
    }
}
