package trb.fps.jsg.skybox;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import javax.vecmath.Color4f;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import trb.jsg.RenderPass;
import trb.jsg.SceneGraph;
import trb.jsg.Shape;
import trb.jsg.Texture;
import trb.jsg.Unit;
import trb.jsg.VertexData;
import trb.jsg.View;
import trb.jsg.enums.Format;
import trb.jsg.enums.TextureType;
import trb.jsg.enums.Wrap;
import trb.jsg.renderer.Renderer;
import trb.jsg.util.Mat4;
import trb.jsg.util.TextureLoader;
import trb.jsg.util.Vec3;
import trb.jsg.util.geometry.VertexDataUtils;

public class Skybox {

    public Shape shape;

    public Texture texture;

    public Skybox() {
        try {
            String[] images = { "rightav9.jpg", "leftav9.jpg", "topav9.jpg", "Topav9.jpg", "frontav9.jpg", "backav9.jpg" };
            ByteBuffer[][] pixels = new ByteBuffer[6][1];
            int w = 0;
            int h = 0;
            for (int i = 0; i < pixels.length; i++) {
                BufferedImage image = ImageIO.read(getClass().getResource(images[i]));
                w = image.getWidth();
                h = image.getHeight();
                pixels[i][0] = TextureLoader.getImageData(image);
            }
            texture = new Texture(TextureType.TEXTURE_CUBE_MAP, GL11.GL_RGBA, w, h, 0, Format.BGRA, pixels, false, false);
            texture.setWrapS(Wrap.CLAMP_TO_EDGE);
            texture.setWrapT(Wrap.CLAMP_TO_EDGE);
            texture.setWrapR(Wrap.CLAMP_TO_EDGE);
            VertexData vertexData = VertexDataUtils.createBox(new Vec3(-1, -1, -1), new Vec3(1, 1, 1));
            vertexData.texCoords.set(new VertexData.TexCoordData(vertexData.coordinates, 3), 0);
            shape = new Shape();
            shape.setVertexData(vertexData);
            shape.getState().setUnit(0, new Unit(texture));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.create();
        View view = View.createPerspective((float) Math.PI / 4, 640 / 480f, 0.1f, 1000f);
        view.setCameraMatrix(new Mat4().translate(0, 0, -10));
        RenderPass renderPass = new RenderPass();
        renderPass.setClearMask(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        renderPass.setClearColor(new Color4f(1, 0, 0, 0));
        renderPass.setView(view);
        renderPass.getRootNode().addShape(new Skybox().shape);
        Renderer renderer = new Renderer(new SceneGraph(renderPass));
        float angle = 0f;
        while (!Display.isCloseRequested()) {
            view.setCameraMatrix(new Mat4().translate(0, 0, -10).rotateEulerDeg(30, angle++, 0));
            renderer.render();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
}
