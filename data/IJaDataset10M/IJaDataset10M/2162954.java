package trb.fps.jsg.shader;

import javax.vecmath.Color4f;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import trb.jsg.LightState.Light;
import trb.jsg.RenderPass;
import trb.jsg.SceneGraph;
import trb.jsg.Shader;
import trb.jsg.Shape;
import trb.jsg.TreeNode;
import trb.jsg.Uniform;
import trb.jsg.VertexData;
import trb.jsg.View;
import trb.jsg.renderer.Renderer;
import trb.jsg.util.ShaderUtils;
import trb.jsg.util.Vec3;

public class UniformTest {

    public static void main(String[] args) throws Exception {
        int windowWidth = 640;
        int windowHeight = 480;
        Display.setLocation((Display.getDisplayMode().getWidth() - windowWidth) / 2, (Display.getDisplayMode().getHeight() - windowHeight) / 2);
        Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
        Display.create();
        View view = new View();
        view.ortho(0, windowWidth, 0, windowHeight, -1000, 1000);
        Light light1 = new Light();
        light1.specular.set(0, 0, 0);
        light1.diffuse.set(0.5f, 0.5f, 0.5f);
        light1.setPointLight(new Vec3(200, 200, 200), 1, 0, 0);
        RenderPass renderPass = new RenderPass();
        renderPass.setClearMask(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        renderPass.setClearColor(new Color4f(0, 0, 0.4f, 0));
        renderPass.setView(view);
        renderPass.getLightState().lights.set(light1, 0);
        Shader shader = ShaderUtils.loadFromResource("/trb/fps/jsg/shader/uniformVertex.shader", "/trb/fps/jsg/shader/uniformFragment.shader");
        shader.putUniform(new Uniform("myColor", Uniform.Type.VEC4, 1f, 0f, 0f, 1f));
        VertexData vertexData = new VertexData();
        vertexData.setCoordinates(new float[] { 100, 100, 0, 100, 400, 0, 400, 400, 0, 400, 100, 0 }, new float[] { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 }, null, 0, null, new int[] { 0, 1, 2, 2, 3, 0 });
        Shape shape = new Shape();
        shape.getState().setShader(shader);
        shape.setVertexData(vertexData);
        TreeNode root = renderPass.getRootNode();
        root.addShape(shape);
        SceneGraph sceneGraph = new SceneGraph();
        sceneGraph.insertRenderPass(renderPass, 0);
        Renderer renderer = new Renderer(sceneGraph);
        while (!Display.isCloseRequested()) {
            renderer.render();
            Display.update();
        }
        Display.destroy();
    }
}
