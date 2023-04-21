package org.xith3d.test.shaders;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.effects.EffectFactory;
import org.xith3d.effects.atmosphere.AtmosphereFactory;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.GLSLContext;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.PointLight;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.schedops.movement.RotatableGroup;
import org.xith3d.schedops.movement.TransformationDirectives;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.listeners.SliderListener;
import org.xith3d.ui.hud.widgets.Label;
import org.xith3d.ui.hud.widgets.Slider;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Xith3D planet Atmosphere test" }, authors = { "Yoann Meste (aka Mancer)" })
public class AtmosphereTest extends Xith3DTest implements SliderListener {

    private static final float RADIUS = 1.8f;

    private static final float ATMOSPHERE_RADIUS = 1.04f;

    private AtmosphereFactory factory;

    private Label label1, label2, label3;

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case SPACE:
                break;
            case ESCAPE:
                this.end();
                break;
        }
    }

    private void createSceneGraph(Xith3DEnvironment env) throws Exception {
        BranchGroup objRoot = new BranchGroup();
        RotatableGroup testRotateYGroup = new RotatableGroup(new TransformationDirectives(0f, 0.01f, 0f));
        objRoot.addChild(testRotateYGroup);
        PointLight light = new PointLight(true, Colorf.GRAY30, new Vector3f(-1f, 0.5f, 0.45f), 0.5f);
        objRoot.addChild(light);
        TransformGroup sceneRootTransform = new TransformGroup();
        Transform3D t = new Transform3D();
        t.setIdentity();
        sceneRootTransform.setTransform(t);
        testRotateYGroup.addChild(sceneRootTransform);
        Texture texture = TextureLoader.getInstance().getTexture("earth.jpg");
        {
            Sphere sphere = new Sphere(RADIUS, 64, 64, Geometry.COORDINATES | Geometry.NORMALS | Geometry.TEXTURE_COORDINATES, false, 2);
            Appearance a = new Appearance();
            a.setTexture(texture);
            sphere.setAppearance(a);
            sceneRootTransform.addChild(sphere);
            sphere.addAtmosphere(ATMOSPHERE_RADIUS, light, this.getUpdater());
        }
        env.addPerspectiveBranch(objRoot);
        this.getAnimator().addAnimatableObject(testRotateYGroup);
    }

    public void onSliderValueChanged(Slider slider, int newValue) {
        Tuple3f wavelength = factory.getWavelength3();
        float value = newValue / 1000f;
        if (slider.getUserObject().equals("WAVELENGTH_1")) {
            wavelength.setX(value);
            label1.setText(String.valueOf(newValue));
        } else if (slider.getUserObject().equals("WAVELENGTH_2")) {
            wavelength.setY(value);
            label2.setText(String.valueOf(newValue));
        } else if (slider.getUserObject().equals("WAVELENGTH_3")) {
            wavelength.setZ(value);
            label3.setText(String.valueOf(newValue));
        }
        factory.setWavelength3(wavelength);
    }

    private void createHUD(Xith3DEnvironment env, Sized2iRO resolution) {
        HUD hud = new HUD(resolution, 1024f);
        Slider slider = new Slider(265f);
        slider.setMinMaxAndValue(250, 1000, 650);
        slider.setSmoothSliding(true);
        slider.addSliderListener(this);
        slider.setUserObject("WAVELENGTH_1");
        hud.getContentPane().addWidget(slider, 32f, 580f);
        label1 = new Label(50, 20f, "650", Colorf.WHITE);
        hud.getContentPane().addWidget(label1, 305f, 580f);
        slider = new Slider(265f);
        slider.setMinMaxAndValue(250, 1000, 550);
        slider.setSmoothSliding(true);
        slider.addSliderListener(this);
        slider.setUserObject("WAVELENGTH_2");
        hud.getContentPane().addWidget(slider, 32f, 620f);
        label2 = new Label(50, 20f, "570", Colorf.WHITE);
        hud.getContentPane().addWidget(label2, 305f, 620f);
        slider = new Slider(265f);
        slider.setMinMaxAndValue(250, 1000, 650);
        slider.setSmoothSliding(true);
        slider.addSliderListener(this);
        slider.setUserObject("WAVELENGTH_3");
        hud.getContentPane().addWidget(slider, 32f, 660f);
        label3 = new Label(50, 20f, "675", Colorf.WHITE);
        hud.getContentPane().addWidget(label3, 305f, 660f);
        env.addHUD(hud);
    }

    public AtmosphereTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        GLSLContext.setDebuggingEnabled(true);
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 2f, 3f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        factory = EffectFactory.getInstance().getAtmosphereFactory();
        createSceneGraph(env);
        createHUD(env, arguments.getResolution());
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
    }

    public static void main(String[] args) throws Throwable {
        AtmosphereTest test = new AtmosphereTest(parseCommandLine(args));
        test.begin();
    }
}
