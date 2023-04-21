package edu.diseno.jaspict3d.gui;

import java.awt.Font;
import java.net.URL;
import java.util.logging.Logger;
import org.jgrapht.Graph;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.FirstPersonHandler;
import com.jme.input.MouseInput;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.math.spring.SpringPointForce;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.effects.cloth.ClothUtils;
import com.jmex.effects.cloth.CollidingClothPatch;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;
import edu.diseno.jaspict3d.control.SceneManager;
import edu.diseno.jaspict3d.model.ModelComponent;
import edu.diseno.jaspict3d.render.GraphicScene;
import edu.diseno.jaspict3d.render.JaspictRenderer;
import edu.diseno.jaspict3d.render.engine.AbstractRendererFactory;
import edu.diseno.jaspict3d.render.engine.jmonkey.JMonkeyRendererDefault;
import edu.diseno.jaspict3d.render.engine.jmonkey.JMonkeyRendererFactory;
import edu.diseno.jaspict3d.render.layout.AspectHierarchyLayout;
import edu.diseno.jaspict3d.render.layout.JaspictLayout;

public class GuiJaspictMainApplication extends SimpleGame {

    private static final Logger logger = Logger.getLogger(GuiJaspictMainApplication.class.getName());

    protected SceneManager sceneManager;

    private AbsoluteMouse am;

    private PickResults pr;

    private CollidingClothPatch cloth;

    private CollidingClothPatch cloth2;

    private float windStrength = 60f;

    private Vector3f windDirection = new Vector3f(.3f, 0f, .2f);

    private SpringPointForce wind, gravity, drag;

    private String action;

    private Node presentationScreen;

    private Node examplesScreen;

    public GuiJaspictMainApplication() {
        super();
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-scenebuilder.xml" });
        BeanFactory factory = (BeanFactory) appContext;
        sceneManager = (SceneManager) factory.getBean("sceneManager");
    }

    public static void main(String[] args) {
        GuiJaspictMainApplication app = new GuiJaspictMainApplication();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        cam.setLocation(new Vector3f(0, 0, 80));
        cam.update();
        ((PointLight) lightState.get(0)).setLocation(new Vector3f(0, 0, 150));
        lightState.setTwoSidedLighting(true);
        if (action == null) {
            presentationScreen = new Node("presentationScreen");
            presentationScreen.attachChild(createUnicenLogo());
            presentationScreen.attachChild(createJaspictLogo());
            presentationScreen.attachChild(createLabelText());
            presentationScreen.attachChild(createMenuButtons());
            examplesScreen = new GuiJaspictMainExamplesMenu(display).create();
            rootNode.attachChild(createMouseAbsolutePointer());
            rootNode.attachChild(presentationScreen);
            for (int i = 0; i < 50; i++) {
                cloth.getSystem().getNode(i).position.x *= .8f;
                cloth.getSystem().getNode(i).setMass(Float.POSITIVE_INFINITY);
            }
            for (int i = 0; i < 70; i++) {
                cloth2.getSystem().getNode(i).position.x *= .8f;
                cloth2.getSystem().getNode(i).setMass(Float.POSITIVE_INFINITY);
            }
        }
        pr = new BoundingPickResults();
        ((FirstPersonHandler) input).getMouseLookHandler().setEnabled(false);
    }

    private AbsoluteMouse createMouseAbsolutePointer() {
        am = new AbsoluteMouse("The Mouse", display.getWidth(), display.getHeight());
        TextureState ts = display.getRenderer().createTextureState();
        URL cursorLoc = GuiJaspictMainApplication.class.getClassLoader().getResource("cursor1.png");
        Texture t = TextureManager.loadTexture(cursorLoc, Texture.MinificationFilter.NearestNeighborNoMipMaps, Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t);
        am.setRenderState(ts);
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        am.setRenderState(as);
        am.setLocalTranslation(new Vector3f(display.getWidth() / 2, display.getHeight() / 2, 0));
        am.registerWithInputHandler(input);
        return am;
    }

    private Node createLabelText() {
        Node textNode = new Node("textNode");
        Font3D font = new Font3D(new Font("Arial", Font.PLAIN, 24), 0.001f, true, true, true);
        Text3D text = font.createText("Universidad Nacional del Centro", 50.0f, 0);
        text.setLocalScale(new Vector3f(5.0f, 5.0f, 0.01f));
        text.setLocalTranslation(-37, 20, 0);
        Text3D text2 = font.createText("Jaspict Degree Thesis", 50.0f, 0);
        text2.setLocalScale(new Vector3f(5.0f, 5.0f, 0.01f));
        text2.setLocalTranslation(-25, -19, -10);
        Text3D text3 = font.createText("Maximo Vezzosi & Martin Uzquiano", 50.0f, 0);
        text3.setLocalScale(new Vector3f(3.0f, 3.0f, 0.01f));
        text3.setLocalTranslation(-27, -23, -3);
        textNode.attachChild(text);
        textNode.attachChild(text2);
        textNode.attachChild(text3);
        return textNode;
    }

