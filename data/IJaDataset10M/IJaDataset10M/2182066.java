package homura.hde.app.core.game.state;

import homura.hde.util.Timer;

/**
 * <code>FPSGameState</code> is a simple extension of TextGameState to display the
 * frames per second. This provides a convenient mechanism to display or disable the
 * frames per second in a game.
 * 
 * @author Matthew D. Hicks
 */
public class FPSGameState extends TextGameState {

    private Timer timer;

    public FPSGameState() {
        super("FPS: 0");
        timer = Timer.getTimer();
    }

    public void update(float tpf) {
        super.update(tpf);
        setText("FPS: " + Math.round(timer.getFrameRate()));
    }
}
