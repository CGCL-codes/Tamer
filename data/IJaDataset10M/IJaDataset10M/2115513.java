package prealpha.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;
import prealpha.character.npc.BadBall;
import prealpha.character.npc.ZomB;
import prealpha.character.pc.Ascio;
import prealpha.character.Character;
import prealpha.core.Core;
import prealpha.core.Entity;
import prealpha.core.Updateable;
import prealpha.core.Updater;
import prealpha.curve.CatmullRomCurve;
import prealpha.curve.Curve;
import prealpha.curve.CurveController;
import prealpha.curve.CurveWrapper;
import prealpha.input.action.FireAction;
import prealpha.input.action.JumpAction;
import prealpha.util.Util;
import prealpha.util.Util.Prop;
import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.Pass;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jme.util.export.Savable;
import com.jme.util.geom.Debugger;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

public class InGameState extends GameState {

    protected final Logger log = Logger.getLogger("PreAlpha");

    /** Manages the whole Display */
    private final DisplaySystem display = DisplaySystem.getDisplaySystem();

    /** Renders the entire scene */
    private final Renderer renderer = display.getRenderer();

    /** Determines what part of the scene is seen and thus rendered */
    private final Camera camera = renderer.getCamera();

    /** The base of the Scene Graph */
    private Node rootNode;

    /** For advanced rendering */
    private BasicPassManager passManager;

    /** The main object for all physics simulation */
    private PhysicsSpace physics;

    /** Updates added updateable core game elements */
    private Updater updater;

    /** Handles the player input */
    InputHandler input;

    DynamicPhysicsNode player;

    Character chara;

    boolean handleInput;

    private final Vector3f calcV = new Vector3f();

    public InGameState() throws MalformedURLException {
        this(true);
    }

    public InGameState(boolean handleInput) throws MalformedURLException {
        super();
        this.handleInput = handleInput;
        log.info("SETUP BASICS\n");
        this.setName("Super Ascio PreAlpha");
        rootNode = new Node("root node");
        Util.get().putProp(rootNode);
        physics = Core.get().getPhysics();
        Util.get().putProp(physics);
        passManager = new BasicPassManager();
        Pass render = new RenderPass();
        render.add(rootNode);
        render.setEnabled(true);
        passManager.add(render);
        text = new Text[5];
        for (int i = 0; i < text.length; i++) {
            text[i] = Text.createDefaultTextLabel("text");
            rootNode.attachChild(text[i]);
            text[i].getLocalTranslation().addLocal(0, 12 * i + 13, 0);
        }
        if (handleInput) {
            KeyBindingManager.getKeyBindingManager().add("exit", KeyInput.KEY_ESCAPE);
            KeyBindingManager.getKeyBindingManager().add("phys", KeyInput.KEY_1);
            KeyBindingManager.getKeyBindingManager().add("bounds", KeyInput.KEY_2);
            KeyBindingManager.getKeyBindingManager().add("wire", KeyInput.KEY_3);
            KeyBindingManager.getKeyBindingManager().add("moveforward", KeyInput.KEY_D);
            KeyBindingManager.getKeyBindingManager().add("movebackward", KeyInput.KEY_A);
            KeyBindingManager.getKeyBindingManager().add("jump", KeyInput.KEY_W);
            KeyBindingManager.getKeyBindingManager().add("fire", KeyInput.KEY_SPACE);
            KeyBindingManager.getKeyBindingManager().add("music", KeyInput.KEY_M);
        } else {
            KeyBindingManager.getKeyBindingManager().add("phys", KeyInput.KEY_1);
            KeyBindingManager.getKeyBindingManager().add("bounds", KeyInput.KEY_2);
            KeyBindingManager.getKeyBindingManager().add("wire", KeyInput.KEY_3);
            KeyBindingManager.getKeyBindingManager().add("moveforward", KeyInput.KEY_H);
            KeyBindingManager.getKeyBindingManager().add("movebackward", KeyInput.KEY_F);
            KeyBindingManager.getKeyBindingManager().add("jump", KeyInput.KEY_T);
            KeyBindingManager.getKeyBindingManager().add("fire", KeyInput.KEY_LCONTROL);
            KeyBindingManager.getKeyBindingManager().add("music", KeyInput.KEY_M);
        }
        LightState light = renderer.createLightState();
        light.setEnabled(true);
        DirectionalLight sun = new DirectionalLight();
        sun.setEnabled(true);
        sun.setShadowCaster(true);
        sun.setAmbient(ColorRGBA.white);
        sun.setDiffuse(new ColorRGBA(.25f, .25f, 0, 1.f));
        sun.getDirection().set(-1, -1, -.5f);
        light.attach(sun);
        rootNode.setRenderState(light);
        ZBufferState zBuff = renderer.createZBufferState();
        zBuff.setEnabled(true);
        rootNode.setRenderState(zBuff);
        CullState cull = renderer.createCullState();
        cull.setEnabled(true);
        rootNode.setRenderState(cull);
        log.info("SETUP FLOOR\n");
        Node floor = new Node("floor");
        rootNode.attachChild(floor);
        StaticPhysicsNode floorPhys = physics.createStaticNode();
        floor.getLocalRotation().fromAngleAxis(0 * FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);
        floor.attachChild(floorPhys);
        floorPhys.attachChild(new Box("floor", new Vector3f(0, -1, 0), 1000, 1, 2f));
        floorPhys.attachChild(new Box("floor", new Vector3f(30, 3, 0), 2, 3, 2f));
        floorPhys.attachChild(new Box("floor", new Vector3f(60, 3, 0), 2, 3, 2f));
        floorPhys.attachChild(new Box("floor", new Vector3f(90, 3, 0), 2, 3, 2f));
        floorPhys.attachChild(new Box("floor", new Vector3f(120, 10, 0), 5, 1, 2f));
        floorPhys.attachChild(new Box("floor", new Vector3f(150, 4, 0), 2, 4, 2f));
        floorPhys.setModelBound(new BoundingBox());
        floorPhys.updateModelBound();
        floorPhys.generatePhysicsGeometry();
        Util.get().putProp(floorPhys);
        log.info("SETUP ENTITYS\n");
        updater = new Updater();
        log.info("SETUP PLAYER\n");
        Ascio ascio = new Ascio(updater, physics, floor);
        ascio.getLocalTranslation().addLocal(0, 1, 0);
        addEntity(ascio);
        chara = ascio;
        player = ascio.getPhysicsNode();
        log.info("SETUP ENEMYS\n");
        BadBall bb = new BadBall(updater, physics, floor);
        bb.getPhysicsNode().getLocalTranslation().addLocal(45, 2, 0);
        addEntity(bb);
        BadBall bb2 = new BadBall(updater, physics, floor);
        bb2.getPhysicsNode().getLocalTranslation().addLocal(70, 2, 0);
        addEntity(bb2);
        BadBall bb3 = new BadBall(updater, physics, floor);
        bb3.getPhysicsNode().getLocalTranslation().addLocal(80, 2, 0);
        addEntity(bb3);
        ZomB zb = new ZomB(updater, physics, floor);
        zb.getPhysicsNode().getLocalTranslation().addLocal(15, 2, 0);
        addEntity(zb);
        log.info("SETUP SOUND\n");
        AudioSystem.getSystem().setDopplerFactor(.1f);
        rootNode.updateRenderState();
        Timer.getTimer().reset();
    }

