package eu.irreality.dai.gameplay.states;

public class MainState extends GameState {

    private static MainState mainInstance;

    static {
        mainInstance = new MainState();
        GameStateManager.register("MainState", mainInstance);
    }

    public static GameState getInstance() {
        return mainInstance;
    }

    private MainState() {
    }
}
