package pl.swmud.ns.swaedit.gui;

import java.awt.Font;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import pl.swmud.ns.swaedit.core.FlagsWrapper;
import pl.swmud.ns.swaedit.core.IFlagsSetter;
import pl.swmud.ns.swaedit.map.Animator;
import pl.swmud.ns.swaedit.map.ExitWrapper;
import pl.swmud.ns.swaedit.map.GLFont;
import pl.swmud.ns.swaedit.map.MapRoom;
import pl.swmud.ns.swaedit.map.RoomCoords;
import pl.swmud.ns.swaedit.map.RoomSpread;
import com.jogamp.common.nio.Buffers;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImage.Format;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.opengl.QGLFormat;
import com.trolltech.qt.opengl.QGLWidget;

public class MapWidget extends QGLWidget {

    private int selectBufLen;

    private static final int FPS = 30;

    private QTimer animator;

    private boolean animate = true;

    private GL2 gl;

    private GLU glu;

    private int glList;

    private boolean multisample = true;

    private static final float ALPHA = .5f;

    private static final int MAX_NAMESTACK_SIZE = 2;

    private static final byte[] selectionColor = { (byte) 218, (byte) 168, (byte) 16, (byte) (255 * ALPHA) };

    private static final byte[] selection1Color = { (byte) 249, (byte) 188, (byte) 11, (byte) (255 * ALPHA) };

    private static final float[] objectColor = { .0f, .7f, .0f, ALPHA };

    private static final float[] object1Color = { .0f, 1.f, .0f, ALPHA };

    private static final byte[] flagColor = { (byte) 0, (byte) 224, (byte) 255, (byte) (255 * ALPHA) };

    private static final byte[] flag1Color = { (byte) 0, (byte) 255, (byte) 255, (byte) (255 * ALPHA) };

    private int cx, cy;

    private int selected = -1;

    private boolean reportSelected;

    private boolean selection;

    private BigInteger selectedVnum;

    private int exitShift;

    private ExitWrapper selectedExit;

    private int w, h;

    private float fov = 60f;

    private float aspect;

    private float near = 1.0f;

    private float far = 500.0f;

    private static final float INITIAL_Z = -31.f;

    private float dz = INITIAL_Z;

    private float rot, angle, dx, dy, xRot, yRot, zRot;

    private QPoint lastPos;

    private boolean zRotAxis;

    QTime lastTime;

    private SortedMap<Integer, List<MapRoom>> islandRooms;

    private float xMiddle, yMiddle, zMiddle;

    private int currentIsland;

    private int currentIslandSize;

    private int maxIslands;

    private int currentLayer;

    private int maxLayer;

    private boolean drawDistantExits;

    private GLFont glFont;

    private Long roomMarkingFlag = Long.valueOf(0);

    private boolean drawCross;

    private boolean showHelp;

    private int showIslandsLayers;

    private static final int BPP = 4;

    private boolean readPixels;

    private boolean transparentShot;

    private String screenShotPath;

    private File screenShotDir;

    private String screenShotBareFileName;

    private long screenShotCnt = 1;

    private static final String USER_HOME = System.getProperty("user.home");

    private static final List<String> MENU_KEYS = new ArrayList<String>() {

        private static final long serialVersionUID = 1041444819386431807L;

        {
            add("d      - toggle distant exits");
            add("m      - toggle multisampling");
            add("space  - toggle rotation");
            add("h      - toggle this help text");
            add("k      - toggle cross");
            add("c      - reset to center");
            add("f      - toggle fullscreen");
            add("r      - refresh map");
            add("i      - select room marking flag");
            add("right  - next island");
            add("left   - previous island");
            add("up     - next layer");
            add("down   - previous layer");
            add("F12    - take a screenshot");
            add("F11    - take a transparent screenshot");
            add("lMouse - drag to move, click to select room/exit");
            add("rMouse - drag to change rotation angle");
            add("shift  - hold to switch Y to Z axis");
        }
    };

    private final Signal1<BigInteger> vnumSelected = new Signal1<BigInteger>();

