package jme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/** Sample 5 - how to map keys and mousebuttons to actions */
public class HelloInput extends SimpleApplication {

    public static void main(String[] args) {
        HelloInput app = new HelloInput();
        app.start();
    }

    protected Geometry player;

    Boolean isRunning = true;

    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        player = new Geometry("Player", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);
        rootNode.attachChild(player);
        initKeys();
    }

    /** Custom Keybinding: Map named actions to inputs. */
    private void initKeys() {
        inputManager.addMapping("Pause", new KeyTrigger(keyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, new String[] { "Pause" });
        inputManager.addListener(analogListener, new String[] { "Left", "Right", "Rotate" });
    }

    /** Use this listener for KeyDown/KeyUp events */
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };

    /** Use this listener for continuous events */
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("Rotate")) {
                    player.rotate(0, value, 0);
                }
                if (name.equals("Right")) {
                    player.move((new Vector3f(value, 0, 0)));
                }
                if (name.equals("Left")) {
                    player.move(new Vector3f(-value, 0, 0));
                }
            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };
}
