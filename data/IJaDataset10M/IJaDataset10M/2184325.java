package gov.sandia.gifomatic;

import java.applet.Applet;
import java.awt.*;
import java.io.PrintStream;
import java.net.URL;

/** Actually views any HTML-valid file format and reloads it at a
    specified time slice interval. */
public class GifGrabber11 extends Canvas implements Runnable {

    int tSlice;

    boolean debug;

    Image img;

    /** Reload the image every "tSlice" seconds. */
    public GifGrabber11(Image img, int tSlice, boolean debug) {
        this.tSlice = tSlice;
        this.img = img;
        this.debug = debug;
        int w = img.getWidth(this);
        int h = img.getHeight(this);
        setSize(new Dimension(w, h));
        trace("w = " + w + "h= " + h);
        trace("tSlice =" + tSlice);
        invalidate();
        repaint();
        setBackground(Color.white);
    }

    public void setInterval(int interval) {
        tSlice = interval;
        trace("tSlice reset to " + tSlice);
    }

    public void paint(Graphics g) {
        trace("paint()");
        if (img != null) g.drawImage(img, 0, 0, this);
    }

    public static void main(String args[]) {
        System.out.println("GifGrabber for jdk1.1");
    }

    public void run() {
        trace("run(): Thread started!");
        do try {
            Thread.sleep(tSlice * 1000);
            trace("Flushing image");
            img.flush();
            MediaTracker mediaTracker_track = new MediaTracker(this);
            mediaTracker_track.addImage(img, 1);
            mediaTracker_track.waitForID(1);
            int h = img.getHeight(this);
            int w = img.getWidth(this);
            trace("img height = " + h + " img width = " + w);
            setSize(new Dimension(w, h));
            repaint();
        } catch (Exception e) {
            trace("Caught:" + e.toString());
        } while (true);
    }

    public GifGrabber11() {
    }

    public void trace(String message) {
        if (debug) System.out.println(message);
    }
}
