package jmetest.monkeymahjongg.playground.model;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import jmetest.monkeymahjongg.playground.view.BackgroundGameState;
import jmetest.monkeymahjongg.playground.view.MahjonggGameState;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.GameSettings;
import com.jme.system.PreferencesGameSettings;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

/**
 * 
 * @author Pirx
 */
public class Main {

    private static Preferences preferences;

    private static GameSettings gameSettings;

    private static StandardGame standardGame;

    private static GameState backgroundGameState;

    private static GameState mainMenuGameState;

    private static GameState settingsMenuGameState;

    private static GameState mahjonggGameState;

    private static String menuPackage;

    private static String layoutName = "standard";

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String[] args) {
        menuPackage = (args.length == 0) ? "swing" : args[0];
        preferences = Preferences.userNodeForPackage(Main.class);
        gameSettings = new PreferencesGameSettings(preferences);
        standardGame = new StandardGame("Monkey Mahjongg", StandardGame.GameType.GRAPHICAL, gameSettings);
        standardGame.setBackgroundColor(ColorRGBA.darkGray);
        standardGame.start();
        backgroundGameState = new BackgroundGameState("jmetest/monkeymahjongg/images/Monkey.jpg");
        GameStateManager.getInstance().attachChild(backgroundGameState);
        backgroundGameState.setActive(true);
        mahjonggGameState = new MahjonggGameState();
        GameStateManager.getInstance().attachChild(mahjonggGameState);
        try {
            String menuPackageRoot = "jmetest.monkeymahjongg.playground.controller.menu.";
            mainMenuGameState = (GameState) Class.forName(menuPackageRoot + menuPackage + ".MainMenuGameState").newInstance();
            GameStateManager.getInstance().attachChild(mainMenuGameState);
            mainMenuGameState.setActive(true);
            Constructor<?> settingsConstructor = Class.forName(menuPackageRoot + menuPackage + ".SettingsMenuGameState").getConstructor(GameSettings.class);
            settingsMenuGameState = (GameState) settingsConstructor.newInstance(gameSettings);
            GameStateManager.getInstance().attachChild(settingsMenuGameState);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        setCursorVisible(true);
    }

    private static void setCursorVisible(final boolean visible) {
        GameTaskQueueManager.getManager().update(new Callable<Object>() {

            public Object call() throws Exception {
                MouseInput.get().setCursorVisible(visible);
                return null;
            }
        });
    }

    public static void selectSettingsMenu() {
        mainMenuGameState.setActive(false);
        settingsMenuGameState.setActive(true);
    }

    public static void selectMainMenu() {
        settingsMenuGameState.setActive(false);
        backgroundGameState.setActive(true);
        mainMenuGameState.setActive(true);
    }

    public static void startLevel() {
        mainMenuGameState.setActive(false);
        backgroundGameState.setActive(false);
        mahjonggGameState.setActive(true);
    }

    public static void stopLevel() {
        mahjonggGameState.setActive(false);
        mainMenuGameState.setActive(true);
        backgroundGameState.setActive(true);
    }

    public static void setLayoutName(String layoutName) {
        Main.layoutName = layoutName;
    }

    public static void exit() {
        standardGame.shutdown();
    }

    public static void savePreferences() {
        try {
            preferences.flush();
        } catch (BackingStoreException ex) {
        }
    }

    public static void changeResolution() {
    }

    public static Board getBoard() {
        return new Board(new XMLLevel("level/" + layoutName + ".xml"));
    }
}
