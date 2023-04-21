package org.jdesktop.j3d.examples.jcanvas3d;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JInternalFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * This is a JInternalFrame holding an universe, which can be configured to
 * be interactive -that is, where user can interact with object- or automatic
 * -where the object spins only-. When in automatic mode, spinning speed is
 * changed so that they look less the same. Changing the spinning start angle
 * helps unsynchronizing the rotations too.
 *
 * @author pepe
 */
public class JInternalWorld extends JInternalFrame {

    /** DOCUMENT ME! */
    private Component comp;

    /**
     * Creates a new JInternalWorld object.
     *
     * @param isInteractive tells the world to be constructed as interactive
     * @param isDelayed tells the rotator to start at a random alpha.
     */
    public JInternalWorld(boolean isInteractive, boolean isDelayed, boolean isRandom) {
        super();
        setSize(256, 256);
        setClosable(true);
        JCanvas3D canvas = new JCanvas3D(new GraphicsConfigTemplate3D());
        if (true == isDelayed) {
            canvas.setResizeMode(canvas.RESIZE_DELAYED);
        }
        comp = canvas;
        Dimension dim = new Dimension(256, 256);
        comp.setPreferredSize(dim);
        comp.setSize(dim);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(comp, BorderLayout.CENTER);
        pack();
        BranchGroup scene = createSceneGraph(isInteractive, isRandom);
        SimpleUniverse universe = new SimpleUniverse(canvas.getOffscreenCanvas3D());
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.getViewer().getView().setMinimumFrameCycleTime(30);
        universe.addBranchGraph(scene);
    }

    /**
     * Creates the world. Only exists to cleanup the source a bit
     *
     * @param isInteractive tells the world to be constructed as interactive
     * @param isDelayed tells the rotator to start at a random alpha.
     *
     * @return a global branchgroup containing the world, as desired.
     */
    private BranchGroup createSceneGraph(boolean isInteractive, boolean isRandom) {
        BranchGroup objRoot = new BranchGroup();
        TransformGroup objTrans = new TransformGroup();
        Transform3D t3dTrans = new Transform3D();
        t3dTrans.setTranslation(new Vector3d(0, 0, -1));
        objTrans.setTransform(t3dTrans);
        TransformGroup objRot = new TransformGroup();
        objRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRoot.addChild(objTrans);
        objTrans.addChild(objRot);
        Font3D f3d = new Font3D(new Font("dialog", Font.PLAIN, 1), new FontExtrusion());
        Text3D text = new Text3D(f3d, "JCanvas3D", new Point3f(-2.3f, -0.5f, 0.f));
        Shape3D sh = new Shape3D();
        Appearance app = new Appearance();
        Material mm = new Material();
        mm.setLightingEnable(true);
        app.setMaterial(mm);
        sh.setGeometry(text);
        sh.setAppearance(app);
        objRot.addChild(sh);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f ambientColor = new Color3f(0.3f, 0.3f, 0.3f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);
        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);
        if (true == isInteractive) {
            MouseRotate mr = new MouseRotate(comp, objRot);
            mr.setSchedulingBounds(bounds);
            mr.setSchedulingInterval(1);
            objRoot.addChild(mr);
        } else {
            Transform3D yAxis = new Transform3D();
            Alpha rotationAlpha = null;
            if (true == isRandom) {
                int duration = Math.max(2000, (int) (Math.random() * 8000.));
                rotationAlpha = new Alpha(-1, (int) ((double) duration * Math.random()), 0, duration, 0, 0);
            } else {
                rotationAlpha = new Alpha(-1, 4000);
            }
            RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objRot, yAxis, 0.0f, (float) Math.PI * 2.0f);
            rotator.setSchedulingBounds(bounds);
            objRoot.addChild(rotator);
        }
        return objRoot;
    }
}
