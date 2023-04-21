package net.walkingtools.javame.canvas;

import java.util.Vector;
import javax.microedition.lcdui.*;
import net.walkingtools.Walker;
import net.walkingtools.international.*;

/** Canvas for the following activity
 * @author Brett Stalbaum
 * @version 0.0.7
 */
public class FollowingCanvas extends KeyEventProducerCanvas {

    private String message = null;

    private int displayCount = -1;

    private static final int CONSTANT_COUNT = 2;

    private Vector walkers = null;

    private static final int DIAMETER = 60;

    private int azi = 0;

    private boolean myLocationValid = false;

    private boolean isLeader = false;

    private int leader = Constants.LTGREEN;

    private int follower = Constants.PINK;

    private int uiColor = Constants.LTBLUE;

    private Font small = null;

    private Font medium = null;

    private int w = -1;

    private int h = -1;

    protected Translation translation = null;

    /**
     * Constructor accepts a language Translation
     * @param translation The language Translation for the UI
     */
    public FollowingCanvas(Translation translation) {
        this.translation = translation;
        this.setTitle(translation.translate("Following"));
        message = translation.translate("Started...");
        small = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        medium = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        displayCount = CONSTANT_COUNT;
        w = getWidth();
        h = getHeight();
    }

    /** jsessionid will be printed as the message
     * @param jsessionid
     */
    public void handshake(String jsessionid) {
        message = translation.translate("ID:") + jsessionid;
        displayCount = CONSTANT_COUNT;
        repaint();
    }

    /** Displays a goodbye message
     */
    public void goodbye() {
        message = translation.translate("goodbye");
        displayCount = CONSTANT_COUNT;
        repaint();
    }

    /** Renders the Vector of Walkers on this canvas.
     * @param walkers the Walkers to be rendered
     * @param isValid the loction object's validity (gray me out if not valid)
     */
    public void update(Vector walkers, boolean isValid) {
        this.walkers = walkers;
        repaint();
    }

    /** Status of relinquish token request
     * @param message
     */
    public void relinquishToken(String message) {
        this.message = message;
        displayCount = CONSTANT_COUNT;
        repaint();
    }

    /** Status of request token request
     * @param message
     */
    public void requestToken(String message) {
        this.message = message;
        displayCount = CONSTANT_COUNT;
        repaint();
    }

    /** The midlet should update whether or not this one is leading
     * @param isLeader true if this is the leader
     */
    public void isLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    /** Location listener should update this Following canvas on the validity of its location
     * @param isValid true if location valid
     */
    public void locationValid(boolean isValid) {
        myLocationValid = isValid;
        repaint();
    }

