package xmage.turbine.test.vt03;

import xmage.turbine.Camera;
import xmage.turbine.shape.Cube;
import xmage.turbine.Material;
import xmage.turbine.Node;
import xmage.turbine.Skin;
import xmage.turbine.Texture;
import xmage.turbine.test.VtBase;
import xmage.raster.Image;
import xmage.raster.codec.TGAInputCodec;
import java.io.IOException;

public class Vt09Fullbright extends VtBase {

    private Vt09Fullbright() throws IOException {
        Node root = new Node();
        Cube cube0 = new Cube(2.0);
        cube0.triangulate();
        cube0.setPosition(3.0, 0.0, -3.0);
        root.add(cube0);
        Cube cube1 = new Cube(2.0);
        cube1.triangulate();
        cube1.setPosition(-3.0, 0.0, 3.0);
        root.add(cube1);
        Cube bigCube = new Cube(20.0, 20.0, 20.0, Cube.INSIDE);
        bigCube.triangulate();
        root.add(bigCube);
        getCanvas().getRenderer().setRoot(root);
        Texture tex0 = new Texture(new TGAInputCodec().read("xmage/turbine/example/images/checkerboard_white_gray.tga"));
        Material m0 = new Material(tex0);
        m0.setFullbrightColor(1.0f, 1.0f, 1.0f);
        cube0.setSkin(new Skin(m0));
        Material m1 = new Material(tex0);
        m1.setFullbrightColor(1.0f, 0.0f, 0.0f);
        cube1.setSkin(new Skin(m1));
        Image image1 = new TGAInputCodec().read("xmage/turbine/example/images/checkerboard_orangeyellow.tga");
        bigCube.setSkin(new Skin(new Material(new Texture(image1))));
        getCanvas().getRenderer().setAmbientLightColor(0.4f, 0.4f, 0.4f);
        Camera camera = getCanvas().getRenderer().getCamera();
        camera.setType(Camera.PERSPECTIVE);
        camera.setNear(0.1);
        camera.setFar(100.0);
        camera.setFov(70.0);
        camera.setPosition(9.0, 9.0, 9.0);
        camera.setTarget(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        show();
        double anglex = 0.0;
        double angley = 0.0;
        double anglez = 0.0;
        float r = 0.0f;
        float g = 1.0f;
        float b = 2.0f;
        while (true) {
            cube0.getRotation().setToEuler(anglex, angley, anglez);
            cube1.getRotation().setToEuler(-anglex, -angley, -anglez);
            anglex += 0.011;
            angley += 0.013;
            anglez += 0.015;
            if (anglex > Math.PI * 2.0) {
                anglex -= Math.PI * 2.0;
            }
            if (angley > Math.PI * 2.0) {
                angley -= Math.PI * 2.0;
            }
            if (anglez > Math.PI * 2.0) {
                anglez -= Math.PI * 2.0;
            }
            r += 0.01;
            g += 0.01;
            b += 0.01;
            if (r > 3.0f) {
                r -= 3.0f;
            }
            if (g > 3.0f) {
                g -= 3.0f;
            }
            if (b > 3.0f) {
                b -= 3.0f;
            }
            m1.setFullbrightColor(Math.abs(1.0f - Math.min(r, 2.0f)), Math.abs(1.0f - Math.min(g, 2.0f)), Math.abs(1.0f - Math.min(b, 2.0f)));
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Vt09Fullbright();
    }
}
