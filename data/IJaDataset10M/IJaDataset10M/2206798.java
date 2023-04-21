package ballsoffury.playground.view;

import java.nio.FloatBuffer;
import ballsoffury.playground.controller.BackToMenuController;
import ballsoffury.playground.controller.CameraController;
import ballsoffury.playground.controller.HintController;
import ballsoffury.playground.controller.MousePickController;
import ballsoffury.playground.model.Board;
import ballsoffury.playground.model.Hint;
import ballsoffury.playground.model.GameManager;
import ballsoffury.playground.model.Tile;
import com.jme.input.InputHandler;
import com.jme.input.controls.GameControlManager;
import com.jme.input.keyboard.KeyboardInputHandlerDevice;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;

/**
 * <code>BoxRemapper</code> allows for management of the game. Various methods 
 * are included in this class to show various screens, alter preferences,  
 * and exit the application.
 *
 * @author Corey Oliver
 * @version $Revision:$ $Date:$
 */
public class BallsOfFuryGameState extends BasicGameState {

    static float BORDER = 0.2f;

    static float SLOPE = 0.1f;

    private static float RATIO = 0.5f * BORDER / (1 + SLOPE);

    private static float TEX = 0.7f;

    private static float[] picture = new float[] { TEX, 0, 0, 0, TEX, 1, 0, 1, TEX * (1 - RATIO), RATIO, TEX * RATIO, RATIO, TEX * (1 - RATIO), 1 - RATIO, TEX * RATIO, 1 - RATIO };

    private static float[] border = new float[] { 1, 0, TEX, 0, 1, 1, TEX, 1, 1 - (1 - TEX) * RATIO, RATIO, TEX + (1 - TEX) * RATIO, RATIO, 1 - (1 - TEX) * RATIO, 1 - RATIO, TEX + (1 - TEX) * RATIO, 1 - RATIO };

    private float dx = 3.5f;

    private float dy = 5f;

    private float dz = 1.5f;

    private Node cameraRotationNode;

    private Node cameraDistanceNode;

    private GameControlManager gameControlManager;

    private Board board;

    public BallsOfFuryGameState() {
        super("ballsoffury");
    }

    @Override
    public void setActive(boolean active) {
        if (active) {
            init(GameManager.getBoard());
        } else {
            rootNode.detachAllChildren();
            rootNode.removeController(2);
            rootNode.removeController(1);
            rootNode.removeController(0);
        }
        super.setActive(active);
    }

    public void init(Board board) {
        this.board = board;
        Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        cameraRotationNode = new Node("camRotation");
        cameraDistanceNode = new CameraNode("camDistance", camera);
        cameraRotationNode.attachChild(cameraDistanceNode);
        cameraDistanceNode.setLocalTranslation(new Vector3f(0, 0, 100f));
        cameraDistanceNode.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.PI, new Vector3f(0, 1, 0)));
        addControllers();
        rootNode.detachAllChildren();
        rootNode.attachChild(cameraRotationNode);
        initLight();
        Vector3f extent = new Vector3f(dx, dy, dz);
        RoundedBox box = new RoundedBox("box", extent, extent.mult(BORDER), extent.mult(SLOPE));
        FloatBuffer fb = box.getTextureBuffer(0, 0);
        fb.rewind();
        fb.put(border);
        fb.put(picture);
        fb.put(border);
        fb.put(picture);
        fb.put(border);
        fb.put(border);
        for (Tile tile : board) {
            if (tile != null) {
                UITile uiTile = new UITile(tile, box);
                rootNode.attachChild(uiTile);
            }
        }
        rootNode.updateRenderState();
    }

    private void initLight() {
        PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.55f, 0.55f, 0.55f, 1.0f));
        light.setAmbient(new ColorRGBA(0.05f, 0.05f, 0.05f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);
        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState);
        rootNode.updateRenderState();
    }

    public void addControllers() {
        gameControlManager = new GameControlManager();
        rootNode.addController(new CameraController(this));
        rootNode.addController(new BackToMenuController(this));
        rootNode.addController(new MousePickController(this));
        rootNode.addController(new HintController(this));
    }

    public Node getCameraRotationNode() {
        return cameraRotationNode;
    }

    public Node getCameraDistanceNode() {
        return cameraDistanceNode;
    }

    public GameControlManager getGameControlManager() {
        return gameControlManager;
    }

    public void hint() {
        Hint hint = board.getHint();
        if (hint != null) {
            hint.getFirst().showHint();
            hint.getSecond().showHint();
        }
    }
}
