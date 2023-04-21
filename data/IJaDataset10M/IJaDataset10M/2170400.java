package xhack.ui.basic;

/**
 * Interprets user commands when the title image is being displayed.
 */
public class SplashContext implements InputContext {

    BasicUI ui;

    public SplashContext(BasicUI ui) {
        this.ui = ui;
    }

    public void inputEvent(int action) {
        ui.game.gameMenu();
    }
}
