package org.chernovia.net.games.arcade.cubeball.test;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.MultiTransformGroup;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class CubeBallTestApp extends Applet implements Runnable {

    static final long serialVersionUID = 0;

    static final Color3f black = new Color3f(0, 0, 0);

    static final Color3f white = new Color3f(1, 1, 1);

    static final Color3f gray = new Color3f(.5f, .5f, .5f);

    static final Color3f orange = new Color3f(1, .5f, 0);

    static final Color3f red = new Color3f(1, 0, 0);

    static final Color3f green = new Color3f(0, 1, 0);

    static final Color3f blue = new Color3f(0, 0, 1);

    static final Point3d origin = new Point3d(0, 0, 0);

    static final Vector3d upVec = new Vector3d(0, 0, 1);

    static final Color3f[] cageCols = { orange, white, gray, red, green, blue };

    static final int MODE_DIM = 0, MODE_MULTI = 1;

    static final int X_CRISS = 0, X_CROSS = 1, Y_CRISS = 2, Y_CROSS = 3, Z_CRISS = 4, Z_CROSS = 5;

    static final int RIGHT = 0, LEFT = 1, UP = 2, DOWN = 3, NEAR = 4, FAR = 5;

    static int VIEW_BALL = 1, TRACK_BALL = 0;

    static final int MODE_BALL = 0, MODE_CUBE = 1, MODE_TRACK = 2, MODE_TRACK2 = 3, MODE_WEIRD = 4, MAX_MODE = 4;

    static final int[][] SQR_PATTERN = { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } };

    static final int NO_DISC = -1;

    static int DEF_WIDTH = 640, DEF_HEIGHT = 480;

    static boolean IS_APPLET;

    static MainFrame MF;

    SimpleUniverse SU;

    Canvas3D canvas;

    Vector<CubeTestBall> balls;

    BoundingBox bounds;

    Shape3D discoShape;

    QuadArray disco;

    Transform3D view;

    Vector3d viewVec, backViewDist, backViewVec;

    Matrix3d[] rotMats;

    int[][][] discTab;

    double SLEW_SPD = 1, ROT_SPD = .1, SHIFT_SPD = .1;

    double CLIP_FACTOR = 8;

    float INI_SIZE = 64, INI_RADIUS = 2f, INI_SPEED = 2f;

    int INI_BALLS = 8, INI_BARS = 8;

    int currDisc, numDiscs, cageBars, mode = MODE_CUBE;

    double cubeSize, quadSize;

    boolean RUNNING, SHOWCAGE = true;

    public static void main(String[] args) {
        MF = new MainFrame(new CubeBallTestApp(args), DEF_WIDTH, DEF_HEIGHT);
        MF.setTitle("LoxBall3D");
    }

    public CubeBallTestApp(String[] args) {
        IS_APPLET = false;
        System.out.println("Running as application...");
    }

    public CubeBallTestApp() {
        IS_APPLET = true;
        System.out.println("Running as applet...");
    }

    @Override
    public void init() {
        CubeTestBall.loadTextures(4, "res/lox");
        initGame(64, 8, 8);
        setLayout(new BorderLayout());
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        add(canvas, BorderLayout.CENTER);
        class KeyCmdListener extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyCode(), e.getKeyChar());
            }
        }
        canvas.addKeyListener(new KeyCmdListener());
        initUniverse();
        resetView();
        setVisible(true);
    }

    public void initGame(double size, int numBalls, int bars) {
        mode = MODE_MULTI;
        cubeSize = size;
        cageBars = bars;
        balls = new Vector<CubeTestBall>();
        for (int i = 0; i < numBalls; i++) {
            balls.add(new CubeTestBall(i, INI_RADIUS, INI_SPEED, rndPt()));
        }
        getBall(VIEW_BALL).setState(CubeTestBall.VIEW_STATE);
        view = new Transform3D();
        viewVec = new Vector3d();
        backViewVec = new Vector3d();
        backViewDist = new Vector3d(0, 0, cubeSize / 4.0);
        rotMats = new Matrix3d[6];
        Matrix3d m = new Matrix3d();
        m.rotX(ROT_SPD);
        rotMats[LEFT] = new Matrix3d(m);
        m.rotX(-ROT_SPD);
        rotMats[RIGHT] = new Matrix3d(m);
        m.rotY(ROT_SPD);
        rotMats[UP] = new Matrix3d(m);
        m.rotY(-ROT_SPD);
        rotMats[DOWN] = new Matrix3d(m);
        m.rotZ(ROT_SPD);
        rotMats[NEAR] = new Matrix3d(m);
        m.rotZ(-ROT_SPD);
        rotMats[FAR] = new Matrix3d(m);
        quadSize = (cubeSize * 2) / cageBars;
        discTab = new int[6][cageBars][cageBars];
        numDiscs = 6 * cageBars * cageBars;
        for (int i = 0; i < 6; i++) for (int j = 0; j < cageBars; j++) for (int k = 0; k < cageBars; k++) discTab[i][j][k] = NO_DISC;
        System.out.println("Discs:" + numDiscs);
        currDisc = 0;
    }

    public void initUniverse() {
        SU = new SimpleUniverse(canvas);
        SU.getViewer().getView().setBackClipPolicy(View.VIRTUAL_SCREEN);
        SU.getViewer().getView().setBackClipDistance(cubeSize * CLIP_FACTOR);
        BranchGroup scene = createSceneGraph();
        scene.compile();
        SU.addBranchGraph(scene);
    }

    public BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        bounds = new BoundingBox(new Point3d(-cubeSize, -cubeSize, -cubeSize), new Point3d(cubeSize, cubeSize, cubeSize));
        AmbientLight AL = new AmbientLight(true, white);
        AL.setInfluencingBounds(bounds);
        root.addChild(AL);
        discoShape = new Shape3D();
        discoShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        disco = new QuadArray(numDiscs * 4, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        disco.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        disco.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        discoShape.setGeometry(disco);
        Appearance app = new Appearance();
        PolygonAttributes PA = new PolygonAttributes();
        PA.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(PA);
        discoShape.setAppearance(app);
        root.addChild(discoShape);
        Shape3D cage = new Shape3D();
        cage.setGeometry(getCage());
        root.addChild(cage);
        for (int i = 0; i < balls.size(); i++) {
            root.addChild(getBall(i).getTransGrp());
        }
        return root;
    }

    public int getSide(Point3d p) {
        double max = 0;
        int side = 0;
        if (Math.abs(p.x) > max) {
            max = Math.abs(p.x);
            side = p.x > 0 ? RIGHT : LEFT;
        }
        if (Math.abs(p.y) > max) {
            max = Math.abs(p.y);
            side = p.y > 0 ? UP : DOWN;
        }
        if (Math.abs(p.z) > max) {
            max = Math.abs(p.z);
            side = p.z > 0 ? NEAR : FAR;
        }
        return side;
    }

    public synchronized void setNextDisc(Point3d p, Color3f c) {
        discoShape.setGeometry(null);
        int side = getSide(p);
        Point3d fp = new Point3d(p);
        fp.clamp(-cubeSize, cubeSize - .01);
        fp.scale(1 / quadSize);
        fp.x = Math.floor(fp.x);
        fp.y = Math.floor(fp.y);
        fp.z = Math.floor(fp.z);
        int X = (int) fp.x + (cageBars / 2);
        int Y = (int) fp.y + (cageBars / 2);
        int Z = (int) fp.z + (cageBars / 2);
        int x = 0, y = 0;
        switch(side) {
            case RIGHT:
            case LEFT:
                x = Y;
                y = Z;
                break;
            case UP:
            case DOWN:
                x = X;
                y = Z;
                break;
            case NEAR:
            case FAR:
                x = X;
                y = Y;
                break;
        }
        int coord = currDisc;
        if (discTab[side][x][y] == NO_DISC) {
            discTab[side][x][y] = coord;
        } else {
            coord = discTab[side][x][y];
        }
        fp.set((X * quadSize) - cubeSize, (Y * quadSize) - cubeSize, (Z * quadSize) - cubeSize);
        Point3d pt = new Point3d();
        for (int i = 0; i < 4; i++) {
            switch(side) {
                case RIGHT:
                case LEFT:
                    pt.set(p.x > 0 ? cubeSize : -cubeSize, fp.y + (SQR_PATTERN[i][0] * quadSize), fp.z + (SQR_PATTERN[i][1] * quadSize));
                    break;
                case UP:
                case DOWN:
                    pt.set(fp.x + (SQR_PATTERN[i][0] * quadSize), p.y > 0 ? cubeSize : -cubeSize, fp.z + (SQR_PATTERN[i][1] * quadSize));
                    break;
                case NEAR:
                case FAR:
                    pt.set(fp.x + (SQR_PATTERN[i][0] * quadSize), fp.y + (SQR_PATTERN[i][1] * quadSize), p.z > 0 ? cubeSize : -cubeSize);
                    break;
            }
            disco.setCoordinate(coord + i, pt);
        }
        Color3f c2 = new Color3f(c == null ? white : c);
        if (c != null) c2.scale((float) Math.random());
        Color3f rndCol = new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());
        if (c == null) rndCol.add(white); else rndCol.add(black);
        rndCol.scale(.5f);
        disco.setColor(coord, cageCols[side]);
        disco.setColor(coord + 1, c2);
        disco.setColor(coord + 2, rndCol);
        disco.setColor(coord + 3, c == null ? cageCols[side] : c);
        if (coord == currDisc) {
            currDisc += 4;
            if (currDisc >= (numDiscs * 4)) {
                currDisc = 0;
                System.out.println("Disc full!");
            }
        }
        discoShape.setGeometry(disco);
    }

    public Geometry getCage() {
        if (!SHOWCAGE) return null;
        int coords = cageBars * 2 * 6 * 2;
        LineArray QA = new LineArray(coords, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        double SIZE2 = cubeSize * 2;
        Point3d p = new Point3d();
        int c = 0;
        for (double S = -cubeSize; S <= cubeSize; S += SIZE2) for (int axis = X_CRISS; axis <= Z_CROSS; axis++) for (double i = 0; i < cageBars; i++) for (double side = -cubeSize; side <= cubeSize; side += SIZE2) {
            double d = (i * quadSize) - cubeSize;
            switch(axis) {
                case X_CRISS:
                    p.set(S, side, d);
                    break;
                case X_CROSS:
                    p.set(S, d, side);
                    break;
                case Y_CRISS:
                    p.set(side, S, d);
                    break;
                case Y_CROSS:
                    p.set(d, S, side);
                    break;
                case Z_CRISS:
                    p.set(side, d, S);
                    break;
                case Z_CROSS:
                    p.set(d, side, S);
                    break;
            }
            QA.setCoordinate(c, p);
            int axisCol = axis / 2;
            if (S > 0) axisCol += 3;
            QA.setColor(c++, cageCols[axisCol]);
        }
        return QA;
    }

    @Override
    public void start() {
        new Thread(this).run();
    }

    public void run() {
        RUNNING = true;
        while (RUNNING) {
            for (int i = 0; i < balls.size(); i++) {
                CubeTestBall ball = getBall(i);
                if (ball.chkBounce(bounds)) {
                    setNextDisc(new Point3d(ball.getLocVec()), i == VIEW_BALL ? null : ball.getCol());
                }
                for (int b = 0; b < balls.size(); b++) {
                    if (ball.chkCollision(getBall(b))) {
                        if (i == VIEW_BALL) {
                            getBall(b).nextState();
                        }
                    }
                }
            }
            for (int i = 0; i < balls.size(); i++) {
                getBall(i).move();
            }
            setView();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public CubeTestBall getBall(int ballnum) {
        return (balls.elementAt(ballnum));
    }

    public void setView() {
        switch(mode) {
            case MODE_BALL:
                CubeTestBall viewBall = getBall(VIEW_BALL);
                view.set(viewBall.getDirTrans());
                view.setTranslation(viewBall.getLocVec());
                backViewVec.set(backViewDist);
                view.transform(backViewVec);
                view.get(viewVec);
                viewVec.add(backViewVec);
                view.setTranslation(viewVec);
                setViewTrans(view);
                break;
            case MODE_WEIRD:
                double dist, d = 9999d;
                Point3d l = getBall(VIEW_BALL).getLoc();
                for (int i = 0; i < balls.size(); i++) {
                    dist = l.distance(getBall(i).getLoc());
                    if (i != VIEW_BALL && dist < d) {
                        d = dist;
                        TRACK_BALL = i;
                    }
                }
                view.lookAt(getBall(VIEW_BALL).getLoc(), getBall(TRACK_BALL).getLoc(), upVec);
                view.invert();
                setViewTrans(view);
                break;
            case MODE_TRACK:
            case MODE_TRACK2:
                view.lookAt(mode == MODE_TRACK ? origin : getBall(TRACK_BALL).getLoc(), getBall(VIEW_BALL).getLoc(), upVec);
                view.invert();
                setViewTrans(view);
                break;
        }
    }

    public void toggleView() {
        mode++;
        if (mode > MAX_MODE) mode = 0;
    }

    public void handleKey(int c, char C) {
        if (c == 27) {
            resetView();
        } else if (c == 32) {
            toggleView();
        } else if (mode == MODE_CUBE && C == '.') {
            slewView(new Vector3d(SLEW_SPD, 0, 0));
        } else if (mode == MODE_CUBE && C == ',') {
            slewView(new Vector3d(-SLEW_SPD, 0, 0));
        } else if (mode == MODE_CUBE && C == 'q') {
            slewView(new Vector3d(0, SLEW_SPD, 0));
        } else if (mode == MODE_CUBE && C == 'a') {
            slewView(new Vector3d(0, -SLEW_SPD, 0));
        } else if (mode == MODE_CUBE && (C == 'o' || c == 155)) {
            slewView(new Vector3d(0, 0, SLEW_SPD));
        } else if (mode == MODE_CUBE && (C == 'i' || c == 12)) {
            slewView(new Vector3d(0, 0, -SLEW_SPD));
        } else if (c == 40) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[LEFT]); else rotateView(rotMats[LEFT]);
        } else if (c == 38) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[RIGHT]); else rotateView(rotMats[RIGHT]);
        } else if (c == 37) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[UP]); else rotateView(rotMats[UP]);
        } else if (c == 39) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[DOWN]); else rotateView(rotMats[DOWN]);
        } else if (C == 'z' || c == 35) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[NEAR]); else rotateView(rotMats[NEAR]);
        } else if (C == 'x' || c == 34) {
            if (mode != MODE_CUBE) getBall(VIEW_BALL).rotDir(rotMats[FAR]); else rotateView(rotMats[FAR]);
        } else if (C == 'e') {
            stabilizeXAxis();
        } else if (C == 'y') {
            stabilizeYAxis();
        } else if (C == 's') {
            stabilizeZAxis();
        }
    }

    public void slewView(Vector3d slewVec) {
        Transform3D T = getViewTrans();
        T.transform(slewVec);
        Vector3d v = new Vector3d();
        Matrix3d m = new Matrix3d();
        T.get(m, v);
        v.add(slewVec);
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

    public void stabilizeXAxis() {
        Transform3D T = getViewTrans();
        Matrix3d m = new Matrix3d();
        T.get(m);
        Vector3d v = new Vector3d();
        T.get(v);
        Matrix3d rm = new Matrix3d();
        rm.rotX(Math.PI - m.getElement(1, 2));
        m.mul(rm);
        rm.rotX(Math.PI - m.getElement(1, 2));
        m.mul(rm);
        T.set(m, v, 1);
        setViewTrans(T);
    }

    public void stabilizeYAxis() {
        Transform3D T = getViewTrans();
        Matrix3d m = new Matrix3d();
        T.get(m);
        Vector3d v = new Vector3d();
        T.get(v);
        Matrix3d rm = new Matrix3d();
        rm.rotY(-m.getElement(2, 0));
        m.mul(rm);
        rm.rotY(-m.getElement(2, 0));
        m.mul(rm);
        T.set(m, v, 1);
        setViewTrans(T);
    }

    public void stabilizeZAxis() {
        Transform3D T = getViewTrans();
        Matrix3d m = new Matrix3d();
        T.get(m);
        Vector3d v = new Vector3d();
        T.get(v);
        Matrix3d rm = new Matrix3d();
        rm.rotZ(-m.getElement(1, 0));
        m.mul(rm);
        rm.rotZ(-m.getElement(1, 0));
        m.mul(rm);
        T.set(m, v, 1);
        setViewTrans(T);
    }

    public void resetView() {
        Transform3D T = getViewTrans();
        T.lookAt(new Point3d(0, 0, cubeSize), new Point3d(cubeSize, cubeSize, -cubeSize), new Vector3d(0, 0, 1));
        T.invert();
        setViewTrans(T);
    }

    public Point3d rndPt() {
        return new Point3d(-cubeSize + (Math.random() * (cubeSize * 2)), -cubeSize + (Math.random() * (cubeSize * 2)), -cubeSize + (Math.random() * (cubeSize * 2)));
    }
}
