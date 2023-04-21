package org.chernovia.lib.graphics.lib3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public abstract class SimVerse extends Applet {

    private static final long serialVersionUID = -8499526572120933580L;

    public static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

    public static final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);

    public static final Color3f RED = new Color3f(1.0f, 0.0f, 0.0f);

    public static final Color3f GREEN = new Color3f(0.0f, 1.0f, 0.0f);

    public static final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);

    Matrix3d tmpmat = new Matrix3d();

    Vector3d tmpvec = new Vector3d();

    Transform3D tmptrans = new Transform3D();

    private static boolean IS_APPLET = true, LOADED = false;

    private static MainFrame MF = null;

    private SimpleUniverse SU = null;

    private Canvas3D VIEW_CAN = null;

    private TextField TEXT_BOX = null;

    private double VIEWSIZE = 25.0, VIEW_DIST_FACTOR = 2.345;

    public boolean isApplet() {
        return IS_APPLET;
    }

    public void setApplet(boolean isApp) {
        IS_APPLET = isApp;
    }

    public boolean isLoaded() {
        return LOADED;
    }

    public void setMainFrame(MainFrame mf) {
        MF = mf;
    }

    public MainFrame getMainFrame() {
        return MF;
    }

    public double getViewSize() {
        return VIEWSIZE;
    }

    public void setViewSize(double vs) {
        VIEWSIZE = vs;
        reset();
    }

    public double getViewDistFactor() {
        return VIEW_DIST_FACTOR;
    }

    public void setViewDistFactor(double vdf) {
        VIEW_DIST_FACTOR = vdf;
        reset();
    }

    public SimpleUniverse getUniverse() {
        return SU;
    }

    public abstract void handleKey(KeyEvent e);

    public abstract BranchGroup createSceneGraph();

    public void setUpperTxt(String txt) {
        TEXT_BOX.setText(txt);
    }

    @Override
    public void init() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        VIEW_CAN = new Canvas3D(config);
        add("Center", VIEW_CAN);
        TEXT_BOX = new TextField();
        add("North", TEXT_BOX);
        SU = new SimpleUniverse(VIEW_CAN);
        BranchGroup scene = createSceneGraph();
        Canvas3D[] canvases = SU.getViewer().getCanvas3Ds();
        class KeyCmdListener extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
            }
        }
        KeyCmdListener KListener = new KeyCmdListener();
        for (int i = 0; i < canvases.length; i++) canvases[i].addKeyListener(KListener);
        scene.compile();
        SU.addBranchGraph(scene);
        resetView();
        LOADED = true;
    }

    public void reset() {
        LOADED = false;
        if (SU == null) return;
        SU.removeAllLocales();
        SU.cleanup();
        SU = new SimpleUniverse(VIEW_CAN);
        BranchGroup BG = createSceneGraph();
        BG.compile();
        SU.addBranchGraph(BG);
        System.gc();
        resetView();
        LOADED = true;
    }

    public void move(Vector3d v, TransformGroup TG) {
        if (TG == null) return;
        Vector3d dirvec = new Vector3d(v);
        TG.getTransform(tmptrans);
        tmptrans.transform(dirvec);
        tmptrans.get(tmpmat, tmpvec);
        tmpvec.add(dirvec);
        tmptrans.set(tmpmat, tmpvec, 1);
        TG.setTransform(tmptrans);
    }

    public void moveView(Vector3d v) {
        move(v, getViewTransGrp(0));
    }

    public void slew(Vector3d v, TransformGroup TG) {
        if (TG == null) return;
        TG.getTransform(tmptrans);
        tmptrans.get(tmpmat, tmpvec);
        tmpvec.add(v);
        tmptrans.set(tmpmat, tmpvec, 1);
        TG.setTransform(tmptrans);
    }

    public void slewView(Vector3d v) {
        slew(v, getViewTransGrp(0));
    }

    public void rotate(Matrix3d rotmat, TransformGroup TG) {
        if (TG == null) return;
        TG.getTransform(tmptrans);
        tmptrans.get(tmpmat, tmpvec);
        tmpmat.mul(rotmat);
        tmptrans.set(tmpmat, tmpvec, 1);
        TG.setTransform(tmptrans);
    }

    public void rotateView(Matrix3d rotmat) {
        rotate(rotmat, getViewTransGrp(0));
    }

    public void clearRotation(TransformGroup TG) {
        if (TG == null) return;
        TG.getTransform(tmptrans);
        tmpmat.setIdentity();
        tmptrans.get(tmpvec);
        tmptrans.set(tmpmat, tmpvec, 1);
        TG.setTransform(tmptrans);
    }

    public void clearViewRotation() {
        clearRotation(getViewTransGrp(0));
    }

    public void setPos(Vector3d pos, TransformGroup TG) {
        TG.getTransform(tmptrans);
        tmptrans.set(pos);
        TG.setTransform(tmptrans);
    }

    public void setViewPos(Vector3d pos) {
        setPos(pos, getViewTransGrp(0));
    }

    public void resetView() {
        setViewPos(new Vector3d(0, 0, VIEWSIZE * VIEW_DIST_FACTOR));
    }

    public TransformGroup getViewTransGrp(int group) {
        try {
            return SU.getViewingPlatform().getMultiTransformGroup().getTransformGroup(group);
        } catch (NullPointerException augh) {
            return null;
        }
    }

    public static double rndDouble(double range) {
        return -range + (Math.random() * (range * 2));
    }

    public static Point3d rndPt3d(double range) {
        double x, y, z;
        x = rndDouble(range);
        y = rndDouble(range);
        z = rndDouble(range);
        return new Point3d(x, y, z);
    }

    public static Vector3d rndVec3d(double range) {
        double x, y, z;
        x = rndDouble(range);
        y = rndDouble(range);
        z = rndDouble(range);
        return new Vector3d(x, y, z);
    }

    public static Color3f rndCol() {
        float x = (float) (Math.random() * 1);
        float y = (float) (Math.random() * 1);
        float z = (float) (Math.random() * 1);
        return new Color3f(x, y, z);
    }

    public Texture getTexture(String texfile) {
        java.net.URL TexImage = null;
        try {
            TexImage = new java.net.URL("file:" + texfile);
        } catch (java.net.MalformedURLException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        TextureLoader tex = new TextureLoader(TexImage, this);
        return tex.getTexture();
    }

    public Texture getTexture(URL TexImage) {
        TextureLoader tex = new TextureLoader(TexImage, this);
        return tex.getTexture();
    }

    public Texture rndTex(String texdir) {
        File F = null;
        if (IS_APPLET) return null; else if (IS_APPLET) {
            URL URLdir = null;
            try {
                URLdir = new URL(getCodeBase(), texdir);
            } catch (MalformedURLException augh) {
                System.out.println("Bad URL: " + getCodeBase().toString() + texdir);
                return null;
            }
            F = new File(URLdir.getFile());
        } else {
            F = new File(texdir);
        }
        if (F.isDirectory()) {
            File[] texfiles = F.listFiles();
            int r = (int) (Math.random() * texfiles.length);
            if (IS_APPLET) {
                try {
                    return getTexture(texfiles[r].toURI().toURL());
                } catch (MalformedURLException augh) {
                    System.out.println("Bad URL file: " + texfiles[r].toString());
                    return null;
                }
            } else return getTexture(texfiles[r].getAbsolutePath());
        } else return null;
    }
}
