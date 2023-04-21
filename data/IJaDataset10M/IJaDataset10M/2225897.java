package jmetest.input.controls;

import com.jme.image.Texture;
import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.input.controls.controller.ActionChangeController;
import com.jme.input.controls.controller.Axis;
import com.jme.input.controls.controller.ControlChangeListener;
import com.jme.input.controls.controller.RotationController;
import com.jme.input.controls.controller.ThrottleController;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;

/**
 * @author Matthew D. Hicks
 */
public class TestControls {

    public static void main(String[] args) throws Exception {
        final StandardGame game = new StandardGame("Test Controls");
        game.start();
        DebugGameState state = new DebugGameState(false);
        GameStateManager.getInstance().attachChild(state);
        state.setActive(true);
        Box box = new Box("Test Node", new Vector3f(), 5.0f, 5.0f, 5.0f);
        state.getRootNode().attachChild(box);
        TextureState ts = game.getDisplay().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture(TestSwingControlEditor.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        t.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(t);
        box.setRenderState(ts);
        box.updateRenderState();
        state.getRootNode().attachChild(box);
        GameControlManager manager = new GameControlManager();
        GameControl forward = manager.addControl("Forward");
        forward.addBinding(new KeyboardBinding(KeyInput.KEY_W));
        GameControl backward = manager.addControl("Backward");
        backward.addBinding(new KeyboardBinding(KeyInput.KEY_S));
        GameControl rotateLeft = manager.addControl("Rotate Left");
        rotateLeft.addBinding(new KeyboardBinding(KeyInput.KEY_A));
        GameControl rotateRight = manager.addControl("Rotate Right");
        rotateRight.addBinding(new KeyboardBinding(KeyInput.KEY_D));
        GameControl exit = manager.addControl("Exit");
        exit.addBinding(new KeyboardBinding(KeyInput.KEY_ESCAPE));
        ThrottleController throttle = new ThrottleController(box, forward, 1.0f, backward, -1.0f, 0.05f, 0.5f, 1.0f, false, Axis.Z);
        state.getRootNode().addController(throttle);
        RotationController rotation = new RotationController(box, rotateLeft, rotateRight, 0.2f, Axis.Y);
        state.getRootNode().addController(rotation);
        ActionChangeController quit = new ActionChangeController(exit, new ControlChangeListener() {

            public void changed(GameControl control, float oldValue, float newValue, float time) {
                if (newValue == 1.0f) {
                    game.shutdown();
                }
            }
        });
        state.getRootNode().addController(quit);
    }
}
