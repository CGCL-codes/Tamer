package jmetest.game;

import java.util.concurrent.Callable;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;

/**
 * <code>TestStandardGame</code> is meant to be an example replacement of
 * <code>jmetest.base.TestSimpleGame</code> using the StandardGame implementation
 * instead of SimpleGame.
 * 
 * @author Matthew D. Hicks
 */
public class TestStandardGame {

    public static void main(String[] args) throws Exception {
        System.setProperty("jme.stats", "set");
        StandardGame game = new StandardGame("A Simple Test");
        if (GameSettingsPanel.prompt(game.getSettings())) {
            game.start();
            GameTaskQueueManager.getManager().update(new Callable<Void>() {

                public Void call() throws Exception {
                    DebugGameState state = new DebugGameState();
                    Box box = new Box("my box", new Vector3f(0, 0, 0), 2, 2, 2);
                    box.setModelBound(new BoundingSphere());
                    box.updateModelBound();
                    state.getRootNode().attachChild(box);
                    box.updateRenderState();
                    GameStateManager.getInstance().attachChild(state);
                    state.setActive(true);
                    return null;
                }
            });
        }
    }
}
