package dde.test.state;

import dde.core.DDGame;
import dde.state.FPSState;

public class TestFPSState {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        DDGame game = DDGame.getInstance();
        game.setDesiredFPS(60);
        FPSState fps = new FPSState();
        fps.setActive(true);
        game.attachState(fps);
        game.start();
    }
}
