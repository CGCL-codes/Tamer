package starforge.scene;

import xero.app.Xero;
import xero.object.XeroAppScene;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class DemoScene extends XeroAppScene implements ActionListener {

    public DemoScene(String name, Xero app) {
        super(name, app);
    }

    public void buffer() {
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -0.7f, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        this.addLight(dl);
        DirectionalLight d2 = new DirectionalLight();
        d2.setDirection(new Vector3f(0.1f, 0.7f, -1).normalizeLocal());
        d2.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        this.addLight(d2);
        Spatial bumpy = (Spatial) assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        this.attachChild(bumpy);
    }

    public void init() {
        app.getFlyByCamera().setDragToRotate(true);
        app.getFlyByCamera().setMoveSpeed(30);
        app.getCamera().setLocation(new Vector3f(0, 2, 10));
        inputManager.deleteMapping("FLYCAM_RotateDrag");
        inputManager.deleteMapping("FLYCAM_ZoomIn");
        inputManager.deleteMapping("FLYCAM_ZoomOut");
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(2, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(2, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(app.getFlyByCamera(), "FLYCAM_RotateDrag");
        inputManager.addMapping("select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, "exit", "select", "FLYCAM_ZoomIn", "FLYCAM_ZoomOut");
    }

    public void update(float tpf) {
    }

    public void onAction(String name, boolean isPressed, float tpf) {
    }
}
