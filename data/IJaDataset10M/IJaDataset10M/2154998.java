package br.furb.inf.tcc.sandbox;

import java.util.logging.Level;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.LoggingSystem;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.states.PhysicsGameState;

public class TestObjects extends DebugGameState {

    public DynamicPhysicsNode dynamicNode;

    public StaticPhysicsNode staticNode;

    public boolean alreadyShot = false;

    public void update(float arg0) {
        super.update(arg0);
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("shot", true) && !alreadyShot) {
        }
    }

    public static void main(String[] args) throws Exception {
        LoggingSystem.getLogger().setLevel(Level.WARNING);
        final StandardGame game = new StandardGame("Physics tutorial");
        game.start();
        final TestObjects debugGameState = new TestObjects();
        final PhysicsGameState physicsGameState = new PhysicsGameState("Physics tutorial");
        GameStateManager.getInstance().attachChild(debugGameState);
        debugGameState.setActive(true);
        GameStateManager.getInstance().attachChild(physicsGameState);
        physicsGameState.setActive(true);
        StaticPhysicsNode staticNode = physicsGameState.getPhysicsSpace().createStaticNode();
        debugGameState.staticNode = staticNode;
        debugGameState.getRootNode().attachChild(staticNode);
        final Box visualFloorBox = new Box("floor", new Vector3f(), 5, 0.25f, 5);
        visualFloorBox.setSolidColor(ColorRGBA.brown);
        staticNode.attachChild(visualFloorBox);
        staticNode.generatePhysicsGeometry();
        DynamicPhysicsNode dynamicNode = physicsGameState.getPhysicsSpace().createDynamicNode();
        debugGameState.dynamicNode = dynamicNode;
        debugGameState.getRootNode().attachChild(dynamicNode);
        final Box visualFallingBox = new Box("falling box", new Vector3f(), 0.5f, 0.5f, 0.5f);
        visualFallingBox.setRandomColors();
        dynamicNode.attachChild(visualFallingBox);
        dynamicNode.generatePhysicsGeometry();
        dynamicNode.getLocalTranslation().set(0, 15, 0);
        dynamicNode.getLocalRotation().fromAngleAxis(FastMath.DEG_TO_RAD * 45, new Vector3f(0, 1, 0));
        Text text = Text.createDefaultTextLabel("text");
        text.setCullMode(SceneElement.CULL_ALWAYS);
        text.setTextureCombineMode(TextureState.REPLACE);
        text.setLocalTranslation(new Vector3f(0, 60, 0));
        text.print("Tanquee");
        Node n = new Node();
        n.attachChild(text);
        n.updateRenderState();
        debugGameState.getRootNode().attachChild(n);
        debugGameState.getRootNode().updateRenderState();
        KeyBindingManager.getKeyBindingManager().add("shot", KeyInput.KEY_LCONTROL);
    }
}
