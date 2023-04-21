package org.j3d.renderer.java3d.navigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * This class will create smooth transitions from one viewpoint to another.
 * <p>
 *
 * The transition effects start when a new set of transform groups are used to
 * drive the view. To provide the driving factor, a Swing Timer object is used
 * rather than using the Java 3D behaviour system.
 *
 * @author <a href="http://www.ife.no/vr/">Halden VR Centre, Institute for Energy Technology</a><br>
 *   Updated for j3d.org by Justin Couch
 * @version $Revision $
 */
public class ViewpointTransition implements ActionListener {

    /** The view that we are moving about. */
    private View view;

    /** The transform group above the view that is being moved each frame */
    private TransformGroup viewTg;

    /** A timer that drives our updates to the screen */
    private Timer timer;

    /** The time that this current transition is to take in milliseconds */
    private int totalTimeMS;

    /** The end time, in epoch coordinates, of the end of the transition */
    private long epochEndTime;

    /** Working calculation about how far along the transition we are */
    private double alfa;

    /** A delay between callbacks from the time for frames */
    private int timerDelay = 0;

    private Point3d eye = new Point3d();

    private Point3d eye1 = new Point3d();

    private Point3d eye2 = new Point3d();

    private Point3d center = new Point3d();

    private Point3d center1 = new Point3d();

    private Point3d center2 = new Point3d();

    private Vector3d up = new Vector3d();

    private Vector3d up1 = new Vector3d();

    private Vector3d up2 = new Vector3d();

    private Vector3d location1 = new Vector3d();

    private Vector3d location2 = new Vector3d();

    private Vector3d direction1 = new Vector3d();

    private Vector3d direction2 = new Vector3d();

    private Transform3D previousFrameTx = new Transform3D();

    private Transform3D currentTx = new Transform3D();

    private Transform3D destinationTx = new Transform3D();

    /** An observer for information about updates for this transition */
    private FrameUpdateListener updateListener;

    /**
     * Construct a new transition object ready to work.
     */
    public ViewpointTransition() {
        timer = new Timer(100, this);
        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.setLogTimers(false);
        timer.setCoalesce(true);
        timer.stop();
    }

    /**
     * Set the listener for frame update notifications. By setting a value of
     * null it will clear the currently set instance
     *
     * @param l The listener to use for this transition
     */
    public void setFrameUpdateListener(FrameUpdateListener l) {
        updateListener = l;
    }

    /**
     * Transition between two locations represented by the initial
     * TranformGroup and the destination transform information starting
     * immediately.
     *
     * @param view The view that is associated with this transform
     * @param viewTg is the transformgroup to be transitioned that holds
     *    the view.
     * @param endTx is the final state to be transitioned to
     * @param totalTime The time to be spent with this transition
     *    (in miliseconds)
     */
    public void transitionTo(View view, TransformGroup viewTg, Transform3D endTx, int totalTime) {
        this.view = view;
        this.viewTg = viewTg;
        destinationTx = new Transform3D(endTx);
        totalTimeMS = totalTime;
        epochEndTime = System.currentTimeMillis() + totalTime;
        timer.start();
        viewTg.getTransform(currentTx);
        currentTx.get(location1);
        eye1.set(location1);
        direction1.set(0, 0, -1);
        currentTx.transform(direction1);
        center1.add(eye1, direction1);
        up1.set(0, 1, 0);
        currentTx.transform(up1);
        destinationTx.get(location2);
        eye2.set(location2);
        direction2.set(0, 0, -1);
        destinationTx.transform(direction2);
        center2.add(eye2, direction2);
        up2.set(0, 1, 0);
        destinationTx.transform(up2);
    }

    /**
     * Process an action event from the timer. This event is only for the time
     * and should not be associated with any other sort of action event like
     * menu callbacks.
     *
     * @param evt The event that caused this action to be called
     */
    public void actionPerformed(ActionEvent evt) {
        viewTg.getTransform(previousFrameTx);
        if (currentTx.equals(previousFrameTx)) {
            timerDelay = 10 + (int) view.getLastFrameDuration() / 2;
            timer.setDelay(timerDelay);
            alfa = 1 - ((double) (epochEndTime - System.currentTimeMillis()) / totalTimeMS);
            if (alfa > 1) alfa = 1;
            eye.interpolate(eye1, eye2, alfa);
            center.interpolate(center1, center2, alfa);
            up.interpolate(up1, up2, alfa);
            currentTx.lookAt(eye, center, up);
            currentTx.invert();
            currentTx.normalize();
            try {
                viewTg.setTransform(currentTx);
            } catch (Exception e) {
                System.out.println("Transition stopping due to invalid value");
                currentTx.set(destinationTx);
                viewTg.setTransform(currentTx);
                alfa = 9;
            }
            if (updateListener != null) updateListener.viewerPositionUpdated(currentTx);
        } else alfa = 9;
        if (alfa >= 1) {
            timer.stop();
            if (updateListener != null) updateListener.transitionEnded(currentTx);
        }
    }
}
