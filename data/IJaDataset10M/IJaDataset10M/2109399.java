package fr.inria.zvtm.fms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import fr.inria.zvtm.engine.*;
import fr.inria.zvtm.glyphs.*;
import fr.inria.zvtm.animation.*;
import fr.inria.zvtm.animation.interpolation.*;
import fr.inria.zvtm.glyphs.*;
import fr.inria.zvtm.lens.*;
import fr.inria.zvtm.widgets.*;

public class Test {

    VirtualSpaceManager vsm;

    static final String mSpaceStr = "mSpace";

    VirtualSpace mSpace;

    Camera mCamera;

    View mView;

    ViewEventHandler eh;

    static int LENS_R1 = 100;

    static int LENS_R2 = 50;

    static final int LENS_ANIM_TIME = 300;

    static double MAG_FACTOR = 2.0;

    Test() {
        vsm = VirtualSpaceManager.INSTANCE;
        vsm.setDebug(true);
        initTest();
    }

    public void initTest() {
        mSpace = vsm.addVirtualSpace(mSpaceStr);
        mCamera = vsm.addCamera(mSpace);
        Vector cameras = new Vector();
        cameras.add(vsm.getVirtualSpace(mSpaceStr).getCamera(0));
        JPanel panel = vsm.addPanelView(cameras, "mainview", 800, 600);
        mView = vsm.getView("mainview");
        panel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Lenses and motor precision");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.requestFocus();
        mView.setBackgroundColor(Color.LIGHT_GRAY);
        eh = new EventHandlerTest(this);
        mView.setEventHandler(eh);
        mView.setNotifyMouseMoved(true);
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                mSpace.addGlyph(new VRectangle(i * 30, j * 30, 0, 10, 10, Color.WHITE));
            }
        }
        vsm.repaintNow();
    }

    FixedSizeLens lens;

    void toggleLens(int x, int y) {
        if (lens != null) {
            unsetLens();
        } else {
            setLens(x, y);
        }
    }

    void setLens(int x, int y) {
        lens = (FixedSizeLens) mView.setLens(getLensDefinition(x, y));
        lens.setBufferThreshold(1.5f);
        lens.setOuterRadiusColor(Color.BLACK);
        lens.setInnerRadiusColor(Color.BLACK);
        Animation a = vsm.getAnimationManager().getAnimationFactory().createLensMagAnim(LENS_ANIM_TIME, (FixedSizeLens) lens, new Float(MAG_FACTOR - 1), true, IdentityInterpolator.getInstance(), null);
        vsm.getAnimationManager().startAnimation(a, false);
    }

    void unsetLens() {
        Animation a = vsm.getAnimationManager().getAnimationFactory().createLensMagAnim(LENS_ANIM_TIME, (FixedSizeLens) lens, new Float(-MAG_FACTOR + 1), true, IdentityInterpolator.getInstance(), new EndAction() {

            public void execute(Object subject, Animation.Dimension dimension) {
                doUnsetLens();
            }
        });
        vsm.getAnimationManager().startAnimation(a, false);
    }

    void doUnsetLens() {
        lens.dispose();
        mView.setLens(null);
        lens = null;
    }

    Lens getLensDefinition(int x, int y) {
        return new FSLinearLens(1.0f, LENS_R1, LENS_R2, x - 400, y - 300);
    }

    boolean lensCursorSync = true;

    void toggleLensCursorSync() {
        lensCursorSync = !lensCursorSync;
    }

    void moveLens(int x, int y) {
        if (lens == null) {
            return;
        }
        lens.setAbsolutePosition(x, y);
        vsm.repaintNow();
    }

    void incX() {
        lens.setXfocusOffset(lens.getXfocusOffset() + 1);
    }

    void incY() {
        lens.setYfocusOffset(lens.getYfocusOffset() + 1);
    }

    public static void main(String[] args) {
        new Test();
    }
}

class EventHandlerTest implements ViewEventHandler {

    Test application;

    long lastX, lastY, lastJPX, lastJPY;

    EventHandlerTest(Test appli) {
        application = appli;
    }

