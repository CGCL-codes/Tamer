package jmeTuts;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;

public class HelloNode extends SimpleGame {

    public static void main(String[] args) {
        HelloNode app = new HelloNode();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        Box b = new Box("My Box", new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        b.setModelBound(new BoundingSphere());
        b.updateModelBound();
        b.setLocalTranslation(new Vector3f(0, 2, 0));
        b.setSolidColor(ColorRGBA.blue);
        Sphere s = new Sphere("My Sphere", 10, 10, 1f);
        s.setModelBound(new BoundingBox());
        s.updateModelBound();
        s.setRandomColors();
        Node n = new Node("My Node");
        n.attachChild(s);
        n.attachChild(b);
        n.setLocalScale(5);
        rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
        rootNode.attachChild(n);
    }
}
