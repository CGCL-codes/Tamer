package org.jdesktop.j3d.examples.distort_glyph;

import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import javax.media.j3d.*;
import javax.vecmath.*;
import org.jdesktop.j3d.examples.Resources;

public class DistortGlyphTest extends javax.swing.JFrame {

    private SimpleUniverse univ = null;

    private BranchGroup scene = null;

    private static GraphicsConfiguration getGraphicsConfig() {
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        template.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
        GraphicsConfiguration gcfg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(template);
        return gcfg;
    }

    private void setupLights(BranchGroup root) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        AmbientLight lightAmbient = new AmbientLight(new Color3f(0.37f, 0.37f, 0.37f));
        lightAmbient.setInfluencingBounds(bounds);
        root.addChild(lightAmbient);
        Vector3f lightDirection1 = new Vector3f(0.0f, 0.0f, -1.0f);
        DirectionalLight lightDirectional1 = new DirectionalLight(new Color3f(1.00f, 0.10f, 0.00f), lightDirection1);
        lightDirectional1.setInfluencingBounds(bounds);
        lightDirectional1.setCapability(Light.ALLOW_STATE_WRITE);
        root.addChild(lightDirectional1);
        Point3f lightPos1 = new Point3f(-4.0f, 8.0f, 16.0f);
        Point3f lightAttenuation1 = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight pointLight1 = new PointLight(new Color3f(0.37f, 1.00f, 0.37f), lightPos1, lightAttenuation1);
        pointLight1.setInfluencingBounds(bounds);
        root.addChild(pointLight1);
        Point3f lightPos2 = new Point3f(-16.0f, 8.0f, 4.0f);
        Point3f lightAttenuation2 = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight pointLight2 = new PointLight(new Color3f(0.37f, 0.37f, 1.00f), lightPos2, lightAttenuation2);
        pointLight2.setInfluencingBounds(bounds);
        root.addChild(pointLight2);
    }

    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        setupLights(objRoot);
        TransformGroup objTransform = new TransformGroup();
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRoot.addChild(objTransform);
        Appearance app = new Appearance();
        Color3f objColor = new Color3f(1.0f, 0.7f, 0.8f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        app.setMaterial(new Material(objColor, black, objColor, black, 80.0f));
        Texture txtr = new TextureLoader(Resources.getResource("resources/images/gold.jpg"), this).getTexture();
        app.setTexture(txtr);
        TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.SPHERE_MAP, TexCoordGeneration.TEXTURE_COORDINATE_2);
        app.setTexCoordGeneration(tcg);
        java.awt.geom.GeneralPath gp = new java.awt.geom.GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(.01f, .01f);
        gp.lineTo(.2f, .01f);
        gp.lineTo(.21f, 0f);
        FontExtrusion fontEx = new FontExtrusion(gp);
        Font fnt = new Font("dialog", Font.BOLD, 1);
        Font3D f3d = new Font3D(fnt, .001, fontEx);
        GeometryArray geom = f3d.getGlyphGeometry('A');
        Shape3D shape = new Shape3D(geom, app);
        objTransform.addChild(shape);
        DistortBehavior eb = new DistortBehavior(shape, 1000, 1000);
        eb.setSchedulingBounds(new BoundingSphere());
        objTransform.addChild(eb);
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objTransform);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);
        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(objTransform);
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseTranslate);
        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(objTransform);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);
        objRoot.compile();
        return objRoot;
    }

    private Canvas3D createUniverse() {
        Canvas3D c = new Canvas3D(getGraphicsConfig());
        univ = new SimpleUniverse(c);
        univ.getViewingPlatform().setNominalViewingTransform();
        univ.getViewer().getView().setMinimumFrameCycleTime(5);
        return c;
    }

    /**
     * Creates new form DistortGlyphTest2
     */
    public DistortGlyphTest() {
        initComponents();
        Canvas3D c = createUniverse();
        drawingPanel.add(c, java.awt.BorderLayout.CENTER);
        scene = createSceneGraph();
        univ.addBranchGraph(scene);
    }

    private void initComponents() {
        drawingPanel = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DistortGlyphTest");
        drawingPanel.setLayout(new java.awt.BorderLayout());
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
                new DistortGlyphTest().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel drawingPanel;
}
