package org.xith3d.test.render;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.MouseButton;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.MouseButtonEvent;
import org.jagatoo.input.handlers.InputHandler;
import org.jagatoo.opengl.enums.TextureBoundaryMode;
import org.jagatoo.opengl.enums.TextureFormat;
import org.openmali.vecmath2.Colorf;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.ObjectRotationInputHandler;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.TexCoordGeneration;
import org.xith3d.scenegraph.TextureCubeMap;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.BackgroundImage;
import org.xith3d.scenegraph.primitives.Torus;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Cubic environment map test" }, authors = { "Abdul Bezrati", "Marvin Froehlich (aka Qudus)" })
public class CubicEnvironmentMapTest extends Xith3DTest {

    private TransformGroup torusTG;

    private InputHandler<?> orih;

    private static TexCoordGeneration[] texCoordGeneration = new TexCoordGeneration[2];

    static {
        texCoordGeneration[0] = new TexCoordGeneration(TexCoordGeneration.REFLECTION_MAP, TexCoordGeneration.TEXTURE_COORDINATE_3);
        texCoordGeneration[1] = new TexCoordGeneration(TexCoordGeneration.NORMAL_MAP, TexCoordGeneration.TEXTURE_COORDINATE_3);
    }

    private Appearance torusAppearance = new Appearance();

    private int textureCoordGenMode = 0;

    private Xith3DTest.FinishListener finishListener;

    @Override
    protected void exit() {
        if (finishListener != null) finishListener.onTestFinished(); else super.exit();
    }

    private void switchTexCoordGenerationMode() {
        textureCoordGenMode = ++textureCoordGenMode % 2;
        torusAppearance.setTexCoordGeneration(texCoordGeneration[textureCoordGenMode]);
    }

    @Override
    public void onKeyPressed(KeyPressedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
            case SPACE:
                switchTexCoordGenerationMode();
                break;
        }
    }

    @Override
    public void onMouseButtonStateChanged(MouseButtonEvent e, MouseButton button, boolean state) {
        orih.setMouseMovementSuspended(!state);
    }

    private static final void applyTextureCubeMap(Shape3D torus) throws Exception {
        String[] textureNames = { "skyboxes/flipped/right.png", "skyboxes/flipped/left.png", "skyboxes/flipped/top.png", "skyboxes/flipped/bottom.png", "skyboxes/flipped/front.png", "skyboxes/flipped/back.png" };
        TextureCubeMap textureCubeMap = new TextureCubeMap(TextureFormat.RGBA, 0, textureNames);
        textureCubeMap.setBoundaryModeS(TextureBoundaryMode.CLAMP_TO_EDGE);
        textureCubeMap.setBoundaryModeT(TextureBoundaryMode.CLAMP_TO_EDGE);
        torus.getAppearance(true).setTexture(textureCubeMap);
        torus.getAppearance(true).setTexCoordGeneration(texCoordGeneration[0]);
    }

    private BranchGroup createScene() throws Exception {
        BranchGroup root = new BranchGroup();
        Torus torus = new Torus(3.0f, 0.5f, 50, 50, Geometry.COORDINATES | Geometry.NORMALS, false, 2);
        applyTextureCubeMap(torus);
        this.torusAppearance = torus.getAppearance();
        this.torusTG = new TransformGroup();
        torusTG.addChild(torus);
        root.addChild(torusTG);
        return (root);
    }

    public CubicEnvironmentMapTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 6f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL();
        resLoc.createAndAddTSL("textures");
        env.addRenderPass(new BackgroundImage("backpic.png", arguments.getResolution().getAspect()));
        env.addPerspectiveBranch(createScene());
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        canvas.setBackgroundColor(Colorf.BLACK);
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        this.orih = new ObjectRotationInputHandler(torusTG);
        orih.setMouseMovementSuspended(true);
        InputSystem.getInstance().addInputHandler(orih);
    }

    public static void main(String[] args) throws Throwable {
        CubicEnvironmentMapTest test = new CubicEnvironmentMapTest(parseCommandLine(args));
        test.begin();
    }
}
