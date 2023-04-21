package net.confex.java3d;

import javax.media.j3d.Background;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Link;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class TreeScene {

    public TreeScene() {
    }

    Shape3D createTree() {
        int counts[] = { 19 };
        TriangleFanArray treeGeom = new TriangleFanArray(19, GeometryArray.COORDINATES | GeometryArray.COLOR_3, counts);
        treeGeom.setCoordinate(0, new Point3f(0.00f, 0.60f, 0.0f));
        treeGeom.setCoordinate(1, new Point3f(-0.05f, 0.00f, 0.0f));
        treeGeom.setCoordinate(2, new Point3f(0.05f, 0.00f, 0.0f));
        treeGeom.setCoordinate(3, new Point3f(0.05f, 0.25f, 0.0f));
        treeGeom.setCoordinate(4, new Point3f(0.15f, 0.30f, 0.0f));
        treeGeom.setCoordinate(5, new Point3f(0.22f, 0.25f, 0.0f));
        treeGeom.setCoordinate(6, new Point3f(0.18f, 0.40f, 0.0f));
        treeGeom.setCoordinate(7, new Point3f(0.20f, 0.55f, 0.0f));
        treeGeom.setCoordinate(8, new Point3f(0.15f, 0.65f, 0.0f));
        treeGeom.setCoordinate(9, new Point3f(0.14f, 0.80f, 0.0f));
        treeGeom.setCoordinate(10, new Point3f(0.08f, 0.95f, 0.0f));
        treeGeom.setCoordinate(11, new Point3f(0.00f, 1.00f, 0.0f));
        treeGeom.setCoordinate(12, new Point3f(-0.20f, 0.85f, 0.0f));
        treeGeom.setCoordinate(13, new Point3f(-0.22f, 0.70f, 0.0f));
        treeGeom.setCoordinate(14, new Point3f(-0.30f, 0.60f, 0.0f));
        treeGeom.setCoordinate(15, new Point3f(-0.35f, 0.45f, 0.0f));
        treeGeom.setCoordinate(16, new Point3f(-0.25f, 0.43f, 0.0f));
        treeGeom.setCoordinate(17, new Point3f(-0.30f, 0.25f, 0.0f));
        treeGeom.setCoordinate(18, new Point3f(-0.02f, 0.24f, 0.0f));
        Color3f c = new Color3f(0.1f, 0.9f, 0.0f);
        for (int i = 0; i < 19; i++) treeGeom.setColor(i, c);
        c.set(0.5f, 0.5f, 0.3f);
        treeGeom.setColor(1, c);
        treeGeom.setColor(2, c);
        treeGeom.setColor(18, c);
        Shape3D tree = new Shape3D(treeGeom);
        return tree;
    }

    Shape3D createLand() {
        LineArray landGeom = new LineArray(44, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        float l = -50.0f;
        for (int c = 0; c < 44; c += 4) {
            landGeom.setCoordinate(c + 0, new Point3f(-50.0f, 0.0f, l));
            landGeom.setCoordinate(c + 1, new Point3f(50.0f, 0.0f, l));
            landGeom.setCoordinate(c + 2, new Point3f(l, 0.0f, -50.0f));
            landGeom.setCoordinate(c + 3, new Point3f(l, 0.0f, 50.0f));
            l += 10.0f;
        }
        Color3f c = new Color3f(0.1f, 0.8f, 0.1f);
        for (int i = 0; i < 44; i++) landGeom.setColor(i, c);
        return new Shape3D(landGeom);
    }

    public BranchGroup createSceneGraph(SimpleUniverse su) {
        TransformGroup vpTrans = null;
        BranchGroup objRoot = new BranchGroup();
        Vector3f translate = new Vector3f();
        Transform3D T3D = new Transform3D();
        TransformGroup TGT = null;
        TransformGroup TGR = null;
        Billboard billboard = null;
        BoundingSphere bSphere = new BoundingSphere();
        objRoot.addChild(createLand());
        SharedGroup share = new SharedGroup();
        share.addChild(createTree());
        float[][] position = { { 0.0f, 0.0f, -3.0f }, { 6.0f, 0.0f, 0.0f }, { 6.0f, 0.0f, 6.0f }, { 3.0f, 0.0f, -10.0f }, { 13.0f, 0.0f, -30.0f }, { -13.0f, 0.0f, 30.0f }, { -13.0f, 0.0f, 23.0f }, { 13.0f, 0.0f, 3.0f } };
        for (int i = 0; i < position.length; i++) {
            translate.set(position[i]);
            T3D.setTranslation(translate);
            TGT = new TransformGroup(T3D);
            TGR = new TransformGroup();
            TGR.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            billboard = new Billboard(TGR);
            billboard.setSchedulingBounds(bSphere);
            objRoot.addChild(TGT);
            objRoot.addChild(billboard);
            TGT.addChild(TGR);
            TGR.addChild(new Link(share));
        }
        vpTrans = su.getViewingPlatform().getViewPlatformTransform();
        translate.set(0.0f, 0.3f, 0.0f);
        T3D.setTranslation(translate);
        vpTrans.setTransform(T3D);
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        objRoot.addChild(keyNavBeh);
        Background background = new Background();
        background.setColor(0.3f, 0.3f, 1.0f);
        background.setApplicationBounds(new BoundingSphere());
        objRoot.addChild(background);
        objRoot.compile();
        return objRoot;
    }
}