    public void press1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        application.toggleLens(jpx, jpy);
    }

    public void release1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void click1(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
    }

    public void press2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void release2(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
    }

    public void click2(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
    }

    public void press3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        lastJPX = jpx;
        lastJPY = jpy;
        v.setDrawDrag(true);
        VirtualSpaceManager.INSTANCE.activeView.mouse.setSensitivity(false);
    }

    public void release3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        VirtualSpaceManager.INSTANCE.getAnimationManager().setXspeed(0);
        VirtualSpaceManager.INSTANCE.getAnimationManager().setYspeed(0);
        VirtualSpaceManager.INSTANCE.getAnimationManager().setZspeed(0);
        v.setDrawDrag(false);
        VirtualSpaceManager.INSTANCE.activeView.mouse.setSensitivity(true);
    }

    public void click3(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
    }

    public void mouseMoved(ViewPanel v, int jpx, int jpy, MouseEvent e) {
        System.out.println("mouse moved");
        if (application.lensCursorSync) {
            application.moveLens(jpx, jpy);
        }
    }

    public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpx, int jpy, MouseEvent e) {
        if (buttonNumber == 3 || ((mod == META_MOD || mod == META_SHIFT_MOD) && buttonNumber == 1)) {
            Camera c = VirtualSpaceManager.INSTANCE.getActiveCamera();
            float a = (c.focal + Math.abs(c.altitude)) / c.focal;
            if (mod == META_SHIFT_MOD) {
                VirtualSpaceManager.INSTANCE.getAnimationManager().setXspeed(0);
                VirtualSpaceManager.INSTANCE.getAnimationManager().setYspeed(0);
                VirtualSpaceManager.INSTANCE.getAnimationManager().setZspeed((c.altitude > 0) ? (long) ((lastJPY - jpy) * (a / 50.0f)) : (long) ((lastJPY - jpy) / (a * 50)));
            } else {
                VirtualSpaceManager.INSTANCE.getAnimationManager().setXspeed((c.altitude > 0) ? (long) ((jpx - lastJPX) * (a / 50.0f)) : (long) ((jpx - lastJPX) / (a * 50)));
                VirtualSpaceManager.INSTANCE.getAnimationManager().setYspeed((c.altitude > 0) ? (long) ((lastJPY - jpy) * (a / 50.0f)) : (long) ((lastJPY - jpy) / (a * 50)));
                VirtualSpaceManager.INSTANCE.getAnimationManager().setZspeed(0);
            }
        }
    }

    public void mouseWheelMoved(ViewPanel v, short wheelDirection, int jpx, int jpy, MouseWheelEvent e) {
        Camera c = VirtualSpaceManager.INSTANCE.getActiveCamera();
        float a = (c.focal + Math.abs(c.altitude)) / c.focal;
        if (wheelDirection == WHEEL_UP) {
            c.altitudeOffset(-a * 5);
            VirtualSpaceManager.INSTANCE.repaintNow();
        } else {
            c.altitudeOffset(a * 5);
            VirtualSpaceManager.INSTANCE.repaintNow();
        }
    }

    public void enterGlyph(Glyph g) {
        g.highlight(true, null);
    }

    public void exitGlyph(Glyph g) {
        g.highlight(false, null);
    }

    public void Ktype(ViewPanel v, char c, int code, int mod, KeyEvent e) {
    }

    public void Kpress(ViewPanel v, char c, int code, int mod, KeyEvent e) {
        if (code == KeyEvent.VK_SPACE) {
            application.toggleLensCursorSync();
        } else {
            Glyph[] pickedGlyphs = application.mView.getCursor().getGlyphsUnderMouseList();
            System.out.print("picked glyphs: ");
            for (int i = 0; i < pickedGlyphs.length; i++) System.out.print(pickedGlyphs[i]);
            System.out.println();
        }
    }

    public void Krelease(ViewPanel v, char c, int code, int mod, KeyEvent e) {
    }

    public void viewActivated(View v) {
    }

    public void viewDeactivated(View v) {
    }

    public void viewIconified(View v) {
    }

    public void viewDeiconified(View v) {
    }

    public void viewClosing(View v) {
        System.exit(0);
    }
}
