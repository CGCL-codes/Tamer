package com.yxl.test.lesson9;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;
import jmetest.renderer.TestSkybox;
import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import com.jmex.model.converters.MaxToJme;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.HillHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/**
 * 游戏加载地形（下文将使用Terrain代替）。这里对于我想要的类型的terrain有一些要求：
 * 
 * l 每次随机 2 不需太多三角形 3 为了跳跃“崎岖” 4 对于快速的交通工具足够大
 * 
 * @author: yxl
 * @version 1.0
 */
public class Lesson9 extends BaseGame {

    private int width, height;

    private int freq, depth;

    private boolean fullscreen;

    private Camera cam;

    protected Timer timer;

    private Node scene;

    private ForceFieldFence fence;

    private TerrainBlock tb;

    private Skybox skybox;

    private Node walls;

    private TextureState ts;

    private CollisionDetection bounce;

    /**文本信息显示节点*/
    private Node textInfoNode = new Node();

    /**显示得分*/
    private Text2D textScore;

    private Vector3f normal = new Vector3f();

    @Override
    protected void initSystem() {
        width = settings.getWidth();
        height = settings.getHeight();
        depth = settings.getDepth();
        freq = settings.getFrequency();
        fullscreen = settings.isFullscreen();
        try {
            display = DisplaySystem.getDisplaySystem(settings.getRenderer());
            display.createWindow(width, height, depth, freq, fullscreen);
            cam = display.getRenderer().createCamera(width, height);
        } catch (JmeException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        display.getRenderer().setBackgroundColor(ColorRGBA.black);
        cam.setFrustumPerspective(60.0f, (float) width / (float) height, 1, 5000);
        Vector3f loc = new Vector3f(100.0f, 80.0f, 350.0f);
        Vector3f left = new Vector3f(-1, 0, 0);
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f dir = new Vector3f(0, 0, -1);
        cam.setFrame(loc, left, up, dir);
        cam.update();
        timer = Timer.getTimer();
        display.getRenderer().setCamera(cam);
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
    }

    @Override
    protected void initGame() {
        display.setTitle("Lesson9");
        scene = new Node("Scene Graph Node");
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        scene.setRenderState(buf);
        ts = display.getRenderer().createTextureState();
        buildTerrain();
        buildLighting();
        buildSkyBox();
        buildPlayer();
        buildChaseCamera();
        buildInput();
        buildFlag();
        buildEnvironment();
        buildTextInfo();
        bounce = new CollisionDetection(player, walls, CollisionDetection.MOVE_BOUNCE_OFF);
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        scene.updateGeometricState(0.0f, true);
        scene.updateRenderState();
    }

    /**
     * 构造文本信息
     */
    private void buildTextInfo() {
        {
            textInfoNode.setLocalTranslation(new Vector3f(5, 5, 0));
            textInfoNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
            scene.attachChild(textInfoNode);
        }
        Font2D my2dfont = new Font2D();
        {
            textScore = my2dfont.createText("CurrentScore: 0", 10, 1);
            textScore.setLocalTranslation(new Vector3f(0, 0, 0));
            ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
            zbs.setFunction(ZBufferState.TestFunction.Always);
            textScore.setRenderState(zbs);
            textInfoNode.attachChild(textScore);
        }
    }

    /**
     * 跟新得分
     */
    private void updateScore(double score) {
        textScore.setText("CurrentScore: " + score);
    }

    /**
     * 输入处理类
     */
    private InputHandler input;

    /**
     * 构造输入处理
     */
    private void buildInput() {
        input = new FlagRushInputHandler(player, settings.getRenderer());
    }

    private Vehicle player;

    private float bikeHeight;

    /**
     * 构造用户
     */
    private void buildPlayer() {
        Node model = null;
        try {
            MaxToJme C1 = new MaxToJme();
            ByteArrayOutputStream BO = new ByteArrayOutputStream();
            URL maxFile = Lesson9.class.getClassLoader().getResource("jmetest/data/model/bike.3ds");
            C1.convert(new BufferedInputStream(maxFile.openStream()), BO);
            model = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            model.setLocalScale(.0095f);
            model.setModelBound(new BoundingBox());
            model.updateModelBound();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = new Vehicle("Player Node", model);
        player.setAcceleration(15);
        player.setBraking(10);
        player.setWeight(25);
        player.setTurnSpeed(2.5f);
        player.setMaxSpeed(25);
        player.setMinSpeed(1);
        player.setLocalTranslation(new Vector3f(100, 0, 100));
        scene.attachChild(player);
        scene.updateGeometricState(0, true);
        bikeHeight = ((BoundingBox) (player.getWorldBound())).yExtent;
    }

    private ChaseCamera chaser;

    /**
     * 构造跟随相机
     */
    private void buildChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = ((BoundingBox) player.getWorldBound()).yExtent * 1.5f;
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "16");
        props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "3");
        props.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + 45 * FastMath.DEG_TO_RAD);
        props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD));
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        chaser = new ChaseCamera(cam, player, props);
        chaser.setMaxDistance(16);
        chaser.setMinDistance(8);
    }

    /**
     * 创建heightmap和terrainBlock
     */
    private void buildTerrain() {
        HillHeightMap heightMap = new HillHeightMap(64, 2000, 2.0f, 10.0f, (byte) 2);
        Vector3f terrainScale = new Vector3f(4, .0575f, 4);
        tb = new TerrainBlock("terrain", heightMap.getSize(), terrainScale, heightMap.getHeightMap(), new Vector3f(0, 0, 0));
        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();
        tb.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        scene.attachChild(tb);
        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
        pt.addTexture(new ImageIcon(getClass().getClassLoader().getResource("jmetest/data/texture/grassb.png")), -128, 0, 128);
        pt.addTexture(new ImageIcon(getClass().getClassLoader().getResource("jmetest/data/texture/dirt.jpg")), 0, 128, 256);
        pt.addTexture(new ImageIcon(getClass().getClassLoader().getResource("jmetest/data/texture/highest.jpg")), 128, 256, 374);
        pt.createTexture(32);
        ts = display.getRenderer().createTextureState();
        Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
        ts.setTexture(t1, 0);
        Texture t2 = TextureManager.loadTexture(Lesson9.class.getClassLoader().getResource("jmetest/data/texture/Detail.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        t2.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(t2, 1);
        t1.setApply(Texture.ApplyMode.Combine);
        t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
        t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
        t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
        t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
        t2.setApply(Texture.ApplyMode.Combine);
        t2.setCombineFuncRGB(Texture.CombinerFunctionRGB.AddSigned);
        t2.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
        t2.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t2.setCombineSrc1RGB(Texture.CombinerSource.Previous);
        t2.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
        tb.setRenderState(ts);
        tb.setDetailTexture(1, 16);
    }

    /**
     * 构造灯光
     */
    private void buildLighting() {
        DirectionalLight light = new DirectionalLight();
        light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setDirection(new Vector3f(1, -1, 0));
        light.setEnabled(true);
        LightState lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        scene.setRenderState(lightState);
    }

    /**
     * 构造天空
     */
    private void buildSkyBox() {
        skybox = new Skybox("skybox", 10, 10, 10);
        Texture north = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/north.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/south.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/east.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/west.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/top.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(TestSkybox.class.getClassLoader().getResource("jmetest/data/texture/bottom.jpg"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);
        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();
        scene.attachChild(skybox);
    }

    @Override
    protected void cleanup() {
        ts.deleteAll();
    }

    @Override
    protected void reinit() {
        display.recreateWindow(width, height, depth, freq, fullscreen);
    }

    /**
     * 绘制场景图
     */
    @Override
    protected void render(float interpolation) {
        display.getRenderer().clearBuffers();
        display.getRenderer().draw(scene);
    }

    /**
     * 在update期间，我们只需寻找Escape按钮 并更新timer去获取帧率
     */
    @Override
    protected void update(float interpolation) {
        timer.update();
        interpolation = timer.getTimePerFrame();
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
            finished = true;
        }
        float characterMinHeight;
        characterMinHeight = tb.getHeight(player.getLocalTranslation()) + bikeHeight;
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) player.getLocalTranslation().y = characterMinHeight + 0.03f;
        chaser.update(interpolation);
        if (cam.getLocation().y < (tb.getHeight(cam.getLocation()) + 2)) {
            cam.getLocation().y = tb.getHeight(cam.getLocation()) + 2;
            cam.update();
        }
        input.update(interpolation);
        tb.getSurfaceNormal(player.getLocalTranslation(), normal);
        if (normal != null) player.rotateUpTo(normal);
        bounce.processCollisions();
        float distance = player.getLocalTranslation().distanceSquared(flag.getLocalTranslation());
        flag.update(interpolation, distance);
        updateScore(flag.score);
        skybox.setLocalTranslation(cam.getLocation());
        scene.updateGeometricState(interpolation, true);
    }

    /** 旗子 */
    private Flag flag;

    /**
     * 我们创建一个新的Flag类， 因此我们将使用这个把Flag加到世界中。 这个flag是我们相要获取的那个。
     */
    private void buildFlag() {
        flag = new Flag(tb);
        scene.attachChild(flag);
        flag.placeFlag();
    }

    /**
     * 构造墙壁
     */
    private void buildEnvironment() {
        walls = new Node("bouncing walls");
        scene.attachChild(walls);
        fence = new ForceFieldFence("forceFieldFence");
        fence.setLocalScale(5);
        fence.setLocalTranslation(new Vector3f(25, tb.getHeight(25, 25) + 10, 25));
        walls.attachChild(fence);
    }

    /**
     * 我们在这里将再次创建main方法。 它很像前一个向导的main方法，除了一个关键的地方不同。 这次我们将显示FlagRush的迷人的新logo。
     * AbstractGame定义了一对setConfigShowMode方法，其中的一个接受一个URL类用于加载Image。
     * 因此，我们将加载FlagRush.png（迷人的logo）并把它传给这个方法。
     * 现在，当PropertiesDialog被显示时，它将显示新的Logo。
     */
    public static void main(String[] args) {
        Lesson9 app = new Lesson9();
        java.net.URL url = app.getClass().getClassLoader().getResource("jmetest/data/images/FlagRush.png");
        app.setConfigShowMode(ConfigShowMode.NeverShow, url);
        app.start();
    }
}
