package net.claribole.eval.alphalens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.util.Vector;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.xerox.VTM.engine.*;
import com.xerox.VTM.glyphs.*;
import net.claribole.zvtm.engine.*;

public class VizFitts implements Java2DPainter {

    static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    static int VIEW_MAX_W = 1600;

    static int VIEW_MAX_H = 1200;

    int VIEW_W, VIEW_H;

    int VIEW_X, VIEW_Y;

    int panelWidth, panelHeight;

    static final int[] vispad = { 100, 100, 100, 100 };

    static final Color PADDING_COLOR = Color.BLACK;

    static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;

    VirtualSpaceManager vsm;

    VirtualSpace mSpace;

    static final String mSpaceName = "mainSpace";

    View mView;

    String mViewName = "Evaluation";

    Camera mCamera;

    VizFittsEventHandler eh;

    static final String INPUT_CSV_SEP = "\t";

    static final Color HTARGET_COLOR = Color.WHITE;

    static final Color TARGET_COLOR = Color.BLACK;

    static int NB_TARGETS_PER_TRIAL = 24;

    VCircle[] targets;

    static final long TARGET_R_POS = Math.round(EvalFitts.D * (Camera.DEFAULT_FOCAL + EvalFitts.CAM_ALT) / Camera.DEFAULT_FOCAL / 2.0);

    static final Color GRID_COLOR = Color.GRAY;

    static final long GRID_STEP = 200;

    static final long GRID_W = 16000;

    static final long GRID_H = 12000;

    public VizFitts(String f) {
        initGUI();
        eh = new VizFittsEventHandler(this);
        mView.setEventHandler(eh);
        initScene();
        mCamera.moveTo(0, 0);
        mCamera.setAltitude(EvalFitts.CAM_ALT);
        loadCinematic(f);
    }

    void initGUI() {
        windowLayout();
        vsm = new VirtualSpaceManager();
        mSpace = vsm.addVirtualSpace(mSpaceName);
        mCamera = vsm.addCamera(mSpaceName);
        Vector v = new Vector();
        v.add(mCamera);
        mView = vsm.addExternalView(v, mViewName, View.STD_VIEW, VIEW_W, VIEW_H, false, true, false, null);
        mView.setVisibilityPadding(vispad);
        mView.getPanel().addComponentListener(eh);
        mView.setNotifyMouseMoved(true);
        mView.setJava2DPainter(this, Java2DPainter.AFTER_DISTORTION);
        mView.mouse.setColor(EvalFitts.CURSOR_COLOR);
        mView.mouse.setSize(5);
        updatePanelSize();
    }

    void windowLayout() {
        if (Utilities.osIsWindows()) {
            VIEW_X = VIEW_Y = 0;
        } else if (Utilities.osIsMacOS()) {
            VIEW_X = 80;
            SCREEN_WIDTH -= 80;
        }
        VIEW_W = (SCREEN_WIDTH <= VIEW_MAX_W) ? SCREEN_WIDTH : VIEW_MAX_W;
        VIEW_H = (SCREEN_HEIGHT <= VIEW_MAX_H) ? SCREEN_HEIGHT : VIEW_MAX_H;
    }

    void initScene() {
        mView.setBackgroundColor(EvalFitts.BACKGROUND_COLOR);
        VRectangle r;
        long x = -GRID_W / 2;
        for (int i = 0; i < GRID_W / GRID_STEP + 1; i++) {
            r = new VRectangle(x, 0, 0, 4, GRID_H / 2, GRID_COLOR);
            r.setDrawBorder(false);
            vsm.addGlyph(r, mSpace);
            x += GRID_STEP;
        }
        long y = -GRID_H / 2;
        for (int i = 0; i < GRID_H / GRID_STEP + 1; i++) {
            r = new VRectangle(0, y, 0, GRID_W / 2, 4, GRID_COLOR);
            r.setDrawBorder(false);
            vsm.addGlyph(r, mSpace);
            y += GRID_STEP;
        }
        targets = new VCircle[NB_TARGETS_PER_TRIAL];
        double angle = 0;
        for (int i = 0; i < NB_TARGETS_PER_TRIAL; i++) {
            x = Math.round(TARGET_R_POS * Math.cos(angle));
            y = Math.round(TARGET_R_POS * Math.sin(angle));
            targets[i] = new VCircle(x, y, 0, Math.round(EvalFitts.W2_6 / 2), TARGET_COLOR);
            targets[i].setDrawBorder(false);
            vsm.addGlyph(targets[i], mSpace);
            if (i % 2 == 0) {
                angle += Math.PI;
            } else {
                angle += 2 * Math.PI / ((double) NB_TARGETS_PER_TRIAL) - Math.PI;
            }
        }
    }

    void loadCinematic(String f) {
        try {
            FileInputStream fis = new FileInputStream(new File(f));
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            br.readLine();
            String line = br.readLine();
            br.readLine();
            String[] lineData;
            while (line != null) {
                if (line.length() > 0) {
                    lineData = line.split(INPUT_CSV_SEP);
                    long x = Math.round(Long.parseLong(lineData[3]) * (Camera.DEFAULT_FOCAL + EvalFitts.CAM_ALT) / Camera.DEFAULT_FOCAL);
                    long y = Math.round(Long.parseLong(lineData[4]) * (Camera.DEFAULT_FOCAL + EvalFitts.CAM_ALT) / Camera.DEFAULT_FOCAL);
                    vsm.addGlyph(new VRectangle(x, y, 0, 10, 10, Color.BLUE), mSpace);
                }
                line = br.readLine();
            }
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void updatePanelSize() {
        Dimension d = mView.getPanel().getSize();
        panelWidth = d.width;
        panelHeight = d.height;
    }

    public void paint(Graphics2D g2d, int viewWidth, int viewHeight) {
        drawVisibilityPadding(g2d, viewWidth, viewHeight);
    }

    void drawVisibilityPadding(Graphics2D g2d, int viewWidth, int viewHeight) {
        g2d.setColor(PADDING_COLOR);
        g2d.fillRect(0, 0, viewWidth, vispad[1]);
        g2d.fillRect(0, vispad[1], vispad[0], viewHeight - vispad[1] - vispad[3] - 1);
        g2d.fillRect(viewWidth - vispad[2], vispad[1], vispad[2], viewHeight - vispad[1] - vispad[3] - 1);
        g2d.fillRect(0, viewHeight - vispad[3] - 1, viewWidth, vispad[3] + 1);
    }

    void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        if (args.length >= 3) {
            EvalFitts.VIEW_MAX_W = Integer.parseInt(args[1]);
            EvalFitts.VIEW_MAX_H = Integer.parseInt(args[2]);
        }
        new VizFitts(args[0]);
    }
}
