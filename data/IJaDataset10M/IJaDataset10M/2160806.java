package fr.inria.zvtm.cluster.examples;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import fr.inria.zvtm.cluster.ClusteredView;
import fr.inria.zvtm.cluster.ClusterGeometry;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.View;
import fr.inria.zvtm.event.ViewListener;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.cluster.ClusteredImage;
import java.awt.Color;
import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Sample master application.
 */
public class ClusteredImageExample {

    private VirtualSpaceManager vsm = VirtualSpaceManager.INSTANCE;

    ClusteredImageExample(ImOptions options) {
        vsm.setMaster("ClusteredImageExample");
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
        URL imgURL = null;
        try {
            if (options.source.equals("")) {
                imgURL = new URL("http://www.urban.youvox.fr/IMG/png/WildSe1.png");
            } else {
                imgURL = new URL(options.source);
            }
        } catch (MalformedURLException e) {
            throw new Error("Malformed URL");
        }
        ClusteredImage cImg = new ClusteredImage(0, 0, 0, imgURL, 2f);
        vs.addGlyph(cImg, false);
    }

    public static void main(String[] args) {
        ImOptions options = new ImOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
            parser.printUsage(System.err);
            return;
        }
        new ClusteredImageExample(options);
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

class ImOptions {

    @Option(name = "-bw", aliases = { "--block-width" }, usage = "clustered view block width")
    int blockWidth = 400;

    @Option(name = "-bh", aliases = { "--block-height" }, usage = "clustered view block height")
    int blockHeight = 300;

    @Option(name = "-r", aliases = { "--num-rows" }, usage = "number of rows in the clustered view")
    int numRows = 2;

    @Option(name = "-c", aliases = { "--num-cols" }, usage = "number of columns in the clustered view")
    int numCols = 3;

    @Option(name = "-s", aliases = { "--source" }, usage = "image source")
    String source = "";
}
