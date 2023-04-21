package visualize;

import java.applet.Applet;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.Canvas3D;
import java.awt.BorderLayout;
import com.sun.j3d.utils.geometry.*;

public class FirstApplet extends Applet {

    private BranchGroup root;

    private TransformGroup mousetrans;

    private SimpleUniverse universe;

    public TransformGroup getMousetrans() {
        return mousetrans;
    }

    public BranchGroup getRoot() {
        return root;
    }

    public FirstApplet() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        universe = new SimpleUniverse(canvas);
        createSceneGraph();
    }

    public void writeScene() {
        universe.addBranchGraph(root);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private BranchGroup createSceneGraph() {
        root = new BranchGroup();
        Transform3D t3d = new Transform3D();
        t3d.rotY(Math.PI / 4.0);
        Transform3D t3dx = new Transform3D();
        t3dx.rotX(Math.PI / 4.0);
        Transform3D t3drotate = new Transform3D();
        t3drotate.setRotation(new AxisAngle4d(1.0, 1.0, -1.0, Math.PI / 6.0));
        t3d.mul(t3dx);
        t3d.mul(t3drotate);
        TransformGroup trans = new TransformGroup(t3d);
        mousetrans = new TransformGroup();
        mousetrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        mousetrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(mousetrans);
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        MouseRotate rotator = new MouseRotate(mousetrans);
        MouseTranslate transrator = new MouseTranslate(mousetrans);
        MouseZoom zoomer = new MouseZoom(mousetrans);
        rotator.setSchedulingBounds(bounds);
        transrator.setSchedulingBounds(bounds);
        zoomer.setSchedulingBounds(bounds);
        root.addChild(rotator);
        root.addChild(transrator);
        root.addChild(zoomer);
        return root;
    }

    public static void main(String[] args) {
        FirstApplet applet = new FirstApplet();
        MainFrame frame = new MainFrame(applet, 500, 500);
    }
}
