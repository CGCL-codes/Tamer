package com.yosimite;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;

/**
   Generates a scene graph with the cylinders that 
   make up the axis, and 3 cones. 
   Add one Cone with rotate before translate, and 
   another that has translate before rotate.
   For reference, throw in a cone without translate
   or rotate

*/
public class TransformOrder extends Applet {

    public static final int X = 1;

    public static final int Y = 2;

    public static final int Z = 3;

    public static final int ROTATE_TOP = 4;

    public static final int TRANSLATE_TOP = 5;

    public static final int NO_TRANSFORM = 6;

    private SimpleUniverse universe;

    private BranchGroup scene;

    private Canvas3D canvas;

    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);

    private Appearance red = new Appearance();

    private Appearance yellow = new Appearance();

    private Appearance purple = new Appearance();

    Transform3D rotate = new Transform3D();

    Transform3D translate = new Transform3D();

    public void setupView() {
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        viewingPlatform.setViewPlatformBehavior(orbit);
    }

    Group rotateOnTop() {
        Group root = new Group();
        TransformGroup objRotate = new TransformGroup(rotate);
        TransformGroup objTranslate = new TransformGroup(translate);
        Cone redCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, red);
        root.addChild(objRotate);
        objRotate.addChild(objTranslate);
        objTranslate.addChild(redCone);
        return root;
    }

    Group translateOnTop() {
        Group root = new Group();
        TransformGroup objRotate = new TransformGroup(rotate);
        TransformGroup objTranslate = new TransformGroup(translate);
        Cone yellowCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, yellow);
        root.addChild(objTranslate);
        objTranslate.addChild(objRotate);
        objRotate.addChild(yellowCone);
        return root;
    }

    Group noTransform() {
        Cone purpleCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, purple);
        return purpleCone;
    }

    /** Represent an axis using cylinder Primitive. Cylinder is 
	aligned with Y axis, so we have to rotate it when 
	creating X and Z axis
    */
    public TransformGroup createAxis(int type) {
        Appearance appearance = new Appearance();
        Material lightingProps = new Material();
        Transform3D t = new Transform3D();
        switch(type) {
            case Z:
                t.rotX(Math.toRadians(90.0));
                lightingProps.setAmbientColor(1.0f, 0.0f, 0.0f);
                break;
            case Y:
                lightingProps.setAmbientColor(0.0f, 1.0f, 0.0f);
                break;
            case X:
                t.rotZ(Math.toRadians(90.0));
                lightingProps.setAmbientColor(0.0f, 0.0f, 1.0f);
                break;
            default:
                break;
        }
        appearance.setMaterial(lightingProps);
        TransformGroup objTrans = new TransformGroup(t);
        objTrans.addChild(new Cylinder(.03f, 2.5f, Primitive.GENERATE_NORMALS, appearance));
        return objTrans;
    }

    /** Create X, Y , and Z axis, and 3 cones. Throws in 
	some quick lighting to help viewing the scene
    */
    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        rotate.rotX(Math.toRadians(45.0));
        translate.setTranslation(new Vector3f(0.0f, 2.0f, 1.0f));
        Material redProps = new Material();
        redProps.setAmbientColor(1.0f, 0.0f, 0.0f);
        red.setMaterial(redProps);
        Material yellowProps = new Material();
        yellowProps.setAmbientColor(1.0f, 1.0f, 0.0f);
        yellow.setMaterial(yellowProps);
        Material purpleProps = new Material();
        purpleProps.setAmbientColor(0.8f, 0.0f, 0.8f);
        purple.setMaterial(purpleProps);
        objRoot.addChild(createAxis(X));
        objRoot.addChild(createAxis(Y));
        objRoot.addChild(createAxis(Z));
        objRoot.addChild(noTransform());
        objRoot.addChild(rotateOnTop());
        objRoot.addChild(translateOnTop());
        Color3f lightColor = new Color3f(.3f, .3f, .3f);
        AmbientLight ambientLight = new AmbientLight(lightColor);
        ambientLight.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLight);
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(lightColor);
        directionalLight.setInfluencingBounds(bounds);
        objRoot.addChild(directionalLight);
        return objRoot;
    }

    public TransformOrder() {
    }

    public void init() {
        BranchGroup scene = createSceneGraph();
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        add("Center", canvas);
        universe = new SimpleUniverse(canvas);
        setupView();
        universe.addBranchGraph(scene);
    }

    public void destroy() {
        universe.removeAllLocales();
    }

    public static void main(String[] args) {
        new MainFrame(new TransformOrder(), 256, 256);
    }
}
