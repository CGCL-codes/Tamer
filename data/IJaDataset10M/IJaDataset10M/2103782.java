package trb.fps.jsg.shader;

import javax.vecmath.Point2f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import trb.fps.LevelGenerator;
import trb.jsg.DepthBuffer;
import trb.jsg.RenderPass;
import trb.jsg.SceneGraph;
import trb.jsg.Shape;
import trb.jsg.Texture;
import trb.jsg.TreeNode;
import trb.jsg.Uniform;
import trb.jsg.View;
import trb.jsg.renderer.Renderer;
import trb.jsg.util.Mat4;
import trb.jsg.util.SGUtil;
import trb.jsg.util.Vec3;

/**
 * Renders a single triangle in ortho mode.
 *
 * @author tombr
 *
 */
public class PointLightTest {

    static float fovyDeg = 60;

    static float aspect = 1;

    static float far = 100f;

    public static void main(String[] args) throws Exception {
        int windowWidth = 1024;
        int windowHeight = 1024;
        Display.setLocation((Display.getDisplayMode().getWidth() - windowWidth) / 2, 10);
        Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
        Display.create(new PixelFormat().withStencilBits(8));
        View view = new View();
        float fovy = (float) Math.toRadians(fovyDeg);
        aspect = Display.getDisplayMode().getWidth() / (float) Display.getDisplayMode().getHeight();
        view.perspective(fovy, aspect, 0.1f, far);
        int basew = 1024;
        int baseh = 1024;
        RenderPass basePass = BasePass.createBasePass(view, basew, baseh);
        NormalMapping.shader.putUniform(new Uniform("farClipDistance", Uniform.Type.FLOAT, far));
        for (TreeNode node : new LevelGenerator().get()) {
            for (Shape shape : node.getAllShapesInTree()) {
                NormalMapping.apply(shape);
            }
            basePass.getRootNode().addChild(node);
        }
        SceneGraph sceneGraph = new SceneGraph();
        Texture lightTexture = SGUtil.createTexture(GL30.GL_RGBA16F, basew, baseh);
        Texture mixedTexture = SGUtil.createTexture(GL11.GL_RGB, basew, baseh);
        Texture baseTexture = basePass.getRenderTarget().getColorAttachments()[0];
        Texture rgbaTexture = basePass.getRenderTarget().getColorAttachments()[1];
        DepthBuffer baseDepth = basePass.getRenderTarget().getDepthBuffer();
        LightManager lightManager = new LightManager(baseTexture, rgbaTexture, baseDepth, lightTexture, view, new Point2f(windowWidth, windowHeight));
        PointLight light1 = lightManager.createPointLight(new Vec3(1, 0, 1), new Vec3(14, 5, 0), 10);
        PointLight light2 = lightManager.createPointLight(new Vec3(0, 1, 0), new Vec3(-14, 5, 0), 10);
        PointLight light3 = lightManager.createPointLight(new Vec3(1, 1, 1), new Vec3(0, 1, 0), 40);
        PointLight light4 = lightManager.createPointLight(new Vec3(1, 1, 1), new Vec3(0, 5, 0), 40);
        lightManager.createHemisphereLight(new Vec3(0.3, 0.2, 0.1), new Vec3(0.1, 0.1, 0.1), new Vec3(1, 1, 0));
        lightManager.createHemisphereLight(new Vec3(0.5, 0.5, 0.2), new Vec3(0.1, 0.2, 0.1), new Vec3(0, 0.5, 1));
        Bloom bloom = new Bloom(mixedTexture);
        SkyboxPass skyboxPass = new SkyboxPass(view, mixedTexture, baseDepth);
        FinalPass.createFinalPass(lightTexture, rgbaTexture, mixedTexture, baseDepth, basew, baseh, view);
        sceneGraph.addRenderPass(basePass);
        sceneGraph.addRenderPass(lightManager.renderPass);
        sceneGraph.addRenderPass(FinalPass.mixPass);
        sceneGraph.addRenderPass(skyboxPass.renderPass);
        sceneGraph.addRenderPass(FinalPass.toScreenPass);
        Renderer renderer = new Renderer(sceneGraph);
        long fpsTime = System.currentTimeMillis();
        int fpsCounter = 0;
        float angle = 0f;
        long start = System.currentTimeMillis();
        while (!Display.isCloseRequested()) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    System.exit(0);
                }
            }
            for (TreeNode node : new LevelGenerator().get()) {
                node.setTransform(new Mat4(node.getTransform()).translate(Math.random() * 20, 0, 0));
            }
            float timeAngle = (System.currentTimeMillis() - start) / 20f;
            angle = Mouse.getX();
            view.setCameraMatrix(new Mat4().rotateEulerDeg(-30, angle, 0).translate(0, 0, 30).invert_());
            light3.positionWorld.set(0, 2, 30 * Math.cos(Math.toRadians(timeAngle * 3.2f)));
            lightManager.update(view);
            skyboxPass.update();
            renderer.render();
            Display.update();
            fpsCounter++;
            long now = System.currentTimeMillis();
            if (now > fpsTime + 1000) {
                Display.setTitle("" + fpsCounter);
                fpsTime = now;
                fpsCounter = 0;
            }
        }
        Display.destroy();
    }
}