    private final Signal3<BigInteger, Short, BigInteger> exitSelected = new Signal3<BigInteger, Short, BigInteger>();

    private final Signal0 windowClosed = new Signal0();

    private final Signal0 mapRefreshed = new Signal0();

    @Override
    protected void initializeGL() {
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_MULTISAMPLE);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glShadeModel(GL2.GL_SMOOTH);
        Font f = new Font(Font.DIALOG_INPUT, 0, 16);
        if (f != null) {
            glFont = new GLFont(f, gl, 0, 0);
        }
    }

    @Override
    protected void paintGL() {
        if (multisample) {
            gl.glEnable(GL2.GL_MULTISAMPLE);
        } else {
            gl.glDisable(GL2.GL_MULTISAMPLE);
        }
        if (selection) {
            select(cx, cy);
            selection = false;
        }
        drawWorld();
    }

    @Override
    protected void resizeGL(int w, int h) {
        this.w = w;
        if (h == 0) {
            h = 1;
        }
        this.h = h;
        aspect = w / h;
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(fov, aspect, near, far);
    }

    private static QGLFormat getQGLFormat() {
        QGLFormat f = QGLFormat.defaultFormat();
        f.setSampleBuffers(true);
        f.setSamples(4);
        f.setAlpha(true);
        f.setAlphaBufferSize(8);
        f.setDoubleBuffer(true);
        return f;
    }

    public MapWidget(SortedMap<Integer, List<MapRoom>> islandRooms, int currentIsland, int maxIslands, boolean defaultFormat) {
        super(defaultFormat ? QGLFormat.defaultFormat() : getQGLFormat());
        setAutoBufferSwap(false);
        this.currentIsland = currentIsland;
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLDrawableFactory factory = GLDrawableFactory.getFactory(profile);
        makeCurrent();
        GLContext ctx = factory.createExternalGLContext();
        ctx.makeCurrent();
        gl = ctx.getGL().getGL2();
        glu = GLU.createGLU(gl);
        resize(800, 600);
        animator = new Animator(this, 30);
        vnumSelected.connect(SWAEdit.ref, "mapRoomVnumSelected(BigInteger)");
        exitSelected.connect(SWAEdit.ref, "mapRoomExitSelected(BigInteger,short,BigInteger)");
        mapRefreshed.connect(SWAEdit.ref, "mapRefreshed()");
        refreshMap(islandRooms, maxIslands);
    }

    public void refreshMap(SortedMap<Integer, List<MapRoom>> islandRooms, int maxIslands) {
        this.islandRooms = islandRooms;
        this.maxIslands = maxIslands;
        setupIsland(currentIsland);
        setScreenShotParent();
        setWindowTitle(SWAEdit.ref.area.getHead().getName());
        setWindowIcon(QApplication.windowIcon());
        genLists();
    }

    @Override
    protected void closeEvent(QCloseEvent e) {
        animator.stop();
        windowClosed.connect(SWAEdit.ref, "mapClosed()");
        windowClosed.emit();
    }

    @Override
    protected void keyPressEvent(QKeyEvent e) {
        int kc = e.key();
        if (kc == Qt.Key.Key_Space.value()) {
            animate = !animate;
            e.accept();
        } else if (kc == 'd' || kc == Qt.Key.Key_D.value()) {
            drawDistantExits = !drawDistantExits;
            e.accept();
        } else if (kc == 'm' || kc == Qt.Key.Key_M.value()) {
            multisample = !multisample;
            e.accept();
        } else if (kc == 'k' || kc == Qt.Key.Key_K.value()) {
            drawCross = !drawCross;
            e.accept();
        } else if (kc == 'c' || kc == Qt.Key.Key_C.value()) {
            center();
            e.accept();
        } else if (kc == 'h' || kc == Qt.Key.Key_H.value()) {
            showHelp = !showHelp;
            e.accept();
        } else if (kc == 'f' || kc == Qt.Key.Key_F.value()) {
            if (isFullScreen()) {
                showNormal();
            } else {
                showFullScreen();
            }
            e.accept();
        } else if (kc == 'r' || kc == Qt.Key.Key_R.value()) {
            mapRefreshed.emit();
            e.accept();
        } else if (kc == 'i' || kc == Qt.Key.Key_I.value()) {
            new FlagsWidget(SWAEdit.ref.roomFlags.getFlag(), new FlagsWrapper(roomMarkingFlag, new IFlagsSetter() {

                public void setFlags(Long flagValue) {
                    roomMarkingFlag = flagValue;
                }
            }), "Map Room Marking Flag").show();
            e.accept();
        } else if (kc == Qt.Key.Key_Left.value()) {
            if (currentIsland > 0) {
                setupIsland(--currentIsland);
            }
            e.accept();
        } else if (kc == Qt.Key.Key_Right.value()) {
            if (currentIsland < maxIslands - 1) {
                setupIsland(++currentIsland);
            }
            e.accept();
        } else if (kc == Qt.Key.Key_Up.value()) {
            incLayer();
            e.accept();
        } else if (kc == Qt.Key.Key_Down.value()) {
            decLayer();
            e.accept();
        } else if (kc == Qt.Key.Key_F12.value()) {
            screenShot();
            e.accept();
        } else if (kc == Qt.Key.Key_F11.value()) {
            transparentScreenShot();
            e.accept();
        } else if (kc == Qt.Key.Key_Shift.value()) {
            zRotAxis = true;
            e.accept();
        } else {
            e.ignore();
        }
    }

    @Override
    protected void keyReleaseEvent(QKeyEvent e) {
        int kc = e.key();
        if (kc == Qt.Key.Key_Shift.value()) {
            zRotAxis = false;
            e.accept();
        } else {
            e.ignore();
        }
    }

    @Override
    protected void wheelEvent(QWheelEvent e) {
        dz -= e.delta() / 120;
        e.accept();
    }

    @Override
    protected void mousePressEvent(QMouseEvent e) {
        lastPos = e.pos();
        if (e.button() == Qt.MouseButton.LeftButton) {
            cx = e.x();
            cy = e.y();
            e.accept();
        } else {
            e.ignore();
        }
    }

    @Override
    protected void mouseReleaseEvent(QMouseEvent e) {
        if (e.button() == Qt.MouseButton.LeftButton) {
            int ex = e.x();
            int ey = e.y();
            if (Math.abs(cx - ex) < 3 && Math.abs(cy - ey) < 3) {
                cx = ex;
                cy = ey;
                selection = true;
                reportSelected = true;
            }
            e.accept();
        } else {
            e.ignore();
        }
    }

    @Override
    protected void mouseMoveEvent(QMouseEvent e) {
        int dx = e.x() - lastPos.x();
        int dy = e.y() - lastPos.y();
        lastPos = e.pos();
        if (e.buttons().isSet(Qt.MouseButton.LeftButton)) {
            if (dz < .0f) {
                this.dx -= ((float) dx) / ((float) w) * dz;
                this.dy += ((float) dy) / ((float) h) * dz;
            } else if (dz > .0f) {
                this.dx += ((float) dx) / ((float) w) * dz;
                this.dy -= ((float) dy) / ((float) h) * dz;
            } else {
                this.dx += ((float) dx) / ((float) w);
                this.dy -= ((float) dy) / ((float) h);
            }
            e.accept();
        } else if (e.buttons().isSet(Qt.MouseButton.RightButton)) {
            setXRotation(((int) xRot) + 8 * dy);
            if (zRotAxis) {
                setZRotation(((int) zRot) + 8 * dx);
            } else {
                setYRotation(((int) yRot) + 8 * dx);
            }
            e.accept();
        } else {
            e.ignore();
        }
    }

    private void setXRotation(int angle) {
        angle = normalizeAngle(angle);
        if (angle != xRot) {
            xRot = angle;
        }
    }

    private void setYRotation(int angle) {
        angle = normalizeAngle(angle);
        if (angle != yRot) {
            yRot = angle;
        }
    }

    private void setZRotation(int angle) {
        angle = normalizeAngle(angle);
        if (angle != zRot) {
            zRot = angle;
        }
    }

    private int normalizeAngle(int angle) {
        while (angle < 0) angle += 360 * 16;
        while (angle > 360 * 16) angle -= 360 * 16;
        return angle;
    }

    private void setupIsland(int islandNo) {
        currentIsland = islandNo;
        currentIslandSize = islandRooms.get(islandNo).size();
        selectBufLen = currentIslandSize * 4 + 5;
        xMiddle = getXMiddle(islandNo);
        yMiddle = getYMiddle(islandNo);
        zMiddle = getZMiddle(islandNo);
        selected = -1;
        currentLayer = 0;
        maxLayer = 0;
        for (MapRoom mr : islandRooms.get(islandNo)) {
            int layer = mr.getCoords().getLayer();
            if (layer > maxLayer) {
                maxLayer = layer;
            }
        }
        showIslandsLayers();
    }

    private void decLayer() {
        if (currentLayer > 0) {
            --currentLayer;
            selected = -1;
            showIslandsLayers();
        }
    }

    private void incLayer() {
        if (currentLayer < maxLayer) {
            ++currentLayer;
            selected = -1;
            showIslandsLayers();
        }
    }

    private void center() {
        dz = INITIAL_Z;
        rot = dx = dy = angle = xRot = yRot = zRot = .0f;
    }

    private void screenShot() {
        screenShotPath = getScreenShotPath();
        readPixels = true;
    }

    private void transparentScreenShot() {
        screenShotPath = getScreenShotPath();
        readPixels = true;
        transparentShot = true;
    }

    private void setScreenShotParent() {
        File parent;
        if (SWAEdit.ref.currentFileName != null) {
            File currentFile = new File(SWAEdit.ref.currentFileName);
            parent = currentFile.getParentFile();
            screenShotBareFileName = currentFile.getName().replace(".xml", "");
        } else {
            parent = new File(USER_HOME, ".swaedit");
            screenShotBareFileName = "unnamed";
        }
        screenShotDir = new File(new File(parent, "mapshots"), screenShotBareFileName).getAbsoluteFile();
    }

    private String getScreenShotPath() {
        screenShotDir.mkdirs();
        File shot = null;
        for (; shot == null || shot.exists(); screenShotCnt++) {
            shot = new File(screenShotDir, screenShotBareFileName + "_" + getIslandNum() + "_" + getLayerNum() + "_" + screenShotCnt + ".png");
        }
        return shot.getAbsolutePath();
    }

    private int getLayerNum() {
        return currentLayer + 1;
    }

    private int getMaxLayerNum() {
        return maxLayer + 1;
    }

    private int getIslandNum() {
        return currentIsland + 1;
    }

    private int getMaxIslandNum() {
        return maxIslands;
    }

    public void show(int x, int y) {
        if (isHidden()) {
            move(x, y);
            show();
            animator.start();
        }
    }

    private void showIslandsLayers() {
        showIslandsLayers = FPS * 2;
    }

    private int getXMiddle(int islandNo) {
        return new RoomSpread(islandRooms.get(islandNo)) {

            @Override
            public int getValue(RoomCoords coords) {
                return coords.getX();
            }
        }.getMiddle();
    }

    private int getYMiddle(int islandNo) {
        return new RoomSpread(islandRooms.get(islandNo)) {

            @Override
            public int getValue(RoomCoords coords) {
                return coords.getY();
            }
        }.getMiddle();
    }

    private int getZMiddle(int islandNo) {
        return new RoomSpread(islandRooms.get(islandNo)) {

            @Override
            public int getValue(RoomCoords coords) {
                return coords.getZ();
            }
        }.getMiddle();
    }

    public void showRoom(BigInteger vnum) {
        int[] islandRoom = findIsland(vnum);
        if (islandRoom != null) {
            if (currentIsland != islandRoom[0]) {
                setupIsland(islandRoom[0]);
            }
            selected = islandRoom[1];
            selectedVnum = vnum;
        }
    }

    private int[] findIsland(BigInteger vnum) {
        for (int islandNo : islandRooms.keySet()) {
            int roomNo = 0;
            for (MapRoom mr : islandRooms.get(islandNo)) {
                if (vnum.equals(mr.getRoom().getVnum())) {
                    return new int[] { islandNo, roomNo };
                }
                ++roomNo;
            }
        }
        return null;
    }

    public void showExit(BigInteger ownerRoomVnum, ExitWrapper ex) {
        int[] islandRoom = findIsland(ownerRoomVnum);
        if (islandRoom != null) {
            if (currentIsland != islandRoom[0]) {
                setupIsland(islandRoom[0]);
            }
            int exitNo = 0;
            boolean exitFound = false;
            for (MapRoom mr : islandRooms.get(currentIsland)) {
                for (ExitWrapper exit : mr.getMapRooms().keySet()) {
                    if (mr.getRoom().getVnum().equals(ownerRoomVnum) && exit.getVnum().equals(ex.getVnum()) && exit.getDirection() == ex.getDirection()) {
                        selectedVnum = ownerRoomVnum;
                        exitFound = true;
                    } else if (exit.isTwoWay() && exit.getVnum().equals(ownerRoomVnum) && exit.getDirection() == ex.getRevDirection()) {
                        selectedVnum = mr.getRoom().getVnum();
                        exitFound = true;
                    }
                    if (exitFound) {
                        selectedExit = exit;
                        selected = currentIslandSize + exitNo;
                        if (exit.isDistant() && !drawDistantExits) {
                            drawDistantExits = true;
                        }
                        return;
                    }
                    exitNo++;
                }
            }
        }
    }

    private void select(int cx, int cy) {
        int[] viewport = new int[4];
        IntBuffer selectBuf = Buffers.newDirectIntBuffer(selectBufLen);
        gl.glSelectBuffer(selectBufLen, selectBuf);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glRenderMode(GL2.GL_SELECT);
        gl.glLoadIdentity();
        glu.gluPickMatrix(cx, viewport[3] - cy + viewport[1], 2, 2, viewport, 0);
        glu.gluPerspective(fov, aspect, near, far);
        drawWorld();
        int hits = gl.glRenderMode(GL2.GL_RENDER);
        if (hits > 0) {
            int[] sb = new int[selectBufLen];
            selectBuf.get(sb, 0, hits * 4);
            processHits(hits, sb);
        } else {
            if (selection) {
                selected = -1;
                selectedExit = null;
            }
        }
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void processHits(int hits, int[] sb) {
        float minz = Float.MAX_VALUE;
        int chosen = -1;
        for (int i = 0; i < hits * 4; i += 4) {
            if (sb[i] < MAX_NAMESTACK_SIZE) {
                float z = sb[i + 1];
                z /= Float.MAX_VALUE;
                if (z < minz) {
                    minz = z;
                    chosen = sb[i + 3];
                }
            }
        }
        if (selection) {
            selected = chosen;
        }
    }

    private void drawWorld() {
        if (animate) {
            rot += 0.002;
            if (rot > Math.PI / 2.f) {
                rot = .0f;
            }
            angle = 360.f * Math.abs((float) Math.sin(rot));
        }
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glInitNames();
        gl.glLoadIdentity();
        drawIslands();
        drawWindRose();
        if (drawCross) {
            gl.glCallList(glList + 4);
        }
        if (selected >= 0 && selectedVnum != null || showHelp || showIslandsLayers > 0) {
            gl.glPushName(-2);
            gl.glPushMatrix();
            gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_TEXTURE_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            setOrthoOn();
            if (selected >= 0) {
                if (selectedVnum != null) {
                    if (selected < currentIslandSize) {
                        gl.glColor3f(1.f, 1.f, 1.f);
                        drawText("vnum selected: " + selectedVnum.toString(), -1.f, -.98f);
                    } else if (selectedExit != null) {
                        drawText("exit selected: " + selectedVnum.toString() + "->" + selectedExit.getDirectionName() + "->" + selectedExit.getVnum().toString(), -1.f, -.98f);
                    }
                }
            }
            if (showHelp) {
                float yShift = .98f;
                for (String mKey : MENU_KEYS) {
                    drawText(mKey, 1.f, yShift);
                    yShift -= .04f;
                }
            }
            if (showIslandsLayers > 0) {
                drawText("island: " + getIslandNum() + "/" + getMaxIslandNum() + " layer: " + getLayerNum() + "/" + getMaxLayerNum(), -1.f, -.94f);
                --showIslandsLayers;
            }
            setOrthoOff();
            gl.glPopAttrib();
            gl.glPopMatrix();
            gl.glPopName();
        }
        swapBuffers();
        if (readPixels) {
            takeScreenshot();
        }
        cleanDrawnMark();
    }

    private void takeScreenshot() {
        readPixels = false;
        Format format = Format.Format_RGB32;
        if (transparentShot) {
            transparentShot = false;
            format = Format.Format_ARGB32;
        }
        byte[] pixels = new byte[w * h * BPP];
        ByteBuffer buf = ByteBuffer.wrap(pixels);
        gl.glReadPixels(0, 0, w, h, GL2.GL_RGBA, GL2.GL_BYTE, buf);
        pixels = flipPixels(pixels);
        RGBAtoARGB(pixels);
        QImage img = new QImage(pixels, w, h, format);
        img.save(screenShotPath);
    }

    private byte[] flipPixels(byte[] imgPixels) {
        byte[] flippedPixels = new byte[imgPixels.length];
        int w4 = w * BPP;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w4; x += BPP) {
                for (int i = 0; i < BPP; i++) {
                    flippedPixels[(h - y - 1) * w4 + x + i] = imgPixels[y * w4 + x + i];
                }
            }
        }
        return flippedPixels;
    }

    private byte[] RGBAtoARGB(byte[] pixels) {
        for (int i = 0; i < pixels.length; i += BPP) {
            byte r = pixels[i];
            pixels[i] = pixels[i + 2];
            pixels[i + 2] = r;
        }
        return pixels;
    }

    private void drawText(String txt, float xShift, float yShift) {
        gl.glPushMatrix();
        float len = txt.length();
        float fScale = .03f;
        float tLen = (len - .5f * len) * fScale;
        tLen = 1.f - tLen - .01f;
        gl.glTranslatef(xShift * -tLen, yShift, 0);
        gl.glScalef(fScale, fScale, 1.f);
        gl.glColor3f(1, 1, 1);
        glFont.print(0, 0, txt);
        gl.glPopMatrix();
    }

    private float[] getLeftBottom(float near) {
        float[] lb = new float[2];
        lb[1] = (float) Math.tan(fov * Math.PI / 360.0) * near;
        lb[0] = aspect * lb[1];
        return lb;
    }

    private void drawIslands() {
        gl.glPushMatrix();
        List<MapRoom> island = islandRooms.get(currentIsland);
        gl.glTranslatef(dx, dy, dz);
        gl.glRotatef(xRot / 16.f, 1.f, .0f, .0f);
        gl.glRotatef(yRot / 16.f, .0f, 1.f, .0f);
        gl.glRotatef(zRot / 16.f, .0f, .0f, 1.f);
        gl.glRotatef(angle, 1.f, 1.f, 1.f);
        RoomCoords coords = island.get(0).getCoords();
        gl.glTranslatef(-.5f + xMiddle - coords.getX(), -.5f + yMiddle - coords.getY(), -.5f + zMiddle);
        gl.glPushName(-1);
        int i = 0;
        exitShift = 0;
        for (MapRoom mr : island) {
            RoomCoords rc = mr.getCoords();
            if (rc.getLayer() == currentLayer) {
                gl.glPushMatrix();
                gl.glTranslatef(rc.getX() * 2, rc.getY() * 2, rc.getZ() * 2);
                gl.glLoadName(i);
                gl.glPassThrough((float) i);
                if (selected == i && !selection) {
                    gl.glColor4ubv(i == 0 ? selection1Color : selectionColor, 0);
                    if (reportSelected) {
                        reportSelected = false;
                        selectedVnum = mr.getRoom().getVnum();
                        vnumSelected.emit(selectedVnum);
                    }
                } else {
                    if (i == 0) {
                        if (roomMarkingFlag.longValue() > 0 && (mr.getRoom().getFlags() & roomMarkingFlag) == roomMarkingFlag) {
                            gl.glColor4ubv(flag1Color, 0);
                        } else {
                            gl.glColor4fv(object1Color, 0);
                        }
                    } else {
                        if (roomMarkingFlag.longValue() > 0 && (mr.getRoom().getFlags() & roomMarkingFlag) == roomMarkingFlag) {
                            gl.glColor4ubv(flagColor, 0);
                        } else {
                            gl.glColor4fv(objectColor, 0);
                        }
                    }
                }
                gl.glCallList(glList);
                drawExits(mr);
                gl.glPopMatrix();
                i++;
            }
        }
        gl.glPopMatrix();
    }

    private void cleanDrawnMark() {
        List<MapRoom> island = islandRooms.get(currentIsland);
        for (MapRoom mr : island) {
            for (ExitWrapper exit : mr.getMapRooms().keySet()) {
                exit.setDrawn(false);
            }
        }
    }

    private void drawExits(MapRoom mr) {
        int shift;
        int i = 0;
        for (ExitWrapper exit : mr.getMapRooms().keySet()) {
            ExitWrapper revExit = exit.getRevExit();
            if ((revExit == null || !revExit.isDrawn()) && !exit.isDrawn()) {
                shift = currentIslandSize + exitShift;
                gl.glLoadName(shift);
                gl.glPushMatrix();
                switch(exit.getDirection()) {
                    case 0:
                        gl.glTranslatef(.5f, .5f, -1.f);
                        break;
                    case 1:
                        gl.glRotatef(90, 0, 1, 0);
                        gl.glTranslatef(-.5f, .5f, 1.f);
                        break;
                    case 2:
                        gl.glTranslatef(.5f, .5f, 1.f);
                        break;
                    case 3:
                        gl.glRotatef(90, 0, 1, 0);
                        gl.glTranslatef(-.5f, .5f, -1.f);
                        break;
                    case 4:
                        gl.glRotatef(90, 1, 0, 0);
                        gl.glTranslatef(.5f, .5f, -2.f);
                        break;
                    case 5:
                        gl.glRotatef(90, 1, 0, 0);
                        gl.glTranslatef(.5f, .5f, .0f);
                        break;
                    case 6:
                        gl.glRotatef(45, 0, -1, 0);
                        gl.glTranslatef(.7f, .5f, -2.3f);
                        gl.glScalef(1, 1, 1.8f);
                        break;
                    case 7:
                        gl.glRotatef(45, 0, 1, 0);
                        gl.glTranslatef(.0f, .5f, -1.6f);
                        gl.glScalef(1, 1, 1.8f);
                        break;
                    case 8:
                        gl.glRotatef(45, 0, 1, 0);
                        gl.glTranslatef(.0f, .5f, 1.2f);
                        gl.glScalef(1, 1, 1.8f);
                        break;
                    case 9:
                        gl.glRotatef(45, 0, -1, 0);
                        gl.glTranslatef(.7f, .5f, .5f);
                        gl.glScalef(1, 1, 1.8f);
                        break;
                    default:
                        System.err.println("virtual exits not yet supported!");
                }
                if ((!drawDistantExits && !exit.isDistant()) || drawDistantExits) {
                    if (!selection && selected == shift) {
                        gl.glColor4ubv(selectionColor, 0);
                        if (reportSelected) {
                            reportSelected = false;
                            selectedExit = exit;
                            selectedVnum = mr.getRoom().getVnum();
                            exitSelected.emit(selectedVnum, exit.getDirection(), exit.getVnum());
                        }
                    } else {
                        gl.glColor4fv(objectColor, 0);
                    }
                    if (exit.isTwoWay()) {
                        gl.glCallList(glList + 1);
                    } else {
                        gl.glCallList(glList + 2);
                    }
                }
                exit.setDrawn();
                gl.glPopMatrix();
            }
            exitShift++;
            i++;
        }
    }

    private void genLists() {
        glList = gl.glGenLists(5);
        gl.glNewList(glList, GL2.GL_COMPILE);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 1, 0);
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(1, 0, 0);
        gl.glVertex3f(0, 0, 1);
        gl.glVertex3f(0, 1, 1);
        gl.glVertex3f(1, 1, 1);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 1, 0);
        gl.glVertex3f(0, 1, 1);
        gl.glVertex3f(0, 0, 1);
        gl.glVertex3f(1, 0, 0);
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(1, 1, 1);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(1, 0, 0);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(0, 0, 1);
        gl.glVertex3f(0, 1, 0);
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(1, 1, 1);
        gl.glVertex3f(0, 1, 1);
        gl.glEnd();
        gl.glEndList();
        gl.glNewList(glList + 1, GL2.GL_COMPILE);
        GLUquadric quadratic = glu.gluNewQuadric();
        glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadratic, true);
        glu.gluCylinder(quadratic, 0.2, 0.2, 1, 32, 32);
        gl.glEndList();
        gl.glNewList(glList + 2, GL2.GL_COMPILE);
        glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadratic, true);
        glu.gluCylinder(quadratic, 0.1, 0.1, 1, 32, 32);
        gl.glEndList();
        gl.glNewList(glList + 3, GL2.GL_COMPILE);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor4f(1, 0, 0, 0.9f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(1, 0, 0);
        gl.glColor4f(1, 1, 0, 0.9f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 1, 0);
        gl.glColor4f(0, 0, 1, 0.9f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0, 1);
        gl.glEnd();
        gl.glPushMatrix();
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(90, 0, 1, 0);
        gl.glColor4f(1, 0, 0, 1);
        glu.gluCylinder(quadratic, 0.1, 0, 0.3, 32, 32);
        glu.gluDisk(quadratic, 0, 0.1, 32, 32);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0, 1, 0);
        gl.glRotatef(90, -1, 0, 0);
        gl.glColor4f(1, 1, 0, 1);
        glu.gluCylinder(quadratic, 0.1, 0, 0.3, 32, 32);
        glu.gluDisk(quadratic, 0, 0.1, 32, 32);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 1);
        gl.glColor4f(0, 0, 1, 1);
        glu.gluCylinder(quadratic, 0.1, 0, 0.3, 32, 32);
        glu.gluDisk(quadratic, 0, 0.1, 32, 32);
        gl.glPopMatrix();
        gl.glEndList();
        gl.glNewList(glList + 4, GL2.GL_COMPILE);
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -1);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(1, 1, 1);
        gl.glVertex3f(-1, 0, 0);
        gl.glVertex3f(1, 0, 0);
        gl.glVertex3f(0, -1, 0);
        gl.glVertex3f(0, 1, 0);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glEndList();
    }

    private void drawWindRose() {
        gl.glPushMatrix();
        final float fScale = .1f;
        final float zRose = -2.f;
        float[] lb = getLeftBottom(zRose);
        gl.glTranslatef(lb[0] + fScale, lb[1] + fScale, zRose);
        gl.glScalef(fScale, fScale, fScale);
        gl.glRotatef(xRot / 16.f, 1.f, .0f, .0f);
        gl.glRotatef(yRot / 16.f, .0f, 1.f, .0f);
        gl.glRotatef(zRot / 16.f, .0f, .0f, 1.f);
        gl.glRotatef(angle, 1, 1, 1);
        gl.glCallList(glList + 3);
        gl.glPopMatrix();
    }

    public void setOrthoOn() {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, 0, 0, 0, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glDisable(GL2.GL_DEPTH_TEST);
    }

    public void setOrthoOff() {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }
}
