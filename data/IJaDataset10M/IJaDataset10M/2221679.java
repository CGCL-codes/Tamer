package fr.inria.zvtm.cluster.examples;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import fr.inria.zvtm.animation.Animation;
import fr.inria.zvtm.animation.AnimationManager;
import fr.inria.zvtm.animation.DefaultTimingHandler;
import fr.inria.zvtm.cluster.ClusteredView;
import fr.inria.zvtm.cluster.ClusterGeometry;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.View;
import fr.inria.zvtm.event.ViewListener;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VCircle;
import java.awt.Color;
import java.util.Random;
import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import org.jdesktop.animation.timing.interpolation.*;

/**
 * Sample master application.
 */
public class AnimCircles {

    private VirtualSpaceManager vsm = VirtualSpaceManager.INSTANCE;

    AnimCircles(ACOptions options) {
        vsm.setMaster("AnimCircles");
        VirtualSpace vs = vsm.addVirtualSpace("testSpace");
        Camera cam = vs.addCamera();
        Vector<Camera> cameras = new Vector<Camera>();
        cameras.add(cam);
        ClusterGeometry clGeom = new ClusterGeometry(options.blockWidth, options.blockHeight, options.numCols, options.numRows);
        ClusteredView cv = new ClusteredView(clGeom, options.numRows - 1, options.numCols, options.numRows, cameras);
        cv.setBackgroundColor(Color.LIGHT_GRAY);
        vsm.addClusteredView(cv);
        View view = vsm.addFrameView(cameras, "Master View", View.STD_VIEW, 800, 600, false, true, true, null);
        view.setListener(new PanZoomEventHandler());
        Random rnd = new Random();
        AnimationManager am = vsm.getAnimationManager();
        double sceneWidth = options.width;
        double sceneHeight = 0.6 * sceneWidth;
        double posIncr = sceneHeight / options.numGlyphs;
        double radius = 0.45 * posIncr;
        for (int i = 0; i < options.numGlyphs; ++i) {
            final Glyph glyph;
            final Color nextColor = Color.getHSBColor(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
            glyph = new VCircle(i * posIncr, i * posIncr, 0, radius, nextColor);
            vs.addGlyph(glyph);
            Animation anim = am.getAnimationFactory().createAnimation(3000, Animation.INFINITE, Animation.RepeatBehavior.REVERSE, glyph, Animation.Dimension.POSITION, new DefaultTimingHandler() {

                final double initX = glyph.vx;

                final double initY = glyph.vy;

                public void timingEvent(float fraction, Object subject, Animation.Dimension dim) {
                    Glyph g = (Glyph) subject;
                    g.moveTo(initX, (1 - fraction) * initY);
                }
            }, new SplineInterpolator(0.1f, 0.95f, 0.2f, 0.95f));
            anim.setStartFraction(rnd.nextFloat());
            am.startAnimation(anim, false);
        }
    }

    public static void main(String[] args) {
        ACOptions options = new ACOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
            parser.printUsage(System.err);
            return;
        }
        if (options.help) {
            System.err.println("Usage: AnimCircles [options] where options are: ");
            parser.printUsage(System.err);
            return;
        }
        new AnimCircles(options);
    }

    private class PanZoomEventHandler implements ViewListener {

        private int lastJPX;

        private int lastJPY;

        public void press1(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
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
            vsm.getActiveView().mouse.setSensitivity(false);
        }

        public void release3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
            Camera c = vsm.getActiveCamera();
            c.setXspeed(0);
            c.setYspeed(0);
            c.setZspeed(0);
            v.setDrawDrag(false);
            vsm.getActiveView().mouse.setSensitivity(true);
        }

        public void click3(ViewPanel v, int mod, int jpx, int jpy, int clickNumber, MouseEvent e) {
        }

        public void mouseMoved(ViewPanel v, int jpx, int jpy, MouseEvent e) {
        }

        public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpx, int jpy, MouseEvent e) {
            if (buttonNumber == 3 || ((mod == META_MOD || mod == META_SHIFT_MOD) && buttonNumber == 1)) {
                Camera c = vsm.getActiveCamera();
                double a = (c.focal + Math.abs(c.altitude)) / c.focal;
                if (mod == META_SHIFT_MOD) {
                    c.setXspeed(0);
                    c.setYspeed(0);
                    c.setZspeed((c.altitude > 0) ? (lastJPY - jpy) * (a / 4.0) : (lastJPY - jpy) / (a * 4));
                } else {
                    c.setXspeed((c.altitude > 0) ? (jpx - lastJPX) * (a / 4.0) : (jpx - lastJPX) / (a * 4));
                    c.setYspeed((c.altitude > 0) ? (lastJPY - jpy) * (a / 4.0) : (lastJPY - jpy) / (a * 4));
                }
            }
        }

        public void mouseWheelMoved(ViewPanel v, short wheelDirection, int jpx, int jpy, MouseWheelEvent e) {
        }

        public void enterGlyph(Glyph g) {
        }

        public void exitGlyph(Glyph g) {
        }

        public void Ktype(ViewPanel v, char c, int code, int mod, KeyEvent e) {
        }

        public void Kpress(ViewPanel v, char c, int code, int mod, KeyEvent e) {
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
            vsm.stop();
            System.exit(0);
        }
    }
}

class ACOptions {

    @Option(name = "-bw", aliases = { "--block-width" }, usage = "clustered view block width")
    int blockWidth = 400;

    @Option(name = "-bh", aliases = { "--block-height" }, usage = "clustered view block height")
    int blockHeight = 300;

    @Option(name = "-r", aliases = { "--num-rows" }, usage = "number of rows in the clustered view")
    int numRows = 2;

    @Option(name = "-c", aliases = { "--num-cols" }, usage = "number of columns in the clustered view")
    int numCols = 3;

    @Option(name = "-n", aliases = { "--num-glyphs" }, usage = "number of glyphs")
    int numGlyphs = 50;

    @Option(name = "-w", aliases = { "--width" }, usage = "scene width")
    int width = 800;

    @Option(name = "-h", aliases = { "--help" }, usage = "print this help message and exit")
    boolean help = false;
}
