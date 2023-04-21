package org.jdesktop.j3d.examples.background;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import org.jdesktop.j3d.examples.Resources;

public class BackgroundGeometry extends javax.swing.JFrame {

    private SimpleUniverse univ = null;

    private BranchGroup scene = null;

    private java.net.URL bgImage = null;

    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.4);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);
        TransformGroup objTrans = new TransformGroup();
        objScale.addChild(objTrans);
        Background bg = new Background();
        bg.setApplicationBounds(bounds);
        BranchGroup backGeoBranch = new BranchGroup();
        Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD | Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_TEXTURE_COORDS_Y_UP, 45);
        Appearance backgroundApp = sphereObj.getAppearance();
        backGeoBranch.addChild(sphereObj);
        bg.setGeometry(backGeoBranch);
        objTrans.addChild(bg);
        TextureLoader tex = new TextureLoader(bgImage, new String("RGB"), TextureLoader.BY_REFERENCE | TextureLoader.Y_UP, this);
        if (tex != null) backgroundApp.setTexture(tex.getTexture());
        Vector3f tranlation = new Vector3f(2.0f, 0.0f, 0.0f);
        Transform3D modelTransform = new Transform3D();
        Transform3D tmpTransform = new Transform3D();
        double angleInc = Math.PI / 8.0;
        double angle = 0.0;
        int numBoxes = 16;
        float scaleX[] = { 0.1f, 0.2f, 0.2f, 0.3f, 0.2f, 0.1f, 0.2f, 0.3f, 0.1f, 0.3f, 0.2f, 0.3f, 0.1f, 0.3f, 0.2f, 0.3f };
        float scaleY[] = { 0.3f, 0.4f, 0.3f, 0.4f, 0.3f, 0.4f, 0.3f, 0.4f, 0.3f, 0.3f, 0.3f, 0.3f, 0.3f, 0.3f, 0.3f, 0.4f };
        float scaleZ[] = { 0.3f, 0.2f, 0.1f, 0.1f, 0.3f, 0.2f, 0.1f, 0.3f, 0.3f, 0.2f, 0.1f, 0.3f, 0.3f, 0.2f, 0.1f, 0.2f };
        Appearance a1 = new Appearance();
        Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f sColor = new Color3f(0.5f, 0.5f, 1.0f);
        Color3f oColor = new Color3f(0.5f, 0.5f, 0.3f);
        Material m = new Material(oColor, eColor, oColor, sColor, 100.0f);
        m.setLightingEnable(true);
        a1.setMaterial(m);
        for (int i = 0; i < numBoxes; i++, angle += angleInc) {
            modelTransform.rotY(angle);
            tmpTransform.set(tranlation);
            modelTransform.mul(tmpTransform);
            TransformGroup tgroup = new TransformGroup(modelTransform);
            objTrans.addChild(tgroup);
            tgroup.addChild(new Box(scaleX[i], scaleY[i], scaleZ[i], Box.GENERATE_NORMALS, a1));
        }
        Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
        Color3f lColor2 = new Color3f(0.2f, 0.2f, 0.1f);
        Vector3f lDir1 = new Vector3f(-1.0f, -1.0f, -1.0f);
        Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
        DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
        DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
        lgt1.setInfluencingBounds(bounds);
        lgt2.setInfluencingBounds(bounds);
        objScale.addChild(lgt1);
        objScale.addChild(lgt2);
        return objRoot;
    }

    private Canvas3D createUniverse() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        univ = new SimpleUniverse(c);
        univ.getViewingPlatform().setNominalViewingTransform();
        univ.getViewer().getView().setMinimumFrameCycleTime(5);
        TransformGroup viewTrans = univ.getViewingPlatform().getViewPlatformTransform();
        MouseRotate behavior1 = new MouseRotate(viewTrans);
        scene.addChild(behavior1);
        behavior1.setSchedulingBounds(bounds);
        MouseZoom behavior2 = new MouseZoom(viewTrans);
        scene.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);
        MouseTranslate behavior3 = new MouseTranslate(viewTrans);
        scene.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);
        return c;
    }

    /**
     * Creates new form BackgroundGeometry
     */
    public BackgroundGeometry() {
        if (bgImage == null) {
            bgImage = Resources.getResource("resources/images/bg.jpg");
            if (bgImage == null) {
                System.err.println("resources/images/bg.jpg not found");
                System.exit(1);
            }
        }
        initComponents();
        scene = createSceneGraph();
        Canvas3D c = createUniverse();
        drawingPanel.add(c, java.awt.BorderLayout.CENTER);
        scene.compile();
        univ.addBranchGraph(scene);
    }

    private void initComponents() {
        drawingPanel = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BackgroundGeometry");
        drawingPanel.setLayout(new java.awt.BorderLayout());
        drawingPanel.setOpaque(false);
        drawingPanel.setPreferredSize(new java.awt.Dimension(700, 700));
        getContentPane().add(drawingPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new BackgroundGeometry().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel drawingPanel;
}
