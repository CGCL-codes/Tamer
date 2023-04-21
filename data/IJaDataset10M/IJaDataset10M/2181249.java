package board;

import menu.MenuMode;
import menu.ModeHighscores;

public class HighscoreViewState extends InteractionState {

    private static HighscoreViewState instance;

    private static ModeHighscores mode;

    private HighscoreViewState() {
    }

    public static HighscoreViewState getInstance() {
        if (instance == null) {
            instance = new HighscoreViewState();
            mode = new ModeHighscores(getListener().getMenuController());
        }
        return instance;
    }

    public void setController(InteractionListener listener) {
        setListener(listener);
    }

    @Override
    void keyEscPressed() {
        getListener().changeState(MenuInteractionState.getInstance());
    }

    @Override
    public MenuMode getMode() {
        return mode;
    }
}
