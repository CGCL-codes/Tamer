package test;

import java.applet.Applet;
import java.awt.BorderLayout;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.Enumeration;
import java.awt.*;
import java.lang.String;

public class PickText3DBounds extends Applet {

    private SimpleUniverse u = null;

    public BranchGroup objRoot = null;

    public BranchGroup createObjRoot(BoundingSphere bounds) {
        Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f objColor = new Color3f(0.6f, 0.6f, 0.6f);
        Color3f lColor1 = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f lColor2 = new Color3f(0.0f, 1.0f, 0.0f);
        Color3f lColor3 = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f alColor = new Color3f(0.2f, 0.2f, 0.2f);
        Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);
        Transform3D t;
        objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.4);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);
        Background bg = new Background(bgColor);
        bg.setApplicationBounds(bounds);
        objScale.addChild(bg);
        Material m = new Material(objColor, eColor, objColor, sColor, 100.0f);
        Appearance a = new Appearance();
        m.setLightingEnable(true);
        a.setMaterial(m);
        TransformGroup spinTg = new TransformGroup();
        spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        spinTg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        spinTg.addChild(createGeometry("Lucas", a));
        objScale.addChild(spinTg);
        t = new Transform3D();
        Vector3d lPos1 = new Vector3d(0.0, 0.0, 2.0);
        t.set(lPos1);
        TransformGroup l1Trans = new TransformGroup(t);
        l1Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        l1Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        l1Trans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objScale.addChild(l1Trans);
        t = new Transform3D();
        Vector3d lPos2 = new Vector3d(0.25, 0.8, 2.0);
        t.set(lPos2);
        TransformGroup l2Trans = new TransformGroup(t);
        l2Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        l2Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        l2Trans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        t = new Transform3D();
        Vector3d lPos3 = new Vector3d(0.0, 0.0, 2.0);
        t.set(lPos3);
        TransformGroup l3Trans = new TransformGroup(t);
        l3Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        l3Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        l3Trans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        objScale.addChild(l2Trans);
        ColoringAttributes caL1 = new ColoringAttributes();
        ColoringAttributes caL2 = new ColoringAttributes();
        ColoringAttributes caL3 = new ColoringAttributes();
        caL1.setColor(lColor1);
        caL2.setColor(lColor2);
        caL3.setColor(lColor3);
        Appearance appL1 = new Appearance();
        Appearance appL2 = new Appearance();
        Appearance appL3 = new Appearance();
        appL1.setColoringAttributes(caL1);
        appL2.setColoringAttributes(caL2);
        appL3.setColoringAttributes(caL3);
        l1Trans.addChild(new Sphere(0.1f, Sphere.GENERATE_NORMALS, 15, appL1));
        l2Trans.addChild(new Sphere(0.025f, Sphere.GENERATE_NORMALS, 15, appL2));
        l3Trans.addChild(new Sphere(0.05f, Sphere.GENERATE_NORMALS, 15, appL3));
        AmbientLight aLgt = new AmbientLight(alColor);
        Light lgt1;
        Light lgt2;
        Light lgt3;
        Point3f lPoint = new Point3f(0.0f, 0.0f, 0.0f);
        Point3f atten = new Point3f(1.0f, 0.0f, 0.0f);
        lgt1 = new PointLight(lColor1, lPoint, atten);
        lgt2 = new PointLight(lColor2, lPoint, atten);
        lgt3 = new PointLight(lColor3, lPoint, atten);
        aLgt.setInfluencingBounds(bounds);
        lgt1.setInfluencingBounds(bounds);
        lgt2.setInfluencingBounds(bounds);
        lgt3.setInfluencingBounds(bounds);
        objScale.addChild(aLgt);
        l1Trans.addChild(lgt1);
        l2Trans.addChild(lgt2);
        l3Trans.addChild(lgt3);
        return objRoot;
    }

    public BranchGroup createSceneGraph(Canvas3D canvas) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        BranchGroup objRoot = createObjRoot(bounds);
        createBehavior(objRoot, canvas, bounds);
        objRoot.compile();
        return objRoot;
    }

    public void createBehavior(BranchGroup objRoot, Canvas3D canvas, BoundingSphere bounds) {
        PickRotateBehavior behavior1 = new PickRotateBehavior(objRoot, canvas, bounds);
        behavior1.setMode(PickTool.BOUNDS);
        objRoot.addChild(behavior1);
        PickZoomBehavior behavior2 = new PickZoomBehavior(objRoot, canvas, bounds);
        behavior2.setMode(PickTool.BOUNDS);
        objRoot.addChild(behavior2);
        PickTranslateBehavior behavior3 = new PickTranslateBehavior(objRoot, canvas, bounds);
        behavior3.setMode(PickTool.BOUNDS);
        objRoot.addChild(behavior3);
    }

    /**
 * @param string
 * @return
 */
    public Node createGeometry(String string, Appearance a) {
        Font3D f3d = new Font3D(new Font("TestFont", Font.PLAIN, 1), new FontExtrusion());
        Text3D txt = new Text3D(f3d, new String(string), new Point3f(-2.0f, 0.0f, 0.0f));
        Shape3D s3D = new Shape3D();
        s3D.setGeometry(txt);
        s3D.setAppearance(a);
        return s3D;
    }

    public PickText3DBounds() {
    }

    public void init() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        u = new SimpleUniverse(c);
        BranchGroup scene = createSceneGraph(c);
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);
    }

    public void destroy() {
        u.cleanup();
    }

    public static void main(String[] args) {
        new MainFrame(new PickText3DBounds(), 700, 700);
    }
}