    protected void addEntity(Entity e) {
        rootNode.attachChild(e);
    }

    Text[] text;

    @Override
    public void update(float tpf) {
        if (handleInput) {
            calcV.set(player.getLocalTranslation());
            calcV.set(2, 40);
            calcV.add(0, 10, 0);
            camera.getLocation().set(calcV);
            camera.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
            camera.update();
        }
        rootNode.updateGeometricState(tpf, true);
        physics.update(tpf);
        passManager.updatePasses(tpf);
        updater.update(tpf);
        text[0].print("FPS : " + Timer.getTimer().getFrameRate());
        text[1].print("HEALTH : " + chara.getHealth());
        if (KeyInput.get().isKeyDown(KeyInput.KEY_3)) {
            player.getLocalTranslation().set(0, .75f, 0);
            player.getLocalRotation().set(0, 0, 0, 1);
            player.clearDynamics();
        }
        if (KeyInput.get().isKeyDown(KeyInput.KEY_6)) {
            player.addTorque(Vector3f.UNIT_Y.mult(50));
        }
        if (KeyInput.get().isKeyDown(KeyInput.KEY_0)) {
            try {
                Util.shoutln(Util.computePi(10, null));
            } catch (InterruptedException e) {
            }
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
            GameStateManager.getInstance().cleanup();
            System.exit(0);
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("phys", false)) {
            drawPhysics = !drawPhysics;
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("bounds", false)) {
            drawBounds = !drawBounds;
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("wire", false)) {
            drawBounds = !drawBounds;
        }
    }

    boolean drawBounds = false;

    boolean drawPhysics = false;

    @Override
    public void render(float tpf) {
        passManager.renderPasses(renderer);
        if (drawBounds) Debugger.drawBounds(rootNode, renderer, true);
        if (drawPhysics) PhysicsDebugger.drawPhysics(physics, renderer);
    }

    @Override
    public void cleanup() {
        rootNode = null;
        physics = null;
    }
}
