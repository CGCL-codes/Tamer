package computergraphicsproject.Engines;

import computergraphicsproject.SceneManagement.XMLSceneManager;
import computergraphicsproject.model.Scenes.Scene;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main game engine for the chess game
 * @author hussein
 */
public class GameEngine {

    private ChessEngine chessEngine = null;

    private GraphicsEngine graphicsEngine = null;

    private Thread graphicsThread = null;

    private Thread chessThread = null;

    private Object signal = new Object();

    private SignalType type;

    private boolean userIsWhite = false;

    private int difficulty = 0;

    private Scene choosenScene = null;

    /**
     *
     * @param type
     */
    public void signal(SignalType type) {
        synchronized (signal) {
            this.type = type;
            signal.notify();
        }
    }

    /**
     *
     * @return
     */
    public ChessEngine getChessEngine() {
        return chessEngine;
    }

    /**
     * 
     * @return
     */
    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    /**
     *
     */
    public void start() {
        graphicsEngine = new GraphicsEngine();
        MainMenu mainMenu = new MainMenu();
        ParticleEngine particleEngine = new ParticleEngine();
        graphicsThread = new Thread(graphicsEngine);
        Thread ParticleEngineThread = new Thread(particleEngine);
        graphicsThread.setPriority(Thread.MAX_PRIORITY);
        graphicsThread.start();
        ParticleEngineThread.start();
        graphicsEngine.setInputHandler(particleEngine);
        synchronized (signal) {
            try {
                signal.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (true) {
            graphicsEngine.setAnalygraph(false);
            Thread mainMenuThread = new Thread(mainMenu);
            mainMenuThread.start();
            graphicsEngine.setInputHandler(mainMenu);
            synchronized (signal) {
                try {
                    signal.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (type == SignalType.Exit) {
                graphicsEngine.setExitSignal(true);
                return;
            } else if (type == SignalType.StartGame) {
                graphicsEngine.clearDisplay();
                graphicsEngine.clearInputHandler();
                chessEngine = new ChessEngine(userIsWhite, difficulty, choosenScene);
                chessThread = new Thread(chessEngine);
                chessThread.setPriority(Thread.MIN_PRIORITY);
                if (SettingsManager.getParameter("analygraph").equals("on")) {
                    graphicsEngine.setAnalygraph(true);
                }
                chessThread.start();
                synchronized (signal) {
                    try {
                        signal.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (type == SignalType.Exit) {
                    graphicsEngine.setExitSignal(true);
                    return;
                } else if (type == SignalType.BackToMain) {
                    graphicsEngine.clearDisplay();
                    continue;
                }
            }
        }
    }

    public void setGameParameters(boolean userIsWhite, int difficulty, Scene scene) {
        this.userIsWhite = userIsWhite;
        this.difficulty = difficulty;
        this.choosenScene = scene;
    }

    private GameEngine() {
    }

    /**
     *
     * @return
     */
    public static GameEngine getInstance() {
        return GameEngineHolder.INSTANCE;
    }

    private static class GameEngineHolder {

        private static final GameEngine INSTANCE = new GameEngine();
    }
}
