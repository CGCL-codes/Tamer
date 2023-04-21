package org.xith3d.test.loaders;

import java.net.URL;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.MouseWheel;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Colorf;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.ObjectRotationInputHandler;
import org.xith3d.input.modules.orih.ORIHInputAction;
import org.xith3d.loaders.models.Model;
import org.xith3d.loaders.models.ModelLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.loaders.util.RotatingModel;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "A test for loading 3DS files through the TDSLoader." }, authors = { "Kevin Glass", "Amos Wenger (aka BlueSky)", "Marvin Froehlich (aka Qudus)" })
public class TDSLoaderTest extends Xith3DTest {

    private Model model;

    private TransformGroup modelTG;

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case SPACE:
                TransformGroup tg = new TransformGroup(50f, 0f, 0f);
                Model newModel = model.getSharedInstance();
                if (newModel.hasAnimations()) newModel.setCurrentAnimation(newModel.getAnimations()[0]);
                tg.addChild(newModel);
                modelTG.addChild(tg);
                break;
            case ESCAPE:
                System.out.println("average FPS: " + this.getTotalAverageFPS());
                this.end();
                break;
        }
    }

    private BranchGroup loadModel(ResourceLocator resLoc) throws Exception {
        URL url = resLoc.getResource("models/character.3ds");
        long t0 = TestUtils.dumpAction("Loading TDS model \"" + url + "\"...");
        model = ModelLoader.getInstance().loadModel(url);
        TestUtils.dumpDoneIn(t0);
        if (model.hasAnimations()) {
            model.setCurrentAnimation(model.getAnimations()[0]);
        }
        RotatingModel rotModel = new RotatingModel(model, 1f);
        modelTG = new TransformGroup();
        modelTG.addChild(rotModel);
        BranchGroup root = new BranchGroup();
        root.addChild(modelTG);
        return (root);
    }

    public TDSLoaderTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(0f, 0f, 100f, 0f, 0f, 0f, 0f, 1f, 0f, this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        env.addPerspectiveBranch(loadModel(resLoc));
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        canvas.enableLighting();
        canvas.setBackgroundColor(Colorf.BLACK);
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        ObjectRotationInputHandler orih = new ObjectRotationInputHandler(modelTG, env.getView());
        orih.setDiscreteZoomStep(10f);
        orih.getBindingsManager().bind(MouseWheel.GLOBAL_WHEEL.getDown(), ORIHInputAction.DISCRETE_ZOOM_IN);
        orih.getBindingsManager().bind(MouseWheel.GLOBAL_WHEEL.getUp(), ORIHInputAction.DISCRETE_ZOOM_OUT);
        InputSystem.getInstance().addInputHandler(orih);
    }

    public static void main(String[] args) throws Throwable {
        TDSLoaderTest test = new TDSLoaderTest(parseCommandLine(args));
        test.begin();
    }
}
