package w3duniverse.poc1.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import w3duniverse.poc1.interfaces.world.I_3DWorld;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class World3DPlugin extends PluginPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2269293426066774788L;

    private Transform3D rotate1 = new Transform3D();

    private Transform3D rotate2 = new Transform3D();

    public World3DPlugin(I_3DWorld world, Component component) {
        super(world, component);
        Canvas3D canvas3D = createCanvas3D();
        BranchGroup scene = createSceneGraph();
        connect(canvas3D, scene);
    }

    private Canvas3D createCanvas3D() {
        setSize(300, 230);
        this.setLayout(null);
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setBackground(Color.BLACK);
        canvas3D.setBounds(new Rectangle(0, 17, 300, 213));
        this.add(canvas3D);
        return canvas3D;
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        TransformGroup spinner = new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        spinner.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        objRoot.addChild(spinner);
        Appearance appearance = new Appearance();
        appearance.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0.0f));
        appearance.setMaterial(new Material());
        appearance.setColoringAttributes(new ColoringAttributes(new Color3f(Color.BLUE), 2));
        Sphere sphere = new Sphere(((I_3DWorld) getWorld()).getRadius(), appearance);
        spinner.addChild(sphere);
        Color3f light1Color = new Color3f(((I_3DWorld) getWorld()).getWorldColor());
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 290.0);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        spinner.addChild(light1);
        spinner.addChild(makeSpin(spinner));
        return objRoot;
    }

    private RotationInterpolator makeSpin(TransformGroup spinner) {
        RotationInterpolator rotator = new RotationInterpolator(new Alpha(-1, ((I_3DWorld) getWorld()).getIncreasingAlphaDuration()), spinner);
        rotator.setTransformAxis(rotateTransform());
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        return rotator;
    }

    private Transform3D rotateTransform() {
        rotate1.rotX(Math.PI / 4.0d);
        rotate2.rotY(Math.PI / 3.0d);
        rotate1.mul(rotate2);
        return rotate1;
    }

    private void connect(Canvas3D canvas3D, BranchGroup scene) {
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        simpleU.addBranchGraph(scene);
    }
}
