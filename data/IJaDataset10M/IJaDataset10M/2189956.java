package org.chernovia.sims.wondrous;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.chessworks.common.javatools.io.FileHelper;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.MultiTransformGroup;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Class Wonder3DApp
 * 
 * @author John Chernoff
 * @version .0000000000000001
 */
public class Wonder3DApp extends Applet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    static String WELCOME_MSG = "Welcome to Wonder3D, by John Chernoff (jachern@yahoo.com).  " + FileHelper.EOL + "Hit '?' for help." + FileHelper.EOL + "(note: before pressing any keys, first click inside the animation box)" + FileHelper.EOL;

    static final Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

    static final Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    static final Color3f red = new Color3f(1.0f, 0.0f, 0.0f);

    static final Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

    static final Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);

    static final Point3d origin = new Point3d(0, 0, 0);

    static boolean IS_APPLET = true;

    static MainFrame MF;

    SimpleUniverse SU;

    Shape3D WonderShape;

    LineStripArray WonderLines;

    BoundingSphere WonderBounds;

    Color3f[] WonderCols;

    boolean SPOTLIGHT = false;

    int VIEW_DIST = 25;

    public static void main(String[] args) {
        IS_APPLET = false;
        MF = new MainFrame(new Wonder3DApp(), 256, 256);
        MF.setTitle("Wonder3D");
    }

    public Wonder3DApp() {
    }

    public BranchGroup createSceneGraph() {
        System.out.print("Loading scene...");
        BranchGroup objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        objRoot.addChild(objScale);
        WonderShape = new Shape3D(getWonderLines(100, 1000, 150, 1000, 3));
        WonderShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        WonderShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        objScale.addChild(WonderShape);
        WonderBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100);
        AmbientLight AL = new AmbientLight(true, white);
        AL.setInfluencingBounds(WonderBounds);
        objScale.addChild(AL);
        System.out.println(" loaded.");
        return objRoot;
    }

    public LineStripArray getWonderLines(int start, int lines, int limit, int ceiling, double ratio) {
        double XFactor = (1 / (double) lines) * ratio;
        double YFactor = (1 / (double) ceiling) * ratio;
        double ZFactor = (1 / (double) limit) * ratio;
        int[] stripVertexCounts = new int[lines];
        int[][] wonderArray = new int[lines][limit];
        int points = 0;
        for (int n = 0; n < lines; n++) {
            int[] pattern = JWondrous.wondrousnessPattern(n + start, limit, 3);
            stripVertexCounts[n] = pattern.length;
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i] < ceiling) wonderArray[n][i] = pattern[i]; else wonderArray[n][i] = ceiling;
            }
            points += pattern.length;
        }
        WonderLines = new LineStripArray(points, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.COLOR_3, stripVertexCounts);
        WonderLines.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        WonderCols = new Color3f[points];
        int c = 0;
        for (int n = 0; n < lines; n++) for (int i = 0; i < stripVertexCounts[n]; i++) {
            Point3d p = new Point3d(n * XFactor, wonderArray[n][i] * YFactor, i * ZFactor);
            WonderLines.setCoordinate(c, p);
            float r = n / (float) lines;
            float g = i / (float) stripVertexCounts[n];
            float b = wonderArray[n][i] / (float) ceiling;
            WonderCols[c] = new Color3f(r, g, b);
            WonderLines.setNormal(c, new Vector3f(p));
            c++;
        }
        WonderLines.setColors(0, WonderCols);
        return WonderLines;
    }

    @Override
    public void init() {
        System.out.println(WELCOME_MSG);
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        add("Center", c);
        BranchGroup scene = createSceneGraph();
        SU = new SimpleUniverse(c);
        resetView();
        Canvas3D[] canvases = SU.getViewer().getCanvas3Ds();
        class KeyCmdListener extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {
                Matrix3d m = new Matrix3d();
                int c = e.getKeyCode();
                char C = e.getKeyChar();
                if (c == 39) {
                    slewView(new Vector3d(1, 0, 0));
                } else if (c == 37) {
                    slewView(new Vector3d(-1, 0, 0));
                } else if (c == 38) {
                    slewView(new Vector3d(0, 1, 0));
                } else if (c == 40) {
                    slewView(new Vector3d(0, -1, 0));
                } else if (c == 155) {
                    slewView(new Vector3d(0, 0, 1));
                } else if (c == 12) {
                    slewView(new Vector3d(0, 0, -1));
                } else if (c == 33) {
                    m.rotX(.1);
                    rotateView(m);
                } else if (c == 36) {
                    m.rotX(-.1);
                    rotateView(m);
                } else if (c == 35) {
                    m.rotY(.1);
                    rotateView(m);
                } else if (c == 34) {
                    m.rotY(-.1);
                    rotateView(m);
                } else if (C == 'z') {
                    m.rotZ(.1);
                    rotateView(m);
                } else if (C == 'x') {
                    m.rotZ(-.1);
                    rotateView(m);
                }
            }
        }
        KeyCmdListener KListener = new KeyCmdListener();
        for (int i = 0; i < canvases.length; i++) canvases[i].addKeyListener(KListener);
        scene.compile();
        SU.addBranchGraph(scene);
    }

    public void slewView(Vector3d sv) {
        Transform3D T = getViewTrans();
        T.transform(sv);
        Vector3d v = new Vector3d();
        Matrix3d m = new Matrix3d();
        T.get(m, v);
        v.add(sv);
        T.set(m, v, 1);
        setViewTrans(T);
    }

    public void rotateView(Matrix3d rotmat) {
        Transform3D T = getViewTrans();
        Vector3d v = new Vector3d();
        Matrix3d m = new Matrix3d();
        T.get(m, v);
        m.mul(rotmat);
        T.set(m, v, 1);
        setViewTrans(T);
    }

    public Transform3D getViewTrans() {
        Transform3D T = new Transform3D();
        try {
            MultiTransformGroup MTG = SU.getViewingPlatform().getMultiTransformGroup();
            MTG.getTransformGroup(0).getTransform(T);
        } catch (NullPointerException e) {
            return null;
        }
        return T;
    }

    public void setViewTrans(Transform3D T3D) {
        try {
            MultiTransformGroup MTG = SU.getViewingPlatform().getMultiTransformGroup();
            MTG.getTransformGroup(0).setTransform(T3D);
        } catch (NullPointerException ignore) {
        }
    }

    public void resetView() {
        Vector3d mv = new Vector3d(0, 0, VIEW_DIST);
        Transform3D T = getViewTrans();
        Matrix3d m = new Matrix3d();
        m.setIdentity();
        T.set(m, mv, 1);
        setViewTrans(T);
    }
}
