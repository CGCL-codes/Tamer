package org.jdesktop.j3d.examples.text2d;

import java.applet.Applet;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Text2DTest extends Applet {

    private SimpleUniverse u = null;

    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.4);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        TransformGroup textTranslationGroup;
        Transform3D textTranslation;
        float yPos = -.5f;
        Shape3D textObject = new Text2D("Rotating Yellow Text", new Color3f(1f, 1f, 0f), "Serif", 60, Font.BOLD);
        Appearance app = textObject.getAppearance();
        PolygonAttributes pa = app.getPolygonAttributes();
        if (pa == null) pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        if (app.getPolygonAttributes() == null) app.setPolygonAttributes(pa);
        objTrans.addChild(textObject);
        textTranslation = new Transform3D();
        textTranslation.setTranslation(new Vector3f(0f, yPos, 0f));
        textTranslationGroup = new TransformGroup(textTranslation);
        textTranslationGroup.addChild(objTrans);
        objScale.addChild(textTranslationGroup);
        yPos += .5f;
        textObject = new Text2D("Blue 40point Text", new Color3f(0f, 0f, 1f), "Serif", 40, Font.BOLD);
        textTranslation = new Transform3D();
        textTranslation.setTranslation(new Vector3f(0f, yPos, 0f));
        textTranslationGroup = new TransformGroup(textTranslation);
        textTranslationGroup.addChild(textObject);
        objScale.addChild(textTranslationGroup);
        yPos += .5f;
        textObject = new Text2D("Green Italic Text", new Color3f(0f, 1f, 0f), "Serif", 70, Font.ITALIC);
        textTranslation = new Transform3D();
        textTranslation.setTranslation(new Vector3f(0f, yPos, 0f));
        textTranslationGroup = new TransformGroup(textTranslation);
        textTranslationGroup.addChild(textObject);
        objScale.addChild(textTranslationGroup);
        yPos += .5f;
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 4000, 0, 0, 0, 0, 0);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(bounds);
        objTrans.addChild(rotator);
        return objRoot;
    }

    public Text2DTest() {
    }

    public void init() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        BranchGroup scene = createSceneGraph();
        u = new SimpleUniverse(c);
        MoverBehavior navigator = new MoverBehavior(u.getViewingPlatform().getViewPlatformTransform());
        scene.addChild(navigator);
        scene.compile();
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);
    }

    public void destroy() {
        u.cleanup();
    }

    public static void main(String[] args) {
        new MainFrame(new Text2DTest(), 256, 256);
    }
}
