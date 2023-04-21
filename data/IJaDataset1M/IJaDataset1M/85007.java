package g2.routemaster.gui;

import g2.routemaster.bundle.gui.GuiAdaptor;
import g2.routemaster.model.GameManager;
import org.eclipse.jface.window.ApplicationWindow;

public class GManager {

    private static GManager instance;

    private GameManager gameManager;

    private ApplicationWindow window;

    private GuiAdaptor guiAdaptor;

    private GManager() {
        this.gameManager = new GameManager();
        this.gameManager.setup();
    }

    public void restart() {
        this.gameManager = new GameManager();
        this.gameManager.setup();
    }

    public static GManager instance() {
        if (instance == null) {
            instance = new GManager();
        }
        return instance;
    }

    /** constructor to invoked on game load */
    private GManager(GameManager gm) {
        this.gameManager = gm;
    }

    /**
    * @return the gameManager
    */
    public GameManager getGameManager() {
        if (guiAdaptor != null) {
            return guiAdaptor.getGameManager();
        } else if (gameManager != null) {
            return gameManager;
        } else {
            this.gameManager = new GameManager();
            this.gameManager.setup();
            return gameManager;
        }
    }

    public void setGameManager(GameManager gameManager) {
        if (guiAdaptor != null) {
            guiAdaptor.setGameManager(gameManager);
        } else if (this.gameManager != null) {
            this.gameManager = gameManager;
        }
    }

    /**
    * On loading of game, use this method to set loaded GameManager
    * @param gm
    */
    public static void setLoadedGameManager(GameManager gm) {
        ApplicationWindow aw = instance.getWindow();
        instance = new GManager(gm);
        instance.setWindow(aw);
    }

    /**
    * @return the window
    */
    public ApplicationWindow getWindow() {
        return window;
    }

    /**
    * @param window the window to set
    */
    public void setWindow(ApplicationWindow window) {
        this.window = window;
    }

    /**
    * @param guiAdaptor the guiAdaptor to set
    */
    public void setGuiAdaptor(GuiAdaptor guiAdaptor) {
        this.guiAdaptor = guiAdaptor;
    }
}
