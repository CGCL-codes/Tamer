package com.pezad.entrippy.demos.Life3D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * 
 * 
 * WrapLife3D.java
 * 
 * @date Jan 10, 2008
 * @author Adam
 * 
 * Copyright 2007 Shayne Riley & Adam Taylor This program is distributed under
 * the GNU General Public License.
 */
public class WrapLife3D extends JPanel {

    private static final int BOUNDSIZE = 100;

    private static final Point3d USERPOSN = new Point3d(-2, 5, 10);

    private static final int TIME_DELAY = 50;

    private final SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds;

    private final Life3D topLevel;

    private final LifeProperties lifeProps;

    /**
     * @param top
     * @param lps
     */
    public WrapLife3D(Life3D top, LifeProperties lps) {
        topLevel = top;
        lifeProps = lps;
        setLayout(new BorderLayout());
        if (lifeProps.isFullScreen()) setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize()); else {
            int width = lifeProps.getWidth();
            int height = lifeProps.getHeight();
            setPreferredSize(new Dimension(width, height));
        }
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
        canvas3D.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
                    topLevel.dispose();
                    System.exit(0);
                }
            }
        });
        su = new SimpleUniverse(canvas3D);
        createSceneGraph();
        initUserPosition();
        orbitControls(canvas3D);
        View view = su.getViewer().getView();
        view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
        su.addBranchGraph(sceneBG);
    }

    private void createSceneGraph() {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        lightScene();
        addBackground();
        addGrid();
        sceneBG.compile();
    }

    private void lightScene() {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLightNode);
        Vector3f light1Direction = new Vector3f(-1.0f, 1.0f, -1.0f);
        Vector3f light2Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);
        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);
    }

    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        int bgColour = lifeProps.getBGColour();
        if (bgColour == LifeProperties.BLUE) back.setColor(0.17f, 0.65f, 0.92f); else if (bgColour == LifeProperties.GREEN) back.setColor(0.5f, 1.0f, 0.5f); else if (bgColour == LifeProperties.WHITE) back.setColor(1.0f, 1.0f, 0.8f);
        sceneBG.addChild(back);
    }

    private void orbitControls(Canvas3D c) {
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    private void initUserPosition() {
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);
        t3d.lookAt(USERPOSN, new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
        t3d.invert();
        steerTG.setTransform(t3d);
    }

    private void addGrid() {
        CellsGrid cellsGrid = new CellsGrid(lifeProps);
        sceneBG.addChild(cellsGrid.getBaseTG());
        TimeBehavior tb = new TimeBehavior(TIME_DELAY, cellsGrid);
        tb.setSchedulingBounds(bounds);
        sceneBG.addChild(tb);
    }
}