    private Node createJaspictLogo() {
        Node jaspictLogoNode = new Node("jaspictLogonode");
        cloth2 = new CollidingClothPatch("cloth2", 30, 30, 1f, 10);
        wind = ClothUtils.createBasicWind(windStrength, windDirection, true);
        cloth2.addForce(wind);
        gravity = ClothUtils.createBasicGravity();
        cloth2.addForce(gravity);
        drag = ClothUtils.createBasicDrag(20f);
        cloth2.addForce(drag);
        cloth2.setLocalTranslation(18, 0, 0);
        TextureState textureState = display.getRenderer().createTextureState();
        URL monkeyLoc = GuiJaspictMainApplication.class.getClassLoader().getResource("jaspict_logo.jpg");
        Texture texture = TextureManager.loadTexture(monkeyLoc, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        textureState.setTexture(texture);
        cloth2.setRenderState(textureState);
        jaspictLogoNode.attachChild(cloth2);
        return jaspictLogoNode;
    }

    private Node createUnicenLogo() {
        Node unicenLogoNode = new Node("unicenLogoNode");
        cloth = new CollidingClothPatch("cloth", 30, 30, 1f, 10);
        wind = ClothUtils.createBasicWind(windStrength, windDirection, true);
        cloth.addForce(wind);
        gravity = ClothUtils.createBasicGravity();
        cloth.addForce(gravity);
        drag = ClothUtils.createBasicDrag(20f);
        cloth.addForce(drag);
        cloth.setLocalTranslation(-18, 0, 0);
        TextureState textureState = display.getRenderer().createTextureState();
        URL monkeyLoc = GuiJaspictMainApplication.class.getClassLoader().getResource("unicen.jpg");
        Texture texture = TextureManager.loadTexture(monkeyLoc, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        textureState.setTexture(texture);
        cloth.setRenderState(textureState);
        unicenLogoNode.attachChild(cloth);
        return unicenLogoNode;
    }

    private Node createMenuButtons() {
        Node buttonsNode = new Node("buttonsNode");
        Sphere s1 = new Sphere("action_Examples", 63, 50, 5);
        s1.setLocalTranslation(-20, -30, 0);
        s1.setModelBound(new BoundingBox());
        s1.updateModelBound();
        buttonsNode.attachChild(s1);
        buttonsNode.attachChild(createButtonLabel("Examples", -23, -30, 5));
        Sphere s2 = new Sphere("action_Help", 63, 50, 5);
        s2.setLocalTranslation(0, -30, 0);
        s2.setModelBound(new BoundingBox());
        s2.updateModelBound();
        buttonsNode.attachChild(s2);
        buttonsNode.attachChild(createButtonLabel("Help", -2, -30, 5));
        Sphere s3 = new Sphere("action_About", 63, 50, 5);
        s3.setLocalTranslation(20, -30, 0);
        s3.setModelBound(new BoundingBox());
        s3.updateModelBound();
        buttonsNode.attachChild(s3);
        buttonsNode.attachChild(createButtonLabel("About", 17, -30, 5));
        return buttonsNode;
    }

    private Text3D createButtonLabel(String label, float x, float y, float z) {
        Font3D font = new Font3D(new Font("Arial", Font.PLAIN, 24), 0.001f, true, true, true);
        Text3D text = font.createText(label, 50.0f, 0);
        text.setLocalScale(new Vector3f(1.7f, 1.7f, 0.01f));
        text.setLocalTranslation(x, y, z);
        return text;
    }

    protected void simpleUpdate() {
        if (MouseInput.get().isButtonDown(0)) {
            Vector2f screenPos = new Vector2f();
            screenPos.set(am.getHotSpotPosition().x, am.getHotSpotPosition().y);
            Vector3f worldCoords = display.getWorldCoordinates(screenPos, 0);
            Vector3f worldCoords2 = display.getWorldCoordinates(screenPos, 1);
            Ray mouseRay = new Ray(worldCoords, worldCoords2.subtractLocal(worldCoords).normalizeLocal());
            pr.clear();
            rootNode.findPick(mouseRay, pr);
            if (pr.getNumber() > 0) {
                action = pr.getPickData(0).getTargetMesh().getName();
                logger.info(pr.getPickData(0).getTargetMesh().getName());
            }
            makeTransition(action);
        }
    }

    private void makeTransition(String action) {
        if (action == "action_Examples") {
            rootNode.detachChild(presentationScreen);
            rootNode.attachChild(examplesScreen);
            lightState.detachAll();
            DirectionalLight dr = new DirectionalLight();
            dr.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1));
            dr.setDiffuse(new ColorRGBA(1, 1, 1, 1));
            dr.setEnabled(true);
            dr.setDirection(new Vector3f(1, 1, -1));
            lightState.attach(dr);
            rootNode.updateRenderState();
            rootNode.updateGeometricState(0.0f, true);
        } else if (action == "action_Help") {
            rootNode.detachChild(presentationScreen);
            sceneManager.createScene();
            Graph<ModelComponent, ModelComponent> modelGraph = sceneManager.getModelScene().getSceneGraph();
            final JaspictRenderer renderer = new JMonkeyRendererDefault();
            JaspictLayout jaspictLayout = new AspectHierarchyLayout();
            GraphicScene graphicScene = new GraphicScene(jaspictLayout, modelGraph, renderer);
            AbstractRendererFactory rendererFactory = new JMonkeyRendererFactory();
            graphicScene.setRendererFactory(rendererFactory);
            graphicScene.buildSceneGraph();
        } else if (action == "action_About") {
            rootNode.detachAllChildren();
        }
    }
}