    /**
     * Override of the paint method
     * @param g the Graphics object
     */
    public void paint(Graphics g) {
        g.setFont(small);
        if (myLocationValid) {
            leader = Constants.LTGREEN;
            follower = Constants.PINK;
            uiColor = Constants.LTBLUE;
        } else {
            leader = Constants.LTGRAY;
            follower = Constants.LTGRAY;
            uiColor = Constants.MIDGRAY;
        }
        g.setColor(Constants.BLACK);
        g.fillRect(0, 0, w, h);
        g.setFont(small);
        if (isLeader) {
            g.setColor(leader);
        } else {
            g.setColor(Constants.MIDGRAY);
        }
        g.drawString("L", 1, 1, Graphics.TOP | Graphics.LEFT);
        if (displayCount > 0) {
            g.drawString(message, 10, 1, Graphics.TOP | Graphics.LEFT);
            displayCount--;
        }
        if (walkers != null) {
            int myEasting = ((Walker) walkers.elementAt(0)).getEasting();
            int myNorthing = ((Walker) walkers.elementAt(0)).getNorthing();
            float aziFloat = ((Walker) walkers.elementAt(0)).getAzimuth();
            g.translate((w / 2) - myEasting, (h / 2) - myNorthing);
            g.setColor(uiColor);
            g.drawArc(myEasting - DIAMETER / 2, myNorthing - DIAMETER / 2, DIAMETER, DIAMETER, 0, 360);
            g.setFont(medium);
            g.drawString("N", myEasting, myNorthing - DIAMETER / 2 - 5, Graphics.BOTTOM | Graphics.HCENTER);
            g.drawString("E", myEasting + DIAMETER / 2 + 7, myNorthing, Graphics.LEFT | Graphics.BASELINE);
            g.drawString("S", myEasting, myNorthing + DIAMETER / 2 + 4, Graphics.TOP | Graphics.HCENTER);
            g.drawString("W", myEasting - DIAMETER / 2 - 5, myNorthing, Graphics.RIGHT | Graphics.BASELINE);
            for (int i = 1; i < walkers.size(); i++) {
                int otherE = ((Walker) walkers.elementAt(i)).getEasting();
                int otherN = flipYaxis(((Walker) walkers.elementAt(i)).getNorthing(), myNorthing);
                if (((Walker) walkers.elementAt(i)).isLeader()) {
                    g.setColor(leader);
                    g.setStrokeStyle(Graphics.DOTTED);
                    g.drawLine(myEasting, myNorthing, otherE, otherN);
                    g.setStrokeStyle(Graphics.SOLID);
                    g.fillArc(otherE - 4, otherN - 4, 8, 8, 0, 360);
                    g.setColor(Constants.BLACK);
                    g.fillArc(otherE - 2, otherN - 2, 4, 4, 0, 360);
                } else {
                    g.setColor(follower);
                    g.setStrokeStyle(Graphics.DOTTED);
                    g.drawLine(myEasting, myNorthing, otherE, otherN);
                    g.setStrokeStyle(Graphics.SOLID);
                    g.fillRect(otherE - 3, otherN - 3, 6, 6);
                    g.setColor(Constants.BLACK);
                    g.fillRect(otherE - 1, otherN - 1, 2, 2);
                }
            }
            if (aziFloat != 0) {
                azi = (int) aziFloat;
            }
            if (isLeader) {
                g.setColor(leader);
            } else {
                g.setColor(follower);
            }
            double radians = (azi - 90) * (Math.PI / 180);
            int x = rotateX(radians, DIAMETER / 2) + myEasting;
            int y = rotateY(radians, DIAMETER / 2) + myNorthing;
            g.drawLine(myEasting, myNorthing, x, y);
            g.fillArc(x - 2, y - 2, 4, 4, 0, 360);
            if (((Walker) walkers.elementAt(0)).isLeader()) {
                g.setColor(leader);
                g.fillArc(myEasting - 4, myNorthing - 4, 8, 8, 0, 360);
                g.setColor(Constants.BLACK);
                g.fillArc(myEasting - 2, myNorthing - 2, 4, 4, 0, 360);
            } else {
                g.fillRect(myEasting - 3, myNorthing - 3, 6, 6);
                g.setColor(Constants.BLACK);
                g.fillRect(myEasting - 1, myNorthing - 1, 2, 2);
            }
        } else {
            g.setColor(Constants.DKGRAY);
            g.drawArc(w / 2 - 30, h / 2 - 30, 60, 60, 0, 360);
        }
    }

    private int rotateX(double radians, double radius) {
        int x = (int) (radius * Math.cos(radians));
        return x;
    }

    private int rotateY(double radians, double radius) {
        int y = (int) (radius * Math.sin(radians));
        return y;
    }

    /**
     * overrides sizeChanged(int w, int h) of the Canvass class
     * merely to gracefully handle any size change in the canvass
     * @param w width
     * @param h height
     */
    protected void sizeChanged(int w, int h) {
        this.w = w;
        this.h = h;
    }

    private int flipYaxis(int y, int yAxis) {
        int offset = 2 * (y - yAxis);
        return y - offset;
    }
}
